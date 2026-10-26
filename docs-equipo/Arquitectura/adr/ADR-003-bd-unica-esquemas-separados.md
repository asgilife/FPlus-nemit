# ADR-003 · Base de datos única dividida en esquemas con privilegios distintos

**Estado:** Aceptada · 2026-09-11

## Contexto

La base de datos es única por decisión del equipo. Pero `fplus-captura` puede quedar
expuesto a internet y NFR-008 exige separar la información comercial entre la captura de
ERSA y el intercambio externo.

Una base única con un solo principal de conexión para ambos módulos significaría que
comprometer el módulo expuesto entrega **todo**: datos comerciales, negociaciones y
credenciales de los usuarios internos.

## Decisión

Una sola base de datos (`FPlus`), dividida en **esquemas de SQL Server con privilegios
distintos por módulo**:

| Esquema | Contenido |
| --- | --- |
| `ops` | Dominio operativo |
| `cfg` | Catálogos y condiciones contractuales |
| `aud` | Historia y auditoría, append-only |
| `seg_cap` | Identidad del módulo de captura |
| `seg_rep` | Identidad del módulo de reportes |

Cada módulo se conecta con **su propio principal** (`svc_fplus_captura`,
`svc_fplus_reportes`), con `GRANT` acotados y `DENY` explícito sobre el esquema de identidad
del otro módulo.

Los privilegios son **parte del esquema** y se aplican por migración Flyway, no a mano.

## Consecuencias

**A favor**

- La separación la impone el **motor de base de datos**, no una condición `WHERE` en el
  código. No depende de que ninguna consulta olvide filtrar.
- Comprometer el módulo expuesto no entrega las credenciales internas ni la auditoría.
- Ningún módulo puede borrar historia: no hay `DELETE` sobre `ops` ni `aud`.
- Se conserva la base única, con sus ventajas de respaldo y consistencia transaccional.

**En contra**

- Más complejidad en las migraciones: hay que gestionar principales y privilegios.
- Requiere una prueba de integración que verifique que el `DENY` sigue vigente; si se pierde
  en una migración posterior, la protección desaparece en silencio.
- Consultas que crucen esquemas requieren privilegios explícitos.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Un solo esquema, un solo principal** | Comprometer el módulo expuesto entregaría todo. Incompatible con NFR-008 |
| **Dos bases de datos separadas** | Rompe la decisión de base única, complica el respaldo y obliga a consultas distribuidas para conciliar |
| **Separación solo en código de aplicación** | Depende de que ninguna consulta olvide filtrar. Una inyección SQL la anula por completo |
