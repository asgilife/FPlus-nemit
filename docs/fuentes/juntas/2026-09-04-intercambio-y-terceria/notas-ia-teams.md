> Texto extraído literalmente de «NOTAS DE SESIÓN 4 SEPT.docx». Ningún otro cambio. El original está junto a este archivo.

Notas IA generadas por Teams
Notas de la reunión
Generado por IA. Asegúrate de comprobar la precisión.
Notas de la reunión:
Flujo De Intercambio Y Tercería: SAFSA-TRN-Sj-SanJulian y el equipo definieron el flujo desde la composición de muestras hasta la comparación entre ERSA, la unidad minera y Met-Mex, incluyendo reensaye, remuestreo, promedio negociado y tercería como rutas de resolución.
Composición De Muestras: La muestra compuesta se divide en tres porciones: una para Met-Mex, otra para ERSA y otra para tercería. El resultado ponderado de la unidad minera funciona como referencia para identificar posibles desviaciones, mientras que el resultado de ERSA se considera el resultado oficial que se intercambia con Met-Mex.
Revisión Y Reensaye: Si se detecta un error humano o una discrepancia atribuible al proceso de ERSA, la ruta inicial es solicitar una revisión y, cuando corresponda, un reensaye con ERSA. El remuestreo no se realiza en esta etapa; normalmente ocurre después del intercambio con Met-Mex.
Resolución De Diferencias: Después del intercambio, las partes pueden promediar cuando la diferencia está dentro de los límites contractuales, negociar cuando la diferencia es marginal o solicitar tercería cuando está fuera de rango. La tercería se realiza por elemento discrepante y puede requerir un nuevo sobre o reiniciar el proceso si no existe otra porción disponible.
Resultado De Tercería: Si el resultado del tercero es anómalo y no existe otro sobre de tercería, debe realizarse un nuevo muestreo e iniciarse nuevamente el proceso; el ensayo ponderado de la mina se conserva como referencia y no se reemplaza.

Captura Y Autorización De Ensayes: El equipo diseñó una tarea para que ERSA capture los resultados oficiales por lote y elemento, con participación de varios usuarios y un workflow de guardado, envío, revisión y autorización que preserve la trazabilidad.
Pantalla De Captura: La pantalla debe permitir seleccionar compañía, unidad, producto, tipo de búsqueda y fechas inicial y final, usando catálogos o autocompletado. Los elementos a capturar se determinarán por compañía, unidad, producto y vigencia contractual, en lugar de depender únicamente de un catálogo general.
Usuarios De ERSA: ERSA puede tener aproximadamente tres personas capturando distintos elementos. Los usuarios deben darse de alta individualmente y el sistema debe registrar, al guardar, quién capturó cada elemento y cuándo; la consulta o el reporte pueden mostrar el detalle de todos los usuarios participantes.
Workflow De Aprobación: Varios usuarios pueden guardar capturas parciales, pero el lote no se puede enviar a revisión hasta que estén capturados todos los elementos requeridos. Después pasa al revisor y posteriormente al autorizador; cualquier nivel puede devolverlo con observaciones para que se corrija desde el nivel correspondiente o, si lo devuelve el tercer nivel, desde el primer nivel.
Estados Y Bloqueos: Los estados considerados son pendiente, guardado, enviado, revisado y autorizado. Una vez enviado el lote, la captura queda bloqueada; si el lote es regresado, vuelve a habilitarse para modificación y debe continuar nuevamente por el workflow.
 
 
 


Configuración Contractual Y Datos De Muestra: SAFSA-TRN-Sj-SanJulian y el equipo acordaron que los requisitos variables deben derivarse de extractos de contrato y que las fechas, tipos de muestra y datos de lote se obtengan mediante interfaces o captura controlada.
Extracto De Contrato: Se propuso mantener un extracto contractual por compañía, unidad, producto y vigencia, incluyendo elementos participantes, pagables, castigables, fórmulas de pago, deducciones mínimas y límites de partición. La vigencia debe indicar si aplica a lotes cerrados o embarcados, porque la fecha de referencia cambia según el proceso.
Elementos Variables: Los elementos requeridos pueden variar por unidad, producto y periodo. El sistema debe desplegar únicamente los elementos aplicables al lote según el extracto contractual y no señalar durante la captura cuáles son pagables o castigables para evitar influir en los resultados reportados.
Fechas Y Tipo De Muestra: La fecha de entrega de muestra de Met-Mex a ERSA puede recibirse mediante interfaz; como alternativa, ERSA capturaría manualmente la fecha y el tipo de muestra. El tipo debe distinguir muestra original, primer remuestreo y segundo remuestreo. La recepción en laboratorio debe asociarse al código de barras y la fecha de entrega de resultados se generará al autorizar el último nivel.
Datos Integrados: El lote de Met-Mex, lote de la unidad, fecha de cierre y peso seco deben provenir de las interfaces disponibles. El sistema debe conservar la distinción entre la entrega física de Met-Mex al representante de ERSA y la entrega posterior del representante al laboratorio de ERSA.
 
 
 

