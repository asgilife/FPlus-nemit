# Requisitos — FPlus

Formato en [README.md](README.md). Los descartados se quedan, con su porqué.

**Cómo leer las fuentes.** Las rutas son relativas a `../fuentes/`. Las transcripciones son automáticas y conservan sus
errores («Elsa», «Erza» = ERSA; «Memex», «Menmex», «Medmex» = Met-Mex; «Proven», «provea», «programa» = PROVEM): las
citas se copian tal cual. En las de Teams de la sesión 3 casi toda la junta es un solo bloque (`0:10`); por eso esas
citas llevan, además, la ubicación aproximada del mismo pasaje en la transcripción de Whisper (`P1`/`P2`). **Dicho
por** dice el lado: «cliente» es alguien de Fresnillo cuyo rol todavía no se conoce; los nombres de quién habló en cada
junta están en el `notas.md` de la junta.

**Alcance de esta primera pasada.** Cinco juntas (14 de agosto y 3, 4, 17 y 18 de septiembre de 2026) y los documentos
que el cliente mostró en ellas. Los requerimientos preliminares v0.1 de `docs-equipo/` son material nuestro y **no** se
usaron como fuente.

**Alcance de la primera etapa** (respuestas del usuario del 2026-10-21 a P-002 y P-003; falta que el cliente lo
confirme en junta). La primera etapa es **la captura de ERSA y el análisis de intercambio y tercería**; del extracto solo
se usan los elementos, cuáles son pagables y castigables y los límites de partición.

| Etapa | Requisitos |
|---|---|
| Primera | RF-001, RF-003 a RF-009, RF-011 a RF-014, RF-020, RF-021, RF-036 a RF-062, RF-064 a RF-070, RF-100, RF-102 y todos los RNF |
| Siguiente | RF-010, RF-015 a RF-019, RF-022 a RF-025, RF-035, RF-072, RF-081 a RF-093 |
| Por confirmar | RF-002 (contingencia), RF-026 a RF-034 (gestión del extracto: flujo, copia, sustitución, lotes específicos), RF-063, RF-071, RF-073 a RF-080 (remuestreos), RF-094 a RF-099 (reportes), RF-101, RF-103 a RF-107 |

## Funcionales

### A. Datos del lote que vienen de otros sistemas

### RF-001 — Traer de PROVEM los datos del lote cerrado

- **Enunciado:** El sistema obtiene de PROVEM, para cada lote cerrado, el número de lote de la unidad, el número de lote
  Met-Mex, la unidad, el producto, la fecha de cierre, el peso seco y las guías (camiones) que lo forman, sin que nadie
  los capture.
- **Cita:** «Estas son las pantallas que es importante traernos de provea, porque ahí es donde está el registro, ahí es donde están los pesos, ahí es donde están todos los ensayos una vez que los publican.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:05:03
- **Dicho por:** cliente (quien presentó el proyecto al equipo de Fresnillo)
- **Cita (2):** «nosotros aquí estamos buscando que esos datos de lo que me administra unidad, fecha de cierre y peso seco, lo traiga el PROVEM que a su vez trae esa información de cierre.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:48:20
- **Cita (3):** «El número de lote, mina y el número de lote, menax lo traerá de proven.»
- **Fuente (3):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:46:24
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** cómo se conecta con PROVEM (interfaz, archivo, base de datos) no está decidido: ver P-005.
- **Respuesta (2026-10-21):** los datos de PROVEM llegarán por un endpoint (respuesta del usuario; falta el
  contrato de datos). Con eso RF-002 queda solo como contingencia.
- **Historias:** HU-001

### RF-002 — Mientras no haya conexión, cargar los archivos de PROVEM

- **Enunciado:** Si la conexión con PROVEM no está lista, el sistema permite cargar los archivos que hoy se exportan de
  PROVEM (lotes cerrados, embarques) para obtener los mismos datos.
- **Cita:** «si en estas semanas que tenemos de inscripción no está la conexión cómo, pues es cargada este exen directo.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:36:35
- **Dicho por:** no identificado (pasaje confuso; la pregunta parece nuestra)
- **Origen:** Deducción nuestra
- **Estado:** Propuesto
- **Respuesta (2026-10-21):** PROVEM expondrá un endpoint (RF-001); esta carga solo haría falta si el endpoint se
  retrasa.
- **Historias:** — (contingencia si el endpoint de PROVEM se retrasa)

### RF-003 — Ensaye avisado de la mina: ponderado de los ensayes por camión

- **Enunciado:** El sistema usa como ensaye de la mina para cada lote el ponderado de los ensayes de cada camión que lo
  forma; ese ponderado es referencia, no resultado oficial, y no se sustituye cuando hay remuestreo.
- **Cita:** «ahí no hace un compósito físico, ahí es un ensayo, pondrá de los ensayes individuales de cada camión»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:10:19
- **Dicho por:** cliente
- **Cita (2):** «Excepto el ponderado, ese ya se quedó. El de la mina.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:32:35
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** no está claro si el ponderado lo calcula el sistema o lo trae de PROVEM (P-006).
- **Historias:** HU-006

### RF-004 — Peso del lote: suma de los camiones

- **Enunciado:** El peso de un lote, en la mina y en Met-Mex, es la suma del peso de los camiones que lo forman.
- **Cita:** «para sacar el peso total de lote, ahí sí es la suma del peso de cada uno de los camiones en los dos casos, eso pasa solo en los ensayes.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:34:59
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-001

### RF-005 — La composición del lote es la del cierre, no la planeada

- **Enunciado:** El sistema toma la composición del lote tal como quedó al cierre, incluido el caso en que una guía
  planeada para un lote pasa al siguiente.
- **Cita:** «Y una guía no alcanzó a llegar y formaba parte de los 200. Nosotros tenemos que hacerlo de la modificación y ya esa guía ya no va a formar parte de los 200. Ahora va a ser 201»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:29:42
- **Dicho por:** cliente
- **Origen:** Deducción nuestra (la excepción la describe el cliente; que el sistema la refleje lo deducimos)
- **Estado:** Propuesto
- **Historias:** HU-001

### RF-006 — Un lote pertenece a una sola unidad

- **Enunciado:** Cada lote pertenece a una sola unidad: la que le vende a Met-Mex, aunque otra unidad lo haya maquilado.
- **Cita:** «No, un lote siempre es de una sola unidad.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:29:02
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** el mismo cliente advirtió: «Yo también aprendí eso, de cosas que decía no, eso no pasa, después sí pasaba.»
  (01:30:23). Conviene no cerrar el modelo de datos a esta regla sin confirmarla (P-020).
- **Historias:** HU-001

### RF-007 — Fecha de entrega de la muestra de Met-Mex al representante: por interfaz o capturada

- **Enunciado:** El sistema obtiene de Met-Mex, por interfaz, la fecha en que Met-Mex entrega los sobres al
  representante de ERSA y el tipo de muestra; si Met-Mex no la proporciona, la captura ERSA.
- **Cita:** «Nosotros estamos pidiendo dos formas. La primera forma más fácil sería que fuera una interfaz. Esa información la tiene BDO que está en Met-Mex.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:43:53
- **Dicho por:** cliente
- **Cita (2):** «la idea que va a ser que ellos llenen la fecha de recepción de la muestra si es que MEDMEX no nos quiere transferir la información»
- **Fuente (2):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:48:11
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** planteado en condicional: depende de que Met-Mex lo autorice (P-007).
- **Historias:** HU-001

### B. Extracto de contrato

### RF-008 — Extracto de contrato por compañía, unidad, producto y vigencia

- **Enunciado:** El sistema mantiene extractos de contrato por compañía, unidad y producto, cada uno con su vigencia
  (fecha inicial y final), cliente, número de contrato y adendum (opcional); a cada lote se le aplica el extracto
  vigente según su fecha.
- **Cita:** «Nosotros tenemos que hacer un extracto de contrato por unidad.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:39:49
- **Dicho por:** cliente
- **Cita (2):** «entonces no hay un solo contrato, sería un contrato por producto, así es y por mina y por mina»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:12:44)
- **Cita (3):** «Aquí viene la compañía, la unidad, el producto que viene siendo concentrado de plomo, la fecha inicial del contrato y la vigencia cuando expira. El cliente, el número del contrato.»
- **Fuente (3):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:07:51
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota de prioridad:** «lo primero, la verdad, es el extracto, que es la base de todo.» (sesión 3, ≈ P2 00:51:01).
  Para la primera parte (ensayes) solo hacen falta los elementos, cuáles son pagables y castigables y los límites de
  partición (RF-019).
- **Historias:** HU-005

### RF-009 — Vigencia aplicable a lotes cerrados (entregados) o embarcados

- **Enunciado:** Cada extracto indica si su vigencia se aplica por la fecha de cierre del lote (lotes entregados) o por
  la fecha de embarque, y el sistema elige el extracto según esa fecha.
- **Cita:** «tenemos que indicar si esa vigencia es para lotes cerrados o lotes embarcados, porque con terceros sí aplica que es como yo embarqué»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:42:23
- **Dicho por:** cliente
- **Cita (2):** «En nuestro caso, con NEMEX, todo es para los lotes que entregamos. ¿Creen ustedes necesario pensando en que esto pueda crecer y que tenemos también ventas con terceros?»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:08:55
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota de prioridad:** previsión: hoy con Met-Mex todo es por lotes entregados («Ahorita no la tenemos.», sesión 4,
  00:10:29).
- **Historias:** HU-005

### RF-010 — Incoterm del extracto, de un catálogo

- **Enunciado:** El extracto registra el Incoterm con su punto de entrega (p. ej. DAP Torreón), elegido de un catálogo.
- **Cita:** «Ah, pues a lo mejor incoterm, agregarle, porque va a ser esa mezcla. Ok. Hay que agregarle también el incoterm, por favor.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:10:57
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-011 — Tipo de determinación del ensaye: comparación o conjunto

- **Enunciado:** El extracto indica si los ensayes del producto se determinan por comparación (cada parte ensaya, hay
  intercambio, límites y tercería) o en conjunto (análisis conjunto con Met-Mex, sin límites ni tercero); el lote sigue
  el flujo que le corresponde.
- **Cita:** «podemos poner el también en el extracto si es de comparación o es de conjunto para que ustedes sepan porque lado va a viajar.»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:37:30)
- **Dicho por:** cliente (probable)
- **Cita (2):** «ensayas, en este caso sería conjunto. Ensayo final no aplica, límites de aparición, en este caso tampoco aplica, así como tampoco laboratorio tercero.»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:31:40
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-005

### RF-012 — Regla de ensaye final del extracto, de un catálogo

- **Enunciado:** En los extractos de comparación, el extracto indica la regla para fijar el ensaye final tras una
  tercería, elegida de un catálogo (p. ej. «el de en medio» o «promedio del tercero con el más cercano»); en conjunto
  no aplica.
- **Cita:** «ensaye tercero y ya tomas el ensaye final de acuerdo al arreglado de negocio, que puede ser, en este caso con Met-Mex, el ensaye final es el de en medio, de cualquiera de las, entre los tres, el de en»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:15:10
- **Dicho por:** cliente
- **Cita (2):** «Pero a veces hay otras reglas. Esas también se las podemos indicar.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:15:31
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** la lista completa de reglas y cómo se calcula cada una está pendiente (P-011).
- **Historias:** HU-005

### RF-013 — Límites de partición por elemento (y rango de ley) en el extracto

- **Enunciado:** El extracto guarda, por cada elemento que interviene en pagos y castigos, el límite de partición
  (diferencia máxima entre el ensaye de Fresnillo y el de la contraparte para promediar sin ir a tercería), en una
  tabla por rangos de ley que se puede ampliar.
- **Cita:** «necesitamos generar ese extracto que indica cómo te van a pagar, cómo te van a castigar y cuáles son los límites de partición, porque sí pueden variar.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:14:03
- **Dicho por:** cliente
- **Cita (2):** «hay límites de partición que también los vamos a tener que indicar dónde vamos a poner todos los elementos que jueguen en los pagos y en los castigos»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:18:46)
- **Cita (3):** «Ahí necesitaríamos tal vez una tabla a irla agregando»
- **Fuente (3):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:12:47
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** contradicción sobre de qué depende el límite (C-002).
- **Historias:** HU-005

### RF-014 — Los elementos a capturar salen del extracto, no de un catálogo general

- **Enunciado:** Al capturar ensayes de un lote, el sistema muestra solo los elementos que el extracto vigente marca
  para esa unidad y producto, en el orden del extracto (agrupados en pagables y castigables), incluidos elementos como
  insoluble, fierro y óxido de magnesio cuando el extracto los incluye.
- **Cita:** «cuáles son los elementos que intervienen para esa mina producto. Y de ahí que te desplegue para ese periodo, también para esa vigencia, por fecha de cierre de lote, cuáles son los elementos que me tiene que llenar.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:41:31
- **Dicho por:** cliente
- **Cita (2):** «El orden del extracto te voy a decir, de hecho te los agrupa por pagables.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:04:28
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** el cliente cambió aquí lo que decía su propio Excel («considerar catálogos de elementos»); ver Acuerdos.
- **Historias:** HU-002, HU-005

### RF-015 — Fórmulas de pago, deducciones y castigos configurables por extracto

- **Enunciado:** Un usuario autorizado define en cada extracto, sin programar, qué elementos se pagan y cuáles se
  castigan, con qué tipo de fórmula y con qué valores; una vigencia nueva puede agregar o quitar elementos.
- **Cita:** «Sí que cuando nosotros hagamos un contrato y le digamos una vigencia, nosotros podamos meterle las fórmulas a como las requiramos.»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:14:59)
- **Dicho por:** cliente
- **Cita (2):** «Sí, pero sí tiene que ser muy dinámico, muy dinámico todo.»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:43:53)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-016 — Fórmulas como redacción con valores: elegir el tipo y llenar los campos

- **Enunciado:** Para cada concepto del extracto, el usuario elige un tipo de redacción (plantilla de fórmula) y llena
  solo sus valores; el sistema muestra el extracto como texto legible y calcula exactamente con esa regla.
