# ADR-006 · Bloqueo optimista para edición concurrente

**Estado:** Aceptada · 2026-09-11

## Contexto

Dos módulos desplegados por separado escriben sobre la misma base de datos
([ADR-002](ADR-002-integracion-por-base-de-datos.md)). NFR-012 señala la edición concurrente
como riesgo, aunque **el comportamiento esperado ante un conflicto no está validado con
negocio**.

NFR-003 prioriza la rapidez de las tareas frecuentes, y en el escenario expuesto el módulo
de captura puede estar del otro lado de una red lenta.

## Decisión

**Bloqueo optimista** como estrategia general: `@Version` en toda entidad modificable, con
manejo explícito de `OptimisticLockException` en la capa de servicio y un mensaje de negocio
comprensible en la vista.

El bloqueo pesimista queda reservado para casos puntuales que lo justifiquen, no como
estrategia general.

## Consecuencias

**A favor**

- No se retienen bloqueos de base de datos mientras un usuario piensa frente a un
  formulario, lo cual sería especialmente dañino con un módulo en red externa.
- No penaliza el caso común, que es que dos personas **no** editen el mismo lote a la vez.
- Los conflictos se detectan siempre: nadie pierde un cambio en silencio.

**En contra**

- El usuario que pierde la carrera debe repetir su trabajo. Con formularios de captura
  largos, es molesto.
- Exige manejar `OptimisticLockException` en todos los flujos de escritura; olvidarlo
  produce un error técnico crudo en pantalla.
- No previene el conflicto: lo detecta.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Bloqueo pesimista general** | Retiene bloqueos durante la interacción humana. Penaliza NFR-003 y es frágil con un módulo en red externa |
| **Última escritura gana** | Pérdida silenciosa de cambios. Incompatible con NFR-002, que prohíbe la sobrescritura silenciosa |
| **Bloqueo funcional del lote durante la revisión** | Posible, pero es una **decisión de negocio**, no técnica. Depende de NFR-012 y P02, ambos abiertos |

## Pendiente

NFR-012 sigue sin validar. Falta decidir con negocio qué debe ocurrir ante un conflicto:
¿avisar y reintentar? ¿bloquear el lote mientras dura la revisión? ¿quién tiene precedencia?
Esta decisión puede modificar o superseder este ADR.
