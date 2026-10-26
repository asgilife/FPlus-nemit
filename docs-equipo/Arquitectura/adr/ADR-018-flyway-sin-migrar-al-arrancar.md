# ADR-018 · Las migraciones las aplica Flyway como paso del despliegue; la aplicación nunca migra al arrancar

**Estado:** Propuesta · 2026-10-21 · **objeción abierta** (abajo)
**Valida:** 2026-10-21 · `nemit:validador` · no se acepta, por su justificación, no por la decisión:
1. La alternativa `sqlcmd -b` se descarta porque «habría que reescribir el historial de versiones que Flyway ya lleva».
   Ese historial no existe: el repositorio no tiene código ni migraciones. Hace falta la fuerza real (por ejemplo, la
   tabla de historial y la suma de verificación de Flyway cumplen la precisión de AR-BD-03, y con `sqlcmd` habría que
   construirlas), o la decisión se queda sin porqué.
2. AR-BD-03 pone los scripts en `database/migrations/`, y este ADR los pone en
   `fplus-domain/src/main/resources/db/migration`. Esa desviación no está declarada. Además, como la aplicación ya no
   migra, meterlos en el JAR del dominio empaqueta el DDL y los `GRANT`/`DENY` dentro de los dos WAR, también del que
   va a la DMZ. Se declara la desviación o se adopta la ruta de la referencia.
Precisión para el esqueleto: Flyway ya abre una transacción por script en SQL Server, así que un `BEGIN TRANSACTION`
explícito (AR-SS-03) se anida. Y `ALTER DATABASE … READ_COMMITTED_SNAPSHOT ON` (AR-SS-04) no corre dentro de una
transacción: va en la creación de la base, fuera de Flyway.
**Precisa** [ADR-005](ADR-005-migraciones-versionadas.md). **Se aparta de AR-SS-07** (Arquitectura de Referencia
Nemit 0.9, borrador) en la herramienta.

## Contexto

ADR-005 eligió Flyway frente a Liquibase, JPA y scripts manuales, pero no frente a lo que pide AR-SS-07 (`sqlcmd -b`
con código de salida). Además, [04](../04-modelo-de-datos.md) y [08](../08-convenciones-de-desarrollo.md) dicen que la
aplicación no migra al arrancar **en PROD**, lo que deja abierto migrar al arrancar en DEV y QA, mientras el mismo
ADR-005 dice que el principal de la aplicación nunca tiene permisos de DDL.

## Decisión

- Las migraciones las aplica **Flyway por línea de comandos**, como paso del despliegue, con el principal dueño del
  esquema; los scripts viven en `fplus-domain/src/main/resources/db/migration`.
- **La aplicación no migra al arrancar en ningún entorno**; su principal no tiene DDL en ninguno.
- Cada script empieza con `SET XACT_ABORT ON` (AR-SS-03). Flyway se detiene en el primer error y termina con código
  distinto de cero: el despliegue no sigue.
- La aplicación conoce la versión de esquema que espera y la compara al arrancar contra la tabla de historial de
  Flyway; si no coincide, lo dice en el log y en la pantalla de salud (AR-BD-03).

## Consecuencias

**A favor**

- Un solo camino de migración igual en todos los entornos: lo que se prueba en QA es lo que corre en PROD.
- Se cumple la intención de AR-SS-07 (detenerse en el primer error, transacción completa) con la herramienta ya elegida.

**En contra**

- En desarrollo hay que correr Flyway antes de desplegar; la guía de instalación (`docs/operacion/setup.md`) lo dice.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **`sqlcmd -b` como pide AR-SS-07** | Habría que reescribir el historial de versiones que Flyway ya lleva |
| **Migrar al arrancar en DEV y QA** | El principal de la aplicación necesitaría DDL en esos entornos: se probaría con permisos distintos a los de PROD |
| **No hacer nada** | La desviación existiría sin ADR y la contradicción con ADR-005 seguiría abierta |