- **Cita:** «Dijeras tú la reacción y solamente le llenaras tú los campos con los valores de cómo hacer la forma. Así no están hechas, ahí están hechas muy complejas.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:29:49
- **Dicho por:** cliente
- **Cita (2):** «Así como las están planteando usted, la verdad era como me la imaginaba al principio yo.»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:29:20
- **Cita (3):** «Es que la idea es que esté armado de fórmulas, que al final esas fórmulas se lo traduzcan en un texto.»
- **Fuente (3):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:43:15)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** la forma la planteamos nosotros y el cliente la aceptó (cita 2). Que la IA interprete una redacción libre
  se habló, pero no se decidió (P-014).
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-017 — Tipos de fórmula de pago

- **Enunciado:** El sistema soporta al menos estos pagos por elemento: (a) ensaye menos una deducción fija, pagando un
  porcentaje del resto; (b) un porcentaje del contenido con deducción mínima, aplicando la deducción mayor; (c) un
  porcentaje directo del contenido. Cada uno a la cotización que indique el extracto.
- **Cita:** «la fórmula sería algo así como seleccionar nosotros el elemento y decir qué tipo de fórmula queremos, verdad, por ejemplo, ensaye menos cuantos gramos por tonelada»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:09:47)
- **Dicho por:** cliente
- **Cita (2):** «Acá no, acá tú pagas un porcentaje o una deducción mínima de un valor, lo que sea la deducción mayor.»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:13:25)
- **Cita (3):** «nos pagan el oro y la plata y está bien directito, pagas el tanto por ciento del oro del contenido a tal cotización»
- **Fuente (3):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:48:29)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** el cliente dijo que hay más formas en otros contratos y no tiene ejemplos (P-013). Las cifras de los
  ejemplos (1.5 g/t, 65 %, 85 %, 8 unidades…) son de los contratos mostrados, no parámetros del sistema.
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-018 — Deducciones: cargo de tratamiento, escalador, refinaciones y deducción a la cotización

- **Enunciado:** El extracto permite definir: el cargo de tratamiento por tonelada seca (o por kilo, convertido); un
  escalador o desescalador del cargo según la cotización; refinaciones de oro y plata por unidad pagable en la unidad
  que diga el contrato; y una deducción a la cotización de un metal.
- **Cita:** «Es un escalador al cargo de tratamiento, entonces va dentro de las deducciones y va a ser positivo o negativo.»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:26:44)
- **Dicho por:** cliente
- **Cita (2):** «Esas refinaciones es por oro pagable y plata pagable»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:38:50)
- **Cita (3):** «aquí aplica una deducción de un centavo de dólar por libra a la cotización del plomo.»
- **Fuente (3):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:32:17)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** no se vio un contrato real con escalador (P-013).
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-019 — Tipos de castigo

- **Enunciado:** Para cada elemento castigable el extracto permite: castigar solo el exceso sobre un contenido libre, a
  una tarifa por escalón («por cada 1 %», «por cada 0.1 %», «por cada 10 ppm»); castigar todo el contenido si se
  supera un umbral; o castigar por tramos acumulables. Un mismo elemento puede ser pagable en un contrato y castigable
  en otro.
- **Cita:** «¿Cuánto es el libre? ¿A cuánto te lo cobran por cada cuánto? Porque a veces por cada 1%, a veces por cada punto uno.»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:17:46)
- **Dicho por:** cliente
- **Cita (2):** «ensayo es igual o mayor a punto uno, entonces todo el contenido te lo van a cobrar a 1.5 acá te cobran los excesos, acá te lo cobran todo.»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:17:46)
- **Cita (3):** «si tu ensaye es mayor a 5, entonces te lo van a cobrar a 2.5 por cada uno más. Aparte, si es arriba de 8 te lo cobran a 3.5 USD por cada uno.»
- **Fuente (3):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:40:06)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-020 — Unidades de cada dato y conversiones antes de calcular

- **Enunciado:** El sistema guarda la unidad de cada dato (ensaye, cotización, cuota, castigo, peso) y lo convierte a
  la unidad de cálculo (p. ej. % → kg/t multiplicando por 10; ppm → % dividiendo entre 10 000; plata kg/t ↔ g/t;
  onzas → gramos; kilos → toneladas).
- **Cita:** «Son oro y plata, gramos por tonelada, todo lo demás en porcentaje. En Memmex el oro lo tienen en gramos por tonelada, la plata la tienen en kilos por tonelada y todo lo demás en porcentaje.»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:21:27)
- **Dicho por:** cliente
- **Cita (2):** «¿El contrato siempre está expresado en estas unidades o todo varía? Puede variar y sus análisis también pueden variar»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:38:09)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-002, HU-010

### RF-021 — Asignación del laboratorio tercero por fecha de cierre, con cambio manual

- **Enunciado:** El sistema asigna el laboratorio tercero de un lote según el día de su fecha de cierre y la regla del
  extracto (hoy con Met-Mex: del 1 al 10 ALS, del 11 al 20 CQP, del 21 al fin de mes LIMSA), y permite cambiarlo por
  excepción.
- **Cita:** «todo lo que se vaya del uno al 10 se va a ir con a LS, todo lo que se vaya del 11 al 20 se va a ir con CQP»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:19:18)
- **Dicho por:** cliente
- **Cita (2):** «En funcionalidad digamos que este por fecha de sierra en automáticos se trata ya del que le correspondería pero con opción a cambiarlo por si hubiese habido una excepción. Sí, hay veces que por algo se quedan muy retrasados los lotes»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:14:24
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** la asignación automática con cambio manual la propusimos nosotros y el cliente la aceptó con sus razones
  (cita 2). La regla y la lista de laboratorios deben poder cambiar: «tampoco está escrito en piedra» (sesión 4,
  00:13:52). Otros contratos rotan los terceros en vez de usar decenas (P-012).
- **Historias:** HU-005, HU-012

### RF-022 — Moneda del extracto

- **Enunciado:** El extracto registra la moneda, elegida de un catálogo; hoy siempre es dólares.
- **Cita:** «¿La moneda invariablemente son dólares? Sí. Ahora, es también dejarlo como catálogo»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:17:15
- **Dicho por:** no identificado (la pregunta parece nuestra; el «Sí» es del cliente)
- **Origen:** Deducción nuestra
- **Estado:** Propuesto
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-023 — Periodo de cotización por extracto, general o por elemento

- **Enunciado:** El extracto define el periodo de cotización (M+1, M+2, M+3 o una regla según el día de cierre o de
  entrega) para todos los metales pagables o para cada uno; el cálculo toma las cotizaciones del mes que corresponda
  según la fecha del lote, también al cruzar de año.
- **Cita:** «promedio aritmético mensual del mes siguiente a la fecha de cierre del lote. ¿Qué quiere decir que si tu lote cerró en agosto?El cupen va a ser septiembre»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:06:36)
- **Dicho por:** cliente
- **Cita (2):** «si entregas del uno al 20 es mes de entregas, si entregas del 21 en adelante es mes siguiente. Entonces también tenemos que indicarle ese tipo de mezclas.»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:49:08)
- **Cita (3):** «Entonces, puede ser para todos los metales o elemento por elemento.»
- **Fuente (3):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:33:21
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** en el Doré, ¿«entrega» es la fecha de recepción o la de cierre? (P-016).
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-024 — El periodo de cotización se pacta antes y no cambia después

- **Enunciado:** El periodo de cotización de un lote no se puede cambiar una vez conocidas las cotizaciones de ese
  periodo; todo término que se aplique a un lote proviene de un extracto (normal o de lotes específicos), nunca se
  asigna directamente al lote.
- **Cita:** «No, todos los periodos de cotizaciones tienen que estar pactados desde antes de que se dé el evento. Ya una vez que se conocen cotizaciones ya no puedes hacer ese cambio»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:10:36)
- **Dicho por:** cliente
- **Cita (2):** «sí que tenga que estar ligado. Tiene que ir a un extracto. Tiene que ir a un extracto. Siempre.»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:34:50
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-025 — Depósito en garantía en el extracto

- **Enunciado:** El extracto define el depósito en garantía: porcentaje del valor provisional de cada lote y regla de
  la fecha de pago (día fijo del mes siguiente al cierre, o N días después del cierre); si la fecha cae en día inhábil,
  se recorre al día hábil siguiente.
- **Cita:** «depósitos en garantía correspondientes a BLOMO, que se paga el 80% del valor provisional de cada lo que he recibido, y debe de equitarse el día 15 del mes siguiente a la fecha de cierre de la noche.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:18:43
- **Dicho por:** cliente
- **Cita (2):** «En caso de ser inávil, se tiene que pagar al día hábil inmediato siguiente.»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:18:43
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** también quieren «más controles, justo de los depósitos en garantía» (misma cita); qué controles, no se
  dijo (P-017).
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-026 — Numeración consecutiva automática del extracto

- **Enunciado:** El sistema asigna a cada extracto un número consecutivo automático.
- **Cita:** «Sí, deberá llevar una numeración, el extracto, pero la numeración es, o sea, subsecuente. Con secutiva. Con secutiva. Será automático el sistema»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:20:50
- **Dicho por:** no identificado (se acordó en el diálogo)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-019

### RF-027 — Flujo generar → revisar → autorizar del extracto

- **Enunciado:** Todo extracto (normal, provisional, sustituto o de lotes específicos) pasa por generación, revisión y
  autorización; solo un extracto autorizado se aplica a cálculos.
- **Cita:** «No es que se genere automáticamente, pero si implica también su workflow, su proceso. Ah, eso es importante. Todo tendría un generador, un revisor y un autorizador.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:25:57
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** nosotros propusimos generar extractos en automático al arrancar el año; el cliente lo rechazó (ver
  «Descartados»).
- **Historias:** HU-019

### RF-028 — Copiar un extracto para crear otro

- **Enunciado:** El sistema permite crear un extracto nuevo copiando uno existente, además de crearlo desde cero; la
  copia sigue su propio flujo de autorización.
- **Cita:** «O sea, bueno, allá lo que hacíamos a veces era copiar un extrajo para que yo no me duplicabas.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:25:57
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-019

### RF-029 — Extracto provisional y extracto que lo sustituye

- **Enunciado:** El sistema permite un extracto provisional (con los términos del año anterior mientras se aprueban los
  nuevos) y un extracto posterior que lo sustituye, registrando qué extracto sustituye a cuál.
- **Cita:** «provisionalmente estamos aplicando los del 2025, entonces tendremos que hacer, que hacen allá, hacen un extracto provisional con los términos del 2025 para el 2026»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:20:21
- **Dicho por:** cliente
- **Cita (2):** «Entonces arriba hay que agregarle extracto que sustituye. Este es un campo obligatorio y eso nada más aplicaría para cuando»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:20:50
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** «obligatorio» y «solo aplica cuando sustituye» se contradicen (C-008).
- **Historias:** HU-019

### RF-030 — Al sustituir un extracto: no tocar lo calculado, recalcular y registrar el ajuste por concepto

- **Enunciado:** Cuando se autoriza un extracto que sustituye a otro, el sistema no modifica las liquidaciones ya
  calculadas; a los lotes ya liquidados les recalcula todo con los términos nuevos y registra el ajuste por concepto
  (pagables, deducciones, castigos) y en total, para contabilidad; los lotes sin liquidación final se calculan
  directamente con los términos nuevos.
- **Cita:** «no es que te cambien las liquidaciones que ya estaban, las deja como estaban, pero para lo que ya tenía liquidación final te hace un ajuste, y para lo que no ya te hace la liquidación es común.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:20:21
- **Dicho por:** cliente
- **Cita (2):** «Importa todo y tener el ajuste de cada concepto para contabilidad porque contabilidad sí registra.»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:21:55
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-019

### RF-031 — Consulta de extractos con su estado y sus sustituciones

- **Enunciado:** El sistema ofrece una consulta de extractos con sus términos, su estado (generado, revisado,
  autorizado), sus fechas y, si aplica, qué extracto sustituye o por cuál fue sustituido.
- **Cita:** «hay que tener una tarea de consulta de extractos, para poder ver los términos, y ponerle su estado autorizado, generado, realizado, sus fechas, y si se sustituyó, extracto que sustituye o extracto sustituido»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:38:58
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-019

### RF-032 — Extracto de lotes específicos

- **Enunciado:** El extracto tiene un tipo (normal o de lotes específicos). Uno de lotes específicos no tiene vigencia
  propia, queda ligado al extracto base del que se copió y se aplica solo a los lotes que se seleccionen de entre los
  del periodo del extracto base (misma compañía, unidad y producto); se redacta completo y cualquier término puede
  variar.
- **Cita:** «Tienen sus contratos con vigencias muy específicas, pero luego hay otros extractos que se llaman extractos para lotes específicos»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:12:35)
- **Dicho por:** cliente
- **Cita (2):** «Es importante que fueran ligados al extracto normal para tener la referencia o la vigencia. Sí, sí, sí. Va a tener una liga del extracto.»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:40:35
- **Cita (3):** «Hay que mostrar lotes del periodo del extracto base para la compañía, unidad y producto. Y de ahí que los pueda seleccionar.»
- **Fuente (3):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:43:17
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota de prioridad:** «O sea, es para excepciones. Sí, sí. Lo vas a usar poco. La verdad casi no se usaría.»
  (sesión 4, 00:47:26).
- **Historias:** HU-019

### RF-033 — Un extracto de lotes específicos autorizado ya no admite lotes

- **Enunciado:** Una vez autorizado un extracto de lotes específicos, ya no se le pueden agregar lotes: se crea otro.
- **Cita:** «O sea, una vez que la autoriza el extracto ya no lo puedes agreder ahí, tendrías que hacer otro extracto de lotes específicos.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:46:57
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-019

### RF-034 — A cada lote le aplica exactamente un extracto

- **Enunciado:** Pueden coexistir varios extractos vigentes para el mismo producto (el normal y los de lotes
  específicos), pero a un lote le aplica uno solo; el de lotes específicos prevalece para sus lotes.
- **Cita:** «Sí, pero para un mismo lote no puede haber dos extractos. Sí, ahí sería uno para el que le aplicaría.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:34:20
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-019

### RF-035 — Simulador de cálculo por extracto

- **Enunciado:** El sistema ofrece, para cada extracto, un simulador donde el usuario captura valores de entrada y ve
  el resultado del cálculo, para compararlo contra su propio Excel de control.
