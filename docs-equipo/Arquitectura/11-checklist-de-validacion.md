# 11 · Checklist de validación

> **Vinculante.** Se recorre en dos momentos: **antes** de ejecutar un AAP o una corrida que
> genere código, y **después**, sobre el código producido. Un incumplimiento reprueba: no se
> negocia caso por caso.
>
> Base normativa: [10 · Controles OWASP ASVS](10-controles-owasp-asvs.md) y los ADR de esta
> carpeta.

---

## Parte A · Revisión del AAP, antes de ejecutar

Se lee el plan buscando **contradicciones con lo ya decidido**. Detenerlo aquí cuesta
minutos; detectarlo en el código cuesta una corrida completa.

### A.1 · Arquitectura

- [ ] ¿Mantiene **dos desplegables independientes** (`fplus-captura`, `fplus-reportes`)? Un
      solo WAR o un EAR con ambos **reprueba** ([ADR-001](adr/ADR-001-dos-modulos-desplegables.md)).
- [ ] ¿Introduce **API, cola o llamada remota entre los dos módulos**? Reprueba: la
      integración es por base de datos ([ADR-002](adr/ADR-002-integracion-por-base-de-datos.md)).
- [ ] ¿Coloca el tema o el CSS **dentro de un WAR** en lugar de `fplus-ui`? Reprueba
      ([ADR-007](adr/ADR-007-tema-propio-compartido.md)).
- [ ] ¿Pone entidades de identidad en `fplus-domain`? Reprueba: son privadas de cada módulo.
- [ ] ¿Respeta los cinco esquemas (`ops`, `cfg`, `aud`, `seg_cap`, `seg_rep`)?

### A.2 · Seguridad

- [ ] ¿Nombra **Apache Shiro 2.x**? Shiro 1.x usa `javax.servlet` y no sirve en Jakarta EE 11.
- [ ] ¿Menciona **`shiro.ini`** en cualquier forma? **Reprueba**: la configuración va en código
      ([ADR-009](adr/ADR-009-apache-shiro-programatico.md)).
- [ ] ¿Usa **bcrypt**? **Reprueba**: trunca a 72 bytes. Debe ser Argon2id.
- [ ] ¿Deja el generador de identificador de sesión por omisión, o usa UUID? **Reprueba**.
- [ ] ¿Habilita `RememberMe`? **Reprueba**.
- [ ] ¿Deriva roles o permisos de **grupos de un proveedor de identidad**? **Reprueba**.
- [ ] ¿Un mismo módulo accede a `seg_cap` **y** a `seg_rep`? **Reprueba**.
- [ ] Si toca LDAP: ¿un solo realm con los directorios en **properties**, o un realm por
      servidor? Un realm por servidor **reprueba** ([ADR-010](adr/ADR-010-autenticacion-por-directorio.md)).
- [ ] ¿Recorre los directorios **hasta que alguno valide**? **Reprueba**: el directorio se
      resuelve desde la cuenta local antes de conectar. Uno de ellos es de la contraparte.
- [ ] ¿Da de alta la cuenta automáticamente al validar contra el directorio? **Reprueba**:
      la cuenta local es la compuerta y se provisiona aparte.
- [ ] ¿Pone LDAP en `fplus-captura`? **Reprueba**: ERSA es local con Argon2id.
- [ ] ¿Incluye el **filtro de cabeceras de respuesta** en `fplus-ui`?
- [ ] ¿Contempla **TLS en la conexión a base de datos**, con validación de certificado?
- [ ] ¿Algún secreto con **valor por omisión** o embebido en el plan? **Reprueba**.

### A.3 · Modelo de datos

- [ ] ¿Modela los **cuatro estados** de un valor (cero real, pendiente, ausente, no aplica), o
      se conforma con una columna anulable? Una columna anulable **reprueba** (NFR-009).
- [ ] ¿Modela los ciclos como **filas**, no como columnas `ensaye_1`, `ensaye_2`? Columnas
      fijas **reprueban** (NFR-006).
- [ ] ¿Usa `DECIMAL` con precisión explícita para valores de ensaye? `FLOAT` o `REAL`
      **reprueban**.
- [ ] ¿Incluye `DELETE` sobre `ops` o `aud`? **Reprueba**: la historia no se borra.
- [ ] ¿Las migraciones son **Flyway versionadas**, e incluyen los `GRANT` y `DENY`?
- [ ] ¿Toda entidad modificable lleva `@Version`?

### A.4 · Alcance

- [ ] ¿Resuelve por su cuenta algún **TBD** que requiere decisión de negocio o TI? Los TBD de
      esta carpeta no se cierran con criterio del equipo de desarrollo.
