# 07 · Atributos de calidad

> Estado: PRELIMINAR · línea base de trabajo v0.1
> Los 26 NFR preliminares se dividen en tres categorías de origen: **A** sustentados en
> evidencia, **B** inferidos del flujo o del riesgo, **C** propuestos para TI y negocio.
> Ninguno está validado. **No hay umbrales cuantitativos acordados**: no existe SLA, RTO,
> RPO, concurrencia, latencia, volumen ni retención comprometidos.

## Requisitos no funcionales de las juntas (2026-10-21)

Los requisitos con cita y origen viven en `docs/requisitos/requisitos.md` y **usan otra numeración** que los NFR
preliminares de abajo. Esta tabla es la que manda desde hoy; la de los NFR preliminares se conserva como historia.

| RNF | Qué pide | Decisión que lo cumple | Estado |
| --- | --- | --- | --- |
| RNF-001 · trazabilidad | Quién, qué, cuándo y valor anterior; nada se borra | Esquema `aud` sin `DELETE`; resultados con versión, nunca reescritos ([04](04-modelo-de-datos.md)) | Cubierto |
| RNF-002 · confidencialidad | La información es confidencial | Identidad y privilegios segregados ([ADR-003](adr/ADR-003-bd-unica-esquemas-separados.md), [ADR-004](adr/ADR-004-identidad-segregada-por-modulo.md)); TLS en toda conexión ([10](10-controles-owasp-asvs.md)) | Cubierto |
| RNF-003 · interno, ERSA externo | VPN con equipos prestados; después internet | [ADR-011](adr/ADR-011-captura-para-internet.md) | Propuesto |
| RNF-004 · SSO corporativo / usuario externo | Fresnillo con su usuario corporativo; ERSA con usuario externo | [ADR-012](adr/ADR-012-sso-corporativo.md) para Fresnillo; credenciales locales con Argon2id para ERSA ([05](05-seguridad-y-control-de-acceso.md)) | **Bloqueado por P-057** |
| RNF-005 · externos solo al aplicativo | Acceso solo a la aplicación, sesión grabada | El módulo solo expone la aplicación ([ADR-011](adr/ADR-011-captura-para-internet.md)); grabar la sesión es un control de TI (obligación residual, [10](10-controles-owasp-asvs.md)) | Cubierto en lo que toca al sistema |
| RNF-006 · ERSA solo ve lo de Fresnillo | ERSA ensaya para otros clientes | FPlus solo contiene lotes de Fresnillo; las cuentas de ERSA son locales de `seg_cap` y no comparten nada con otros sistemas del laboratorio | Cubierto |
| RNF-007 · terceros sin acceso | Ningún vínculo con los laboratorios terceros | No existen cuentas ni pantallas para terceros; sus archivos los sube Fresnillo ([ADR-015](adr/ADR-015-evidencias-en-base.md)) | Cubierto |
| RNF-008 · ágil y práctico | Rapidez, pocos pasos | Captura separada de la consulta ([ADR-001](adr/ADR-001-dos-modulos-desplegables.md)); bloqueo optimista ([ADR-006](adr/ADR-006-bloqueo-optimista.md)); consultas sin N+1 (AR-BD-09). **Sin cifra que verificar** | Bloqueado por P-045 |
| RNF-009 · computadoras fijas, lector de código | Captura en el laboratorio | Aplicación web; el lector funciona como teclado, sin integración de hardware | Cubierto |
| RNF-010 · exportaciones analizables | Sin encabezados repetidos ni celdas combinadas; unidades explícitas | Convención para toda exportación ([08](08-convenciones-de-desarrollo.md)) | Cubierto |
| RNF-011 · decimales y redondeo | Reproducir los resultados de Met-Mex | `DECIMAL` con precisión explícita, nunca `FLOAT` ([04](04-modelo-de-datos.md)); la regla por paso es de negocio | Bloqueado por P-046 |
| RNF-012 · no analizado no es cero | Distinguir vacío de cero | Cuatro estados del valor además del número ([04](04-modelo-de-datos.md)) | Cubierto |
| RNF-013 · catálogos, no valores fijos | Configurable para crecer | Clasificación AR-G-24 y AR-G-23 ([04](04-modelo-de-datos.md)) | Cubierto |
| RNF-014 · cálculos verificables | Comparar contra los controles del usuario | Primera etapa: el análisis enseña los valores de origen de cada diferencia. El simulador (RF-035) es de la siguiente etapa | Parcial por alcance |
| RNF-015 · separado de PROVEM | Lógica propia sin tocar PROVEM | FPlus solo **lee** PROVEM por su endpoint ([ADR-013](adr/ADR-013-provem-por-endpoint.md)) | Cubierto |
| RNF-016 · apariencia sobria | Corporativa, claro y oscuro | Tema propio compartido ([ADR-007](adr/ADR-007-tema-propio-compartido.md), [ADR-008](adr/ADR-008-tres-modos-de-visualizacion.md)) | Cubierto |