- **Cita:** «y lo que te permita simular, para que nosotros tengamos un Excel, por ejemplo, y comparar, meter los valores, y ver qué es lo que nos resulta.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:30:53
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### C. Captura de ensayes por el representante (ERSA)

### RF-036 — ERSA captura sus ensayes en el sistema

- **Enunciado:** El representante (hoy ERSA) captura en el sistema los ensayes de cada lote y los entrega a Fresnillo
  por el sistema, en lugar de libreta, Excel y correo.
- **Cita:** «necesitamos que no sea un Excel, necesitamos que eso llegue por sistema, ¿sí?»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:13:22
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota de prioridad:** «el punto justo que queremos corregir es eso que nos reporta ERSA» (sesión 2, 00:06:00); las
  pérdidas por errores de transcripción de ERSA son el origen del proyecto (sesión 1, 00:11:17).
- **Historias:** HU-002

### RF-037 — Pantalla de entrada de la captura: compañía, unidad, producto y fechas

- **Enunciado:** La captura se abre filtrando por compañía, unidad y producto (con autocompletado sobre catálogos), por
  tipo de búsqueda (fecha de cierre del lote o fecha de recepción de la muestra) y por rango de fechas; la misma
  entrada se usa en las consultas de Fresnillo.
- **Cita:** «El nombre de la compañía, la unidad, el producto de lo que están buscando, más bien de lo que van a capturar, el tipo de búsqueda, la fecha de cierre del lote o la fecha de recepción de la muestra»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:33:52
- **Dicho por:** cliente
- **Cita (2):** «Tipo de busqueda» «(fecha cierre de lote o fecha recepción de muestra)»
- **Fuente (2):** `documentos/2026-09-04-sesion-2/CAPTURA DE ENSAYES - MUESTRAS OFICIALES.xlsx`, hoja «ACCESO PANTALLA», A3:B8
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-001

### RF-038 — Datos del lote ya cargados: ERSA solo captura ensayes y sus fechas

- **Enunciado:** En la captura, ERSA ve los lotes con su número de lote Fresnillo y Met-Mex, unidad, producto, fecha de
  cierre y peso seco ya cargados (RF-001); solo captura los ensayes y las fechas que le corresponden.
- **Cita:** «para que ellos ya no tengan que estar capturando ni fechas de cierre de lote, ni número de lote, ni peso»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:45:55
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-001

### RF-039 — Varios capturistas de ERSA, cada uno con su usuario, sin restricción por elemento

- **Enunciado:** Cada capturista de ERSA (hoy unas tres personas, más suplentes) tiene su propio usuario; cualquiera
  puede capturar cualquier elemento del lote, y el sistema registra quién capturó cada elemento y cuándo, al guardar.
- **Cita:** «pues se quedarían así más personas a cargo y para que no usen el mismo usuario, pues ya tendríamos que darlos de alto también a ellos.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:35:18
- **Dicho por:** cliente
- **Cita (2):** «No, no, no, eso es justo lo que estamos pidiendo, que no lo restringen»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:54:01
- **Cita (3):** «Que ahí en captura aparezcan todos los usuarios que capturaron, pero que te diga qué elemento capturó quién.»
- **Fuente (3):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:36:13
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota de prioridad:** el cliente antepone que la captura no se trabe: «terminábamos porque eran tareas con demasiado
  control que no te hacen práctico el sistema.» (00:55:32).
- **Historias:** HU-002

### RF-040 — Guardado parcial; enviar solo con todos los elementos capturados

- **Enunciado:** Varios usuarios pueden guardar capturas parciales del mismo lote; el lote solo puede enviarse a
  revisión cuando están capturados todos los elementos que pide el extracto.
- **Cita:** «No se puede enviar a revisar hasta que están todos los elementos capturados.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:38:26
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-002

### RF-041 — Flujo de tres niveles en ERSA: captura, revisión, autorización

- **Enunciado:** Cada lote pasa por captura (guardar y enviar), revisión (revisado o regresar) y autorización
  (autorizar o regresar). Si la revisión lo regresa, vuelve a captura; si la autorización lo regresa, vuelve a captura y
  repite todo el flujo. Al regresar se puede escribir una observación, no obligatoria. Solo con la autorización los
  ensayes pasan a Fresnillo.
- **Cita:** «Si el segundo nivel lo regresa, pasa al nivel anterior y lo pueden modificar nuevamente. Es exactamente eso. Si el tercer nivel regresa el lote, se libera nuevamente hasta el primer nivel.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:59:28
- **Dicho por:** cliente
- **Cita (2):** «Ahí lo quieren con un comentario para que entiendan qué es lo que hay que corregir. Sí, exacto, que si le abre una ventana de observación»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:38:06
- **Cita (3):** «a través de un proceso de workflow, vayan revisando y autorizando los ensayes, dependiendo de cómo definamos el proceso. Puede ser solamente que nos reporta y hay un nivel autorizado.»
- **Fuente (3):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:12:22
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** quién ocupa cada nivel (¿todos en ERSA?) no está dicho (P-021).
- **Historias:** HU-003

### RF-042 — Estados del lote en la captura y bloqueo después de enviar

- **Enunciado:** Cada lote en captura tiene estado pendiente, guardado, enviado, revisado o autorizado; una vez enviado
  ya no se puede modificar, salvo que se lo regresen, y solo se pueden seleccionar para captura los lotes no enviados.
- **Cita:** «a la persona que le den enviar ya no va a poder modificar ningún dato a menos de que la persona del segundo nivel se lo regrese.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:59:28
- **Dicho por:** cliente
- **Cita (2):** «Sí, porque se han cambiado cosas y no les avisa. Sí, eso, precisamente. Una vez que se envía, se cierra.»
- **Fuente (2):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:50:17
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-003

### RF-043 — Tipo de muestra en cada captura

- **Enunciado:** Cada captura indica el tipo de muestra, elegido de un catálogo: original, primer remuestreo o segundo
  remuestreo; ERSA lo indica al recibir la muestra.
- **Cita:** «Ese sí es un catálogo. Va a venir de un catálogo que diga tipo de muestra original, primer remuestreo, segundo remuestreo.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:44:54
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-002

### RF-044 — Recepción de la muestra en el laboratorio de ERSA, con lector de código de barras

- **Enunciado:** ERSA registra la fecha en que la muestra llega a su laboratorio, leyendo el código de barras del sobre
  o capturándola a mano; el sistema también permite localizar el lote leyendo ese código.
- **Cita:** «en el sobrecito viene un código de barras, tienen que leer el código de barras»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:46:19
- **Dicho por:** cliente
- **Cita (2):** «hay dos opciones, o lo pone manual o le damos de forma que ya ven que trae el código de barras»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:47:08
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** se dijo que la fecha «se va a poner en automático» y luego que «no se pone en automático» (C-005); qué
  identifica el código (lote fundición o clave de remitente) está por confirmar (P-022).
- **Historias:** HU-001

### RF-045 — Fecha estimada de entrega de resultados

- **Enunciado:** ERSA registra una fecha estimada de entrega de resultados por lote.
- **Cita:** «Esta sería la fecha estimada de los resultados y ya sería lo que se acuerde con ERSA para la fecha estimada que se ponga aquí. Ellos decían que la querían capturar manualmente.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:47:30
- **Dicho por:** cliente
- **Cita (2):** «Fecha estimada entrega resultados (se puede acordar cantidad de días que se sumen a partir de la fecha de recepción)»
- **Fuente (2):** `documentos/2026-09-04-sesion-2/CAPTURA DE ENSAYES - MUESTRAS OFICIALES.xlsx`, hoja «CIENEGA CONC PB», I8
- **Origen:** Cliente
- **Estado:** Por precisar
- **Nota:** la junta dice «capturar manualmente» y el Excel, «días que se sumen»: ¿captura o cálculo? (C-006).
- **Historias:** HU-002

### RF-046 — Fecha de entrega de resultados: automática al autorizar

- **Enunciado:** El sistema fija la fecha de entrega de resultados en el momento en que el tercer nivel autoriza el lote
  y los ensayes pasan a Fresnillo.
- **Cita:** «Ya que el último nivel le dio clic en autorizar, que se transmitieron los ensayes de esa tarea a la otra tarea»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:47:57
- **Dicho por:** cliente
- **Cita (2):** «que se llenen automáticos cuando ellos capturen los ensayos y le den un enviar, hay que que aparezca que se llenen»
- **Fuente (2):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:48:40
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-003

### RF-047 — Oro y plata normales y ajustados; configurar cuál se usa en el intercambio

- **Enunciado:** El sistema recibe de ERSA (y de Met-Mex y del tercero) oro y plata normales y ajustados, y una
  configuración indica cuál de los dos se usa para el intercambio.
- **Cita:** «El oro y la plata nos reportan dos ensayos. Oro y plata normales y oro y plata base ajustada.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:49:59
- **Dicho por:** cliente
- **Cita (2):** «lo que queremos es nosotros poder indicar en algún cataloguito para lo que los otros procesos, decir cuál es el que queremos usar para el intercambio, si el oro y la plata normalitos o el oro y la plata ajustado.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:51:07
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** si la configuración es general o por cliente, producto o vigencia no quedó claro (P-023); en la sesión 1
  las dos grabaciones dicen cosas distintas sobre los ajustados (C-004).
- **Historias:** HU-002

### RF-048 — ERSA reporta su resultado tal cual: sin rangos que lo bloqueen ni datos que lo sesguen

- **Enunciado:** La captura de ERSA no rechaza ni condiciona valores por rangos esperados, no le muestra los ensayes de
  la mina ni qué elementos son pagables o castigables; el análisis lo hace solo Fresnillo.
- **Cita:** «Conerza, el primer resultado lo necesitamos así tal cual, porque no siempre lo se restan con Erza, a veces se restan con la mina.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:58:09
- **Dicho por:** cliente
- **Cita (2):** «A ERSA no le vamos a decir cuáles son pagables y cuáles son castigables, porque incluso eso haría que diéramos tendencia»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:38:45
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** el cliente rechazó nuestra propuesta de validar rangos en la captura de ERSA (ver «Descartados»).
- **Historias:** HU-002

### RF-049 — ERSA ve solo sus lotes pendientes

- **Enunciado:** En su captura, ERSA solo ve los lotes pendientes de reportar; los ya reportados no aparecen salvo en
  una consulta, y un lote con remuestreo solicitado le vuelve a aparecer (RF-073).
- **Cita:** «solamente les deben de aparecer los lotes que se van a ir a la postera ya los que han reportado ya no les tienen a menos de que hagan una consulta»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:23:13
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-001

### RF-050 — Exportar a Excel lo capturado, en el formato de ERSA

- **Enunciado:** Desde la captura, con los lotes seleccionados, ERSA exporta a Excel la tabla en el formato en que hoy
  reporta, para llenar sus propios formatos de calidad.
- **Cita:** «Ellos piden que lo que nos capturan en esa tarea tenga la opción de exportarse a Excel en el formato, la tabla, en el formato que actualmente nos reportan, solo la tabla»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:15:50
- **Dicho por:** cliente
- **Cita (2):** «con que todo lo que ellos nos capturen lo puedan exportar también ellos a una tabla ellos lo necesitan porque necesitan llenar sus propios formatos»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:13:57
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** falta la plantilla de ERSA (P-024).
- **Historias:** HU-004

### D. Revisión de Fresnillo y devolución a ERSA

### RF-051 — Consulta de ensayes de Fresnillo

- **Enunciado:** Fresnillo consulta por lote: tipo de muestra, empresa que reporta, quién capturó, revisó y autorizó,
  fechas de entrega y recepción de la muestra, fecha estimada y de entrega de resultados, lotes, fecha de cierre, peso
  seco, ensayes por elemento con su condición pagable o castigable, y estatus; siempre con los valores vigentes de ERSA
  (confirmados o actualizados).
- **Cita:** «La empresa que reporta los ensayes sería ERSA, quien capturó, quien revisó, quien autorizó, la fecha de entrega, cuando se entregó de Met-Mex a la persona de ERSA y cuando lo recibió ERSA en las oficinas de ERSA.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:02:35
- **Dicho por:** cliente
- **Cita (2):** «Esta tarea deberá traer los valores de ERSA que ERSA envíe con los resultados actualizados o confirmados.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:07:16
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** ¿Fresnillo ve los lotes antes de que ERSA autorice? (C-007).
- **Historias:** HU-006

### RF-052 — Análisis ERSA contra mina, con las diferencias sobre el límite en rojo

- **Enunciado:** Fresnillo ve por lote y elemento el ensaye de ERSA, el ponderado de la mina y su diferencia; las
  diferencias por encima del límite de partición del elemento se marcan en rojo, como apoyo y sin obligar a ninguna
  acción; desde ahí autoriza o regresa el lote.
- **Cita:** «El ensayo de ERSA, el ensayo de la mina y la diferencia de cada uno de los elementos.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:05:21
- **Dicho por:** cliente
- **Cita (2):** «No significa que todas a fuerza las vayamos a mandar todo lo que aparece en rojo. Eso va a ser una base de decisión para quienes estén administrando los contratos.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:07:37
- **Cita (3):** «La columna de los ensayes es de los lotes cerrados y no las columnas de ERSA y marcar en un semáforo los que tienen cierto rango de diferencia para hacer ahí con un clic»
- **Fuente (3):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:44:55
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** qué límite se usa para ERSA contra mina (¿el del contrato con Met-Mex?) y qué significa «autorizar» aquí
  (P-025).
- **Historias:** HU-006

### RF-053 — Regresar lotes a ERSA con observación y correo automático

- **Enunciado:** Al seleccionar uno o varios lotes, Fresnillo puede regresarlos a ERSA con una observación de hasta mil
  caracteres; el sistema notifica a ERSA y envía un correo automático con los lotes, compañía, unidad, producto y
  observaciones.
- **Cita:** «Al dar clic en el botón regresar se desplegará una ventana de observación que tenga un campo como de mil caracteres y abajo deberá tener un botón de enviar.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:06:43
- **Dicho por:** cliente
- **Cita (2):** «Cuando Fresnillo PLC regrese un lote, deberá mandar un correo automático del aviso de él o los lotes, compañía, unidad, producto, de lo que se tiene que revisar.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:06:18
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** destinatarios de los correos por definir (P-027).
- **Historias:** HU-007

### RF-054 — Lote devuelto: ERSA confirma sin cambios o corrige y reenvía

