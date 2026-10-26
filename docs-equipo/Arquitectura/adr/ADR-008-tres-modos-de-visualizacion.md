# ADR-008 · Tres modos de visualización: claro, descansa vista y oscuro

**Estado:** Aceptada · 2026-09-11
**Relación:** precisa [ADR-007](ADR-007-tema-propio-compartido.md), que había dejado los
modos alternativos fuera de alcance.

## Contexto

[ADR-007](ADR-007-tema-propio-compartido.md) estableció que el tema se diseña completo en
`fplus-ui`. Al redactarlo se asumió un solo modo visual, dejando claro y oscuro como
evolución futura.

Esa lectura era incorrecta para este sistema. `fplus-captura` se usa para capturar valores
numéricos durante jornadas largas y `fplus-reportes` para leer tablas comparativas densas.
La fatiga visual es una condición real de uso, no una preferencia.

Además, el momento importa: la estructura de tokens de un tema de un solo modo y la de uno
de tres modos **no son la misma**, y convertir la primera en la segunda es una reescritura.

## Decisión

El tema se construye desde el inicio con **tres modos**: **claro** (por omisión),
**descansa vista** y **oscuro**.

Reglas asociadas:

- **Los tokens de diseño son semánticos, nunca literales.** `--superficie-base`,
  `--texto-primario`, `--estado-fuera-de-limite`. Prohibido `--gris-claro` o `--azul-marca`:
  el nombre deja de ser cierto al cambiar de modo.
- Los tres modos **redefinen el mismo conjunto de tokens**. Ningún componente conoce el modo
  activo.
- El modo se aplica con un atributo en la raíz del documento
  (`data-tema="claro | descansa-vista | oscuro"`). Nada más cambia.
- **El cambio de modo no recarga la página ni pierde el estado del formulario.**
- La preferencia se guarda **en el esquema de identidad de cada módulo** (`seg_cap`,
  `seg_rep`), no en una tabla compartida
  ([ADR-004](ADR-004-identidad-segregada-por-modulo.md)).
- **Impresión y exportación usan siempre la paleta clara**, sea cual sea el modo activo.

## Consecuencias

**A favor**

- Atiende una condición real de uso: jornadas largas frente a datos numéricos densos.
- La disciplina de tokens semánticos, adoptada desde el día uno, cuesta poco y vuelve el
  tema mucho más mantenible aunque nunca se agregaran más modos.
- Permite respetar la preferencia del sistema operativo en el primer acceso.

**En contra**

- **La superficie de verificación de accesibilidad se triplica.** Todo contraste y todo
  estado se valida en tres paletas, no en una (NFR-023).
- Más trabajo de diseño: tres paletas coherentes, no una con variantes improvisadas.
- **"Descansa vista" tiene una tensión interna**: bajar la agresividad visual sin caer bajo
  el contraste mínimo legible. El mínimo legible manda sobre la suavidad, y esto debe
  verificarse, no suponerse.
- Los colores de estado deben funcionar en los tres modos. Refuerza que el color nunca sea
  el único portador de información.
- Una preferencia más que persistir, y por la segregación de identidad, **dos veces** para
  quien tenga cuenta en ambos módulos.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Un solo modo claro** | Era el supuesto de ADR-007. Ignora la fatiga visual en jornadas largas de captura, que es condición real de uso |
| **Claro y oscuro solamente** | Deja fuera el caso que más lo justifica aquí: la jornada larga con buena luz ambiental, donde el modo oscuro no ayuda y el claro cansa |
| **Agregar los modos más adelante** | El punto caro no son las paletas, es la estructura de tokens. Hacerlo después obliga a reescribir el CSS completo |
| **Guardar la preferencia en una tabla de usuarios compartida** | Rompería la segregación de identidad de ADR-004. Una preferencia visual no justifica abrir un padrón común |

## Pendientes

- Definición de las tres paletas concretas: **en aprobación, previstas hacia el 25 de septiembre de 2026**. Llegarán como imágenes de los tres temas; de ahí se extraen los tokens semánticos.
- Criterio y herramienta de verificación de contraste en los tres modos (NFR-023).
