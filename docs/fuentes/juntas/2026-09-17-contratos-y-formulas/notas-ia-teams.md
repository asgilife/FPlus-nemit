> Texto extraído literalmente de «Sesión 03.docx». Ningún otro cambio. El original está junto a este archivo.

Notas generadas por IA Teams
Generado por IA. Asegúrate de comprobar la precisión.
Notas de la reunión:
Configuración Dinámica De Contratos Y Fórmulas: SAFSA-TRN-Sj-SanJulian explicó al equipo cómo los contratos por mina y producto definen pagos, deducciones, castigos, conversiones y periodos de cotización, y se acordó que el sistema debe representar estas reglas de forma configurable y vigente en el tiempo.
Reglas De Pago: Los contratos pueden calcular pagos por elemento mediante ensaye menos una deducción fija y un porcentaje del remanente, como oro y plata, o mediante un porcentaje con deducción mínima, como zinc, plomo, oro o plata en otros contratos. El sistema debe permitir capturar el porcentaje pagable, la deducción, el tipo de cotización y el elemento correspondiente.
Deducciones Y Castigos: Las deducciones incluyen cargos de tratamiento, refinaciones, escaladores o desescaladores y castigos por impurezas. Los castigos pueden aplicarse al exceso sobre un contenido libre, a todo el contenido cuando se supera un umbral o por rangos progresivos; por ello deben modelarse como distintos tipos de fórmula y no como una sola plantilla fija.
Contratos Y Vigencias: Se indicó que existe un contrato por producto y por mina, con vigencia propia. Aunque las fórmulas suelen repetirse entre contratos del mismo producto, cambian valores, cargos o elementos castigables; por ejemplo, Ciénega puede incluir un elemento adicional. El sistema debe permitir modificar reglas sin dejar fórmulas rígidas.
Unidades Y Conversiones: Las entradas pueden venir en gramos por tonelada, kilogramos por tonelada, porcentajes, partes por millón, dólares por onza, dólares por gramo, dólares por kilogramo o dólares por tonelada. Las fórmulas deben convertir unidades antes de calcular, incluyendo conversiones de oro y plata, porcentajes a kilogramos por tonelada y ppm a porcentaje    
 
 
 
 
 
 
 
Cálculo De Proformas Y Liquidaciones: SAFSA-TRN-Sj-SanJulian mostró cómo las proformas y liquidaciones calculan pagos, deducciones, valor por tonelada y valor por lote, y señaló que el sistema debe permitir escenarios con distintos ensayes, pesos y cotizaciones antes de la liquidación final.
Flujo De Cálculo: El cálculo parte de los ensayes pagables, que se multiplican por las cotizaciones normalizadas para obtener los pagos en dólares por tonelada. Después se suman cargos de tratamiento, refinaciones y castigos; el total de deducciones se resta de los pagos para obtener el valor por tonelada, que finalmente se multiplica por el peso seco para obtener el valor del lote.
Escenarios Proforma: Las proformas deben permitir seleccionar qué ensayes utilizar, por ejemplo los avisados por la mina, los propios, los de Met-Mex o los de tercería, así como comparar el impacto de promediar o enviar a tercería. También deben permitir variar pesos y cotizaciones para evaluar diferentes escenarios antes de contar con valores finales.
Diferencias De Redondeo: El cálculo del sistema puede no coincidir exactamente con la liquidación de Met-Mex debido a conversiones y redondeos realizados con diferentes cantidades de decimales. Se indicó que las diferencias esperadas pueden ser pequeñas y deben revisarse identificando unidades, precisión y reglas de redondeo.
Liquidación Final: La liquidación final utiliza los ensayes finales acordados, las cotizaciones que correspondan al contrato y los pesos oficiales. El resultado debe incluir pagos, deducciones, valor bruto, valor por tonelada y valor total del lote, siguiendo la estructura de los ejemplos mostrados del portal de Met-Mex.
 
 
 
 
 
 
 
 
 
 
 