- **Enunciado:** En la captura de ERSA el lote devuelto se resalta con sus observaciones; ERSA puede confirmarlo sin
  cambios (se quita el resaltado, se bloquea y llega un correo automático de confirmación a Fresnillo) o corregir
  cualquier elemento y «actualizar y enviar», que reinicia el flujo.
- **Cita:** «Si el usuario confirma el valor deberá dar clic en check en esta línea.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:10:38
- **Dicho por:** cliente
- **Cita (2):** «van a estar habilitados esos campos de todos los elementos para que pueda generar las modificaciones correspondientes y deberá dar clic en el botón actualizar y enviar.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:12:33
- **Cita (3):** «A lo mejor con un cheque y entonces que les dé la posibilidad o de confirmar ensay o de corregir ensay»
- **Fuente (3):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:50:17
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** quién en ERSA confirma está por definir con ERSA (P-026).
- **Historias:** HU-007

### RF-055 — La confirmación de ERSA es oficial y queda registrada

- **Enunciado:** El sistema registra quién confirmó y cuándo, porque el valor confirmado tiene validez oficial y
  sustenta reclamaciones a ERSA.
- **Cita:** «ya también nosotros haremos reclamaciones si nos confirman un valor que al final no era correcto.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:12:10
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-007

### RF-056 — Historial de valores y consulta «Ensayes revisados»

- **Enunciado:** El sistema guarda los valores anteriores y nuevos de cada lote devuelto, con su observación, quién
  cambió qué y cuándo; la consulta «Ensayes revisados» los muestra con estatus original, confirmado o actualizado y la
  fecha del estado.
- **Cita:** «Se deberá guardar la historia de los valores anteriores y los nuevos y se podrán consultar en tarea de los desrevisados.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:13:01
- **Dicho por:** cliente
- **Cita (2):** «Igual sería el estatus original, confirmado, actualizado. La fecha del estado, la empresa que reporta, quién capturó, quién revisó, quién autorizó»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:14:17
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-008

### RF-057 — Solicitar revisión o reensaye a ERSA antes del intercambio

- **Enunciado:** Antes del intercambio, Fresnillo puede pedir a ERSA una revisión o un reensaye de un lote y registrar
  el resultado (confirmado o corregido); el remuestreo no se pide en esta etapa.
- **Cita:** «No sé si en este punto es donde se llega a solicitar un reensaye o hasta un remuestreo. No, aquí solo reensaye.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:27:42
- **Dicho por:** cliente
- **Cita (2):** «Una vez que se resuelven, puedes confirmarse que el dato era correcto o puede haber una corrección. Si el dato era correcto y la dar diferencias se mantiene.»
- **Fuente (2):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 01:02:04
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** ¿el reensaye es lo mismo que «regresar» el lote (RF-053) o un flujo aparte? (P-028). La revisión con la
  mina queda para otra etapa (ver Acuerdos).
- **Historias:** HU-007

### E. Intercambio con Met-Mex

### RF-058 — Cédula de intercambio en Excel y PDF

- **Enunciado:** Al seleccionar lotes, el sistema genera la cédula de intercambio en Excel y PDF con la compañía y
  unidad (con su logo), el producto, el lote Met-Mex, el lote de la mina, los resultados por elemento, el periodo
  («lotes cerrados en» mes y año) y el usuario que la genera.
- **Cita:** «ya procedemos con lo de la cédula para intercambio, que queremos que venga en Excel y en PDF, en ambos por si se llega a requerir.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:15:24
- **Dicho por:** cliente
- **Cita (2):** «que indique lotes cerrados en, y dependiendo de la fecha de selección, te diga en qué periodo, en qué mes, año cerrar.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:18:34
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** el Excel de especificación anuncia «la siguiente tabla» para la cédula y la tabla no viene (P-029).
- **Historias:** HU-009

### RF-059 — El intercambio con Met-Mex sigue por correo; Fresnillo carga los ensayes de Met-Mex

- **Enunciado:** El intercambio con Met-Mex se sigue haciendo por correo y en simultáneo; el sistema no expone los
  ensayes de Fresnillo a la contraparte y Fresnillo carga en él los ensayes de Met-Mex.
- **Cita:** «No puede estar todo dentro de este sistema, porque el intercambio de sales tiene que ser por correo.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 01:07:38
- **Dicho por:** cliente
- **Cita (2):** «esos tienen que ser simultáneos. No los puede mostrar a alguien antes que el otro, porque representaría una ventaja para el otro el conocer los ensayes de la contraparte.»
- **Fuente (2):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 01:06:38
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-009

### RF-060 — Carga del Excel de Met-Mex, tolerante a cambios de formato

- **Enunciado:** El sistema carga los ensayes de Met-Mex subiendo el Excel tal como lo envían, convierte las unidades
  (p. ej. plata de kg/t a g/t) y tolera que Met-Mex cambie la plantilla sin aviso.
- **Cita:** «Sería subirlo, subir el archivo como se los manda.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:24:59
- **Dicho por:** cliente
- **Cita (2):** «Es que un día puede cambiar y ustedes no tienen control sobre eso y el sistema tiene que considerarlo.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:26:20
- **Cita (3):** «En la plata ellos tienen kilos por tonelada, nosotros manejamos gramos por tonelada»
- **Fuente (3):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:25:27
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-010

### RF-061 — Comparación con Met-Mex: DIF, INT (T/P) y RN

- **Enunciado:** Por elemento, el sistema compara el ensaye de Met-Mex con el de la unidad (el de ERSA), pone la
  diferencia en DIF, marca en INT «T» si excede el límite de partición y «P» si es menor o igual, y agrega RN
  (resultado negociado), que replica INT y queda editable.
- **Cita:** «deberá comparar los ensayes de Met-Mex y unidad minera y agregar el resultado en la columna DIF. Si la diferencia excede el límite de partición, deberá indicar T.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:32:03
- **Dicho por:** cliente
- **Cita (2):** «En esta columna se aplicarán los valores de la columna INT y dejará editables los que indiquen T para que se puedan modificar a P.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:32:38
- **Cita (3):** «puede sugerir y todo, pero les pedimos que se dejen editables.»
- **Fuente (3):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:35:10
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** la regla escrita dice que solo los T se editan a P; en la junta también piden forzar tercería sobre un P
  (C-009). Con qué ensaye se elige el rango del límite (hoy el de la mina) y qué pasa fuera de rango: P-030.
- **Historias:** HU-010, HU-011

### RF-062 — Resolución por elemento: promediar, aceptar el ensaye de una parte o tercería

- **Enunciado:** El sistema resuelve cada elemento del lote por separado: promediar (dentro del límite), negociar
  (promediar o aceptar el ensaye de una de las partes, sin valores intermedios arbitrarios) o enviar a tercería; un
  elemento puede quedar resuelto mientras otro sigue en tercería.
- **Cita:** «Pero para la plata a lo mejor puedes decir que está fuera del límite de partición y esto lo envía a tercería. Para ese elemento en ese lote todavía está tercería, todavía no tiene ensaye final.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:07:14
- **Dicho por:** cliente
- **Cita (2):** «O acuerdas promediar o mantenerte en otra serie. O aceptar ensayos del otro también.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:09:26
- **Cita (3):** «O sea, no es que todo el lote se va a tercería, solo es por elemento.»
- **Fuente (3):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 01:08:29
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** «aceptar el ensaye de la contraparte» no aparece en el Excel, que solo habla de T y P.
- **Historias:** HU-011

### RF-063 — Sugerir promedio cuando no se alcanza pago ni castigo

- **Enunciado:** El sistema sugiere promediar cuando un elemento castigable no alcanza el umbral de castigo o uno
  pagable no alcanza el mínimo pagable, aunque la diferencia exceda el límite de partición.
- **Cita:** «Si es un ensaye castigable y el ensaye final no alcanza castigo, aunque la diferencia esté por encima del límite de partición, puede surgir, puede sugerir promedio.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:34:39
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Por precisar
- **Nota:** contra qué valor se compara el umbral (¿ERSA, Met-Mex, promedio?) no se dijo (P-031).
- **Historias:** — (esperando P-054)

### RF-064 — Columnas del análisis de intercambio

- **Enunciado:** El análisis de intercambio muestra por lote: tipo de muestra, lote Met-Mex, lote de la unidad, fecha
  de cierre, peso seco, fecha de intercambio, fecha de tercería y laboratorio tercero; y por elemento: ensaye de
  Met-Mex, ensaye de la unidad, diferencia y límite de partición (del extracto), con los mínimos pagables y castigos.
- **Cita:** «la fecha de intercambio de ensayes, que cuando ya lo intercambian con Met-Mex, y si es que se va a tercería, la fecha de tercería.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:23:10
- **Dicho por:** cliente
- **Cita (2):** «Ese límite de partición, si necesitamos que nos lo despliegue a nivel lote, para que pueda hacer esa diferencia, pero ya se lo va a traer del extracto.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:24:30
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-010

### RF-065 — Evidencias del intercambio y de la tercería

- **Enunciado:** El sistema guarda como evidencia auditable el Excel de Met-Mex y la imagen de su correo, y en cada
  tercería el archivo del tercero, la imagen de su correo y su hoja de trabajo; se aceptan imágenes, Excel y PDF.
- **Cita:** «Imagen del correo de Met-Mex de los intercambios y el archivo del intercambio y en los terceros imagen del correo que nos llega con la tercería y el archivo de la tercería.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:47:47
- **Dicho por:** cliente
- **Cita (2):** «Eso sí, o sea, las evidencias son archivos, las imágenes, Excel, PDF.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:47:27
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** la idea la abrió una pregunta nuestra; el cliente la aceptó y la precisó. La hoja de trabajo la exige
  Met-Mex en su solicitud de tercería (`documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/Orden de compra.pdf`,
  p. 1).
- **Historias:** HU-010, HU-012

### F. Tercería

### RF-066 — Captura de los resultados de tercería, con carga masiva acotada

- **Enunciado:** Fresnillo registra los resultados de tercería solo para los lotes y elementos enviados a tercería;
  hoy a mano, y con carga de archivo si TI lo aprueba, con campos llave obligatorios y aceptando solo el elemento
  solicitado.
- **Cita:** «Los ensayos de los terceros se subirán manualmente, se revisará con TI si existe la opción de cargarlo en masivo.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:35:10
- **Dicho por:** cliente
- **Cita (2):** «La idea es subir los archivos, subir los archivos. Sí, porque lo que no queremos es que estemos capturando y que vaya a haber algún error de captura.»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:28:31)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** cada laboratorio entrega su propio formato (PDF y a veces Excel), y un reporte puede agrupar varios lotes
  con separadores distintos; si se estandariza antes de cargar o el sistema lee cada formato, no se decidió (P-032).
- **Historias:** HU-012

### RF-067 — Corrección por las notas del laboratorio: plata incluye oro, zinc incluye cadmio

- **Enunciado:** Cuando el laboratorio tercero indica que un resultado incluye otro elemento (hoy solo LIMSA: «la plata
  incluye oro», «el zinc incluye cadmio»), el sistema obtiene el valor corregido restando el elemento incluido; si el
  laboratorio ya reporta «zinc corregido», no se vuelve a corregir.
- **Cita:** «aquí en esta que la plata incluye oro, nosotros cuando vaciamos.A nuestro formato de no de intercambios, ahí hacemos algunas conversiones para a la plata, quitarle este oro»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:22:26)
- **Dicho por:** cliente
- **Cita (2):** «El zinc (Zn) corregido, significa que ya es solo el Zn, debido a que el cadmio (Cd) tuvo análisis de tercería.»
- **Fuente (2):** `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/S00950 resumen.xlsx`, hoja «S00950», A50
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** la conversión exacta de la plata no se explicó (P-033).
- **Historias:** HU-012

### RF-068 — Ensaye final tras tercería según la regla del extracto

- **Enunciado:** Cuando llega el resultado de tercería, el sistema fija el ensaye final del elemento con la regla del
  extracto (con Met-Mex: el valor de en medio entre ERSA, Met-Mex y el tercero); después de la tercería ya no hay
  promedio negociado, solo revisión si el resultado es anómalo.
- **Cita:** «Si te vas con el tercero, tomas el ensayo, ya tienen los resultados de la tercería y tomas el ensayo del medio»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:10:54
- **Dicho por:** cliente
- **Cita (2):** «Sí, pero no es promedio negociado. Después del tercero ya no es promedio negociado. O sea, es revisión en todo caso.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:15:31
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** en la junta previa se dijo «vale el del tercero» (C-001).
- **Historias:** HU-012

### RF-069 — Resultado de tercería anómalo: otro sobre o remuestreo

- **Enunciado:** Si el resultado del tercero es anómalo, el sistema permite registrar una nueva tercería con otro sobre;
  si Met-Mex ya no tiene sobre, se registra un remuestreo que reinicia el proceso del lote, salvo el ponderado de la
  mina.
- **Cita:** «el tercero pues si sale muy desvirtuado, pero en apariencia está bien, entonces vuelve a mandar otro sobre de tercería. Solo en caso de que Met-Mex no tuviera otro sobre de tercería, tienes que hacer remuestreo»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:32:07
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota de prioridad:** excepción poco frecuente: «debería de pasar nunca, pero nos ha pasado dos veces» (00:14:45).
- **Historias:** HU-013

### RF-070 — Historia de resultados de tercería: primer resultado, reensaye, análisis en conjunto

- **Enunciado:** Para un mismo lote y elemento, el sistema guarda los resultados sucesivos de tercería (primer
  resultado, reensaye del tercero, análisis en conjunto Fresnillo–Met-Mex, análisis posteriores con su fecha) y marca
  cuál es el vigente.
- **Cita:** «Entonces aquí se mandó un ranzaje y nos da 178.5. Y aquí se hizo otra vez un análisis ya en conjunto entre Medmex y Fresnillo y ya nos dio este valor.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 01:12:05
- **Dicho por:** cliente
- **Cita (2):** «entonces toda esa trazabilidad hay que tenerla en esas capturas aquí pero ya es por ustedes porque el tercero no va a venir a capturar aquí»
- **Fuente (2):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 01:13:13
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-013

### RF-071 — Costo de tercería por laboratorio y elemento

