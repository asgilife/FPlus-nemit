# Requisitos

Lo que el sistema debe hacer y cumplir, **cada requisito con su cita y su origen**. Los escribe `nemit:analista` a partir
de `../fuentes/`.

## El origen se dice siempre

| Origen | Qué significa | Cómo se trata |
|---|---|---|
| **Norma** | Lo exige una ley, reglamento o norma oficial | No se negocia; se cita el artículo |
| **Cliente** | Lo pidió el cliente: en una junta, en un documento | Se cita dónde y quién (por rol) |
| **Deducción nuestra** | Lo inferimos para que lo demás funcione | **Se le pregunta al cliente** antes de darlo por bueno |

Presentar una deducción como pedido del cliente es la forma más común de construir algo que nadie pidió.

## Archivos

- `requisitos.md`: los requisitos funcionales (`RF-NNN`) y no funcionales (`RNF-NNN`).
- `preguntas-abiertas.md`: lo que hay que preguntarle al cliente, incluidas las contradicciones entre fuentes.

## Estados

`Propuesto` (extraído, sin confirmar) · `Por precisar` (se pidió, pero todavía no es verificable: tiene su pregunta) →
`Confirmado` (el cliente o la norma lo sostienen) · `Descartado` (con su porqué; no se borra).