## Cómo respondía la arquitectura a los NFR preliminares (v0.1)

### A · Sustentados

| NFR | Requerimiento | Respuesta arquitectónica |
| --- | --- | --- |
| **001** Trazabilidad | Conservar cambios, confirmaciones y decisiones con responsable, fecha, evento y contexto | Esquema `aud` append-only, sin `DELETE` para ningún módulo. Identidad desnormalizada en el evento ([05](05-seguridad-y-control-de-acceso.md)) |
| **002** Historia | Preservar valores anteriores y su vigencia sin sobrescritura silenciosa | Regla de modelado: nada se sobrescribe. Valor anterior y nuevo en el evento ([04](04-modelo-de-datos.md)) |
| **003** Rendimiento | Priorizar rapidez en captura, consulta y análisis | Módulo de captura ligero y separado del de consulta: la carga analítica no compite con la transaccional. Bloqueo optimista en vez de pesimista |
| **004** Usabilidad | Minimizar interacciones sin omitir revisión ni autorización | Módulo de captura con pocas pantallas, dedicado a una sola tarea |
| **005** Autorización | Restringir por facultades y mantener identidad individual | RBAC con permisos como dato; identidad individual obligatoria en todo evento |
| **006** Ciclos | Múltiples reensayes sin tope artificial, con secuencias separadas | `CICLO` como filas, nunca columnas fijas. Prohibido modelar dos ciclos |
| **007** Procedencia | Auditar origen analítico, versión y autoridad por etapa | Origen del dato conservado en toda ingesta; otorgamientos de rol con quién y cuándo |
| **008** Confidencialidad | Separar información comercial entre captura ERSA e intercambio externo | **Es el driver principal de la segregación de identidad y de esquemas** ([ADR-004](adr/ADR-004-identidad-segregada-por-modulo.md)) |

### B · Inferidos, por validar

| NFR | Requerimiento | Respuesta arquitectónica | Riesgo |
| --- | --- | --- | --- |
| **009** Integridad semántica | Distinguir **cero real, pendiente, ausente y no aplica** sin convertirlos silenciosamente | Campo de estado explícito junto al valor: una columna anulable no alcanza para cuatro estados ([04](04-modelo-de-datos.md)) | **Alto.** Tratar un pendiente como cero inventa una diferencia contra Met-Mex y puede disparar una tercería improcedente |
| **010** Unidades y precisión | Consistencia de unidades, decimales y redondeo | `DECIMAL` con precisión explícita, nunca `FLOAT`. Unidad almacenada junto al valor | Alto |
| **011** Consistencia del contexto | Evitar asociaciones cruzadas entre compañía, unidad, producto, muestra, lote y elemento | Condiciones contractuales en base de datos con vigencia. El NFR pide consistencia **lógica** y aclara que **no prescribe separación física de BD**: coherente con la base única de [ADR-003](adr/ADR-003-bd-unica-esquemas-separados.md) | Medio |
| **012** Edición concurrente | Prevenir **pérdida silenciosa** cuando varios guardan o envían el mismo contexto | Bloqueo optimista con `@Version` ([ADR-006](adr/ADR-006-bloqueo-optimista.md)). El NFR pide considerar el conflicto **sin imponer patrón técnico**: la elección es nuestra, no suya | **El comportamiento esperado no está validado con negocio** |

### C · Propuestos para TI y negocio

Estos **no tienen respuesta arquitectónica todavía** porque no tienen métrica. Se listan
para que no se pierdan:

