# ADR-005 · Migraciones versionadas con Flyway

**Estado:** Aceptada · 2026-09-11

## Contexto

Con la integración por base de datos ([ADR-002](ADR-002-integracion-por-base-de-datos.md)),
**el esquema es el contrato entre los dos módulos**. Además, los privilegios de los
principales de aplicación ([ADR-003](ADR-003-bd-unica-esquemas-separados.md)) son parte de la
protección del sistema: si se pierden en un cambio manual, la separación se anula en
silencio.

## Decisión

El esquema evoluciona mediante **Flyway**, con los scripts en
`fplus-domain/src/main/resources/db/migration`.

Reglas:

- Un script por cambio, numerado (`V<n>__descripcion.sql`) e **inmutable una vez aplicado en
  cualquier entorno**.
- Los `GRANT` y `DENY` de los principales de aplicación **son parte de la migración**, no
  configuración manual del DBA.
- Un cambio de esquema exige redesplegar **ambos** módulos.
- La aplicación **no ejecuta migraciones al arrancar en PROD**: se aplican como paso
  controlado del despliegue, con un principal administrativo distinto del de la aplicación.

## Consecuencias

**A favor**

- El esquema de cualquier entorno es reproducible y auditable.
- Los privilegios de seguridad quedan versionados junto al esquema que protegen.
- Elimina la deriva entre DEV, QA y PROD.
- El principal de la aplicación nunca necesita permisos de DDL.

**En contra**

- Disciplina: un script aplicado no se edita, se corrige con uno nuevo.
- Coordinación obligada entre los dos módulos ante cada cambio.
- Un paso más en el proceso de despliegue.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Generación automática de esquema por JPA (`hbm2ddl`)** | Inaceptable en producción: no versiona, no controla privilegios y puede destruir datos |
| **Scripts manuales del DBA** | Sin trazabilidad ni reproducibilidad; los privilegios se pierden en cuanto alguien olvida aplicarlos |
| **Liquibase** | Equivalente en capacidad. Flyway se elige por preferir SQL plano, que aquí importa porque las migraciones incluyen `GRANT` y `DENY` específicos de SQL Server |