- [ ] ¿Usa como configuración real algún **número de los ejemplos** de los documentos
      preliminares? Reprueba.
- [ ] ¿Asume una **matriz de roles** concreta? P02 sigue abierto: la matriz es dato, no código.

---

## Parte B · Revisión del código generado

Cada punto trae una forma concreta de buscarlo. Una revisión que solo lee no encuentra lo
que no está buscando.

### B.1 · Patrones prohibidos

Cualquier coincidencia **reprueba** hasta que se justifique y corrija:

| Qué se busca | Búsqueda |
| --- | --- |
| Configuración de Shiro fuera del código | `shiro.ini` en cualquier ruta |
| Generador de sesión por omisión | `JavaUuidSessionIdGenerator`, `UUID.randomUUID` cerca de sesión |
| Algoritmo que trunca | `BCrypt`, `bcrypt`, `Bcrypt` |
| Escapado de salida desactivado | `escape="false"` en `.xhtml` |
| Certificado no validado | `trustServerCertificate=true`, `TrustManager`, `checkServerTrusted`, `HostnameVerifier`, `setHostnameVerifier` |
| SQL por concatenación | `createQuery("` u `createNativeQuery("` seguidos de `+`; `Statement` en lugar de `PreparedStatement` |
| Secreto con valor por omisión | `getProperty(` con segundo argumento; literales junto a `password`, `secret`, `key` |
| `RememberMe` activo | `setRememberMeManager` con argumento distinto de `null`; `RememberMeManager` |
| Clases de prueba en el artefacto | Clases de prueba fuera del alcance de prueba; endpoints de diagnóstico |
| Cabecera de reenvío sin condición | `X-Forwarded-For` o `getHeader(` usados sin comprobar el proxy de confianza |

### B.2 · Autenticación y sesión

- [ ] El `SessionIdGenerator` es propio, sobre `SecureRandom`, con **32 bytes**.
- [ ] El gestor de sesión es el **nativo de Shiro**, no el del contenedor.
- [ ] `subject.login(...)` va precedido de la **invalidación de la sesión previa** y seguido
      de una sesión nueva. No se asume que el framework lo haga.
- [ ] Las cookies se llaman `__Host-fplus-cap-sid` y `__Host-fplus-rep-sid`, con `Secure`,
      `HttpOnly`, `Path=/`, `SameSite`, y **sin atributo `Domain`**.
- [ ] El `CredentialsMatcher` es propio, sobre **Argon2id**.
- [ ] La contraseña se usa **tal cual llega**: sin `trim()`, sin `toLowerCase()`, sin
      `Normalizer`, sin lista de caracteres permitidos.
- [ ] El arreglo de la contraseña se **limpia** tras usarlo.
- [ ] La comparación de hash es en **tiempo constante**.
- [ ] La respuesta ante credencial inválida es **idéntica** exista o no el usuario.

### B.3 · Autorización

- [ ] La comprobación de permiso está en el **servicio de aplicación**, no solo en la vista.
- [ ] Se comprueban **permisos** (`ensaye:capturar`), no roles.
- [ ] **Ningún comodín** (`ensaye:*`) en las asignaciones a roles.
- [ ] El realm de cada módulo consulta **solo su propio esquema**.

### B.4 · Cabeceras y salida

- [ ] Un filtro único emite, en **toda** respuesta: `Content-Security-Policy` con al menos
      `object-src 'none'`, `base-uri 'none'` y `frame-ancestors`; `X-Content-Type-Options:
      nosniff`; `Referrer-Policy`; y `Strict-Transport-Security` con `max-age` de un año
      como mínimo e `includeSubDomains`.
- [ ] `Cache-Control: no-store` en toda respuesta autenticada.
- [ ] Las cabeceras se escriben **antes** de comprometer la respuesta.

### B.5 · Entradas y salidas de datos

- [ ] Los backing beans exponen **objetos de formulario**, nunca una entidad `@Entity`.
- [ ] El traslado formulario a dominio es **campo por campo**, sin copia automática.
- [ ] Toda consulta es **parametrizada**.
- [ ] Toda dirección o asunto de correo **rechaza** retorno de carro y salto de línea, aunque
      el valor venga de la base de datos.
- [ ] Todo valor externo se **sanea antes de registrarlo**.
- [ ] **No se registran** contraseñas, hashes, tokens ni identificadores de sesión.
- [ ] El nombre JNDI es **constante**: ningún valor externo lo alcanza.

### B.6 · Conexiones

- [ ] La cadena de conexión lleva cifrado exigido y **validación de certificado activa**.
- [ ] Hay una **versión mínima de protocolo explícita**.
- [ ] Ninguna conexión admite retroceso a texto plano, **tampoco en desarrollo**.


### B.7 · Directorio (LDAP)