| NFR | Tema | Qué falta |
| --- | --- | --- |
| 013–016 | Continuidad, respaldo, RTO, RPO, retención | Definición de TI. Mientras tanto, no se implementa purga alguna |
| 017 | Seguridad técnica: identidad, sesión, acceso interno y externo | Línea base técnica ya fijada en [10](10-controles-owasp-asvs.md) (ASVS 5.0 L1/L2). Siguen TBD los **umbrales de negocio**: política de contraseñas, bloqueo y expiración. La topología sigue **bloqueada por si ERSA sale a internet** |
| 018 | Cifrado | **En tránsito: resuelto.** TLS obligatorio en toda conexión, incluida la de base de datos, con versión mínima explícita ([10](10-controles-owasp-asvs.md)). **En reposo: TBD**, TDE y Always Encrypted evaluables una vez definido qué es sensible |
| 019–021 | Observabilidad y monitoreo, concurrencia y volumen, tiempos de respuesta | Sin cifras de carga real. NFR-020 advierte **no convertir ejemplos en capacidad objetivo** |
| 022 | Compatibilidad de dispositivos y navegadores | **Bloquea el diseño del tema** ([09](09-interfaz-y-tema.md)): sin saber qué navegadores y si hay uso en tableta, no se fija el CSS ni la densidad |
| 023 | Accesibilidad | Se decide en el tema, no en las pantallas. Piso mínimo fijado en [09](09-interfaz-y-tema.md). **Se verifica en los tres modos visuales**, no solo en claro ([ADR-008](adr/ADR-008-tres-modos-de-visualizacion.md)); nivel de conformidad exigido por determinar |
| 024–026 | Ambientes, logging, SLA de interfaces | Definición de TI. **NFR-026 deja de ser hipotético**: la interfaz de PROVEM va a existir ([06](06-integraciones.md)), así que su SLA, sus fallos y su disponibilidad requieren acuerdo |

## Compromisos aceptados

Toda arquitectura sacrifica algo. Estos son los intercambios asumidos, para que se revisen
si cambian las condiciones:

| Se gana | Se pierde | Por qué se acepta |
| --- | --- | --- |
| Contención del daño si se compromete el módulo expuesto | Dos padrones de usuarios; doble cuenta para quien necesite ambos módulos | NFR-008 lo requiere, y el módulo puede quedar expuesto a internet |
| Simplicidad: sin API ni capa de integración que mantener | Acoplamiento por esquema: un cambio obliga a redesplegar ambos módulos | Un equipo, un esquema, y el módulo de reportes necesita ver todo |
| Independencia de despliegue y de red entre módulos | Dos instancias de Payara que operar | Sin esto no se puede poner un módulo en DMZ |
| Historia completa y auditable | Más escrituras y crecimiento sostenido de la base | Es requerimiento de primer orden, no una opción |
| Matriz de permisos cambiable sin recompilar | Una indirección extra entre rol y permiso | P02 está abierto y la matriz va a cambiar |
| Interfaz coherente y corregible en un solo lugar | Tercer artefacto que versionar; cambiarlo obliga a redesplegar ambos módulos | PrimeFaces 16 no trae tema: hay que diseñarlo completo, y duplicarlo divergiría de inmediato |
| Menor fatiga visual en jornadas largas: tres modos | Tres paletas que diseñar y verificar; el contraste se valida por triplicado | Condición real de uso: captura numérica y lectura de tablas densas durante horas |
| Controles de seguridad verificables y una superficie de ataque acotada | Más trabajo de implementación y pruebas; algunos controles exigen acción de TI para quedar cumplidos | Un módulo puede quedar expuesto a internet con datos comerciales detrás |

## Lo que esta arquitectura NO promete

Dicho explícitamente para que no se lea de más:

- **No promete disponibilidad ni tiempos de respuesta.** No hay SLA acordado.
- **No promete capacidad infinita.** "Ciclos sin tope artificial" significa que el sistema
  no impone un máximo arbitrario; no que el almacenamiento o el rendimiento sean ilimitados.
- **No promete integridad del intercambio con Met-Mex.** Conserva evidencia del envío; no
  certifica que la contraparte haya recibido ni que el contenido coincida.
- **No resuelve el valor aplicable.** La regla depende de P01 y sigue abierta.
