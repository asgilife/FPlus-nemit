# Proceso de negocio — FPlus

Cómo trabaja hoy el negocio, **sin el sistema**. Es la base para decidir qué automatizar; si se describe con el sistema
ya puesto, se pierde lo que la gente hace a mano y nadie lo pidió por escrito.

Fuentes: rutas relativas a `../fuentes/`. «S1»…«S4» son las sesiones del 3, 4, 17 y 18 de septiembre de 2026; el
detalle de cada paso está en los requisitos que lo citan.

## Flujo — concentrados de plomo y zinc (ensaye por comparación)

```
Mina ──► PROVEM ──► Met-Mex recibe y arma el lote ──► muestra en 3 porciones
                                                     │
              ┌──────────────────────────────────────┼──────────────────────┐
              ▼                                      ▼                      ▼
     ERSA (por Fresnillo)                    Laboratorio Met-Mex      Respaldo (tercería)
              │                                      │
   Fresnillo revisa ERSA vs mina                     │
              └──────────► Intercambio por correo ◄──┘
                                    │
             dentro del límite ─► promedio      fuera ─► negociación o tercería ─► ensaye final
                                    │
                            liquidación (Met-Mex) ─► Fresnillo la confronta en Excel
```

## Pasos

| # | Paso | Quién | Entrada | Salida | Fuente |
|---|---|---|---|---|---|
| 1 | Pesar, muestrear y ensayar cada camión (guía) en la mina; el LIMS de la mina sube los ensayes a PROVEM. | Unidad minera | Camión de concentrado | Ensayes avisados por camión | S1 `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md` 00:07:57 |
| 2 | Agrupar camiones en lotes (1 a 15) y calcular el ponderado y el peso del lote. | PROVEM | Ensayes y pesos por camión | Ensaye ponderado y peso del lote | S1 00:09:20, 00:34:59 |
| 3 | Recibir, pesar y muestrear; componer la muestra del lote y dividirla en tres: Fresnillo, Met-Mex y respaldo para tercería. Cerrar el lote y asignar número Met-Mex. | Met-Mex | Camiones | Lote cerrado; sobres | S1 00:09:50; S2 `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md` 00:01:54 |
| 4 | Entregar los sobres al representante de ERSA que está en Met-Mex; él los lleva al laboratorio de ERSA. | Met-Mex, representante | Sobres con código de barras | Muestra en ERSA | S2 00:43:53, 00:46:44 |
| 5 | Analizar (varias áreas, unas tres personas), anotar en libreta, pasar a Excel y enviar por correo por mes y rango de lotes. | ERSA | Muestra | Excel de resultados (oro y plata normales y ajustados) | S1 00:11:17, 00:42:56 |
| 6 | Pasar el Excel de ERSA a otro archivo, compararlo contra el ponderado de la mina y, si hay dudas, pedir revisión o reensaye a ERSA (o a la mina) por correo. | Fresnillo | Excel de ERSA, ensayes de mina | Ensayes de ERSA validados | S1 00:58:36; S2 01:16:38 |
| 7 | Preparar la cédula en Excel e intercambiar ensayes con Met-Mex por correo, en simultáneo. | Fresnillo y Met-Mex | Ensayes validados | Ensayes de ambas partes | S1 00:13:51, 01:06:38 |
| 8 | Por elemento: dentro del límite, promediar; fuera, negociar (promedio o aceptar el ensaye de una parte) o ir a tercería. | Administradores de contratos | Diferencias y límites del contrato | Decisión por elemento | S2 00:06:31, 00:09:26 |
| 9 | Met-Mex manda el sobre de respaldo al tercero que toca por la decena de cierre; el tercero reporta por correo en su formato (PDF, a veces Excel) y Fresnillo lo pasa a su Excel, corrigiendo notas como «la plata incluye oro». | Met-Mex, tercero, Fresnillo | Sobre de respaldo | Resultado del tercero | S1 00:14:18; S3 `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md` 0:10 (≈ P2 00:22:26) |
| 10 | Ensaye final: con Met-Mex, el de en medio entre los tres. | Fresnillo y Met-Mex | Tres ensayes | Ensaye final | S2 00:15:10 |
| 11 | Met-Mex calcula la liquidación final; Fresnillo la vacía en Excel y la confronta con su propia proforma. Un lote no se liquida mientras algún elemento siga en tercería. | Met-Mex, Fresnillo | Ensayes finales, pesos, cotizaciones | Liquidación | S3 0:10 (≈ P2 00:08:57); S1 `…/transcripcion-grabacion-b.md` 00:12:17 |
| 12 | A fin de mes, reporte «Ajuste Balmet» de avisado contra liquidado. | Fresnillo | Lotes liquidados | Reporte | S1 00:19:15 |

