# 01 · Visión y alcance

> Estado: PRELIMINAR · línea base de trabajo v0.1

## El problema

Cuando la mina envía concentrado a Met-Mex Peñoles, el valor del embarque depende del
contenido de cada elemento (Pb, Zn, Au, Ag, …). Ese contenido se determina por ensaye, y
cada parte ensaya por su cuenta muestras del mismo lote:

- **ERSA** ensaya en representación de Fresnillo PLC.
- **Met-Mex** ensaya con su laboratorio central.
- Si la diferencia entre ambos excede el límite de partición, entra un **laboratorio de
  tercería**.

Hoy ese seguimiento vive repartido entre hojas de Excel, correos y PDFs. No hay historia
confiable, la autoría de cada dato se pierde y la conciliación es manual.

## Qué resuelve FPlus

Concentrar el ciclo completo del ensaye en un solo sistema con historia, autoría y
auditabilidad: captura, revisión, liberación, intercambio con Met-Mex, negociación,
tercería y resolución del valor aplicable por elemento.

## Alcance

**Dentro:** captura de ensayes por elemento · revisión y autorización · conciliación
interna · intercambio con Met-Mex · negociación · tercería · resolución del valor
aplicable · ciclos de reensaye y remuestreo · historia y auditoría · condiciones
contractuales por vigencia · importación y exportación.

**Fuera (o no decidido):** liquidación comercial · DW/BI y Power BI · acceso directo de
laboratorios de tercería al sistema · integración automática con sistemas de Met-Mex.

## Drivers arquitectónicos

Lo que realmente moldea el diseño, en orden de peso:

1. **Dos audiencias en dos redes distintas.** ERSA es un laboratorio representante, no
   personal interno de Fresnillo; puede necesitar entrar desde fuera. Fresnillo PLC opera
   en red interna. Esto parte el sistema en dos módulos desplegables
   ([ADR-001](adr/ADR-001-dos-modulos-desplegables.md)).
2. **Confidencialidad comercial entre esas audiencias** (NFR-008). La información del
   intercambio y la negociación no debe quedar expuesta por el hecho de que ERSA capture en
   el mismo sistema. Esto segrega la identidad y los permisos
   ([ADR-004](adr/ADR-004-identidad-segregada-por-modulo.md)).
3. **Historia y auditabilidad como requisito de primer orden** (NFR-001, NFR-002, NFR-007).
   Nada se sobrescribe en silencio; todo cambio conserva responsable, fecha, evento y valor
   anterior. Esto condiciona el modelo de datos más que el rendimiento.
4. **Rapidez y mínimo de clics en las tareas frecuentes** (NFR-003, NFR-004). Prioridad
   cualitativa decidida; el umbral medible está por acordar.
5. **Ciclos sin tope artificial** (NFR-006). El modelo no puede asumir dos reensayes ni dos
   columnas fijas.
6. **El reporte necesita verlo todo.** El módulo de consulta requiere acceso al dato
   completo que captura el otro módulo, lo que empuja a integración por base de datos
   compartida ([ADR-002](adr/ADR-002-integracion-por-base-de-datos.md)).

## Restricciones

| Restricción | Origen |
| --- | --- |
| Java 25, Jakarta EE 11, PrimeFaces 16, Payara 7.2026.8, SQL Server 2022 | Decidido por el equipo |
| Base de datos única | Decidido por el equipo |
| Sin API con Met-Mex: el acceso técnico no está aprobado | Insumos de arquitectura, TR M01/E03 |
| Sin SSO concreto ni CyberArk aprobados | Insumos de arquitectura, G1/G2/G3 |
| Sin acceso directo de laboratorios de tercería | Insumos de arquitectura, TR R03 |

## Supuestos declarados

Se asumen para poder avanzar. Si alguno resulta falso, cambia el diseño:

- **S1.** Los analistas de ERSA acceden por navegador a una aplicación web, no por un
  cliente instalado.
- **S2.** El volumen de lotes y ensayes cabe holgadamente en una instancia de SQL Server;
  no se requiere particionamiento ni escalado horizontal en la primera versión.
- **S3.** ~~La ingesta es manual.~~ **Corregido:** una parte entra por la **interfaz de
  PROVEM**, donde Met-Mex carga su información ([06](06-integraciones.md)). La carga manual
  de archivo sigue existiendo para lo que no entre por esa interfaz.
- **S4.** Las dos aplicaciones se despliegan sobre instancias de Payara distintas.

## Pendientes que bloquean decisiones de arquitectura

| Pendiente | Qué bloquea |
| --- | --- |
| ~~¿ERSA entra desde internet o por VPN/enlace dedicado?~~ | **Respondido el 2026-10-21:** VPN en la primera etapa, internet después; se diseña para internet ([ADR-011](adr/ADR-011-captura-para-internet.md)) |
| **¿Qué es el SSO corporativo de Fresnillo?** (P-057) | Autenticación de `fplus-reportes` ([ADR-012](adr/ADR-012-sso-corporativo.md) frente a [ADR-010](adr/ADR-010-autenticacion-por-directorio.md)) |
| **P02 · Autoridades** | Matriz de roles y permisos; dónde corta la frontera entre módulos; evento de liberación. |
| **P01/P07 · Valor aplicable y tercería** | Modelo de resolución del resultado aplicable por elemento. |
| **P05 · Condiciones contractuales** | Estructura de límites de partición y su vigencia. |
| **P08/P09 · Integración y calidad** | Llaves, duplicados, fallos parciales, volumen y concurrencia real. **Ahora bloquea la interfaz de PROVEM**, que sí va a existir ([06](06-integraciones.md)). Desde el 2026-10-21: PROVEM es un endpoint; falta su contrato (P-005, [ADR-013](adr/ADR-013-provem-por-endpoint.md)) |

Ninguno de estos se resuelve con criterio del equipo de desarrollo: requieren decisión de
negocio o de TI.
