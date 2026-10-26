# Fuentes

Todo lo que entregó el cliente, **tal como llegó**. Es la evidencia de cada requisito: si se edita, deja de serlo.

- **No se corrige ni se resume aquí.** El análisis va a `../requisitos/` y `../negocio/`.
- **Se agrega, nunca se borra.** Si una versión nueva de un documento sustituye a otra, se guardan las dos.

## Juntas

Una carpeta por junta: `juntas/AAAA-MM-DD-tema/`

| Archivo | Qué es |
|---|---|
| `audio.m4a` (o `.mp3`, `.mp4`) | La grabación. **Queda fuera de git** (pesa y lleva voces de terceros); se respalda aparte |
| `transcripcion.md` | La transcripción completa, con marcas de tiempo si la herramienta las da. Es lo que lee `nemit:analista`. Si la grabación viene en varias partes: `transcripcion-parte-1.md`, `-parte-2.md`… (cada una con su propio reloj) |
| `notas.md` | Opcional: quién asistió (por rol), acuerdos, material que se mostró |

Para que una cita se pueda verificar, la transcripción conserva las marcas de tiempo y no se limpia: un requisito cita
«`juntas/2026-10-08-recepcion/transcripcion.md`, 00:14:32».

## Documentos

`documentos/`: PDF, hojas de cálculo, fotos de formatos en papel, procedimientos y normas. Nombre del archivo tal como
llegó, o con fecha delante si hay varias versiones.