## Flujo — precipitados, Doré y carbón (ensaye en conjunto)

| # | Paso | Quién | Entrada | Salida | Fuente |
|---|---|---|---|---|---|
| 1 | La mina saca muestras de referencia (lote por lote) y PROVEM registra peso y ensayes. | Unidad minera | Lote | Ensaye de referencia | S3 `…/transcripcion-parte-2.md` P2-047 00:30:35 |
| 2 | Met-Mex analiza la muestra oficial en presencia de un representante de Fresnillo y manda un PDF con el resultado (en Doré, ponderado de varias muestras y escoria). | Met-Mex | Muestra oficial | PDF de resultados | S3 0:10 (≈ P2 00:31:06) |
| 3 | Fresnillo pasa el PDF a su Excel comparativo (peso, ley y contenido, mina contra Met-Mex) y marca el estado del lote con colores. | Fresnillo | PDF | Comparativo | `documentos/2026-09-18-sesion-4/REGLAS DE NEGOCIO PARA F-PLUS PRECIPITADOS.xlsx` |
| 4 | Si la diferencia pasa de ≈1 % en oro o ≈2 % en plata, revisa con la mina (sobres plateados, testigo), pide reensaye y, si persiste, remuestreo a Met-Mex por correo con una captura del comparativo. | Fresnillo, GAC | Comparativo | Lote liberado o en remuestreo | S3 0:10 (≈ P2 00:35:19); S4 `…/transcripcion-parte-2.md` 00:58:02 |
| 5 | La GAC libera; después se liquida. | GAC | Lote revisado | Lote liquidado | `REGLAS…PRECIPITADOS.xlsx` Z8:Z9 |

## Contratos y cotizaciones

| # | Paso | Quién | Entrada | Salida | Fuente |
|---|---|---|---|---|---|
| 1 | Cada mes, tesorería en México comparte las cotizaciones; una persona las vacía en el Excel «Imprecios» y lo comparte con las unidades. | Tesorería, Fresnillo | Publicaciones de precios | Promedios mensuales | S3 0:10 (≈ P1 00:04:51) |
| 2 | Las proformas se calculan en Excel, una por producto y mina, con fórmulas que armó una persona. | Fresnillo | Contrato, ensayes, peso, cotizaciones | Valor del lote | S3 0:10 (≈ P1 00:41:07) |
| 3 | Si los términos del año no están aprobados, se aplican los del año anterior y al aprobarse se ajusta lo ya liquidado. | Fresnillo (como en Met-Mex) | Términos | Ajuste | S4 `…/transcripcion-parte-1.md` 00:20:21 |
| 4 | Para entregas diferidas acordadas con Met-Mex se pactan condiciones particulares para lotes específicos. | Fresnillo y Met-Mex | Acuerdo | Términos especiales | S3 0:10 (≈ P1 00:07:18) |

## Excepciones

- **Una guía no llega a tiempo** y pasa al lote siguiente al cierre de mes (S1 00:29:42).
- **ERSA anota mal un número** («entendieron que era un 9 y era un 4», S1 00:11:51) o **cambia su Excel sin avisar**
  (S1 00:12:51); también **mezcla lotes de distintos meses** en un envío (S1 00:42:28).
- **No se sabe cuándo entregará ERSA** (S1 `…/transcripcion-grabacion-b.md` 00:38:41).
- **Cruce de muestras**: se intercambia, pero se pide remuestreo (S1 01:03:05).
- **Resultado de tercería anómalo**: otro sobre o, si ya no hay, remuestreo; ha pasado dos veces (S2 00:14:45, 00:32:07).
- **Un tercero marca un patrón** fuera de rango y se le pide revisar todo (S1 01:13:43).
- **El laboratorio tercero equivocado**: por retrasos, urgencias o error de quien envía las muestras (S4 P1 00:14:24).
- **Remuestreos que tardan mucho** sin que nadie lleve las fechas (S4 P2 00:24:09).
- **Error de captura de peso en la mina** detectado en el comparativo (`documentos/2026-09-18-sesion-4/PROFORMAS F-PLUS.xlsx`,
  comentario en «ENSAYES EN CONJUNTO SAU»!D18).
- **Fórmulas de Excel con errores** de referencia y de lógica en el libro de intercambios de Ciénega
  (`documentos/2026-09-03-sesion-1/Formato Intercambios Ciénega 02-2026.xlsx`).