- **Enunciado:** El sistema guarda las tarifas de tercería por laboratorio y por elemento, como base para decidir y
  para medir el costo de las tercerías.
- **Cita:** «El costo de tercería, para que ustedes tengan esa base de comparación, también hay que meterla aquí, ¿verdad? Sí, porque varía por laboratorio. No es el mismo costo con un laboratorio que con el otro.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:07:11
- **Dicho por:** cliente (responde a una pregunta nuestra)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** en el Excel actual, la tercería la paga la parte que quedó más lejos del tercero (o 50 % cada una); no hay
  regla escrita (P-034).
- **Historias:** — (esperando P-054)

### RF-072 — Cálculo rápido para decidir si ir a tercería

- **Enunciado:** Cuando un elemento sale del límite de partición, el sistema calcula el valor del lote con el promedio
  y con los ensayes propios, junto al costo de la tercería, para decidir si conviene promediar o ir a tercería.
- **Cita:** «¿Cuánto me cuesta la tercería? Entonces, ¿me conviene ir a tercería o no me conviene ir a tercería?»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:07:11
- **Dicho por:** cliente
- **Cita (2):** «quieren hacer su evaluación para saber si aunque estén fuera del límite de partición conviene promediar hoy la tercería.»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:03:32)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota de prioridad:** «ya es más adelante» (sesión 4, P2 00:04:27); depende del cálculo con el extracto (RF-081).
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### G. Remuestreos

### RF-073 — Solicitud de remuestreo, en una tarea propia

- **Enunciado:** En una tarea de remuestreos, Fresnillo filtra por compañía, unidad, producto y rango de fechas de
  cierre, marca uno o varios lotes, indica por cada uno si es primer o segundo remuestreo y registra la fecha de
  solicitud y la evidencia (el correo a Met-Mex). Sirve para lotes de comparación y de conjunto, y también cuando el
  remuestreo lo pide Met-Mex.
- **Cita:** «O sea, hay que hacer una tarea que diga remostreos y que nosotros pujamos la compañía, la unidad, el producto, el ote y lo indicamos fechar de solicitud de remostreo y si es primer remostreo solo pero tiene que ser una tarea aparte.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:26:00
- **Dicho por:** cliente
- **Cita (2):** «¿qué tal que quieres solicitar varios lotes a la vez de remasterio?»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:28:58
- **Cita (3):** «COMPAÑÍA», «(AUTOCOMPLETE)», «UNIDAD», «PRODUCTO», «FECHA INICIAL DE CIERRE», «FECHA FINAL DE CIERRE», «FECHA DE SOLICITUD», «EVIDENCIA»
- **Fuente (3):** `documentos/2026-09-18-sesion-4/REMUESTREOS REPORT.xlsx`, hoja «SOLICITUD», A5:G8
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** si la fecha y la evidencia son por lote o una para toda la solicitud quedó contradictorio (C-010); si la
  solicitud lleva niveles de autorización, también (C-011).
- **Historias:** HU-015

### RF-074 — Hasta dos remuestreos por lote, en orden

- **Enunciado:** Un lote admite hasta dos remuestreos; el segundo solo se habilita si ya existe el primero.
- **Cita:** «Sí, ahora pueden tenerse hasta dos Ramastreos.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:18:22
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-015

### RF-075 — Fecha de solicitud: la del correo, editable

- **Enunciado:** La fecha de solicitud toma por omisión la fecha de captura y es editable, porque la que vale es la del
  correo a Met-Mex.
- **Cita:** «Por default es la captura, pero con opción nada meditable.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:31:43
- **Dicho por:** cliente (acepta una propuesta nuestra)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-015

### RF-076 — Consulta de remuestreos con identificador de solicitud

- **Enunciado:** La tarea de remuestreos tiene una pestaña de consulta con los mismos filtros, que muestra lotes,
  fechas, tipo, la evidencia como vínculo que abre el archivo y un identificador que agrupa los lotes pedidos juntos
  (solo informativo).
- **Cita:** «Ok, que sea una tarea con dos pestañas, una pestaña que sea de solicitud y la segunda pestaña que sea de consulta.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:36:00
- **Dicho por:** cliente
- **Cita (2):** «No tendrá ningún efecto más que ustedes encuentran todos los que solicitaron juntos, nada más por referencia y información.»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:41:02
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-015

### RF-077 — Remuestreo de un lote de comparación: se reabre a ERSA y cancela el primer intercambio

- **Enunciado:** Al solicitar remuestreo de un lote de comparación, el sistema lo vuelve a habilitar en la captura de
  ERSA para la nueva muestra, que sigue el mismo proceso de captura y revisión; los ensayes del intercambio anterior
  quedan inhabilitados (dejan de contar para pago y para intercambio) y se conservan como historia.
- **Cita:** «Cuando hay remuestreo, automáticamente se eliminan los valores del primer intercambio.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 01:05:06
- **Dicho por:** cliente
- **Cita (2):** «Se inhabilita, queda como historia, pero ya no juego para efectos de pago ni nada.»
- **Fuente (2):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 01:05:34
- **Cita (3):** «Si es un lote de comparación, se le va a habilitar a Elsa.»
- **Fuente (3):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:37:50
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-016

### RF-078 — El último ensaye queda vigente; los anteriores, en el histórico

- **Enunciado:** Al capturarse un remuestreo, el sistema no borra nada: deja vigente el último (original, primer o
  segundo remuestreo) e inhabilita los anteriores, que se pueden consultar en un histórico; en un lote de conjunto
  actualiza los valores en el comparativo de ensayes y conserva el anterior.
- **Cita:** «Pero si ya hubo primer remuestro, deja inhabilita el original y deja habilitado el remuestro. Si hay segundo remuestro, inhabilita también el primer remuestro y deja. Esto los puedo consultar en algún reporte de historia, pero el que va a dejar aquí es el último.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:27:04
- **Dicho por:** cliente
- **Cita (2):** «3.- Si es un lote de conjunto, se actualizarán los valores en la tarea de comparativo de ensayes y se deberá dejar en el histórico también para consulta el valor de la muestra anterior.»
- **Fuente (2):** `documentos/2026-09-18-sesion-4/REMUESTREOS REPORT.xlsx`, hoja «SOLICITUD», A18
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-016

### RF-079 — Medir cuánto tarda cada remuestreo

- **Enunciado:** El sistema registra las fechas de solicitud y de avance de cada remuestreo para medir cuánto tarda.
- **Cita:** «porque ahorita un problema es que nosotros solicitamos un remuestreo por decir hoy y se tardan a veces un chorro en hacer y si nosotros no llenamos esas fechas ahorita si es algo que la verdad está ahí falta seguimiento»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:24:09
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-015

### RF-080 — Registrar la sospecha de cruce de muestras

- **Enunciado:** El sistema permite registrar que se sospecha un cruce de muestras como motivo de un remuestreo.
- **Cita:** «Si hay un cruce de muestras, hacemos el intercambio, pero necesitamos un remuestre.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 01:03:05
- **Dicho por:** cliente
- **Origen:** Deducción nuestra (la excepción la describe el cliente; registrar el motivo lo deducimos)
- **Estado:** Propuesto
- **Historias:** HU-015

### H. Comparativo de ensayes de conjunto (precipitados, Doré, carbón)

### RF-081 — Comparativo mina contra Met-Mex por fecha de cierre

- **Enunciado:** El sistema reemplaza el Excel de comparativo: por fecha de cierre muestra el lote mina, el lote
  Met-Mex, el peso seco de mina y el de Met-Mex (en kilos), su diferencia en kg y en %, los ensayes de oro y plata de la
  mina (de PROVEM) y de Met-Mex, sus diferencias unitarias y porcentuales y los contenidos (peso × ley / 1 000 000).
- **Cita:** «Que el sistema en lugar de dejar llenando con Excel y este archivo, que se nos tiene este archivo sistema que nos traigan por fecha de cierre de lote los lotes de Minas»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:36:19)
- **Dicho por:** cliente
- **Cita (2):** «La diferencia de ensay y de oro y de plata en valores unitarios y porcentuales.»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:52:56
- **Cita (3):** «5. los contenidos de oro y plata serán multilicados peso * ensaye / 1 millon»
- **Fuente (3):** `documentos/2026-09-18-sesion-4/REGLAS DE NEGOCIO PARA F-PLUS PRECIPITADOS.xlsx`, hoja «ENSAYES EN CONJUNTO SJ», B129
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** en la junta el signo de las diferencias y el divisor quedaron mal reconocidos; el documento del cliente dice
  «valor de met.mex menos el valor de mina» (B128) y «peso * ensaye / 1 millon» (B129). Se toma el documento, y se
  confirma (P-035).
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-082 — Ensayes de Met-Mex tomados de su PDF, que queda como evidencia

- **Enunciado:** Los ensayes de oro y plata que reporta Met-Mex en PDF se pasan al sistema sin teclearlos de nuevo, y
  el PDF se guarda ligado al lote como evidencia.
- **Cita:** «En Sayes de Medmex, tenemos esos formatos en PDF y no sé si se pueda traer en lugar de que alguien los esté tecleando.»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:36:56)
- **Dicho por:** cliente
- **Cita (2):** «3. El ensaye de oro y plata Met-Mex se extraerá de los archivos que manda Met-Mex en PDF, y se subirAn para que queden como evidencia»
- **Fuente (2):** `documentos/2026-09-18-sesion-4/REGLAS DE NEGOCIO PARA F-PLUS PRECIPITADOS.xlsx`, hoja «ENSAYES EN CONJUNTO SJ», B127
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** planteado en condicional («no sé si se pueda»): lectura automática del PDF o captura asistida con el PDF a la
  vista, por decidir (P-036).
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-083 — Señalar las diferencias que piden revisión

- **Enunciado:** El sistema marca para revisión las diferencias entre mina y Met-Mex que superan un umbral por elemento
  (hoy, por práctica, ≈1 % en oro y ≈2 % en plata), considerando el nivel del ensaye.
- **Cita:** «casi todo lo que esté arriba del 1% de sujeto a revisión en el oro, arriba del 2% de la plata en el sujeto a revisión»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:35:19)
- **Dicho por:** cliente
- **Origen:** Deducción nuestra (los umbrales son la práctica del cliente; que el sistema marque lo deducimos)
- **Estado:** Propuesto
- **Nota:** «un 1% puede ser mucho o poco dependiendo del ensayo» (misma cita): la regla exacta, por definir (P-037).
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-084 — Validar el ensaye en conjunto antes de usarlo

- **Enunciado:** El ensaye en conjunto que reporta Met-Mex queda pendiente hasta que Fresnillo lo valida: lo libera o
  pide revisión o remuestreo.
- **Cita:** «ese ensaye nos lo reportan a nosotros y hacemos una validación y ya decimos si estamos de acuerdo con ese ensayo.O si solicitamos algún tipo de muestreo o alguna revisión.»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:31:06)
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-085 — Estado del lote y su recorrido

- **Enunciado:** El comparativo muestra el estado de cada lote y su historial de eventos (cuándo reportó Met-Mex,
  cuándo hubo revisión de la mina, cuándo se liberó, cuándo se pidió remuestreo, cuándo se liquidó).
- **Cita:** «¿Cuándo nos reportó ensayes MENMEX? ¿Cuándo hicimos nosotros una revisión? Porque a veces hacemos una revisión. ¿Cuándo liberamos ensayes o cuándo solicitamos remastero?»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:56:51
- **Dicho por:** cliente
- **Cita (2):** «Sobres plateados (enviados al Lab de mina)», «Se envia a la GAC», «Revision, en espera de resultados de mina », «Solicitud de remuestreo, en espera de resultados por MMP», «Liberados por la GAC y sin liquidar», «Lotes liquidados»
- **Fuente (2):** `documentos/2026-09-18-sesion-4/REGLAS DE NEGOCIO PARA F-PLUS PRECIPITADOS.xlsx`, hoja «ENSAYES EN CONJUNTO SJ», Z4:Z9
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota de prioridad:** «Oye, por lo que los estados tiene más peso, es más relevante.» (sesión 4, P2 00:56:51).
  ¿Los mismos estados aplican a concentrados? (P-038).
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-086 — Exportar el comparativo a Excel

- **Enunciado:** El comparativo se exporta a Excel, para usarlo al solicitar remuestreos a Met-Mex.
- **Cita:** «8. esta información se podrá extraer a excel»
- **Fuente:** `documentos/2026-09-18-sesion-4/REGLAS DE NEGOCIO PARA F-PLUS PRECIPITADOS.xlsx`, hoja «ENSAYES EN CONJUNTO SJ», B132
- **Dicho por:** documento del cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### I. Cotizaciones, proformas y liquidación

### RF-087 — Cotizaciones mensuales: cargar el archivo que ya llevan

- **Enunciado:** El sistema lee las cotizaciones mensuales por metal subiendo el archivo de cotizaciones que el área
  sigue llevando fuera del sistema; el extracto solo indica qué tipo de cotización y qué mes aplican.
- **Cita:** «Sí, yo creo que si llevar este archivo aparte, porque te digo, eso no solo lo usamos nosotros, esto también se lo compartimos a las unidades mineras»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:12:09)
- **Dicho por:** cliente (acepta una opción que planteamos nosotros)
- **Cita (2):** «Se extrae de los datos de la revista y eso es algo general. Lo que cambia es, ¿cuál aplicaría para un metal si el del M más 1 o si el M más 13?»
- **Fuente (2):** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:36:23
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-088 — Tipo de cotización por elemento, incluido el promedio de varias

- **Enunciado:** El extracto indica por elemento pagable el tipo de cotización (London final, AM o PM, Comex, London
  Spot…) o el promedio aritmético de varias.
- **Cita:** «las cotizaciones solamente podemos indicar qué tipo de cotización es la que se usa si la London a M.O la PN en el caso del oro o el promedio o la Comex»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:22:27)
- **Dicho por:** cliente
- **Cita (2):** «se pagará el 70% del resto del contenido al promedio aritmético de estas otras 2 cotizaciones.»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:10:22)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-089 — Obtener las cotizaciones en automático, si hay licencia

- **Enunciado:** Si existe una licencia o API autorizada, el sistema obtiene en automático las cotizaciones oficiales;
  si no, se usa la carga del archivo (RF-087).
