# ADR-013 · PROVEM por su endpoint, desde `fplus-reportes`, con una sola ejecución a la vez

**Estado:** Aceptado con supuesto (P-005) · 2026-10-21 — la llave del lote (compañía + unidad + producto + lote
Met-Mex) y el contrato son supuestos; cambiarlos toca solo el cliente detrás de `FuenteLotes` y el índice único del lote
**Valida:** 2026-10-21 · `nemit:validador`
**Relación:** cierra «¿Dónde vive la ingesta?» y «¿Extracción periódica o bajo demanda?» de
[06 · Integraciones](../06-integraciones.md).

## Contexto

- Los datos del lote (lote de la unidad y de Met-Mex, unidad, producto, fecha de cierre, peso seco, ponderado de la
  mina) vienen de PROVEM y ERSA ya no los captura (RF-001, RF-003, RF-038).
- El usuario respondió el 2026-10-21 que PROVEM los entrega **por un endpoint** (P-005). Faltan su contrato de datos,
  sus llaves y su autenticación.
- `fplus-captura` no habla con sistemas externos ([ADR-011](ADR-011-captura-para-internet.md)).

## Decisión

- El cliente de PROVEM vive en **`fplus-reportes`** (red interna), detrás de una interfaz de servicio
  (`FuenteLotes`), con dos disparadores:
  - **bajo demanda**, desde la pantalla («Traer lotes de PROVEM» por compañía, unidad y rango de cierre);
  - **programado**, con una tarea del servidor (`@Schedule`) cuya frecuencia es configuración de integración.
- **Una sola ejecución a la vez**, aunque haya más de una instancia: la tarea toma un candado en la base (una fila de
  `cfg` con dueño y vencimiento) antes de extraer, y si no lo obtiene, no corre.
- **Idempotente**: la llave del lote es la que defina el contrato (hoy suponemos compañía + unidad + producto + lote
  Met-Mex). Una extracción repetida actualiza, nunca duplica, y deja historia de lo que cambió.
- Lo que llega se guarda **con su origen** (`PROVEM` o `MANUAL`) y la carga cruda como evidencia, como ya preveía 06.
- **Si PROVEM no responde**, la operación sigue: los lotes ya traídos siguen disponibles, la pantalla dice desde cuándo
  no hay extracción, y queda la carga de archivo (RF-002) como contingencia.
- La dirección, credencial y frecuencia del endpoint son **configuración de integración**: con pantalla, prueba y sin
  reiniciar (AR-G-23); la credencial se guarda cifrada y nunca se muestra.

## Consecuencias

**A favor**

- `fplus-captura` sigue sin conexiones salientes; ERSA ve los lotes porque están en la base (ADR-002).
- No hay un tercer desplegable que operar.
- Una caída de PROVEM no detiene la captura.

**En contra**

- El candado en la base es una pieza más que probar (que dos ejecuciones simultáneas no dupliquen).
- El diseño del cliente queda incompleto hasta tener el contrato (P-005).

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Un componente de ingesta propio** (tercer desplegable) | Resuelve la ejecución duplicada, pero agrega un servidor que operar para una sola integración; el candado en la base resuelve lo mismo |
| **Solo bajo demanda** | Más simple, pero deja a ERSA sin lotes nuevos hasta que alguien de Fresnillo se acuerde de traerlos |
| **Que `fplus-captura` consulte PROVEM** | Rompe ADR-011: el módulo expuesto abriría una conexión saliente hacia un sistema interno |
| **No hacer nada** (carga manual de archivo) | Es lo que el cliente pidió evitar: «necesitamos que eso llegue por sistema» |
