# ADR-007 · Tema propio en un artefacto de interfaz compartido

**Estado:** Aceptada · 2026-09-11
**Precisado por:** [ADR-008](ADR-008-tres-modos-de-visualizacion.md) · tres modos de visualización

## Contexto

PrimeFaces 16 no aporta un tema listo para usar en este proyecto: hay que diseñar el tema
completo, desde los tokens de color hasta el comportamiento de cada estado de componente.

FPlus se despliega como **dos WAR independientes**
([ADR-001](ADR-001-dos-modulos-desplegables.md)) que para el negocio son **un solo
sistema**. Sin una decisión explícita, cada módulo acabaría con su propia hoja de estilos y
divergiría a la primera corrección.

Además, NFR-003 y NFR-004 priorizan rapidez y mínimo de interacciones, y NFR-023 plantea
accesibilidad: ambas cosas se deciden en el tema, no en las pantallas.

## Decisión

Se crea **`fplus-ui`**, un módulo Maven empaquetado como JAR de recursos web, del que
dependen ambos WAR. Contiene tokens de diseño, skin de componentes PrimeFaces, plantilla de
layout, componentes compuestos e iconografía.

Reglas asociadas:

- **Ningún estilo se escribe en una pantalla.** Si falta algo, se agrega al tema.
- Los colores viven en variables CSS, nunca como literales dispersos.
- Cada módulo define un **color de acento y un identificador propios**, para que el usuario
  distinga a simple vista en cuál está. Es un token por módulo, no un tema duplicado.

## Consecuencias

**A favor**

- Un solo lugar donde corregir contraste, foco, densidad o espaciado, con efecto en ambos
  módulos.
- Los dos módulos se ven como un sistema coherente sin ser el mismo despliegue.
- El esfuerzo de diseñar el tema se paga una vez.
- Los módulos siguen siendo distinguibles entre sí, que importa porque tienen padrones de
  usuario distintos y uno puede estar expuesto a internet.

**En contra**

- Tercer artefacto que versionar. Un cambio en `fplus-ui` obliga a redesplegar ambos
  módulos, igual que `fplus-domain`.
- Riesgo de que `fplus-ui` acumule lógica que no le corresponde. La frontera es estricta:
  presentación reutilizable, nunca pantallas ni reglas de negocio.
- Diseñar un tema completo es un frente de trabajo con esfuerzo propio, y requiere
  competencia de diseño, no solo de desarrollo.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **CSS propio en cada WAR** | Divergen de inmediato. Cada corrección de accesibilidad habría que hacerla dos veces, y la segunda se olvida |
| **Un solo tema idéntico sin distinción entre módulos** | El usuario no sabría en cuál está. Con padrones de identidad separados y un módulo posiblemente expuesto, la confusión tiene costo real |
| **Tema comercial de PrimeFaces** | **Conviene confirmar que se evaluó y se descartó.** Reduciría el esfuerzo de forma sustancial a cambio de licencia y de menos control sobre densidad y accesibilidad. Si se adquiere, este ADR se supersede: `fplus-ui` seguiría existiendo, pero como capa de ajuste sobre el tema comprado en lugar de tema completo |
| **Un solo modo visual** | Era el supuesto original de este ADR y resultó incorrecto: la fatiga visual en jornadas largas de captura es condición real de uso. **Precisado por [ADR-008](ADR-008-tres-modos-de-visualizacion.md)**, que establece tres modos y la disciplina de tokens semánticos que exigen |

## Pendientes

- ¿Existe guía de marca de Fresnillo aplicable?
- NFR-022: navegadores y dispositivos a soportar. Condiciona todo el CSS.
- Asignación de quién diseña el tema.
