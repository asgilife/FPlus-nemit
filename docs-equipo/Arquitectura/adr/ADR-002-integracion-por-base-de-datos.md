# ADR-002 · Integración por base de datos compartida, sin API entre módulos

**Estado:** Aceptada · 2026-09-11

## Contexto

`fplus-captura` y `fplus-reportes` son dos desplegables ([ADR-001](ADR-001-dos-modulos-desplegables.md))
que trabajan sobre el mismo dominio. El módulo de reportes necesita ver **absolutamente
todo** lo que captura el otro: es el que concilia, intercambia y resuelve el valor
aplicable.

## Decisión

Los módulos se integran **a través de la base de datos compartida**. No hay API, ni cola de
mensajes, ni llamada remota entre ellos. No se conocen: no hay dependencia de compilación
en ninguna dirección.

El mapeo del esquema se comparte como biblioteca (`fplus-domain`).

## Consecuencias

**A favor**

- Sin capa de integración que construir, versionar, documentar y mantener.
- Sin latencia añadida ni modos de fallo parcial entre módulos.
- Consistencia transaccional real: una transacción de base de datos, no consistencia
  eventual entre servicios.
- El módulo de reportes consulta el dato directamente, que es justo lo que necesita para
  informes.

**En contra**

- **El esquema es el contrato.** Un cambio afecta a los dos módulos.
- Obliga a redesplegar ambos ante cualquier cambio de esquema. No hay despliegue parcial.
- Exige disciplina: la lógica de negocio no puede filtrarse a la base de datos en forma de
  triggers o procedimientos que ningún módulo controle.
- Requiere migraciones versionadas y coordinadas
  ([ADR-005](ADR-005-migraciones-versionadas.md)) y bloqueo optimista
  ([ADR-006](ADR-006-bloqueo-optimista.md)). Ambos ADR existen por esta decisión.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **API REST entre módulos** | Agrega latencia, una capa que mantener y modos de fallo nuevos, para resolver un problema que la base de datos ya resuelve. Un equipo, un esquema |
| **Base de datos por módulo, sincronizada** | Duplica el dato y obliga a resolver consistencia eventual. El reporte necesita el dato completo y actual |
| **Cola de mensajes** | Ningún requerimiento pide procesamiento asíncrono ni desacoplamiento temporal |

## Cuándo revisar esta decisión

Si aparece un consumidor externo real y concreto —Power BI, una app móvil, una interfaz
automatizada con PROVEM—, se evalúa exponer una API **desde `fplus-reportes`**, que es donde
está el dato consolidado. Esa API no sustituiría la integración por base de datos entre los
dos módulos.
