# Decisiones de arquitectura (ADR)

Registro de las decisiones estructurales de FPlus. Cada ADR es inmutable: si una decisión
cambia, se escribe un ADR nuevo que **supersede** al anterior; no se edita el viejo.

| ADR | Decisión | Estado |
| --- | --- | --- |
| [001](ADR-001-dos-modulos-desplegables.md) | Dos módulos desplegables independientes | Aceptada |
| [002](ADR-002-integracion-por-base-de-datos.md) | Integración por base de datos compartida, sin API entre módulos | Aceptada |
| [003](ADR-003-bd-unica-esquemas-separados.md) | Base de datos única dividida en esquemas con privilegios distintos | Aceptada |
| [004](ADR-004-identidad-segregada-por-modulo.md) | Identidad, roles y permisos segregados por módulo | Aceptada |
| [005](ADR-005-migraciones-versionadas.md) | Migraciones versionadas con Flyway | Aceptada |
| [006](ADR-006-bloqueo-optimista.md) | Bloqueo optimista para edición concurrente | Aceptada |
| [007](ADR-007-tema-propio-compartido.md) | Tema propio en un artefacto de interfaz compartido | Aceptada |
| [008](ADR-008-tres-modos-de-visualizacion.md) | Tres modos de visualización: claro, descansa vista y oscuro | Aceptada |
| [009](ADR-009-apache-shiro-programatico.md) | Apache Shiro configurado por código, sin shiro.ini | Aceptada |
| [010](ADR-010-autenticacion-por-directorio.md) | Autenticación por directorio: un realm, N directorios por configuración | Aceptada · **rige como etapa provisional** mientras no haya SSO (decisión del usuario del 2026-10-21); la sustituirá ADR-012 |
| [011](ADR-011-captura-para-internet.md) | `fplus-captura` se diseña para internet; la primera etapa entra por VPN | Aceptado con supuesto (P-061) |
| [012](ADR-012-sso-corporativo.md) | Fresnillo entra con el SSO corporativo; la autoridad sigue en las cuentas locales | Propuesta · espera P-057 y P-061; vuelve al arquitecto por la decisión del usuario del 2026-10-21 |
| [013](ADR-013-provem-por-endpoint.md) | PROVEM por su endpoint, desde `fplus-reportes`, con una sola ejecución a la vez | Aceptado con supuesto (P-005) |
| [014](ADR-014-correo-por-bandeja.md) | Los avisos por correo salen de una bandeja en la base; los envía `fplus-reportes` | Propuesta · supuesto P-058 · objeción abierta (destinatarios con identidad segregada) |
| [015](ADR-015-evidencias-en-base.md) | Las evidencias se guardan en la base de datos | Propuesta · objeción abierta (el principal de captura lee `ops.evidencia`) |
| [016](ADR-016-motor-sql-server.md) | El motor de base de datos es SQL Server 2022, con intercalación `Modern_Spanish_100_CI_AI` | Aceptado con supuesto (P-060) |
| [017](ADR-017-capas-cdi-repositorios.md) | Capas con servicios CDI y repositorios (se aparta de AR-JF-01) | Aceptado |
| [018](ADR-018-flyway-sin-migrar-al-arrancar.md) | Flyway como paso del despliegue; la aplicación nunca migra (precisa ADR-005; se aparta de AR-SS-07) | Propuesta · objeción abierta (justificación y ruta de los scripts) |

## Formato

Contexto · Decisión · Consecuencias · Alternativas consideradas.

Estados posibles: **Propuesta** (así nace; la acepta la validación en otra sesión) · **Aceptada** ·
**Aceptada con supuesto (P-NNN)** · **Superseded por ADR-nnn** · **Precisada por ADR-nnn** · **Obsoleta**.