- **Cita:** «habría que ver si se pueden jalar en automático o te ofrecen algún cero en alguna APISería cuestión de ver con la gente de México»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:41:55)
- **Dicho por:** no identificado
- **Origen:** Deducción nuestra
- **Estado:** Por precisar
- **Nota:** pendiente de que el cliente confirme la licencia (ver Acuerdos).
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-090 — Cálculo del valor del lote con el extracto

- **Enunciado:** El sistema calcula el valor de un lote con su extracto: ensayes pagables por cotización = pagos;
  deducciones = cargo de tratamiento ± escalador + refinaciones + castigos; valor por tonelada = pagos − deducciones;
  valor del lote = valor por tonelada × peso seco. El peso, los ensayes y las cotizaciones son entradas del lote, no
  del contrato.
- **Cita:** «Entonces sumas todas sus deducciones, tanto de cargos de tratamiento, refinaciones, si las tuvieras, en este caso no las tienes, y castigos y tienes un total de deducciones.»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:29:30)
- **Dicho por:** cliente
- **Cita (2):** «El peso, pero ese es el peso para cada lote. Eso no lo podemos poner en el contrato. De hecho, ni los valores de la cotización.»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:22:27)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota de prioridad:** «Las fórmulas de cómo se paga y todo, para estos fines de ensayes que estamos haciendo, no
  sirven.» (sesión 4, P2 00:04:27): el cálculo económico es posterior a la primera parte. Si la liquidación completa
  entra al alcance de F-Plus no se ha dicho con esas palabras (P-002).
- **Respuesta (2026-10-21):** el cálculo económico y la liquidación van en la siguiente etapa (P-002).
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-091 — Proformas por escenario: elegir ensayes, peso y cotizaciones

- **Enunciado:** El usuario genera proformas de un lote eligiendo qué ensayes usar (avisados de la mina, de ERSA, de
  Met-Mex, de tercería, promedio), qué peso (mina u oficial de Met-Mex) y qué cotizaciones (libres en la proforma); en
  la liquidación final el sistema toma las cotizaciones del periodo que dicta el extracto.
- **Cita:** «Nosotros te queremos poder hacer este mismo cálculo, pero en los diferentes momentos con los diferentes ensayos para evaluar nuestro lote»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:08:15)
- **Dicho por:** cliente
- **Cita (2):** «Yo usuario pueda seleccionar qué tipo de programa quiero correr, con qué datos, con qué datos.»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:39:38)
- **Cita (3):** «Que cada quien pueda meter las cotizaciones en los ejercicios solamente de profor. Sí, este ya para la final, no para la final que se traiga la cosa que le toque.»
- **Fuente (3):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:40:52)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** «seleccionable» lo eligió el cliente entre dos opciones nuestras. Si las proformas se guardan varias por
  lote, no está claro (P-039).
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-092 — Ensayes provisionales o finales según el propósito del cálculo

- **Enunciado:** El sistema usa ensayes provisionales para los cálculos provisionales (p. ej. depósito en garantía) y
  los finales (promediado, de tercero…) para la liquidación final.
- **Cita:** «si tú vas a hacer un cálculo provisional, pues los ensayos provisionales que tú tengas, si vas a hacer un cálculo final, pues ya agarramos entre los finales.»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:03:12)
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### RF-093 — Liquidaciones de Met-Mex: cargarlas, consultarlas y confrontarlas, sin modificarlas

- **Enunciado:** El sistema recibe las liquidaciones finales de Met-Mex (por conexión si existe; si no, por archivo o
  captura), las guarda sin modificarlas, permite consultarlas y descargar el documento, y las confronta con el cálculo
  propio, señalando las diferencias que excedan una tolerancia.
- **Cita:** «Sí, pero ojo, nosotros no le podemos ajustar nada a las liquidaciones de menes, es como que contra nuestros portes cálculos.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:44:56
- **Dicho por:** cliente
- **Cita (2):** «en lugar de que lo estén vaciando en un Excel, que nuestro sistema haga como que un match, metamos el extracto del contrato, nuestro sistema haga un cálculo.»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:08:57)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** diferencias de pocos dólares son normales y de cientos indican un problema; la tolerancia exacta no se
  definió (P-040).
- **Historias:** — (siguiente etapa, decisión del 2026-10-21)

### J. Reportes y análisis

### RF-094 — Cuánto se promedia y cuánto va a tercería

- **Enunciado:** El sistema reporta, por unidad, lote y elemento, cuánto se promedia y cuánto va a tercería, y en qué
  elementos, minas y productos se va más a tercería.
- **Cita:** «cuánto estamos promediando, pero es a nivel elemento. O sea, es por lote, por elemento.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:16:17
- **Dicho por:** cliente
- **Cita (2):** «con cuántos elementos nos vamos a tercería o en qué, en qué elementos nos vamos más a tercería, de qué mina, de qué producto»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:52:13)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-017

### RF-095 — Desempeño por laboratorio tercero: con quién se gana o se pierde y cuánto cuesta

- **Enunciado:** El sistema reporta, por laboratorio tercero, cuántas tercerías se ganan y se pierden y cuánto cuestan
  (costo de tercería y costo financiero), para evaluar a los laboratorios y los límites de partición.
- **Cita:** «con qué tercero, cómo nos está yendo con cada tercero, las estamos perdiendo o las estamos ganando.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:16:48
- **Dicho por:** cliente
- **Cita (2):** «¿cuántos ganas? ¿Cuántos pierdes? ¿Con quién pierdes más? ¿Con quién ganas más?»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:52:49)
- **Origen:** Cliente
- **Estado:** Por precisar
- **Nota:** qué es «ganar» o «perder» una tercería no se definió (P-010).
- **Historias:** HU-017

### RF-096 — Tiempos del ciclo del lote

- **Enunciado:** El sistema registra la fecha de cada evento del lote (embarque, recepción en Met-Mex, entrega de
  muestra, reporte de ERSA, intercambio, envío a tercería, resultado del tercero, liquidación) y reporta los tiempos
  entre ellos por lote, representante, tercero y Met-Mex.
- **Cita:** «Elsa, ¿cuánto tiempo te tardas en reportarme los ensayes? ¿Cuánto tiempo nos estamos tardando en intercambiar ensayes?»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:18:19
- **Dicho por:** cliente
- **Cita (2):** «desde que embarcamos, cuánto nos tardamos entre que embarcamos y entre que se reciben menos.»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:51:41)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** varias de esas fechas ocurren fuera del sistema (Met-Mex, terceros): quién las registra, por definir
  (P-041).
- **Historias:** HU-018

### RF-097 — Comparativas cruzadas entre fuentes de ensaye

- **Enunciado:** El sistema compara ensayes contra ensayes de distintas fuentes (avisados de mina, planta Met-Mex,
  ERSA, finales, terceros) por periodo, año, mina, producto, elemento y tipo de ensaye.
- **Cita:** «contra los de ERSA, contra los finales, contra los terceros, poder empezar a hacer esa mezcla en trensalles para saber cómo comparan unos con otros»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:20:13
- **Dicho por:** cliente
- **Cita (2):** «por periodo específico, por año, por fecha inicial, fecha final, de qué tipo de ensayos»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:51:11)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-018

### RF-098 — Lotes que no se pueden liquidar porque un elemento sigue en tercería

