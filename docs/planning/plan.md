# Plan — FPlus

Índice maestro: qué está cerrado, qué falta y en qué orden. Lo mantiene `nemit:coordinador` al cerrar cada ficha.

## Estados de una ficha

`[ ]` sin redactar · `[F]` redactada · `[V]` validada · `[W]` en curso · `[X]` cerrada y verificada

## Orden de trabajo

| # | Estado | Qué | Ficha | Historia | Quién y rama (modo equipo) |
|---|---|---|---|---|---|
| 0 | `[ ]` | Validar la arquitectura (ADR-011 a ADR-018) en una sesión aparte: `/nemit:validador arquitectura`. **2026-10-21: validación hecha, resultado «Pendiente»**: ADR-011, 013, 016 y 017 aceptados; siguen ADR-012, 014, 015 y 018, las objeciones O-1 a O-7 (`nemit:arquitecto`) y los faltantes F-1 a F-3 (`nemit:appsec`, `nemit:dba`, `nemit:product-owner`), en `docs-equipo/Arquitectura/README.md` § «Validaciones». Después, otra validación | — | — | |
| 1 | `[ ]` | Esqueleto del sistema: POM padre, `fplus-domain`, `fplus-ui`, `fplus-captura`, `fplus-reportes`, migración inicial, `CLAUDE.md` de cada módulo con las AR que aplican | | — | |

## Pendientes de arquitectura (2026-10-21)

Del contraste con la Arquitectura de Referencia Nemit 0.9 (`docs-equipo/Arquitectura/README.md`, «Arquitectura de
referencia») y de la revisión de la arquitectura contra los requisitos. Cada uno con su dueño.

| Pendiente | Definición | Dueño |
|---|---|---|
| Matriz ASVS control por control en `docs/seguridad/asvs.md`, generada del CSV oficial de ASVS 5.0.0 guardado con su SHA-256; el documento 10 enlaza a ella | AR-S-01 | `nemit:appsec` |
| Modelo de amenazas por frontera (navegador ↔ captura por VPN y luego internet, captura ↔ base, reportes ↔ SSO, reportes ↔ PROVEM, correo) | AR-S-02 | `nemit:appsec` |
| Autenticación y sesión: longitud máxima de contraseña, lista de contraseñas comunes, vida máxima de sesión, cerrar sesión y desactivar cuenta terminan sus sesiones, sin reescritura de URL, quitar «caducidad y complejidad» de 05 (choca con ASVS 5); decisión escrita del segundo factor | AR-S-07 | `nemit:appsec` (el segundo factor lo decide el usuario: ver la respuesta del arquitecto del 2026-10-21) |
| No registrar tal cual un identificador que no es cuenta (corregir 10 §8); mensaje genérico con identificador de error; lista de eventos de seguridad | AR-S-10 | `nemit:appsec` |
| Clasificación de datos sensibles (personales, credenciales, comerciales) | AR-S-11 | `nemit:appsec` |
| Análisis de dependencias por ficha y versiones exactas (Shiro 2.x → versión fija); ZAP *baseline*, búsqueda de secretos y matriz al día antes de liberar (en 08 y 11) | AR-S-04, AR-S-05 | `nemit:appsec` |
| Convenciones de base: `id` bigint + `uuid` en lo que sale a URL, exportación o auditoría; cuatro columnas de auditoría por listener con el «quién» calificado (`cap:`, `rep:`, `sistema:`); triggers de inmutabilidad en `aud` y en lo inmutable desde un estado; nombres de objetos; `READ_COMMITTED_SNAPSHOT`; confirmar la intercalación de ADR-016 | AR-BD-01, 02, 06, 08, AR-SS-02, 04 | `nemit:dba` |
| Convención de tiempo: UTC en `datetime2`, zona del negocio escrita, filtrar y agrupar por fecha con la zona en código o SQL (ADR) | AR-BD-07 | `nemit:dba` con `nemit:arquitecto` |
| Tabla de parámetros (umbrales de sesión y bloqueo por esquema de identidad, parámetros de negocio en `cfg`) | AR-G-01 | `nemit:dba` (en la ficha del esqueleto) |
| IP, canal y dispositivo en `aud.evento`, escritos en la misma transacción que el cambio | AR-G-14 | `nemit:dba` (en la ficha del esqueleto) |
| Autorización sin caché, `@Cacheable(false)` en usuario, rol y permiso; nunca sin un administrador activo por padrón | AR-JF-09 | Ficha del esqueleto |
| ~~Medir el identificador de sesión de Payara 7~~ **Medido el 2026-10-21** (7.2026.10, a lo más 56 bits de generador criptográfico): falta el ADR de desviación de AR-JF-09 con la sesión nativa de Shiro (O-5); el esqueleto lo vuelve a medir en la versión declarada | AR-JF-09 | `nemit:arquitecto` |
| Endurecer Payara (lo que expone de fábrica), alias del pool, `FACELETS_SKIP_COMMENTS`, un solo reloj; volver a medir `FilaNativa` con SQL Server y Java 25 | AR-JF-14, 15, 16, AR-G-22, AR-BD-13 | Ficha del esqueleto |
| ~~Hacer commit de `.gitattributes` y renormalizar~~ **Hecho el 2026-10-26**: entra en el primer commit del repositorio propio, ya normalizado | AR-G-21 | Usuario |
| Contradicciones internas de la arquitectura v0.1 que no se tocaron hoy: unicidad de `usuario` contra el par (directorio, usuario) en 05; revocar un rol sin `DELETE` en las tablas puente; servicios de `fplus-domain` que dependen de Shiro (se resuelve con el `Autorizador` de ADR-017); diagrama de 05 sin `captura → cfg`; `Cache-Control` en 10 y en el `README.md`; `SameSite` ausente en el `README.md`; ADR-002 sin registrar la revisión por PROVEM; la frase que 06 atribuye a 02 | — | `nemit:validador` al validar la arquitectura; las de 05, con `nemit:appsec` |
| Historias que faltan en el backlog: administración de catálogos (compañía, unidad, producto, elemento, laboratorio, representante), configuración de integraciones (PROVEM, SMTP) y alta de usuarios de ERSA | AR-G-09, AR-G-23 | `nemit:product-owner` |
| El prototipo tiene dos modos visuales y ADR-008 decidió tres (claro, descansa vista, oscuro) | ADR-008 | `nemit:disenador` |

## Pendientes sin ficha

<Todo hallazgo que no se corrigió en su sesión entra aquí el mismo día, con dueño, o como ficha nueva. Nunca en un
párrafo de cierre.>