Periodos De Cotización Y Extractos Específicos: SAFSA-TRN-Sj-SanJulian explicó que el periodo de cotización depende del contrato y de la fecha de cierre, con excepciones pactadas para lotes específicos, por lo que el sistema debe manejar extractos con vigencias y condiciones particulares.
Regla M Más Uno: Para ciertos metales, el periodo estándar es el promedio aritmético mensual del mes siguiente a la fecha de cierre del lote; por ejemplo, un lote cerrado en agosto utiliza el promedio de septiembre.
Excepciones Contractuales: Algunos contratos aplican reglas por fecha de entrega, como utilizar el mes de entrega para lotes recibidos dentro de un rango de días y el mes siguiente para los recibidos después. Estas reglas deben configurarse desde antes del evento y no cambiarse una vez conocidas las cotizaciones.
Lotes Específicos: Cuando un comprador no puede recibir material y se acuerda reconocerlo como entregado en otro mes, se genera un extracto para lotes específicos. Ese extracto se asocia posteriormente a los números de lote definidos y puede cambiar el periodo de cotización de M+1 a M+0, además de otras condiciones pactadas.
Fuentes De Cotización: Las cotizaciones provienen de publicaciones especializadas que el equipo de tesorería recopila y concentra en un archivo mensual. Para las proformas se propuso cargar el archivo de cotizaciones; para la liquidación final se deben tomar los valores correspondientes al contrato y al periodo aplicable.
 
 
Carga De Resultados De Laboratorio: SAFSA-TRN-Sj-SanJulian presentó los archivos PDF y Excel de ALS, CQP y LINSA, y el equipo definió como necesidad funcional cargar los resultados directamente para evitar errores de captura y transformar notas o unidades especiales antes del cálculo.
Archivos De Tercería: Los laboratorios terceros entregan resultados en PDF y, en algunos casos, en Excel. Los reportes pueden agrupar varios lotes o identificar cada lote individualmente; el sistema debe reconocer el número de lote, el laboratorio, los elementos analizados y los resultados correspondientes.
Carga Automática: Se propuso cargar los archivos directamente al sistema en lugar de capturar manualmente los resultados. Si un proveedor cambia el formato, será necesario ajustar el lector o crear una configuración que permita definir las posiciones y campos del archivo.
Notas Del Laboratorio: Algunos reportes incluyen notas que modifican la interpretación del resultado, como plata que incluye oro o zinc que incluye cadmio. En esos casos se deben realizar conversiones y restar el contenido del elemento incluido para obtener el valor corregido antes de usarlo en pagos o comparativos.
Laboratorios Y Fechas: La asignación de terceros puede depender del contrato y de la fecha de cierre; se mencionaron ALS, CQP y LINSA como laboratorios utilizados bajo distintas reglas. El sistema debe conservar esa relación para determinar qué resultados corresponden a cada lote y elemento.
 
 
 
 
 
 
 
Comparación De Ensayes Y Pesos: SAFSA-TRN-Sj-SanJulian describió el proceso para comparar ensayes y pesos de mina, Met-Mex y terceros, incluyendo revisiones por diferencias relevantes y la selección del conjunto de datos utilizado en cada proforma.
Datos Comparativos: El comparativo debe mostrar fecha de cierre, lote de mina, lote de Met-Mex, peso seco reportado por cada parte, diferencia en kilogramos y diferencia porcentual. También debe incluir ensayes avisados de la mina, ensayes de Met-Mex y resultados de terceros por elemento.
Revisión De Diferencias: Se explicó que diferencias superiores a ciertos umbrales suelen llevar a revisión; como referencia, en oro alrededor de 1% y en plata alrededor de 2%, aunque la relevancia depende del nivel absoluto del ensaye. La revisión puede confirmar controles, solicitar reensaye o pedir un nuevo muestreo.
Peso Seleccionable: Para una proforma, el usuario debe poder seleccionar si utiliza el peso reportado por la mina o el peso oficial de Met-Mex. Esa selección permite evaluar si una disminución en ensaye se compensa con un mayor peso recibido o si el peso modifica significativamente el valor final.
Tipos De Ensayo: Se distinguieron ensayos de comparación, donde cada parte analiza su muestra y se comparan resultados, y ensayos en conjunto, donde una muestra oficial se analiza en presencia de representantes. El resultado final se valida antes de utilizarlo en la liquidación.
 
 
 
