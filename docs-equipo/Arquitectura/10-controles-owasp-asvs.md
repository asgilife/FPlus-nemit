# 10 · Controles OWASP ASVS

> **Vinculante.** A diferencia del resto de esta carpeta, este documento no es preliminar:
> es la línea base de seguridad que **todo código generado debe cumplir**. Un cambio aquí
> requiere decisión explícita, no criterio de quien implementa.

Objetivo: **OWASP ASVS 5.0, niveles 1 y 2**, para todo control satisfacible en código de
aplicación. Donde un control no sea satisfacible en código, se genera lo que una
implementación conforme requiere y se declara la **obligación residual** al entregar.

Estado actual: **no existe código todavía**. Este documento fija las restricciones para
cuando se escriba.

---

## 1 · Escapado de LDAP y JNDI

**Requisito.** Escapar todo valor interpolado en un filtro de búsqueda LDAP conforme a
RFC 4515, y todo valor que llegue a una búsqueda JNDI. Los identificadores de usuario llegan
a consultas de directorio.

**Situación en FPlus.** **Sí hay LDAP.** `fplus-reportes` autentica contra directorio
([ADR-010](adr/ADR-010-autenticacion-por-directorio.md)). `fplus-captura` no: ERSA usa
credenciales locales. También hay **JNDI**, en la búsqueda del datasource.

> Este control **dejó de ser residual**. Era la obligación R8; con la adopción de LDAP pasa a
> ser exigible en el código.

**Control · LDAP.**

- **Escapado RFC 4515** de todo valor interpolado en un filtro de búsqueda. El identificador
  que teclea el usuario llega al filtro: es exactamente el caso que el requisito señala.
- **Escapado RFC 4514** de todo valor que forme parte de un DN.
- Prohibido construir un filtro por concatenación de cadenas.
- **LDAPS o StartTLS siempre**, con validación del certificado y versión mínima explícita.
  Sin retroceso a LDAP en claro, tampoco en desarrollo (control 12).
- **Sin bind anónimo** para la búsqueda. Las credenciales de la cuenta de servicio son
  secreto sin valor por omisión.
- **Rechazar el bind con contraseña vacía.** Muchos servidores lo tratan como bind anónimo y
  responden éxito: sería una autenticación exitosa sin contraseña.
- **El directorio se resuelve desde la cuenta local antes de conectar**, nunca probando uno
  por uno. Uno de los directorios pertenece a la contraparte comercial: un recorrido ciego le
  entregaría credenciales de Fresnillo.

**Control · JNDI.** El nombre JNDI es una **constante del código**
(`java:app/jdbc/fplusCaptura`). Ningún valor externo lo alcanza, ni por concatenación ni por
parámetro. Queda prohibido construir un nombre JNDI a partir de configuración que un usuario
pueda influir.

## 2 · Cabeceras de correo

**Requisito.** Rechazar CR y LF en cualquier valor usado como cabecera de correo, incluidas
las direcciones de destinatario leídas de la base de datos.

**Situación.** El intercambio con Met-Mex es por correo manual, fuera del sistema
([06](06-integraciones.md)). Pero sí habrá correo saliente en cuanto existan **avisos**
(P10) o **recuperación de contraseña**. Las direcciones salen de `seg_cap.usuario.correo` y
`seg_rep.usuario.correo`: valores de base de datos, que es exactamente el caso que el
requisito señala.

**Control.**

- Toda dirección y todo asunto se validan **antes** de construir el mensaje: se **rechaza**
  cualquier valor que contenga `\r` o `\n`, en cualquier codificación. Rechazar, no depurar:
  depurar oculta un dato corrupto en origen.
- La validación se aplica aunque el valor venga de la base de datos. Un dato almacenado no
  es un dato confiable: pudo entrar antes de que existiera el control.
- Prohibido concatenar valores externos en cabeceras. Se usa la API de mensajes con campos
  tipados.

## 3 · Identificador de sesión nuevo en cada autenticación

**Requisito.** Emitir un identificador de sesión nuevo en toda autenticación, incluida la
reautenticación, e invalidar el anterior.

**Control.** Implementado de forma explícita alrededor de `subject.login(...)`, sin asumir
que Shiro lo haga. Detalle en [05](05-seguridad-y-control-de-acceso.md).