- **Enunciado:** El sistema muestra los lotes pendientes de liquidar porque algún elemento sigue en tercería.
- **Cita:** «no se liquidan lotes si cualquiera de los elementos que se intercambieron está en tercería hasta que se conozcan todos los ensayos finales»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-b.md`, 00:12:17 (sección A)
- **Dicho por:** cliente
- **Origen:** Deducción nuestra (la regla es del cliente; el seguimiento lo deducimos)
- **Estado:** Propuesto
- **Historias:** HU-017

### RF-099 — Reporte mensual «Ajuste Balmet»: avisado contra liquidado

- **Enunciado:** El sistema produce el reporte mensual de ajuste entre lo avisado y lo liquidado por lote, en ensayes
  (y después en pesos y contenidos).
- **Cita:** «Nosotros al final de cada mes hacemos un reporte que se llama Ajuste Balment, donde decimos, mira, estos lotes sea lo liquide»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:19:15
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Por precisar
- **Nota de prioridad:** «ahorita nos estamos enfocando los ensayos, pero en ese ajuste hay un impacto por pesos.»
  (misma marca). Si entra en esta etapa, por confirmar (P-042).
- **Historias:** — (necesita la liquidación: siguiente etapa; P-042)

### K. Roles, catálogos y configuración

### RF-100 — Roles configurables con tareas asignables

- **Enunciado:** El sistema permite dar de alta roles, asignar a cada rol sus tareas y asignar uno o más roles a cada
  usuario, y cambiarlos con el tiempo.
- **Cita:** «nosotros podemos dejar también configurable esa parte, que se den de alta los roles y a cada rol se le da de alta las diferentes tareas y luego ya a cada usuario le asignan que rol»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:45:32)
- **Dicho por:** nosotros (propuesta)
- **Cita (2):** «y ya tal cual así lo queremos, porque aparte vamos a suponer a lo mejor Laura ahorita tiene un rol.»
- **Fuente (2):** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:45:32)
- **Origen:** Cliente (aceptó la propuesta con su razón)
- **Estado:** Propuesto
- **Nota:** el cliente quedó de mandar la lista de roles y tareas (ver Acuerdos).
- **Historias:** HU-014

### RF-101 — Revisión de valores como colaboración, no como autorización por sistema

- **Enunciado:** En el trabajo de Fresnillo sobre los valores de cada mina, el sistema facilita que varios integrantes
  carguen y revisen los datos; la confirmación no se modela como una autorización formal en el sistema.
- **Cita:** «Quien administra esa mina va a llenar esa información y luego esa me la pasan a mí y empiezo a realizar datos.»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:46:34)
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Por precisar
- **Nota:** quedamos en proponer cómo sería la colaboración (ver Acuerdos). Convive con flujos formales que sí pidió
  (ERSA, extractos).
- **Historias:** — (por precisar: propondremos cómo sería la colaboración)

### RF-102 — Representante y laboratorios terceros configurables

- **Enunciado:** El representante (hoy ERSA) y los laboratorios terceros (hoy CQP, LIMSA y ALS) son catálogos que pueden
  cambiar sin modificar el sistema.
- **Cita:** «Son tres laboratorios terceros, los que estamos utilizando, que son CQP, LINSA y ALS, las que estamos teniendo para nuestra empresa. Pueden haber después otros.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:21:18
- **Dicho por:** cliente
- **Cita (2):** «Puede ser eso, se puede llamar de otra manera, pero quien va a ser nuestro representante tiene que hacerlo de esa forma.»
- **Fuente (2):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:22:21
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-005

### L. De la junta previa (14 de agosto), por confirmar

Estas cinco las dijo un expositor que habla de la Gerencia Administrativa en tercera persona («el sistema que quiere
Delia es…»); no está confirmado que sea del cliente (P-001). Por eso quedan como deducción hasta que el cliente las
confirme.

### RF-103 — Control de calidad del laboratorio tercero, con evidencia fotográfica

- **Enunciado:** Antes de aceptar el resultado de una tercería, el sistema registra un checklist de control de calidad
  del tercero (quién analiza, balanza e instrumento calibrado, condiciones del análisis) con fotos, para tener
  argumentos para rechazar o negociar ese resultado.
- **Cita:** «previo al tercero le tenemos que pedir como una validación de control de calidad un QACUC de su proceso»
- **Fuente:** `juntas/2026-08-14-preview-embarques/transcripcion.md`, ≈0:01–16:59
- **Dicho por:** no identificado (expositor que reporta lo que pide la Gerencia Administrativa)
- **Cita (2):** «para que ahora sí Delia llegue con mayores argumentos y decir yo con esto no puedo aceptar el de la tercería»
- **Fuente (2):** `juntas/2026-08-14-preview-embarques/transcripcion.md`, ≈0:01–16:59
- **Origen:** Deducción nuestra (de segunda mano; confirmar con la Gerencia Administrativa)
- **Estado:** Por precisar
- **Nota:** qué puntos lleva el checklist, si va antes o después del análisis y quién lo llena no se dijo (P-043). En
  la sesión 2 el cliente dijo que no puede tener ningún vínculo con los terceros (RNF-007).
- **Historias:** — (esperando P-001: se dijo de segunda mano)

### RF-104 — Impacto económico de las diferencias de calidad

- **Enunciado:** El sistema estima el impacto económico de las diferencias de calidad de una tercería con montos de
  referencia por calidad.
- **Cita:** «a lo mejor aquí le va a querer meter el impacto en billete»
- **Fuente:** `juntas/2026-08-14-preview-embarques/transcripcion.md`, ≈0:01–16:59
- **Dicho por:** no identificado
- **Origen:** Deducción nuestra (planteado en condicional y de segunda mano)
- **Estado:** Por precisar
- **Nota:** se cruza con RF-072 y RF-090, que el cliente sí pidió en las sesiones 3 y 4.
- **Historias:** — (esperando P-001: se dijo de segunda mano)

### RF-105 — Equivalencia entre las claves del sistema de embarques y las oficiales

- **Enunciado:** El sistema mantiene la equivalencia entre las claves internas de unidades y productos del sistema de
  embarques y sus claves oficiales y contables, y muestra el nombre oficial.
- **Cita:** «la HP 711, esa es Fresnillo pero en el sistema embarque esa es la 2»
- **Fuente:** `juntas/2026-08-14-preview-embarques/transcripcion.md`, ≈0:01–16:59
- **Dicho por:** no identificado
- **Origen:** Deducción nuestra (el problema se describe; la tabla la deducimos)
- **Estado:** Propuesto
- **Historias:** — (esperando P-001: se dijo de segunda mano)

### RF-106 — Análisis anual de envíos por unidad y producto

- **Enunciado:** El sistema permite ver a lo largo del año los envíos por unidad y producto y detectar qué unidad dejó
  de enviar un producto en una semana.
- **Cita:** «al final no pueden analizar durante todo el año cómo fue mi envío de embarques qué unidad se me quedó sin enviar en una semana X producto»
- **Fuente:** `juntas/2026-08-14-preview-embarques/transcripcion.md`, ≈0:01–16:59
- **Dicho por:** no identificado
- **Origen:** Deducción nuestra (de segunda mano)
- **Estado:** Por precisar
- **Historias:** — (esperando P-001: se dijo de segunda mano)

### RF-107 — Consulta concentrada («sábana») por varias dimensiones

- **Enunciado:** El sistema concentra la información de embarques y tercerías en una vista tabular que se filtra por
  unidad, producto, periodo y estatus.
- **Cita:** «es más bien concentración de la información para que ella la pueda tener en una sábana y poder jugar por consultarla por dimensión X»
- **Fuente:** `juntas/2026-08-14-preview-embarques/transcripcion.md`, ≈0:01–16:59
- **Dicho por:** no identificado
- **Origen:** Deducción nuestra (de segunda mano)
- **Estado:** Por precisar
- **Nota:** en la misma junta se propuso resolverlo con una herramienta de BI sobre un almacén de datos, sin respuesta
  del cliente (P-044).
- **Historias:** — (esperando P-001: se dijo de segunda mano)

## No funcionales

### RNF-001 — Trazabilidad completa y sin borrar la historia

- **Enunciado:** El sistema registra quién hizo cada cambio, qué cambió, cuándo y el valor anterior, para ensayes,
  revisiones, resultados de tercería y decisiones; nunca pierde la historia, porque la auditoría la exige.
- **Cita:** «que lleve controles de cambios, quién hizo qué, si hay alguna revisión, una actualización, lo que sea»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:13:22
- **Dicho por:** cliente
- **Cita (2):** «nada más agregar otra columna donde fecha de actualización para no perder porque a nosotros para auditoría nos piden esa trazabilidad.»
- **Fuente (2):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:49:10
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-008; transversal

### RNF-002 — Información confidencial

- **Enunciado:** Los procesos y datos del sistema se tratan como confidenciales.
- **Cita:** «son procesos confidenciales, entonces nada más les pedimos por favor tratarlo de esa forma.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:23:26
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** transversal (restricciones del backlog)

### RNF-003 — Sistema interno que ERSA usa como usuario externo

- **Enunciado:** El sistema es interno de Fresnillo y lo usan también los usuarios de ERSA, que son externos (incluso
  con equipos que Fresnillo les preste).
- **Cita:** «Este sistema es interno y ERSA lo va a usar porque le vamos a prestar incluso a nuestros equipos.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:40:39
- **Dicho por:** cliente
- **Cita (2):** «lo que nosotros quisiéramos, pero ya esos, o sea, tiene que ver con permisos y todo eso, que Elsa utilizara nuestro sistema en equipos nuestros»
- **Fuente (2):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:45:27
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** si ERSA entra desde la red de Fresnillo o desde fuera cambia la arquitectura (P-004).
- **Respuesta (2026-10-21):** al inicio, ERSA trabajará con equipos prestados por Fresnillo y acceso por VPN;
  después pasará a internet. Se diseña desde el principio para internet (respuesta del usuario; confirmar con TI del
  cliente).
- **Historias:** transversal (restricciones del backlog)

### RNF-004 — Inicio de sesión: SSO corporativo para Fresnillo, usuario externo para ERSA

- **Enunciado:** Los usuarios de Fresnillo entran con el inicio de sesión único corporativo; los de ERSA, que no tienen
  usuario corporativo, con un usuario externo.
- **Cita:** «Es un single sign-on, entras por tu usuario de Oracle. Y eso sí ya es política de la empresa.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 02:00:21
- **Dicho por:** cliente (TI)
- **Cita (2):** «Porque ERSA no tiene usuario de Oracle. Buscaríamos un usuario externo, un usuario externo.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 02:00:51
- **Origen:** Cliente
- **Estado:** Propuesto
- **Nota:** dicho como probable («muy probablemente de esa forma»). Contrasta con ADR-010 de nuestra arquitectura
  (directorio LDAP); lo revisa `nemit:arquitecto`.
- **Historias:** HU-014; transversal

### RNF-005 — Acceso de externos solo al aplicativo, con la sesión grabada

- **Enunciado:** Un usuario externo accede únicamente al aplicativo, con los controles de TI de Fresnillo (sesión
  grabada para auditoría).
- **Cita:** «se les puede dar únicamente el acceso al, si va a ser vía web, que se le interesa, que únicamente tenga acceso al aplicativo como tal. Hay manera de hacerlo.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:41:44
- **Dicho por:** cliente (TI)
- **Cita (2):** «Porque ahí tenemos la factibilidad de grabar toda la sesión que hace el usuario.»
- **Fuente (2):** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:43:31
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** HU-014; transversal

### RNF-006 — ERSA solo ve lo de Fresnillo

- **Enunciado:** Los usuarios del representante solo ven los lotes de Fresnillo, porque el representante también ensaya
  para otros clientes.
- **Cita:** «cada mencionar que también no somos su único cliente ellos también le ensayan a otra gente, hasta otras minas»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:55:16
- **Dicho por:** cliente
- **Origen:** Deducción nuestra (la cita es contexto; el aislamiento lo deducimos)
- **Estado:** Propuesto
- **Historias:** HU-014; transversal

### RNF-007 — Los laboratorios terceros no acceden al sistema

- **Enunciado:** Los laboratorios terceros no entran ni capturan en el sistema; Fresnillo sube sus resultados.
- **Cita:** «Nosotros con los terceros no podemos tener ningún tipo de vínculos. Ninguno.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:45:38
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** transversal (restricciones del backlog)

### RNF-008 — Ágil y práctico

- **Enunciado:** El sistema responde con agilidad y es práctico de usar (autocompletados, pocos pasos), en especial la
  captura de ERSA, que debe ser muy fácil.
- **Cita:** «Uno, en cuanto a rapidez, ¿qué tan ágil va a ser este sistema?»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:59:12
- **Dicho por:** cliente
- **Cita (2):** «Si son muy, solamente picarle a botones pues que la capacidad vaya así, no sea un sistema tan complejo para ellos.»
- **Fuente (2):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:02:34
- **Origen:** Cliente
- **Estado:** Por precisar
- **Nota:** sin cifras de tiempos ni volúmenes (P-045).
- **Historias:** transversal (restricciones del backlog)

### RNF-009 — Computadoras fijas en el laboratorio y lectores de código de barras

- **Enunciado:** La captura de ERSA se hace en computadoras fijas del laboratorio, con lector de código de barras.
- **Cita:** «Pues mira, por la experiencia en los laboratorios, todo se es computador. Todo es computador.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:53:48
- **Dicho por:** cliente (probable; responde a una pregunta nuestra)
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** transversal (restricciones del backlog)

### RNF-010 — Exportaciones listas para analizar, con unidades explícitas

- **Enunciado:** Lo que el sistema exporta no trae encabezados repetidos ni celdas combinadas, y todo valor dice su
  unidad (g/t, %, toneladas húmedas o secas).
- **Cita:** «ese reporte por ejemplo me la trae mucho encabezados entonces cuando quieres hacer análisis tienes que estar lo borrando todo»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:24:51
- **Dicho por:** cliente
- **Cita (2):** «eso no trae unidad, es importante saber que esos son toneladas, ya sea nombres o secos.»
- **Fuente (2):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:33:24
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** transversal (restricciones del backlog)

### RNF-011 — Decimales y redondeo que reproduzcan los de Met-Mex

- **Enunciado:** El sistema define los decimales y el redondeo de cada paso del cálculo para reproducir los resultados
  de Met-Mex.
- **Cita:** «a lo que te queda, pues es el 5%, entonces lo manejamos a 3 decimales.»
- **Fuente:** `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P1 00:23:57)
- **Dicho por:** cliente
- **Origen:** Cliente (3 decimales en pagables) y Deducción nuestra (una regla para cada paso)
- **Estado:** Por precisar
- **Nota:** Whisper dice «13 cimales» en el mismo pasaje (C-012); las proformas del cliente redondean distinto según el
  producto (P-046).
- **Historias:** transversal (restricciones del backlog)

### RNF-012 — No confundir «no analizado» con cero

- **Enunciado:** El sistema distingue un elemento no solicitado o no analizado de un valor cero, y no lo trata como
  cero en promedios, diferencias ni castigos.
- **Cita:** «----»
- **Fuente:** `documentos/2026-09-17-sesion-3/Informe de tercería CQP/Consolidado 1023-2026 Saucito Conc. Pb.xlsx`, D10 (elemento no solicitado)
- **Dicho por:** documento del laboratorio CQP
- **Origen:** Deducción nuestra (en los documentos el vacío y el cero se mezclan: «----» de CQP, celdas vacías de ALS,
  ceros en el reporte de lotes cerrados; nadie lo pidió con esas palabras)
- **Estado:** Propuesto
- **Historias:** transversal (restricciones del backlog)

### RNF-013 — Configurable para crecer: catálogos en lugar de valores fijos

- **Enunciado:** Incoterm, laboratorios, moneda, periodo de cotización, representante y fórmulas son configurables, para
  que el sistema sirva también a ventas con terceros y a contratos nuevos.
- **Cita:** «creen conveniente ponerlo de una vez por si en algún momento se llega a presentar algo que no nos estorba, no nos quita mucho tiempo y nos puede dar facilidad de cosas nuevas»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:09:24
- **Dicho por:** cliente
- **Cita (2):** «todo lo que definamos que no sea cuadrado justo por lo que bien menciona, o sea, porque hay cosas que pueden cambiar, o sea, hoy sabemos qué es Elsa»
- **Fuente (2):** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:21:49
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** transversal (restricciones del backlog)

### RNF-014 — Los cálculos se pueden verificar contra los controles del usuario

- **Enunciado:** Todo cálculo del sistema se puede comprobar contra los controles propios del usuario (simulador,
  exportaciones, confrontación con Met-Mex).
- **Cita:** «no nos vamos a olvidar de nuestros controles, ¿eh? No se vale decir, ah, pues ya, es que el sistema lo hace todo y se equivocó»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:30:25
- **Dicho por:** cliente
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** transversal (restricciones del backlog)

### RNF-015 — Del lado de Fresnillo, independiente de PROVEM

- **Enunciado:** El sistema es de Fresnillo y está separado de PROVEM (compartido con Peñoles), para tener lógica y
  controles propios sin afectar a otras unidades ni productos.
- **Cita:** «Ahorita es una sola versión y no se le puede modificar absolutamente nada porque no son instancias separadas. No podemos segmentar.»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:01:05
- **Dicho por:** cliente (quien presentó el proyecto)
- **Cita (2):** «esas son las cosas que tiene el sistema el sistema no lo quiere mover trae mucha lógica»
- **Fuente (2):** `juntas/2026-08-14-preview-embarques/transcripcion.md`, ≈0:01–16:59
- **Origen:** Cliente
- **Estado:** Propuesto
- **Historias:** transversal (restricciones del backlog)

### RNF-016 — Apariencia corporativa sobria

- **Enunciado:** La interfaz usa una paleta corporativa neutra con toques de rojo, en modo claro y oscuro, sin paletas
  personalizables por usuario.
- **Cita:** «O dejar nomás el claro y el oscuro. Sí, algo que sea más corporativo, sin tanta libertad.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:57:27
- **Dicho por:** no identificado (responde a una pregunta nuestra sobre paletas)
- **Origen:** Deducción nuestra (quedamos en hacer una propuesta)
- **Estado:** Por precisar
- **Nota de prioridad:** «Y lo menos importante.» (sesión 3, ≈ P2 00:53:31), sobre lo visual.
- **Historias:** transversal (restricciones del backlog)

## Descartados

### RF-D01 — Validar rangos en la captura de ERSA

- **Enunciado:** La captura de ERSA rechaza o avisa de valores fuera de un rango esperado.
- **Cita:** «eso no lo podemos enerzar, eso tiene que estar con nosotros, porque que tal que Elsa está bien y la Mina es la que está mantiendo»
- **Fuente:** `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:57:42
- **Dicho por:** cliente
- **Origen:** Deducción nuestra (la propusimos nosotros)
- **Estado:** Descartado — el cliente quiere el resultado de ERSA tal cual; el análisis lo hace Fresnillo (RF-048).
  Si se puede un aviso que no bloquee, o validar la captura que hace Fresnillo, sigue abierto (P-047).

### RF-D02 — Generar automáticamente los extractos al arrancar el año

- **Enunciado:** El sistema genera solo los extractos del año nuevo para que no haya lotes sin contrato vigente.
- **Cita:** «No es que se genere automáticamente, pero si implica también su workflow, su proceso.»
- **Fuente:** `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:25:57
- **Dicho por:** cliente
- **Origen:** Deducción nuestra (la propusimos nosotros)
- **Estado:** Descartado — en su lugar: copiar extractos (RF-028) y el extracto provisional (RF-029).

### RF-D03 — Catálogo general de elementos por compañía, unidad y producto

- **Enunciado:** Un catálogo de elementos, aparte del contrato, define qué se captura.
- **Cita:** «Vale la pena entonces no considerar catálogos de elementos, sino les digo que...»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:39:27
- **Dicho por:** cliente
- **Origen:** Cliente (estaba en su propio Excel de especificación)
- **Estado:** Descartado — lo sustituye el extracto de contrato (RF-014).

### RF-D04 — Una ventana para que el laboratorio tercero suba su archivo

