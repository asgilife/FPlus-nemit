# ADR-016 · El motor de base de datos es SQL Server 2022

**Estado:** Aceptado con supuesto (P-060) · 2026-10-21 — el motor es una restricción del equipo (perfil, «Stack»); el
supuesto es que TI del cliente acepta operar SQL Server. Cambiarlo es barato **solo** mientras no exista la migración
inicial: P-060 se responde antes de cerrar la ficha del esqueleto
**Valida:** 2026-10-21 · `nemit:validador` · `Modern_Spanish_100_CI_AI` existe (medido en `sys.fn_helpcollations()`
de una instancia local, de solo lectura)

## Contexto

SQL Server 2022 aparece como restricción «decidida por el equipo» en [01](../01-vision-y-alcance.md) y lo usan
[ADR-003](ADR-003-bd-unica-esquemas-separados.md) y [ADR-005](ADR-005-migraciones-versionadas.md), pero ningún ADR lo
declara. Es la decisión que dice qué documento de la Arquitectura de Referencia Nemit aplica a la base: `AR-SS` (con
`AR-BD`) y no `AR-PG`.

## Decisión

La base de datos de FPlus es **SQL Server 2022**. Aplican `AR-BD` y `AR-SS` de la referencia; `AR-PG` no aplica.

**Intercalación (AR-SS-02): `Modern_Spanish_100_CI_AI`** en la base y en todas las columnas de texto. Sin distinguir
mayúsculas ni acentos: `JPerez` y `jperez` son el mismo usuario, y una búsqueda por «Cienega» encuentra «Ciénega».
Las columnas de texto son `NVARCHAR` (AR-SS-05). `nemit:dba` la confirma al fijar las convenciones; cambiarla después
exige reconstruir índices y restricciones de unicidad.

## Consecuencias

- Las convenciones de base las fija `nemit:dba` con `AR-BD` y `AR-SS`.
- Los esquemas como frontera de privilegios ([ADR-003](ADR-003-bd-unica-esquemas-separados.md)) se apoyan en
  `GRANT`/`DENY` por esquema de SQL Server.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **PostgreSQL** | Cumpliría igual; ninguna fuerza pide cambiar lo que el equipo ya decidió y sobre lo que descansan ADR-003 y ADR-005. Si TI del cliente opera otro motor, se revisa (P-060) |
| **No declararlo** | Deja sin decidir qué parte de la referencia aplica a la base |
