# Backlog — FPlus

Índice de las historias de usuario, priorizadas. Las escribe `nemit:product-owner` a partir de `requisitos/`, **una por
archivo** en [`historias/`](historias/) (`HU-NNN.md`, con [`_plantilla.md`](historias/_plantilla.md)). Cada historia
será la cabecera de su ficha.

**Cada cosa vive en un solo lugar**: lo que la historia dice (rol, acción, beneficio, requisitos, criterios) en su
archivo; su prioridad, su estado, su prototipo y su ficha, solo en la tabla de abajo. Cambiar de estado una historia
toca esta tabla, no su archivo.

## Estados

`Por diseñar` → `Prototipo` (su pantalla en `../prototipo/`) → `Aprobada` (el cliente la usó) → `En ficha` → `Hecha`

## Resumen

| HU | Título | Prioridad | Estado | Prototipo | Ficha |
|---|---|---|---|---|---|
| [HU-001](historias/HU-001.md) | Encontrar los lotes que me tocan, con sus datos ya cargados | P1 | Prototipo | `prototipo/web/…/vistas/ensayes/captura-ersa.xhtml` | — |
| [HU-002](historias/HU-002.md) | Capturar los ensayes de un lote | P1 | Prototipo | `prototipo/web/…/vistas/ensayes/captura-ersa.xhtml` | — |
| [HU-003](historias/HU-003.md) | Revisar y autorizar la captura antes de entregarla a Fresnillo | P1 | Prototipo | `prototipo/web/…/vistas/ensayes/captura-ersa.xhtml` | — |
| [HU-004](historias/HU-004.md) | Exportar lo capturado en el formato de ERSA | P2 | Por diseñar | — | — |
| [HU-005](historias/HU-005.md) | Dar de alta el extracto de contrato con sus elementos y límites | P1 | Por diseñar | — | — |
| [HU-006](historias/HU-006.md) | Revisar los ensayes de ERSA contra los de la mina | P1 | Prototipo | `prototipo/web/…/vistas/ensayes/revision-fresnillo.xhtml` | — |
| [HU-007](historias/HU-007.md) | Regresar un lote a ERSA y recibir su confirmación o corrección | P1 | Prototipo | `revision-fresnillo.xhtml` + `captura-ersa.xhtml` | — |
| [HU-008](historias/HU-008.md) | Consultar la historia de los ensayes revisados | P2 | Por diseñar | — | — |
| [HU-009](historias/HU-009.md) | Generar la cédula de intercambio | P2 | Por diseñar | — | — |
| [HU-010](historias/HU-010.md) | Cargar los ensayes de Met-Mex y ver qué se promedia y qué va a tercería | P2 | Por diseñar | — | — |
| [HU-011](historias/HU-011.md) | Decidir por elemento: promediar, aceptar un ensaye o ir a tercería | P2 | Por diseñar | — | — |
| [HU-012](historias/HU-012.md) | Registrar el resultado de la tercería y obtener el ensaye final | P2 | Por diseñar | — | — |
| [HU-013](historias/HU-013.md) | Registrar una tercería anómala y la historia de sus resultados | P3 | Por diseñar | — | — |
| [HU-014](historias/HU-014.md) | Dar acceso a los usuarios con los roles que les tocan | P2 | Por diseñar | — | — |
| [HU-015](historias/HU-015.md) | Solicitar y consultar remuestreos | P2 | Por diseñar | — | — |
| [HU-016](historias/HU-016.md) | Que el remuestreo reabra la captura y deje vigente el último ensaye | P2 | Por diseñar | — | — |
| [HU-017](historias/HU-017.md) | Ver cuánto se promedia, cuánto va a tercería y cómo nos va con cada tercero | P2 | Por diseñar | — | — |
| [HU-018](historias/HU-018.md) | Medir los tiempos del ciclo y comparar fuentes de ensaye | P3 | Por diseñar | — | — |
| [HU-019](historias/HU-019.md) | Llevar el ciclo de vida del extracto | P3 | Por diseñar | — | — |

## Restricciones que aplican a todo

No funcionales transversales: toda historia y toda ficha las respeta.

| Requisito | Restricción |
|---|---|
| RNF-001 | Toda acción sobre ensayes, decisiones y catálogos queda registrada (quién, qué, cuándo, valor anterior); nada se borra. |
| RNF-002 | La información es confidencial. |
| RNF-003 | Sistema interno de Fresnillo que ERSA usa como usuario externo: al inicio por VPN con equipos prestados, después por internet; se diseña para internet desde el principio. |
| RNF-004 | Fresnillo entra con su inicio de sesión corporativo; ERSA, con usuario externo. |
| RNF-005 | Un externo solo accede al aplicativo, con los controles de TI de Fresnillo. |
| RNF-006 | ERSA solo ve lo de Fresnillo. |
| RNF-007 | Los laboratorios terceros no acceden al sistema. |
| RNF-008 | Ágil y práctico; la captura de ERSA, muy fácil (sin cifras todavía: P-045). |
| RNF-009 | La captura de ERSA se hace en computadoras fijas con lector de código de barras. |
| RNF-010 | Lo que se exporta no trae encabezados repetidos ni celdas combinadas, y cada valor dice su unidad. |
| RNF-011 | Decimales y redondeo definidos por paso (por precisar: P-046). |
| RNF-012 | «No analizado» no es cero. |
| RNF-013 | Catálogos en lugar de valores fijos (representante, laboratorios, Incoterm, moneda…). |
| RNF-014 | Los cálculos se pueden verificar contra los controles del usuario. |
| RNF-015 | El sistema es de Fresnillo, separado de PROVEM; no modifica el sistema de embarques ni PROVEM. |
| RNF-016 | Apariencia corporativa sobria, en claro y oscuro (por precisar). |
| Todos | **Ninguna historia pasa a ficha mientras sus requisitos estén en «Propuesto» o «Por precisar».** Hoy todos lo están: la revisión de requisitos con el cliente, que acordamos el 4 de septiembre, es la que los confirma. |

## Decisiones de priorización

| Fecha | Decisión | Quién | Por qué |
|---|---|---|---|
| 2026-10-21 | Primera etapa: captura de ERSA y análisis de intercambio y tercería; del extracto solo elementos, pagables y castigables y límites. Lo demás, en la siguiente etapa. | Usuario (respuestas a P-002 y P-003) | Falta confirmarlo con el cliente en junta. |
| 2026-10-21 | Primer corte para el prototipo: HU-001, HU-002, HU-003, HU-006 y HU-007, como un solo recorrido de punta a punta. | Propuesta de `nemit:product-owner` | Es el caso principal del cliente: «el punto justo que queremos corregir es eso que nos reporta ERSA» (sesión 2, 00:06:00). |
| 2026-10-21 | Remuestreos (HU-015, HU-016), reportes (HU-017, HU-018) y ciclo del extracto (HU-019) quedan en P2 y P3 como supuesto mientras se responden P-051 a P-053. | Supuesto de `nemit:product-owner` | El usuario no los incluyó ni los excluyó de la primera etapa. |