Análisis De Diferencias Y Resultado Negociado: El equipo definió que el análisis comparará los ensayes de ERSA, Met-Mex y la mina contra los límites de partición, dejando la decisión final a los responsables de contratos mediante una columna editable de resultado negociado.
Comparación De Ensayes: La tarea de análisis debe mostrar los valores de ERSA, Met-Mex y la mina, la diferencia por elemento y el límite de partición correspondiente. Las diferencias superiores al límite deben aparecer en rojo como apoyo visual para la toma de decisiones.
Interpretación Del Límite: Una diferencia mayor al límite de partición no obliga automáticamente a enviar el lote a tercería. El límite sirve como base para que los administradores de contratos consideren el impacto económico, la condición pagable o castigable del elemento y el costo de una tercería.
Resultado Negociado: Después de la columna de decisión o INT se agregará la columna RN, Resultado Negociado. Esta columna replicará inicialmente el valor sugerido por INT, pero permanecerá editable para registrar si la decisión final es promediar, negociar o mantener la ruta de tercería.
Sugerencias Del Sistema: El sistema podrá sugerir un promedio cuando un elemento castigable no alcance el umbral de castigo o cuando un elemento pagable no alcance el límite de pago, aun si la diferencia técnica llevaría inicialmente a tercería. La sugerencia no sustituye la decisión de los responsables, quienes podrán elegir otra opción y dejarla registrada.


 
 
 
 

Remuestreos Y Control Histórico: El equipo identificó que el módulo de remuestreos aún debe definirse, pero estableció que un remuestreo invalida la selección del ensayo original para intercambio y que deben conservarse todos los valores históricos.
Sustitución De Resultados: Cuando existe un remuestreo, los ensayes de la muestra original deben quedar inhabilitados para intercambio y utilizarse los resultados del primer o segundo remuestreo, según corresponda. La muestra ponderada de la mina permanece sin sustituirse.
Consulta De Remuestreos: Debe crearse una tarea específica para consultar lotes remuestreados, mostrando cuál fue el ensayo original, cuál correspondió al primer remuestreo y cuál al segundo. Esta funcionalidad quedó pendiente de definición detallada.
Historial De Cambios: Si ERSA confirma o modifica un valor, el sistema debe conservar el valor anterior, el nuevo valor, la fecha, el usuario, el evento y las observaciones. La tarea de lotes revisados deberá permitir consultar esta trazabilidad.
 
Carga Masiva Y Evidencias: SAFSA-TRN-Sj-SanJulian y el equipo acordaron priorizar cargas mediante Excel para evitar errores de tecleo, evaluar la compatibilidad con distintos formatos de terceros y almacenar archivos o imágenes como evidencia auditable.
Carga De Met-Mex: Los resultados que Met-Mex envía en Excel deben cargarse al sistema mediante un proceso masivo, ya que no se cuenta con una interfaz directa. El sistema debe considerar la conversión entre unidades, por ejemplo, plata en kilogramos por tonelada frente a gramos por tonelada, o permitir seleccionar la unidad de origen y la unidad de visualización.
Carga De Tercería: Los resultados de terceros se cargarán manualmente y se revisará con TI si pueden cargarse en modo masivo. La plantilla debe aceptar lotes y elementos parciales, siempre que incluya las llaves necesarias, como identificadores de lote, fecha de cierre y elemento solicitado.
Formatos Externos: Como cada laboratorio tercero puede utilizar un formato diferente, se deben evaluar dos alternativas: estandarizar previamente los archivos o preparar el sistema para leer varios formatos. El requisito debe contemplar posibles cambios futuros en las plantillas y en los elementos reportados.
Evidencia Documental: Además de procesar los valores, el sistema debe permitir adjuntar el archivo recibido y la evidencia de comunicación. Para intercambios se conservarían el Excel y una imagen o copia del correo; para tercería, el archivo del tercero y la evidencia del correo correspondiente, con fines de control y auditoría.
 
 
 

Cédulas De Intercambio: El equipo definió que la consulta de ensayes permitirá seleccionar lotes y generar cédulas de intercambio en Excel y PDF con la información oficial, contractual y comparativa necesaria para Met-Mex.
Contenido De La Cédula: La cédula debe incluir compañía, unidad, producto, lote de Met-Mex, lote de la unidad, fecha de cierre, peso seco y resultados por elemento. También debe mostrar el usuario que generó el reporte y, cuando aplique, el laboratorio tercero y la fecha de tercería.
Información Contractual: La cédula debe mostrar los mínimos pagables, los castigos y los límites de partición por elemento, obtenidos del extracto contractual. Los elementos deben agruparse conforme al extracto y mantenerse en el orden definido por la configuración vigente.
Selección Y Estados: Solo deben poder seleccionarse lotes que cumplan las reglas del proceso, incluyendo que un lote remuestreado no utilice el ensayo original para intercambio. La tarea debe traer los valores de ERSA confirmados o actualizados antes de generar la cédula.
Formatos De Salida: La cédula se generará en Excel y PDF. ERSA también requiere exportar la tabla de captura a Excel en el formato que utiliza actualmente para integrarla a sus controles internos de calidad.
 
 
 
