# Proceso de fichas

Toda tarea no trivial pasa por una ficha antes de tocar código. **Sin ficha validada no se implementa.**

```
REDACTAR → VALIDAR (sesión aparte) → IMPLEMENTAR (sesión aparte) → CERRAR (coordinación)
nemit:lider-tecnico  nemit:validador   nemit:desarrollador        nemit:coordinador
```

Cada paso en su propia sesión: quien valida no es quien escribió, y quien implementa recibe un prompt verificado contra el
código del día, no la conversación donde se redactó.

## Estados

| Marca | Significado |
|---|---|
| `[ ]` | Sin redactar |
| `[F]` | Redactada, pendiente de validar |
| `[V]` | Validada, lista para implementar |
| `[W]` | En curso |
| `[X]` | Cerrada y verificada |

El índice vive en [`../plan.md`](../plan.md). Formato de una ficha en [`_plantilla.md`](_plantilla.md); se nombra
`FICHA-NNN.md`, y si se parte, `FICHA-NNNa`, `FICHA-NNNb`.

## Trabajo en paralelo (modo equipo)

Si el perfil del proyecto declara **modo equipo**, cada ficha se trabaja en su rama (`ficha/NNN-titulo`) desde la rama
de integración, y se integra según el perfil: **directo** (el coordinador revisa el diff de la rama) o **con PR** (la
revisión de cierre se hace sobre el pull request). Dos fichas van en paralelo solo si sus **zonas** no se cruzan;
el coordinador arma las olas, reserva el número de migración de cada una y fija el orden de integración. La regresión
diaria corre sobre la rama de integración; **si queda en rojo, no se integra nada más hasta que vuelva a verde**, y si la
corrección no es inmediata se revierte la integración culpable. En modo individual, todo en la misma rama con commits
por pathspec.

## Preguntas abiertas

**Una ficha con preguntas abiertas no se implementa.** Resolver una pregunta no es anotarla: es bajarla a una decisión
con su porqué, a los cambios, a los casos de prueba y a los criterios. Las técnicas las decide la validación; las de
negocio se le llevan al usuario en lenguaje llano, con una recomendación y siempre con «la alternativa que no agrega
nada».

## Pruebas

- **Cada caso empieza en rojo**: antes de tocar código se comprueba que el caso falla por la razón correcta, y se anota
  cómo falló. Un caso que nunca falló no prueba nada.
- «did not run» y «skipped» no son verdes: se lee el resumen final.
- **Una ficha cierra con** sus casos, las suites de su zona y las unitarias de lo que toque. La regresión completa corre
  una vez al día, con `nemit:qa`.

## Lo que se encuentra en el camino se corrige

Un defecto hallado durante una ficha se corrige en la misma sesión, con un caso que primero falle y en commit propio. Se
para y se consulta solo si pide decisión de negocio, una migración, un cambio de contrato entre componentes o es más que
un arreglo chico. **Ningún hallazgo queda sin dueño**: lo que no se corrige entra el mismo día al plan o a una ficha.