**Verificación.** Prueba automatizada obligatoria: autenticar dos veces y comprobar que el
identificador cambió y que el anterior ya no resuelve. **Forma parte de la puerta de
revisión.**

## 4 · Entropía del identificador de sesión

**Requisito.** Generar identificadores de sesión con un CSPRNG de al menos 128 bits de
entropía. **Un UUID no lo satisface.**

**Control.** `SessionIdGenerator` propio sobre `SecureRandom`, 32 bytes (256 bits). Obliga a
usar el gestor de sesión nativo de Shiro, no el del contenedor. Detalle en
[05](05-seguridad-y-control-de-acceso.md).

**Nota.** El generador por omisión de Shiro es `JavaUuidSessionIdGenerator`, que **no
cumple**. Sustituirlo no es opcional.

## 5 · Verificación de contraseñas

**Requisito.** Verificar la contraseña exactamente como se recibe. Nunca truncar, plegar
mayúsculas ni normalizar parte alguna del valor enviado, y nunca restringir qué caracteres
puede contener.

**Control.** Argon2id mediante un `CredentialsMatcher` propio. Sin `trim`, sin
`toLowerCase`, sin normalización Unicode, sin lista de caracteres permitidos, comparación en
tiempo constante. Detalle en [05](05-seguridad-y-control-de-acceso.md).

**Consecuencia directa: bcrypt queda prohibido**, porque trunca a 72 bytes en silencio.

## 6 · Cabeceras de respuesta

**Requisito.** Emitir en **toda** respuesta:

| Cabecera | Valor mínimo |
| --- | --- |
| `Content-Security-Policy` | al menos `object-src 'none'`, `base-uri 'none'` y `frame-ancestors` |
| `X-Content-Type-Options` | `nosniff` |
| `Referrer-Policy` | presente |
| `Strict-Transport-Security` | `max-age` de un año como mínimo, con `includeSubDomains` |
| `Cache-Control` | `no-store` en toda respuesta con datos sensibles |

**Control.** Un filtro de servlet único en `fplus-ui`, aplicado a todas las rutas de ambos
módulos, que escribe las cabeceras **antes** de que se comprometa la respuesta. No se delega
al proxy inverso: si las emite la aplicación, se cumplen aunque cambie la topología.

Valores base:

```
Content-Security-Policy: object-src 'none'; base-uri 'none'; frame-ancestors 'none'
X-Content-Type-Options: nosniff
Referrer-Policy: no-referrer
Strict-Transport-Security: max-age=31536000; includeSubDomains
Cache-Control: no-store
```

`frame-ancestors 'none'`: ninguno de los dos módulos tiene caso de uso legítimo dentro de un
marco.

`Cache-Control: no-store` se aplica a toda respuesta autenticada. En esta aplicación eso es,
en la práctica, todo salvo los recursos estáticos del tema.

**Tensión conocida con JSF y PrimeFaces.** Ambos emiten script y estilo en línea. El
**mínimo exigido arriba es compatible** con eso, porque no restringe `script-src`. Endurecer
`script-src` es deseable y requiere apoyarse en `nonce` por respuesta; hay que verificar el
soporte concreto en la versión de Mojarra y PrimeFaces que se fije, y **no se da por
supuesto**.

**Obligación residual (TI).** `includeSubDomains` afecta a todos los subdominios del host,
incluidos los que no son de FPlus. Debe coordinarse antes de habilitarlo en producción. La
inclusión en la lista de precarga HSTS, si se desea, es decisión de TI.

## 7 · Nombre de la cookie de sesión

**Requisito.** Nombrar las cookies de sesión con el prefijo `__Host-`, o `__Secure-` cuando
la cookie se comparta deliberadamente entre hosts.

**Control.** `__Host-fplus-cap-sid` y `__Host-fplus-rep-sid`. Se elige `__Host-` porque las
cookies **no** se comparten: cada módulo tiene su padrón de identidad y su sesión. El
navegador lo garantiza.

**Consecuencia operativa.** `__Host-` exige `Secure` y por tanto HTTPS, **también en
desarrollo local**. No se admite una variante sin prefijo para DEV.

## 8 · Registro

**Requisito.** Codificar todo valor escrito a un registro. Nunca registrar credenciales,
tokens ni identificadores de sesión.

**Control.**

