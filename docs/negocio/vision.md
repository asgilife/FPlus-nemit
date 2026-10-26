# Visión — FPlus

Sistema de seguimiento a ensayes de lotes de concentrado, desde el muestreo en mina hasta el ensaye aplicable acordado con la contraparte.

Fuentes: rutas relativas a `../fuentes/`. Solo lo que dijo el cliente; nada de lo que prometimos nosotros.

## El problema

El seguimiento de los ensayes de cada lote vive en libretas, Excel y correos, y eso cuesta dinero:

- **Errores de transcripción de ERSA que se convierten en pérdidas.** «hemos tenido problemas de pérdidas en los
  intercambios de ensayes porque nos comportan mal el ensayo.» — `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`,
  00:11:17. «el punto justo que queremos corregir es eso que nos reporta ERSA» —
  `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:06:00.
- **Cambios sin aviso.** «a veces nos pasan los Excel y luego hacen cambios a los Excel y no nos dicen que cambiaron los
  Excel» — `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:12:51.
- **Muchas tercerías, que cuestan.** «Hoy nos estamos viendo muchísimo a tercería.» — misma fuente, 00:16:17; «lo que
  quieren es dinero por costo de tercería y es dinero por costo financiero» —
  `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-b.md`, 00:12:17.
- **Doble captura y errores de tecleo** al pasar archivos de Met-Mex y de los terceros: «lo que no queremos es que
  estemos capturando y que vaya a haber algún error de captura» —
  `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:28:31).
- **Evidencias dispersas en correos** cuando llega una auditoría: «estaría bien hasta para las auditorías, para ya no
  tener que estar buscando en los correos» — `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:46:26.
- **Un sistema compartido que no se puede adaptar.** PROVEM es de Peñoles: «Ahorita es una sola versión y no se le puede
  modificar absolutamente nada porque no son instancias separadas.» —
  `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:01:05.
- **Contratos complejos y cambiantes**, calculados en Excel que armó una persona: «Tener todas las variantes posibles,
  sí, porque un solo archivo no representa todas las acciones. Ese es el tema.» —
  `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:42:13).

## Para quién

| Actor | Qué hace hoy | Qué necesita |
|---|---|---|
| ERSA (representante) | Analiza la muestra oficial, anota en libreta, pasa a Excel y manda por correo. | Capturar una vez, en un sistema sencillo, con los datos del lote ya puestos. |
| Fresnillo: quien revisa ensayes y administra contratos | Pasa el Excel de ERSA a otro archivo, compara contra la mina, intercambia con Met-Mex, decide promedio o tercería, vacía resultados de terceros. | Ver todo en un lugar, con diferencias marcadas, historia y evidencias, para decidir mejor. |
| Fresnillo: quien calcula y confronta liquidaciones | Lleva cotizaciones y proformas en Excel y confronta la liquidación de Met-Mex. | Que el sistema calcule con el extracto y haga el «match». |
| Gerencia Administrativa | (Según la junta del 14 de agosto, por confirmar) negocia con los terceros. | Argumentos para aceptar o rechazar un resultado de tercería. |
| Contabilidad | Registra ajustes cuando cambian los términos. | Ajustes por concepto. |
| Met-Mex, laboratorios terceros | Contrapartes: mandan archivos y correos. | No son usuarios del sistema. |

## Qué espera ganar el cliente

- **Decidir con toda la información a la mano**: «necesitamos que esté todo aquí para que ellos puedan tomar decisiones
  mejores.» — `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:49:27.
- **Detectar a tiempo** qué falló (muestreo, ERSA, límites, terceros): «Pero eso nos ha ayudado a detectar
  oportunamente, pues que algo sucedió con la más» — `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`,
  00:15:47.
- **Medir**: cuánto se promedia y cuánto va a tercería, con qué tercero se gana o se pierde, cuánto tarda cada etapa:
  «¿Estamos haciendo tercería en qué elementos? ¿Con qué tercero perdemos más? Todo eso.» —
  `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:49:49.
- **El sistema apoya, no decide**: «Porque al final de cuentas el sistema no va a tomar las decisiones por ellos.» —
  misma fuente, 01:49:27.

## Qué entrega el sistema

Según lo dicho hasta hoy (alcance por confirmar, P-002 y P-003):

- **Captura de ensayes del representante**, con su flujo de revisión y autorización y la devolución desde Fresnillo.
- **Análisis de Fresnillo**: ERSA contra mina, intercambio con Met-Mex, tercería, remuestreos.
- **Extracto de contrato**, base de elementos, límites y, más adelante, del cálculo económico.
- **Comparativo de ensayes en conjunto** (precipitados, Doré, carbón).
- **Reportes** de promedio y tercería, laboratorios y tiempos.

## Qué no hace

- **No reemplaza el sistema de embarques ni PROVEM**; toma sus datos. La migración de PROVEM queda para después
  (Acuerdos, 2026-09-03).
- **No hace el intercambio con Met-Mex**: sigue por correo (Acuerdos, 2026-09-03).
- **No da acceso a los laboratorios terceros** (RNF-007).
- **No modela lo que pasa dentro de Met-Mex**: «ahí sí es para ustedes una caja negra» —
  `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:05:29.
- **No es un LIMS para ERSA** ni se conecta a equipos de laboratorio en esta etapa (Acuerdos, 2026-09-03).
- **No toma decisiones** de promedio o tercería: sugiere y marca.
