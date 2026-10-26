# Arquitectura FPlus

Documentación de arquitectura base del sistema de seguimiento a ensayes.

> **Estado: línea base de trabajo v0.1 · PRELIMINAR**, revisada el 2026-10-21 contra los requisitos de las juntas y
> contra la Arquitectura de Referencia Nemit.
> **Validación (2026-10-21): Pendiente.** Faltan ADR-012, ADR-014, ADR-015 y ADR-018 (siguen «Propuesta»), las
> objeciones O-1 a O-7 y los faltantes F-1 a F-3 de [«Validaciones»](#validaciones). ADR-011, 013, 016 y 017 quedan
> aceptados.
> Deriva de los requerimientos preliminares v0.1 (36 RF, 26 NFR), que no están validados
> ni tienen baseline. Las decisiones aquí registradas son de trabajo: sostienen el arranque
> de la implementación y se revisan cuando se cierren los pendientes P01–P10.
> Lo que aún no está decidido se marca explícitamente como **TBD**; no debe convertirse en
> supuesto.

## Dos numeraciones de requisitos (léase antes de citar)

Los documentos 01 a 11 y los ADR-001 a ADR-010 citan los **requerimientos preliminares v0.1** de
`docs-equipo/*.docx` (RF-001 a RF-036, NFR-001 a NFR-026) y sus pendientes P01–P10. Desde el 2026-10-21 los requisitos
con cita y origen viven en **`docs/requisitos/requisitos.md`** (RF-001 a RF-107, RNF-001 a RNF-016) y las preguntas en
`docs/requisitos/preguntas-abiertas.md` (P-001 en adelante). **Son numeraciones distintas**: «RF-011» o «P05» no
significan lo mismo en un lado y en el otro. Los ADR-011 en adelante y las fichas citan la numeración nueva; la tabla
que manda para los no funcionales es la de [07](07-atributos-de-calidad.md) «Requisitos no funcionales de las juntas».

## Arquitectura de referencia

Contrastada el 2026-10-21 contra la **Arquitectura de Referencia Nemit 0.9 (borrador)**. Aplican `AR-G`, `AR-S`,
`AR-BD`, `AR-SS` y `AR-JF`; no aplican `AR-PG` (el motor es SQL Server, [ADR-016](adr/ADR-016-motor-sql-server.md)) ni
`AR-IO` (no hay app móvil). Las definiciones `AR-JF` se escribieron para Jakarta EE 10, PrimeFaces 14 y Payara 6: sus
precisiones medidas se vuelven a medir en la ficha del esqueleto con Jakarta EE 11, PrimeFaces 16 y Payara 7.

Lo no listado **se adopta**. Las reglas que el proyecto ya repetía (en 04, 05, 08, 10 y el `README.md` de la raíz)
siguen donde están y equivalen a su definición: AR-G-02, 04, 08, 10, 12, 15; AR-S-01, 06, 07 (en parte), 08, 09, 10
(en parte); AR-BD-03, 04, 08 (en parte), 12; AR-SS-05, 06; AR-JF-01 (la regla de fondo), 07, 09 (en parte).

| Definición | Veredicto | Por qué / dónde |
| --- | --- | --- |
| AR-G-13, AR-G-19, AR-G-20 | No aplica hoy | No hay captura en campo ni sin red; una sola base sin identificadores hacia otros sistemas; desarrollo a la medida, no producto |
| AR-JF-01 | Se aparta | [ADR-017](adr/ADR-017-capas-cdi-repositorios.md): servicios CDI y repositorios |
| AR-SS-07 | Se aparta | [ADR-018](adr/ADR-018-flyway-sin-migrar-al-arrancar.md): Flyway |
| AR-G-17 | Se aparta | [ADR-015](adr/ADR-015-evidencias-en-base.md): evidencias en la base |
| AR-G-23, AR-G-03 | Se aparta hoy (directorios LDAP en un archivo de propiedades, [ADR-010](adr/ADR-010-autenticacion-por-directorio.md)) | Se resuelve con P-057: si se acepta [ADR-012](adr/ADR-012-sso-corporativo.md) desaparece; si no, hace falta un ADR de desviación (cambiar el destino del bind redirige las contraseñas de todos, y eso debe exigir acceso al servidor). PROVEM y correo ya adoptan AR-G-23 ([ADR-013](adr/ADR-013-provem-por-endpoint.md), [ADR-014](adr/ADR-014-correo-por-bandeja.md)) |
| AR-JF-09 | Se aparta en las sesiones (gestor nativo de Shiro, [ADR-009](adr/ADR-009-apache-shiro-programatico.md)), pendiente de medir | La ficha del esqueleto mide el identificador de sesión de Payara 7: si alcanza 128 bits, se adopta la sesión del contenedor con un ADR que precisa ADR-009; si no, ADR de desviación con la medición |
| AR-G-18, AR-JF-12 | Aplican desde ADR-013 y ADR-014 | Los cumple su diseño (bandeja y candado de una sola ejecución) |
| AR-G-24 | Adopta | Tabla de clasificación en [04](04-modelo-de-datos.md) desde el 2026-10-21 |
| AR-G-01, AR-G-14, AR-G-21, AR-S-01, 02, 04, 05, 07, 10, 11, AR-BD-01, 02, 06, 07, AR-SS-02 (resuelta en ADR-016), AR-JF-09 (autorización sin caché) | **Adopta y hoy no cumple** | Cada uno con su dueño en `docs/planning/plan.md` («Pendientes de arquitectura») |

## Índice

| # | Documento | Contenido |
| --- | --- | --- |
| 01 | [Visión y alcance](01-vision-y-alcance.md) | Problema, alcance, drivers arquitectónicos y restricciones |
| 02 | [Vista de módulos](02-vista-de-modulos.md) | Los dos módulos, responsabilidades, capas y dependencias |
| 03 | [Vista de despliegue](03-vista-de-despliegue.md) | Topología, redes, entornos y configuración |
| 04 | [Modelo de datos](04-modelo-de-datos.md) | Esquemas de la base única, modelo conceptual, migraciones |
| 05 | [Seguridad y control de acceso](05-seguridad-y-control-de-acceso.md) | Identidad segregada por módulo, roles, permisos, auditoría |
| 06 | [Integraciones](06-integraciones.md) | Met-Mex, laboratorio de mina, PROVEM, tercerías |
| 07 | [Atributos de calidad](07-atributos-de-calidad.md) | Mapeo NFR → decisión arquitectónica |
| 08 | [Convenciones de desarrollo](08-convenciones-de-desarrollo.md) | Paquetes, capas, transacciones, estándares |
| 09 | [Interfaz y tema visual](09-interfaz-y-tema.md) | Tema propio, artefacto compartido, tres modos visuales, densidad y accesibilidad |
| 10 | [Controles OWASP ASVS](10-controles-owasp-asvs.md) | **Vinculante.** Línea base de seguridad para todo el código generado |
| 11 | [Checklist de validación](11-checklist-de-validacion.md) | **Vinculante.** Lista verificable para revisar un AAP y el código generado |
| — | [Decisiones (ADR)](adr/) | Registro de decisiones de arquitectura |

## Resumen en una página

FPlus son **dos aplicaciones web Jakarta EE desplegadas por separado contra una sola base
de datos SQL Server**:

- **`fplus-captura`** — lo usan los analistas de ERSA, posiblemente desde fuera de la red
  corporativa. Captura de ensayes por elemento, revisión y autorización.
- **`fplus-reportes`** — lo usa Fresnillo PLC en red interna. Ingesta de lo que manda
  Met-Mex y el laboratorio de mina, conciliación, intercambio, negociación, tercería,
  historia y salidas. Es el centro de gravedad del sistema.

Los módulos **no se hablan por API**: se integran a través de la base de datos compartida
([ADR-002](adr/ADR-002-integracion-por-base-de-datos.md)). La base es única pero está
**dividida en esquemas**, y la identidad —usuarios, contraseñas, roles y permisos— está
**segregada por módulo** ([ADR-004](adr/ADR-004-identidad-segregada-por-modulo.md)): el
módulo expuesto a internet no tiene ningún privilegio sobre las credenciales del módulo
interno.

La seguridad usa **Apache Shiro 2.x configurado por código**, sin `shiro.ini`
([ADR-009](adr/ADR-009-apache-shiro-programatico.md)), y todo el código generado debe
cumplir la línea base **[OWASP ASVS 5.0 niveles 1 y 2](10-controles-owasp-asvs.md)**, que
es vinculante y no preliminar.

Antes de ejecutar una corrida que genere código, y sobre el código producido, se recorre
el **[11 · Checklist de validación](11-checklist-de-validacion.md)**.

## Por qué esta carpeta no la lee Blitzy

`docs-equipo/` está en [`.blitzyignore`](../../.blitzyignore) **a propósito**.

Estos documentos describen una arquitectura que **todavía no está aprobada**, derivada de
requerimientos preliminares sin baseline. Si Blitzy los leyera, podría arrancar a construir
contra un diseño que aún no pasa por validación de negocio y de TI.

**No sacar la carpeta del `.blitzyignore`** hasta que la arquitectura se apruebe formalmente
y se decida qué parte se entrega como insumo de una corrida.

## Cómo usar esta documentación

- Antes de proponer un cambio estructural, revisar si ya hay un [ADR](adr/) que lo cubra.
- Toda decisión nueva de arquitectura se registra como ADR, no como edición silenciosa de
  estos documentos.
- Los **TBD** son deliberados. Cerrarlos requiere decisión de negocio o de TI, no criterio
  del equipo de desarrollo.

## Validaciones

### Validación — 2026-10-21 (`nemit:validador`)

**Qué se verificó y contra qué.** Los documentos 01 a 08 y los ADR-011 a ADR-018 contra `docs/requisitos/`
(RF-001 a RF-107, RNF-001 a RNF-016, P-001 a P-060), las historias HU-001 a HU-019 y `docs/negocio/`, y contra la
Arquitectura de Referencia Nemit 0.9 (borrador): `general.md`, `seguridad.md`, `base-de-datos.md`, `sqlserver.md`,
`jakarta-primefaces.md`. Todavía no hay código: lo «documentado contra el código» no aplica. Cada cita de requisito de
los ADR nuevos existe y dice lo que el ADR afirma, salvo lo anotado abajo.

**Lo que se midió, en solo lectura** (las medidas temporales están en la carpeta temporal de la sesión, fuera del
proyecto):

- **Identificador de sesión de Payara 7** (para AR-JF-09). En la instalación local, que es la 7.2026.10 (`glassfish-version.properties`;
  el stack declara 7.2026.8), se desensamblaron con `javap` `org.apache.catalina.session.ManagerBase` (`web-core.jar`)
  y `com.sun.enterprise.util.uuid.UuidUtil` (`common-util.jar`). `StandardManager` no sobrescribe `generateSessionId`,
  y este arma el identificador con `UuidUtil.generateUuid`. Son cuatro bloques de 7 dígitos hexadecimales:
  `System.currentTimeMillis()` (predecible), `System.identityHashCode` de un objeto nuevo (no criptográfico), la
  dirección IP más un `SecureRandom.nextInt()` y otro `SecureRandom.nextInt()`, los dos truncados a 28 bits. **La
  parte de un generador criptográfico suma a lo más 56 bits, menos de los 128 que pide ASVS 5.** La sesión del
  contenedor no sirve tal cual; se sostiene la sesión nativa de Shiro con su generador de 256 bits
  ([05](05-seguridad-y-control-de-acceso.md), [ADR-009](adr/ADR-009-apache-shiro-programatico.md)).
- **Intercalación de ADR-016.** `Modern_Spanish_100_CI_AI` existe en `sys.fn_helpcollations()` de la instancia local,
  que es SQL Server **2025** (17.0.1000.7), no 2022.
- **Dependencias en el repositorio local de Maven:** `shiro-core` 2.0.4, 2.0.5 y 3.0.1, cuando el stack dice «Shiro
  2.x»: la ficha del esqueleto fija la versión exacta (AR-S-04). No hay Flyway en el repositorio local, así que no se
  pudo medir. El `java` del `PATH` es el 21; el JDK 25 está en `C:\java\jdk-25`.

**Lo que se corrigió aquí (no decide nada).**

- [05](05-seguridad-y-control-de-acceso.md): al diagrama de esquemas le faltaba `fplus-captura → cfg`, que su propio
  `GRANT SELECT ON SCHEMA::cfg` ya da.
- [06](06-integraciones.md): se quitó una cita atribuida a [02](02-vista-de-modulos.md) que 02 no contiene.
- [04](04-modelo-de-datos.md): a la tabla de configuración se le agregaron la llave maestra de cada módulo (AR-G-03) y
  la zona horaria del negocio (AR-G-23, AR-BD-07).
- [`adr/README.md`](adr/README.md) y las líneas de estado de ADR-010 a ADR-018.
- Preguntas nuevas para el cliente: P-061 y P-062 en `docs/requisitos/preguntas-abiertas.md`.

**Decisiones del usuario (2026-10-21).**

1. **Segundo factor para ERSA desde el primer día** («Sí, desde el primer día»), con un código de una aplicación
   autenticadora. Confirma y precisa [ADR-011](adr/ADR-011-captura-para-internet.md).
2. **Si el SSO no está listo, Fresnillo entra por LDAP corporativo mientras tanto** («LDAP corporativo mientras
   tanto»), frente a cuentas locales provisionales o esperar. Cambia [ADR-012](adr/ADR-012-sso-corporativo.md), que
   vuelve al arquitecto (O-4).

**Estado de cada ADR.** Aceptados: ADR-017. Aceptados con supuesto: ADR-011 (P-061), ADR-013 (P-005) y ADR-016
(P-060, que se responde antes de cerrar la ficha del esqueleto). Siguen «Propuesta»: ADR-012, ADR-014, ADR-015 y
ADR-018.

**Objeciones, para `nemit:arquitecto`.**

| # | Qué | Evidencia | Qué se propone |
| --- | --- | --- | --- |
| O-1 | ADR-014: los destinatarios no se pueden resolver con la identidad segregada | El correo de RF-053 lo escribe reportes para ERSA, y reportes tiene `DENY` sobre `seg_cap` (05) | Decidir de dónde salen las direcciones de cada sentido sin abrir `seg_cap` a reportes |
| O-2 | ADR-015 y el `GRANT` provisional de 05: el principal de captura lee todo `ops` | `GRANT SELECT … ON SCHEMA::ops TO svc_fplus_captura`; en `ops` viven la evidencia, el ponderado de la mina, el intercambio, la decisión, la tercería y el correo. Choca con ADR-003, RNF-002, RF-048 y los casos de abuso de HU-002 y HU-015 | Fijar ahora el alcance de captura en `ops` (esquema propio o permisos por tabla). La migración inicial del esqueleto lo escribe. Con `nemit:dba` y `nemit:appsec` |
| O-3 | ADR-018: la alternativa descartada se apoya en un historial de Flyway que no existe, y la ruta de los scripts se aparta de AR-BD-03 sin decirlo | El repositorio no tiene código; AR-BD-03 dice `database/migrations/` | Escribir la fuerza real y declarar o adoptar la ruta |
| O-4 | ADR-012 se rehace con la decisión del usuario (ADR-010 como etapa provisional), y la lista de directorios en un archivo de propiedades (ADR-010) necesita su ADR de desviación de AR-G-23 y AR-G-03 | Decisión del usuario del 2026-10-21; fila AR-G-23 de «Arquitectura de referencia» | Rehacer ADR-012 y escribir la desviación (o llevar los directorios a `cfg` con pantalla) |
| O-5 | AR-JF-09 (sesiones): falta el ADR de desviación, y ya está la medición | Medición de arriba: a lo más 56 bits | ADR que se aparta de AR-JF-09 en las sesiones; el esqueleto lo vuelve a medir en la versión declarada y confirma `sessionIdUrlRewritingEnabled = false` en Shiro |
| O-6 | 05: dos contradicciones internas que el plan dejó para esta sesión | `usuario … UNIQUE` en la tabla base contra el índice por `(directorio, usuario)`; sin `DELETE` en `seg_*` no se puede quitar un rol (`usuario_rol` es tabla puente, AR-BD-02) | Decidir la llave de la cuenta y cómo se revoca un rol. Con `nemit:dba` |
| O-7 | Textos que deciden algo y no cuadran | 08 «Control de versiones» (rama por cambio, PR) contra el perfil (modo individual, sin ramas por ficha); el `CLAUDE.md` dice que el lote lo escribe «solo la ingesta de PROVEM», mientras 04 y ADR-013 suman la carga de contingencia (RF-002, «por confirmar»); el `README.md` de la raíz no lleva `SameSite` en la regla de cookies, y 05 decide `Strict` | Alinear cada uno con su fuente (el perfil manda en el proceso) |

**Faltantes, cada uno con su dueño.**

- **F-1 · `nemit:appsec`.** El modelo de amenazas (`docs/seguridad/amenazas.md`) sigue siendo la plantilla, y la matriz ASVS (AR-S-01, AR-S-02) no existe. Se
  necesitan antes de validar la ficha del esqueleto, con las fronteras nuevas: reportes ↔ directorio corporativo
  (provisional) y el segundo factor de captura.
- **F-2 · `nemit:dba`.** Las convenciones de base (AR-BD, AR-SS) y el ADR de tiempo (AR-BD-07) no están escritos.
- **F-3 · `nemit:product-owner`.** Faltan historias para administrar catálogos, integraciones y usuarios de ERSA, y
  ahora también para dar de alta y restablecer el segundo factor (HU-014).

**Lo que comprueba coordinación al lanzar el esqueleto.**

- Qué versiones de Payara (7.2026.8 o 7.2026.10) y de SQL Server (2022) usa cada entorno. La instancia local es 2025,
  así que la imagen de Testcontainers se fija en 2022.
- Que P-060 tenga respuesta antes de cerrar la ficha del esqueleto.
- Que P-062 tenga respuesta antes de la primera prueba de ingreso de Fresnillo.

**Siguiente paso.** `/nemit:arquitecto` con O-1 a O-7, `nemit:appsec` con F-1 y `nemit:dba` con F-2, y después otra
validación de ADR-012, ADR-014, ADR-015, ADR-018 y de los ADR nuevos (desviaciones de AR-JF-09 y de AR-G-23).