- Todo valor externo pasa por una función de saneo antes de llegar a la línea de registro:
  se neutralizan retorno de carro, salto de línea y caracteres de control. Sin eso, un valor
  con salto de línea fabrica entradas de registro falsas.
- **Nunca** se registran: contraseñas (ni su longitud), hashes, identificadores de sesión,
  tokens, ni el contenido de `clave_hash`.
- El registro técnico y la **auditoría funcional** (`aud.evento`) son cosas distintas
  ([04](04-modelo-de-datos.md)). El registro técnico no sustituye la auditoría, y la
  auditoría no es lugar para secretos.
- Al registrar un fallo de autenticación se anota el identificador **tecleado**, saneado, y
  nunca la contraseña.

## 9 · Tokens autocontenidos

**Requisito.** Validar los tokens autocontenidos contra una lista explícita de algoritmos
permitidos. Rechazar el algoritmo `none` y rechazar el material de clave que el propio token
nombre. Validar el `audience`.

**Situación.** **No se usan tokens autocontenidos.** La sesión es de servidor, gestionada
por Shiro. No hay JWT, y `RememberMe` de Shiro —que sí transporta estado firmado en una
cookie— está **deshabilitado explícitamente**.

**Obligación residual.** Si se introducen, por una API futura o por SSO, antes de la primera
línea de código: lista explícita de algoritmos, rechazo de `none`, rechazo de `jku`, `jwk`,
`x5u` y de cualquier referencia a clave que nombre el token, y validación de `aud`. Ningún
token se valida con la clave que él mismo indica.

## 10 · Vinculación de parámetros

**Requisito.** Vincular los parámetros de la petición a una lista explícita de campos por
acción. Nunca vincular un cuerpo de petición directamente sobre una entidad persistente.

**Control.** Coherente con la estratificación ya definida ([02](02-vista-de-modulos.md),
[08](08-convenciones-de-desarrollo.md)):

- Los backing beans exponen **objetos de formulario propios**, con exactamente los campos que
  esa pantalla edita. **Nunca una entidad JPA.**
- El servicio de aplicación traslada campo por campo del formulario al dominio, aplicando las
  reglas. No hay copia automática de propiedades.
- Prohibido exponer una entidad anotada como `@Entity` como propiedad vinculable de un bean
  de vista.

Sin esto, un campo que la pantalla no muestra —el estado de autorización, el identificador
del responsable— es escribible por quien construya la petición a mano.

## 11 · Dirección del cliente

**Requisito.** Derivar la dirección del cliente solo de una fuente que el usuario final no
pueda fijar. Una cabecera de reenvío solo es confiable si un proxy de confianza la
sobrescribe.

**Control.**

- Por omisión, la dirección se toma de la **conexión**, con `getRemoteAddr()`.
- La cabecera de reenvío se considera **solo** cuando la conexión proviene de la dirección
  del proxy de confianza, y **solo** si ese proxy está configurado para **sobrescribirla**,
  no para anexarle valores.
- La lista de proxies de confianza es configuración del despliegue, **sin valor por
  omisión**. Si no está configurada, no se confía en ninguna cabecera: se usa la dirección
  de la conexión.

**Obligación residual (TI).** En el escenario con DMZ ([03](03-vista-de-despliegue.md)), el
proxy inverso o WAF **debe sobrescribir** la cabecera de reenvío. Si solo la anexa, la
dirección registrada es falsificable por el cliente y el control no se cumple, por correcto
que sea el código.

## 12 · TLS en toda conexión

**Requisito.** Exigir TLS en toda conexión que abra la aplicación, incluida la de base de
datos, con una versión mínima de protocolo explícita.

**Control.**

- **Base de datos:** cifrado exigido y validación del certificado del servidor activa
  (`encrypt=true`, `trustServerCertificate=false`). No se desactiva en ningún entorno.
- **Versión mínima de protocolo explícita**, no la que traiga el entorno por omisión.
- Toda conexión saliente que se agregue en el futuro, correo incluido, usa TLS sin retroceso
  a texto plano.
- Prohibido un `TrustManager` permisivo, un verificador de nombre de host que acepte todo, o
  confiar en el certificado del servidor sin validarlo, **también en desarrollo**. Si el
  certificado de DEV estorba, se instala uno válido; no se desactiva la verificación.

**Obligación residual (TI).** Emisión, distribución y rotación de certificados, incluido el
del servidor de base de datos, y la configuración TLS del proxy inverso.