- [ ] **Un solo realm**; los directorios se leen de un archivo de propiedades, no del código.
- [ ] Un directorio lógico puede tener varios servidores: son **conmutación por error**, no
      directorios distintos.
- [ ] El directorio del usuario se resuelve desde **`seg_rep.usuario.directorio`** antes de
      conectar. **Ningún recorrido ciego** por todos los directorios.
- [ ] La identidad local es el par **(directorio, usuario)**, con índice único por par.
- [ ] Todo valor que entra al filtro de búsqueda pasa por **escapado RFC 4515**; todo valor
      que forma parte de un DN, por **RFC 4514**. Sin concatenación de cadenas.
- [ ] La conexión es **LDAPS o StartTLS**, con certificado validado y versión mínima
      explícita. Sin `ldap://` en claro en ninguna propiedad, tampoco en DEV.
- [ ] **Sin bind anónimo** para la búsqueda; credenciales de servicio sin valor por omisión.
- [ ] **Se rechaza el bind con contraseña vacía** antes de enviarlo al servidor.
- [ ] La contraseña se envía al directorio **tal cual llega**, sin recortar ni normalizar.
- [ ] El evento de auditoría registra **qué directorio validó**.
- [ ] No hay alta automática de cuenta al validar contra el directorio.
- [ ] `fplus-captura` **no** contiene código de LDAP.

Búsquedas rápidas: `ldap://` sin la `s`, `SECURITY_AUTHENTICATION` con `none`,
`Context.SECURITY_CREDENTIALS` con literal, `search(` con `+` de concatenación.


### B.8 · Interfaz de PROVEM

- [ ] La conexión usa **TLS con certificado validado** y versión mínima explícita.
- [ ] Las credenciales de acceso son **secreto sin valor por omisión**: si faltan, la
      aplicación falla al arrancar.
- [ ] Lo que llega se trata como **dato externo no confiable**: se valida y se conserva la
      carga original como evidencia.
- [ ] La extracción es **idempotente o detecta el duplicado**. Repetirla no crea dos veces el
      mismo envío de muestra.
- [ ] Un **fallo parcial es visible**: el usuario ve qué registros fallaron y por qué.
- [ ] El **origen y el momento** de cada extracción quedan en auditoría.
- [ ] Si la interfaz es de tipo consulta, toda consulta es **parametrizada**.
- [ ] Si hay proceso programado, está resuelto que **no se ejecute duplicado** con más de una
      instancia desplegada.

---

## Parte C · Pruebas que deben existir

No son opcionales. Un control que solo se comprueba leyendo el código se pierde en el tercer
cambio.

- [ ] **Rotación de sesión**: autenticar dos veces; el identificador cambia y el anterior ya
      no resuelve.
- [ ] **Cabeceras**: toda respuesta lleva el juego completo de B.4.
- [ ] **Aislamiento de esquemas**: `fplus-captura` **no** puede leer `seg_rep`. Debe fallar la
      construcción si el `DENY` desaparece en una migración posterior.
- [ ] **Contraseña sin alterar**: una contraseña con espacios al final, mayúsculas mezcladas y
      caracteres no ASCII se verifica correctamente.
- [ ] **Aislamiento de directorios**: un intento de autenticación **solo** contacta al
      directorio declarado en la cuenta; nunca a los demás.
- [ ] **Cuatro estados**: cero real, pendiente, ausente y no aplica no se colapsan entre sí en
      consulta, comparación ni exportación.

---

## Parte D · Lo que no se valida en código

Estos controles **no quedan cumplidos** por revisar el código. Se acompañan como notas de
entrega y requieren acción de TI o de negocio. El detalle y los responsables están en
[10 · Obligaciones residuales](10-controles-owasp-asvs.md).

- El proxy inverso **sobrescribe** la cabecera de reenvío, no la anexa.
- `includeSubDomains` de HSTS coordinado con TI antes de producción.
- Certificados TLS emitidos, distribuidos y rotados, incluido el de base de datos.
- HTTPS disponible también en desarrollo local.
- Secretos provistos en el dominio de Payara, sin valor por omisión.
- Cifrado en reposo (NFR-018).
- Política de contraseñas, bloqueo y expiración (NFR-017).

---

## Cómo se usa

1. **Antes de lanzar una corrida**: recorrer la Parte A contra el AAP. Si algo reprueba, se
   corrige el plan, no el resultado.
2. **Sobre el código generado**: recorrer la Parte B, empezando por la tabla de búsquedas de
   B.1, que es la que más rápido encuentra.
3. **Antes de dar por cerrada la entrega**: confirmar que las pruebas de la Parte C existen y
   pasan, y declarar por escrito las obligaciones de la Parte D que sigan abiertas.