Alcance Y Próximos Pasos: El equipo acordó continuar con el módulo de ensayes dentro de F Plus, completar los requisitos pendientes y utilizar el periodo sin reuniones para revisar materiales antes de la siguiente sesión prevista para el día 14.
Pendientes Funcionales: Quedaron pendientes la definición completa del proceso de remuestreos, la consulta de ensayes remuestreados, los extractos contractuales, los cálculos económicos para apoyar decisiones y reportes comparativos entre mina, ERSA, Met-Mex y terceros.
Nombre Del Módulo: Se identificó el sistema como F Plus y se propuso denominar esta sección F Plus Ensayes, con tareas separadas para captura, consulta, análisis, intercambio y remuestreos.
Materiales Compartidos: Se solicitó concentrar en el grupo de Teams los reportes y archivos de referencia, incluidos ejemplos de embarques, lotes cerrados, reportes actuales de ERSA y comparativas de intercambio. También se reiteró que los ensayes de oro y plata se manejan en gramos por tonelada y los pesos en toneladas.
Generación De Requisitos: El equipo explicó que convertirá las especificaciones en requisitos e historias de usuario con maquetas, que serán revisadas antes de generar código. La preparación de requisitos requiere aproximadamente dos semanas; las historias de usuario, una o dos semanas; y la generación de código aún debe estimarse con base en la experiencia del sistema recién lanzado.
Tareas de seguimiento:
Flujo De Reensayos Y Remuestreos: Documenten las reglas y excepciones para solicitar, validar y ejecutar reensayos, remuestreos y tercerías, incluyendo el tratamiento de resultados sin discrepancia resoluble y la inhabilitación de resultados originales tras un remuestreo. (SAFSA-TRN-Sj-SanJulian)
Extractos De Contrato: Definan la estructura del extracto de contrato por compañía, unidad, producto y vigencia, incluyendo tipo de vigencia, elementos aplicables, criterios de pago y castigo, y límites de partición para configurar el sistema. (SAFSA-TRN-Sj-SanJulian)
Usuarios Y Captura De Ensayos: Propongan el diseño de captura que permita a varios usuarios registrar distintos elementos por área, mantenga la trazabilidad de usuario, elemento y fecha, y ofrezca acceso flexible sin bloquear capturas por suplencias. (SAFSA-TRN-Sj-SanJulian)
Interfaz De Fechas Y Muestras: Confirmen con Medmex la factibilidad de una interfaz para transmitir fechas de entrega y tipo de muestra; si no es viable, definan la captura manual por ERSA. (SAFSA-TRN-Sj-SanJulian)
Carga De Resultados De Medmex: Definan con TI el mecanismo de carga masiva del archivo de Excel de Medmex, incluyendo conversión de unidades, validación de campos llave y tolerancia a cambios controlados en la plantilla. (SAFSA-TRN-Sj-SanJulian)
Carga De Resultados De Tercería: Definan con TI la opción de cargar masivamente resultados de terceros y los formatos o plantillas necesarios para distintos laboratorios, considerando que pueden incluir solo determinados lotes y elementos. (SAFSA-TRN-Sj-SanJulian)
Resultado Negociado: Incorporen al requerimiento la columna de resultado negociado, dejando editable la decisión sugerida por el sistema para registrar si se promedia o se envía a tercería, junto con la justificación correspondiente. (SAFSA-TRN-Sj-SanJulian)
Notificaciones De Revisión: Especifiquen el correo automático que se enviará cuando un lote sea regresado, incluyendo compañía, unidad, producto, lote y observaciones de revisión. (SAFSA-TRN-Sj-SanJulian)
Confirmación Y Actualización: Definan con Elsa qué rol debe confirmar los valores después de una devolución y documenten el flujo para confirmar sin cambios o actualizar resultados antes de reenviar el lote. (SAFSA-TRN-Sj-SanJulian)
Trazabilidad Y Evidencias: Agreguen al sistema el resguardo consultable de valores anteriores y nuevos, observaciones, usuarios y fechas, además de archivos, imágenes de correos y Excels de intercambios y tercerías como evidencia de auditoría. (SAFSA-TRN-Sj-SanJulian)
Exportación De Capturas: Incorporen la exportación a Excel de los lotes seleccionados en el formato requerido actualmente por ERSA para sus controles internos y de calidad. (SAFSA-TRN-Sj-SanJulian)

Notas de Sonia
No tomé notas. Estuve poniendo atención.