---

## Patrones prohibidos

Su presencia **reprueba la revisión**, sin excepción ni justificación caso por caso:

| # | Patrón prohibido |
| --- | --- |
| 1 | Concatenar cualquier valor externo en un filtro LDAP, un nombre JNDI, una sentencia SQL, una cabecera de correo o una línea de registro |
| 2 | Desactivar el escapado de salida en una plantilla o vista |
| 3 | Cualquier clase, endpoint o configuración de prueba dentro del artefacto de producción |
| 4 | Un gestor de confianza permisivo, una verificación de certificado desactivada, o un retroceso a texto plano en cualquier protocolo |
| 5 | Un valor por omisión para cualquier secreto, y cualquier secreto en el código fuente, en un artefacto de compilación o en un registro |
| 6 | Derivar autoridad de los grupos o atributos de un proveedor de identidad |

Notas sobre algunos de ellos:

- **(2)** En JSF significa que `escape="false"` queda prohibido en cualquier salida que pueda
  contener un valor de origen externo. Incluye lo que llega por carga de archivos de Met-Mex.
- **(3)** El artefacto de producción no lleva clases de prueba, endpoints de diagnóstico ni
  perfiles de configuración alternos. Las pruebas viven en alcance de prueba y no se
  empaquetan.
- **(5)** Sin valor por omisión significa **fallo al arrancar** si falta un secreto, no un
  respaldo silencioso. Las credenciales viven en la configuración del dominio de Payara
  ([03](03-vista-de-despliegue.md)), nunca versionadas.
- **(6)** Ya está resuelto por diseño: la autoridad se resuelve contra `seg_cap` o `seg_rep`
  ([ADR-004](adr/ADR-004-identidad-segregada-por-modulo.md)). Un SSO futuro aportaría
  autenticación, nunca permisos.

## Puerta de revisión

El código no se integra si:

1. Introduce cualquiera de los patrones prohibidos.
2. Alguna respuesta no lleva el juego de cabeceras de la sección 6.
3. Alguna escritura a directorio, correo o registro no escapa sus entradas.
4. Alguna autenticación no emite un identificador de sesión nuevo.

Los puntos 2 y 4 se verifican con **pruebas automatizadas**, no con inspección. Un control
que solo se comprueba leyendo el código se pierde en el tercer cambio.

## Obligaciones residuales · notas de entrega

Controles que **no se satisfacen en código de aplicación** y requieren acción de TI o de
negocio. Se genera el código que una implementación conforme requiere, pero el control no
queda cumplido hasta que estas se resuelvan:

| # | Obligación | Responsable |
| --- | --- | --- |
| R1 | El proxy inverso o WAF debe **sobrescribir** la cabecera de reenvío, no anexarla | TI |
| R2 | `includeSubDomains` de HSTS afecta a subdominios ajenos a FPlus: coordinar antes de producción | TI |
| R3 | Emisión, distribución y rotación de certificados TLS, incluido el del servidor de base de datos | TI |
| R4 | HTTPS también en desarrollo local, exigido por el prefijo `__Host-` | TI y equipo |
| R5 | Provisión de secretos en el dominio de Payara, sin valor por omisión | TI |
| R6 | Cifrado en reposo: TDE y, si aplica, Always Encrypted (NFR-018) | TI |
| R7 | Política de contraseñas, umbrales de bloqueo y expiración de sesión (NFR-017) | TI y negocio |
| R8 | ~~Escapado RFC 4515 si se adopta directorio~~ · **Ya no es residual**: LDAP adoptado, control exigible en código (sección 1) | — |
| R10 | Certificados y disponibilidad de los directorios LDAP; cuenta de servicio para el bind de búsqueda | TI |
| R11 | **Decisión sobre el acceso de Met-Mex**: su directorio existe, pero el acceso técnico no está aprobado y NFR-008 exige separar la información comercial | Negocio y TI |
| R12 | Naturaleza, contrato de datos, llaves y SLA de la **interfaz de PROVEM**; credenciales de acceso sin valor por omisión | TI |
| R9 | Endurecer `script-src` en la CSP requiere verificar el soporte de `nonce` en Mojarra y PrimeFaces | Equipo, al fijar versiones |

La forma operativa de recorrer estos controles está en
**[11 · Checklist de validación](11-checklist-de-validacion.md)**.