Diseño De Roles Y Colaboración: SAFSA-TRN-Sj-SanJulian planteó que los roles del sistema deben ser configurables y que la revisión de valores debe manejarse principalmente como colaboración, no como un flujo rígido de autorización.
Roles Configurables: El sistema debe permitir dar de alta roles, asociar tareas a cada rol y asignar roles a los usuarios. Los perfiles no deben quedar fijos porque una persona puede cambiar de función con el tiempo.
Revisión Colaborativa: La carga y revisión de información puede involucrar a distintos integrantes, pero la confirmación interna de que los ensayes son aceptables actualmente se realiza fuera del sistema, principalmente mediante comunicación interna. La funcionalidad propuesta debe facilitar la colaboración y consulta de valores.
Acción Pendiente: El equipo quedó en revisar un archivo y proponer los roles necesarios, junto con las tareas asociadas a cada rol, para que la configuración pueda incorporarse al diseño del sistema.
Archivos Y Próximos Insumos: SAFSA-TRN-Sj-SanJulian y el equipo identificaron los archivos necesarios para continuar el análisis funcional, incluyendo proformas, liquidaciones, cotizaciones, extractos, comparativos y reportes de laboratorio.
Archivos Requeridos: Se enlistaron proformas de Excel, formatos de liquidación de Met-Mex, archivo de cotizaciones, extractos contractuales, comparativos de ensayes, resultados de terceros y PDFs de ensayos en conjunto.
Revisión Del Equipo: El equipo revisará las grabaciones y archivos compartidos para desglosar las fórmulas y plantear dudas puntuales. También se indicó que aún falta explicar el proceso de remuestreo y otros reportes de tiempos y flujo.
Reportes Futuros: Se anticipó un reporte más completo que permita analizar tiempos desde el embarque hasta la recepción, muestreo, análisis, intercambio de ensayes, tercería, resultados y liquidación, además de comparar resultados por periodo, mina, producto y elemento.
Tareas de seguimiento:
Archivos De Referencia: Compartan los archivos de proformas, liquidaciones, cotizaciones, extractos, comparativos de ensayes, resultados de terceros y PDFs de ensayes en conjunto para que el equipo pueda desmenuzar las fórmulas y formatos. (SAFSA-TRN-Sj-SanJulian)
Escenarios De Cálculo: Revisen los archivos compartidos y preparen dudas puntuales sobre las variantes de fórmulas, unidades, conversiones, ensayes, pesos y cotizaciones que deberán soportarse en el sistema. (the team)
Remuestreos: Expliquen al equipo el proceso de remuestreo y sus reglas para completar el alcance pendiente de ensayes y validaciones. (SAFSA-TRN-Sj-SanJulian)
Roles Del Sistema: Definan los roles requeridos para el proceso y las tareas asociadas a cada rol, considerando que la configuración deberá permitir asignarlos y modificarlos posteriormente. (the team)
Cotizaciones De Metales: Verifiquen con el equipo de México si existe autorización, licencia o una API disponible para consultar automáticamente las cotizaciones oficiales; mientras tanto, consideren la carga del archivo de cotizaciones. (SAFSA-TRN-Sj-SanJulian)
Validación De Fórmulas: Seleccionen ejemplos representativos por producto, mina y variante —incluidos Ciénaga, carbón, mercurio y notas de corrección de plata u otros elementos— para usarlos como escenarios de referencia y validar los cálculos del sistema. (the team)
Formato De Resultados: Revisen los formatos de PDF y Excel de ALS, CQP y LINSA para definir cómo se identificarán los lotes, elementos reportados, notas de inclusión y conversiones necesarias durante la carga automática. (the team)
Propuesta Visual: Envíen la propuesta visual pendiente para revisarla y proyectarla con el equipo. (WILLIAM)
