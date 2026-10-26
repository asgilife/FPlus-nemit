# ADR-015 · Las evidencias (archivos y capturas de correo) se guardan en la base de datos

**Estado:** Propuesta · 2026-10-21 · **objeción abierta** (abajo)
**Valida:** 2026-10-21 · `nemit:validador` · no se acepta. La desviación de AR-G-17 se sostiene (dos servidores en
redes distintas sin disco común). La objeción es otra: «solo las ve `fplus-reportes`» lo cumple el código, no el motor.
`ops.evidencia` queda en el esquema `ops`, y [05](../05-seguridad-y-control-de-acceso.md) da a `svc_fplus_captura`
`SELECT` sobre todo `ops`. Si se compromete el módulo expuesto, ese principal lee el Excel de Met-Mex, los informes de
tercería y los correos. Es justo lo que [ADR-003](ADR-003-bd-unica-esquemas-separados.md) dice que contiene la
división en esquemas («la separación la impone el motor»), y lo que piden RNF-002, RF-048 y los casos de abuso de
HU-002 y HU-015. El ADR, o un ajuste de 04 y 05 que él cite, tiene que dejar `ops.evidencia` fuera del alcance de
`svc_fplus_captura`: en un esquema propio o con permisos por tabla. Lo mismo vale para `ops.ensaye_mina`,
`ops.intercambio`, `ops.decision_elemento`, `ops.terceria`, `ops.ensaye_final` y `ops.correo_saliente`. La migración
inicial del esqueleto escribe esos `GRANT`, así que hace falta antes de la ficha del esqueleto.
**Se aparta de AR-G-17** (Arquitectura de Referencia Nemit 0.9, borrador: evidencias en disco).

## Contexto

El cliente pide guardar como evidencia auditable el Excel de Met-Mex y la imagen de su correo, el archivo y el correo
de cada tercería, la hoja de trabajo del tercero y el correo de cada solicitud de remuestreo (RF-065, RF-073,
RF-082), «para ya no tener que estar buscando en los correos» (`docs/fuentes/juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`,
01:46:26). Son imágenes, Excel y PDF; ningún requisito habla de volumen, y P-045 (volúmenes) sigue abierta.

## Decisión

- Las evidencias se guardan **en la base de datos**, en una tabla propia (`ops.evidencia`: contenido, nombre original,
  tipo, tamaño, huella SHA-256, quién la subió y cuándo), ligada a lo que respalda.
- **Nunca se borran ni se reemplazan**: una evidencia equivocada se corrige subiendo otra (regla 5 de
  [06](../06-integraciones.md)).
- **Tipos permitidos**: imágenes (PNG, JPEG), PDF y Excel (`.xlsx`, `.xls`), comprobados por su contenido y no solo por
  la extensión; **tamaño máximo** por archivo como configuración (supuesto: 10 MB).
- Solo las sube y las ve `fplus-reportes`. ERSA no sube evidencias en la primera etapa.
- Se sirven por **un solo punto** que aplica el alcance de quien las pide (AR-G-16), nunca por una URL directa.
- **Por qué no en disco, como pide AR-G-17:** los dos módulos corren en Payara de redes distintas sin un disco común,
  y la base es el único respaldo que ya existe; en disco habría que respaldar y proteger otro almacén.

## Consecuencias

**A favor**

- Un solo respaldo para datos y evidencias: no pueden quedar desfasados.
- La auditoría y los permisos de la base cubren también los archivos; no hay una carpeta compartida que proteger aparte.
- No depende de una ruta de red entre el servidor de aplicación y un almacén de archivos.

**En contra**

- La base crece con los archivos. Se acepta mientras P-045 no diga otra cosa: unos cuantos archivos por lote. Si el
  volumen resulta alto, el cambio a almacenamiento externo se hace con otro ADR y una migración de contenido.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Carpeta compartida o almacén de objetos** | Otro sistema que respaldar, permisos aparte y riesgo de que la base apunte a un archivo que ya no está |
| **FILESTREAM de SQL Server** | Útil con archivos grandes; aquí agrega configuración del servidor sin una fuerza que lo pida |
| **No hacer nada** (seguir en el correo) | Es el dolor que el cliente describió |