- **Enunciado:** El laboratorio tercero carga sus resultados directamente en el sistema.
- **Cita:** «Pero no es la opción que me convenza.»
- **Fuente:** `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:45:38
- **Dicho por:** cliente
- **Origen:** Deducción nuestra (no está claro quién la planteó)
- **Estado:** Descartado — el cliente no puede tener vínculos con los terceros (RNF-007).

## Acuerdos

Lo que se decidió en las juntas sobre el alcance (qué se enfoca, qué se descarta, qué queda para después). No son
requisitos, pero los deciden.

| Fecha | Acuerdo | Cita y fuente |
|---|---|---|
| 2026-08-14 | Lo que se construye es la parte de tercerías y de consulta; el sistema de embarques actual sigue existiendo. | «acá arriba va a seguir existiendo el sistema de embarque» — `juntas/2026-08-14-preview-embarques/transcripcion.md`, ≈0:01–16:59 (expositor no identificado) |
| 2026-09-03 | Esta etapa son dos módulos: los controles de la captura de ERSA y el análisis de intercambios y tercerías. La migración completa de PROVEM queda para después. | «Entonces meterles los controles es uno de los módulos. Y el segundo tiene que ver con toda la batería de análisis que necesitan saber cuando...» — `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:02:34; «No sentamos ahora así analizar el monstruo grande del Proven.» — misma fuente, 00:03:32 |
| 2026-09-03 | Plazo objetivo del cliente: de seis a ocho semanas, hacia el 27 de octubre. | «traemos un objetivo como de seis o ocho semanas para hacer todo el desarrollo de estos modos, acabando ese módulo estamos hablando de octubre, como 27 de octubre» — `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:03:00 |
| 2026-09-03 | La primera versión es la captura web; un LIMS completo para ERSA y la conexión con equipos de laboratorio quedan para etapas posteriores. | «Podríamos tener una primera versión que sea la captura web y ya de ir viendo siguientes alcances como etapas de los tabios.» — `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:56:43 |
| 2026-09-03 | El intercambio con Met-Mex sigue por correo; Fresnillo captura los ensayes de Met-Mex y de los terceros. | «Nosotros vamos a tener que capturar los ensayos de MENMEX, los ensayos del tercero también, porque los del tercero tampoco.» — `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 01:07:58 |
| 2026-09-03 | El enfoque es ensayes; pesos y contenidos, después. | «Ahorita nos enfocaremos en lo que es lo de ensayé, ¿verdad? Sí.» — `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md`, 00:26:51 |
| 2026-09-04 | El foco es el módulo de ERSA; la revisión con la mina es otra etapa. | «Ahorita nos estamos enfocando hacia ERSA, el módulo que queremos cubrir.» — `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:17:09 |
| 2026-09-04 | Extracto de contrato en lugar de catálogo de elementos. | «vale la pena hacer ese extracto del contrato, así como les digo, indicándole la vigencia» — `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 00:40:23 |
| 2026-09-04 | Remuestreos, consulta de remuestreados, extractos y cálculo económico quedan pendientes de definir. | «Nos falta todavía lo de los extractos. Nos falta también el cálculo que queramos hacer» — `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:49:07 |
| 2026-09-04 | El sistema se llama F-Plus. | «No, de hecho el sistema se va a llamar F-Plus.» — `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:51:19 |
| 2026-09-04 | Revisaremos los requisitos con el cliente antes de escribir historias (propuesta nuestra, informada). | «seguramente haremos alguna revisión previa de esos requisitos con ustedes antes de hacer la generación de las famosas historias de usuario.» — `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:54:50 |
| 2026-09-17 | El cliente entrega sus archivos: proformas, liquidación de Met-Mex, cotizaciones, extracto, comparativo, terceros y PDF de Met-Mex. | «archivos de proformas de Excel, liquidación memex, archivo cotizaciones, archivo de extracto, archivo comparativo de ensayos, archivos de terceros, archivos de PDF de memex» — `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:48:04) |
| 2026-09-17 | El cliente propone los roles y las tareas de cada rol. | «que ustedes piensen o platiquen qué roles requieren el sistema y más o menos qué tareas haría cada» — `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:45:01) |
| 2026-09-17 | El cliente revisa con «la gente de México» si hay licencia o API para las cotizaciones. | «habría que ver si se pueden jalar en automático o te ofrecen algún cero en alguna APISería cuestión de ver con la gente de México» — `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:41:55) |
| 2026-09-17 | El extracto es la base de todo y debió verse primero. | «lo primero, la verdad, es el extracto, que es la base de todo.» — `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md`, 0:10 (≈ P2 00:51:01) |
| 2026-09-18 | Para la primera parte (ensayes) del extracto solo se necesitan elementos, pagables y castigables, y límites; las fórmulas de pago, más adelante. | «Para nuestro análisis, ¿cuáles son los elementos pagables y cuáles son los castellanos? ¿Cuáles son los dímites de partición? Para eso te va a servir el extracto. Las fórmulas de cómo se paga y todo, para estos fines de ensayes que estamos haciendo, no sirven.» — `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:04:27 |
| 2026-09-18 | Se agregan al formato de extracto: vigencia para lotes, Incoterm, extracto que sustituye, tipo de extracto y extracto base. Nosotros documentamos y analizamos las implicaciones. | «Hay que agregarle también el incoterm, por favor.» — `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md`, 00:10:57 |
| 2026-09-18 | Nosotros preparamos maquetas de los procesos principales para que el cliente las valide antes de desarrollar. | «algunas maquetas para que por si ustedes las vean, para lidiar con ustedes sobre maqueta ya, funcionalidad» — `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 01:01:00 |
| 2026-09-18 | Siguiente sesión tentativa: semana del 28 de septiembre, cerca del cierre contable del cliente. | «gustan que ponga mucha oficia tentativa el 28 o conforme se acerque y tengan ustedes más claras su agenda lo definimos.» — `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md`, 00:04:58 |
| 2026-10-21 | Primera etapa: la captura de ERSA y el análisis de intercambio y tercería. | Respuesta del usuario a P-003 (sin grabación); confirmar con el cliente |
| 2026-10-21 | Del extracto, en esta etapa solo se usan los elementos, pagables y castigables y los límites de partición; fórmulas, cálculo y liquidación, en la siguiente. | Respuesta del usuario a P-002 (sin grabación); confirmar con el cliente |
| 2026-10-21 | ERSA empieza con equipos prestados y VPN y después pasa a internet; se diseña para internet desde el principio. | Respuesta del usuario a P-004 (sin grabación); confirmar con TI del cliente |
| 2026-10-21 | Los datos de PROVEM llegan por un endpoint. | Respuesta del usuario a P-005 (sin grabación) |

## Fuentes procesadas

Lo que `nemit:analista` ya leyó, **un archivo por fila**. Una fuente nueva en `../fuentes/` que no esté aquí es lo que falta analizar.

| Fuente | Analizada | Requisitos que salieron | Preguntas |
|---|---|---|---|
| `documentos/2026-08-14-preview/PREVIEW-EMB-002_Evidencia_visual_original_Embarques_1.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-03-sesion-1/Copia de MRA. FRESNILLO FEBRERO-2026.xlsx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-03-sesion-1/EmbarqueCarro - 2026-09-03T125525.733 Plomo.xlsx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-03-sesion-1/EmbarqueCarro - 2026-09-03T125543.551 Zinc.xlsx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-03-sesion-1/Formato Intercambios Ciénega 02-2026.xlsx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-03-sesion-1/LotesCerrados - 2026-09-03T125438.485.xlsx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-03-sesion-1/Sesión 1 - Muestra Met-Mex - Concentrado de plomo - vista 1.jpeg` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-03-sesion-1/Sesión 1 - Muestra Met-Mex - Concentrado de plomo - vista 2.jpeg` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-03-sesion-1/Sesión 1 - Muestra Met-Mex - Concentrado de zinc.jpeg` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-04-sesion-2/ANALISIS DE FORMATO DE INTERCAMBIOS DE ENSAYES CON MMP.xlsx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-04-sesion-2/CAPTURA DE ENSAYES - MUESTRAS OFICIALES.xlsx` | 2026-10-21, completo | RF-037, RF-045 | ver `preguntas-abiertas.md` |
| `documentos/2026-09-04-sesion-2/CONSULTA DE ENSAYES - MUESTRAS OFICIALES VENTA MET MEX.xlsx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-04-sesion-2/CONSULTA DE ENSAYES REVISADOS.xlsx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería ALS/A0012605372.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería ALS/A0012605372.xls` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería CQP/1023-2026 Informe de Resultados Saucito Conc. Pb.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería CQP/Consolidado 1023-2026 Saucito Conc. Pb.xlsx` | 2026-10-21, completo | RNF-012 | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería CQP/ENVÍO SAUCITO PLOMO CQP 2do MAYO152,158 al 162.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/Hojas de trabajo/Z20260545  102-M.jpeg` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/Hojas de trabajo/Z20260554  103-M.jpeg` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/Hojas de trabajo/Z20260561  104-M.jpeg` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/Hojas de trabajo/Z20260574  105-M.jpeg` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/Hojas de trabajo/Z20260575  106-M.jpeg` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/Hojas de trabajo/Z20260580  107-M.jpeg` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/Hojas de trabajo/Z20260581  108-M.jpeg` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/Hojas de trabajo/Z20260587  109-M.jpeg` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/Orden de compra.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/S00950 resumen.xlsx` | 2026-10-21, completo | RF-067 | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/ZN-1934.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/ZN-1935.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/ZN-1936.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/ZN-1937.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/ZN-1938.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/ZN-1939.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/ZN-1940.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Informe de tercería LIMSA/ZN-1941.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Proforma Ciénega - Concentrado de plomo.xlsx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Proforma de liquidacion Final - Carbón.xlsx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-17-sesion-3/Proforma de liquidacion Final - Dore.xlsx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-18-sesion-4/0464-46 GM-26 PENMONT.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-18-sesion-4/0484-44-26 PENMONT CARBON.pdf` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-18-sesion-4/EXTRACTO CARBON ACTIVADO.docx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-18-sesion-4/EXTRACTO CONCENTRADO DE PLOMO.docx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-18-sesion-4/EXTRACTO CONCENTRADO DE ZINC.docx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-18-sesion-4/EXTRACTO DE LOTES ESPECIFICOS.docx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-18-sesion-4/EXTRACTO DORE.docx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-18-sesion-4/EXTRACTO PRECIPITADOS ORO Y PLATA.docx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-18-sesion-4/PROFORMAS F-PLUS.xlsx` | 2026-10-21, completo | — | ver `preguntas-abiertas.md` |
| `documentos/2026-09-18-sesion-4/REGLAS DE NEGOCIO PARA F-PLUS PRECIPITADOS.xlsx` | 2026-10-21, completo | RF-081, RF-082, RF-085, RF-086 | ver `preguntas-abiertas.md` |
| `documentos/2026-09-18-sesion-4/REMUESTREOS REPORT.xlsx` | 2026-10-21, completo | RF-073, RF-078 | ver `preguntas-abiertas.md` |
| `juntas/2026-08-14-preview-embarques/transcripcion.md` | 2026-10-21, completa (sin marcas finas: un bloque) | RF-103, RF-104, RF-105, RF-106, RF-107, RNF-015 | ver `preguntas-abiertas.md` |
| `juntas/2026-09-03-flujo-embarques/notas-ia-teams.md` | 2026-10-21, solo orientación (resumen de IA; no se cita) | — | ver `preguntas-abiertas.md` |
| `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-a.md` | 2026-10-21, completa | RF-001, RF-002, RF-003, RF-004, RF-005, RF-007, RF-036, RF-038, RF-041, RF-042, RF-046, RF-048, RF-052, RF-054, RF-057, RF-059, RF-062, RF-070, RF-077, RF-080, RF-094, RF-095, RF-096, RF-097, RF-099, RF-102, RNF-001, RNF-002, RNF-003, RNF-006, RNF-008, RNF-009, RNF-010, RNF-013, RNF-015, RF-D01 | ver `preguntas-abiertas.md` |
| `juntas/2026-09-03-flujo-embarques/transcripcion-grabacion-b.md` | 2026-10-21, completa | RF-098 | ver `preguntas-abiertas.md` |
| `juntas/2026-09-04-intercambio-y-terceria/notas-ia-teams.md` | 2026-10-21, solo orientación (resumen de IA; no se cita) | — | ver `preguntas-abiertas.md` |
| `juntas/2026-09-04-intercambio-y-terceria/transcripcion.md` | 2026-10-21, completa | RF-001, RF-003, RF-006, RF-007, RF-008, RF-009, RF-012, RF-013, RF-014, RF-037, RF-039, RF-040, RF-041, RF-042, RF-043, RF-044, RF-045, RF-046, RF-047, RF-048, RF-050, RF-051, RF-052, RF-053, RF-054, RF-055, RF-056, RF-057, RF-058, RF-060, RF-061, RF-062, RF-063, RF-064, RF-065, RF-066, RF-068, RF-069, RNF-003, RNF-004, RNF-005, RNF-007, RNF-008, RNF-016, RF-D03, RF-D04 | ver `preguntas-abiertas.md` |
| `juntas/2026-09-17-contratos-y-formulas/Módulos Embarques Fresnillo_ continuación análisis requerimientos.pdf` | 2026-10-21, misma transcripción de Teams en PDF; se citó el .md | — | ver `preguntas-abiertas.md` |
| `juntas/2026-09-17-contratos-y-formulas/notas-ia-teams.md` | 2026-10-21, solo orientación (resumen de IA; no se cita) | — | ver `preguntas-abiertas.md` |
| `juntas/2026-09-17-contratos-y-formulas/transcripcion-parte-1.md` | 2026-10-21, completa | — | ver `preguntas-abiertas.md` |
| `juntas/2026-09-17-contratos-y-formulas/transcripcion-parte-2.md` | 2026-10-21, completa | — | ver `preguntas-abiertas.md` |
| `juntas/2026-09-17-contratos-y-formulas/transcripcion-teams.md` | 2026-10-21, completa (casi todo en un bloque 0:10) | RF-008, RF-011, RF-013, RF-015, RF-016, RF-017, RF-018, RF-019, RF-020, RF-021, RF-023, RF-024, RF-032, RF-066, RF-067, RF-072, RF-081, RF-082, RF-083, RF-084, RF-087, RF-088, RF-089, RF-090, RF-091, RF-092, RF-093, RF-094, RF-095, RF-096, RF-097, RF-100, RF-101, RNF-011 | ver `preguntas-abiertas.md` |
| `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-1.md` | 2026-10-21, completa | RF-008, RF-009, RF-010, RF-011, RF-013, RF-016, RF-021, RF-022, RF-023, RF-024, RF-025, RF-026, RF-027, RF-028, RF-029, RF-030, RF-031, RF-032, RF-033, RF-034, RF-035, RF-087, RF-093, RNF-013, RNF-014, RF-D02 | ver `preguntas-abiertas.md` |
| `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-parte-2.md` | 2026-10-21, completa | RF-001, RF-049, RF-050, RF-071, RF-072, RF-073, RF-074, RF-075, RF-076, RF-077, RF-078, RF-079, RF-081, RF-085 | ver `preguntas-abiertas.md` |
| `juntas/2026-09-18-proformas-y-remuestreos/transcripcion-teams.md` | 2026-10-21, completa (exportación incompleta de Teams) | — | ver `preguntas-abiertas.md` |
