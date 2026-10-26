# Perfil del proyecto — FPlus

Datos que leen los agentes Nemit. Los agentes traen el **método**; aquí va lo que es **de este proyecto**. Si algo de
aquí contradice a un agente, gana esto.

**Cómo se llena:** cada sección dice qué agente la necesita. Si un agente encuentra su sección con el marcador `<…>`, la
pregunta al usuario y la escribe antes de seguir. Nadie inventa comandos, rutas ni servidores. **Excepción:** «Stack y
trampas», «Pruebas» y «Regresión diaria» las escribe la ficha del esqueleto (no hay comandos antes de que exista el
código); hasta entonces el marcador es lo esperado y no se pregunta.

## Proyecto

- **Qué es:** Sistema de seguimiento a ensayes de lotes de concentrado, desde el muestreo en mina hasta el ensaye aplicable acordado con la contraparte.
- **Idioma:** español, con ortografía completa. Identificadores y términos técnicos, en su forma original.
- **Iniciado:** 2026-10-21
- **Repositorio:** `D:\Asgi\FPlus-nemit`, repositorio propio creado el 2026-10-26 desde cero (sin la historia de
  `Blitzy-Fresnillo/FPlus`) con el estado de `D:\Asgi\FPlus` (rama `blitzy-branch`, commit `f721805`) más lo que estaba
  sin commitear. Remoto: un repositorio privado en GitHub de la cuenta del usuario. `D:\Asgi\FPlus` queda como copia de
  Blitzy y los agentes ya no trabajan ahí.
- **Rama de trabajo:** `main`.
- **Push:** lo hace una persona, nunca una sesión.
- **Árbol compartido:** no declarado; por omisión, commits siempre con pathspec.
- **Humor:** leve <!-- leve | apagado -->
  - *leve*: de vez en cuando, un chiste autocrítico de una línea en el chat, nunca en un archivo (ver el plugin,
    `comun/humor.md`). Sin esta línea, es `leve`.
  - *apagado*: ninguno; para demostraciones con el cliente o sesiones grabadas.

## Equipo e integración — `nemit:coordinador`, `nemit:desarrollador`, `nemit:qa`

- **Modo:** individual <!-- individual | equipo -->
  - *individual*: una o dos personas sobre la misma rama, commits con pathspec, sin ramas por ficha.
  - *equipo*: varias personas en paralelo, cada una en su ambiente y en una rama por ficha (`ficha/NNN-titulo`).
- **Lanzamiento:** preguntar <!-- preguntar | agente | manual -->
  - *manual*: el coordinador entrega el prompt de implementación y se pega en una sesión nueva.
  - *agente*: el coordinador lanza la implementación como agente en segundo plano.
  - *preguntar*: el coordinador entrega el prompt y ofrece lanzarlo como agente; se elige en cada ficha.
- **Después de validar (ciclo completo con agentes):** pausa <!-- pausa | lanzar -->
  - *pausa*: el coordinador enseña las decisiones técnicas de la ficha `[V]` y espera la indicación para lanzarla.
  - *lanzar*: las enseña y la lanza a desarrollo sin esperar.
- **Integración (solo en modo equipo):** <directo | pr>
  - *directo*: el desarrollador sube su rama; el coordinador revisa el diff contra la rama de integración y la integra
    (o da el visto bueno para que la integre quien diga «Quién integra»).
  - *pr*: el desarrollador abre un pull request; la revisión de cierre se hace sobre el PR.
- **Rama de integración:** <main | develop | …> — la regresión diaria la mide; en rojo, se detienen las integraciones
- **Rama de liberación:** <la misma de integración | release/… (entonces la regresión corre también ahí antes de liberar)>
- **Quién integra:** <el coordinador | el desarrollador con visto bueno | una persona nombrada>
- **Forma del merge:** <merge sin fast-forward | squash>
- **Personas:** <nombre o usuario de GitHub de cada quien, para asignar fichas>
- **Integración continua:** <no | sí, y qué corre en cada rama o PR>
- **Migraciones:** <cómo se numeran; el coordinador reserva el número al lanzar cada ficha>

## Documentos

| Qué | Dónde |
|---|---|
| Fuentes del cliente (no se editan) | `docs/fuentes/` |
| Negocio: visión y proceso | `docs/negocio/` |
| Glosario | `docs/glosario.md` |
| Requisitos y preguntas abiertas | `docs/requisitos/` |
| Backlog: índice de historias | `docs/backlog.md` |
| Historias, una por archivo | `docs/historias/` |
| Prototipo (web y móvil) | `prototipo/` |
| Arquitectura, modelo de datos, ADR | `docs-equipo/Arquitectura/` (ADR en `adr/`, formato `ADR-NNN-titulo.md`) |
| Matriz OWASP ASVS | `docs-equipo/Arquitectura/10-controles-owasp-asvs.md` |
| Modelo de amenazas | `docs/seguridad/amenazas.md` |
| Requerimientos preliminares v0.1 (nuestros, sin baseline: deducción, no fuente del cliente) | `docs-equipo/*.docx` |
| Reglas vinculantes del código | `README.md` § «Reglas vinculantes para el código» |
| Plan (índice maestro, orden, pendientes) | `docs/planning/plan.md` |
| Carpeta de fichas y sus reglas | `docs/planning/fichas/` (reglas en su `README.md`) |
| Reglas vivas del código | `CLAUDE.md` de la raíz y de cada módulo |
| Operación y entornos | `docs/operacion/` |
| Informes de avance | `docs/informes/` |

## Reglas propias del usuario

- **2026-10-21** — La arquitectura se queda en `docs-equipo/Arquitectura/`; no se crea `docs/arquitectura/`.
- **2026-10-26** — Repositorio propio (ver «Proyecto»). Aquí sí se versionan `CLAUDE.md`, `AGENTS.md` y `.claude/`
  (salvo `.claude/settings.local.json`); `target/` queda excluido. `docs/private/` y los audios de `docs/fuentes/`
  siguen sin exclusión: no se hace `git add` de esas rutas. Sustituye la regla del 2026-10-21 sobre `.gitignore`.
- **2026-10-21** — Sin regla cero de nombres: el sistema es para un solo cliente. Las personas se citan por su rol.
- **2026-10-21** — El prototipo se construye con Java 25 y PrimeFaces 16 (el stack del sistema), no con las versiones
  del kit.

## Fuentes — `nemit:analista`

- **Cómo llegan:** sesiones funcionales grabadas en audio `.m4a` (a veces dos grabaciones de la misma sesión, por
  persona) y en Teams (transcripción `.docx`/`.pdf`); transcripciones automáticas sin validar en `.txt`. Algunas
  sesiones solo tienen audio y hay que transcribirlas. Llegan con los archivos que el área funcional comparte
  (proformas, informes de tercería, extractos, reglas de negocio en Excel).
- **Material recibido fuera del repositorio:** `D:\f-plus\01_Definición_funcional_Embarques\01_Fuentes_y_evidencia\`
  (sesiones 1 a 4: 3, 4, 17 y 18 de septiembre de 2026). Lo acomoda `nemit:analista` en `docs/fuentes/`.
- **No son fuente del cliente:** research, propuestas de UI, diagramas y propuestas internas marcadas «no baseline · no
  decisión funcional» de esa misma carpeta, ni los `.docx` de `docs-equipo/`.
- **Normas:** <ninguna declarada todavía>

## Diseño — `nemit:disenador`

- **Tecnología:** PrimeFaces 16 sobre Jakarta EE 11 y Java 25, igual que el sistema (CLAUDE.md § Stack); prototipo en
  `prototipo/web/`. El kit salió del iniciador con PrimeFaces 14, Jakarta EE 10 y Java 21: se sube a estas versiones
  antes de la primera pantalla y no se baja. El servidor donde se enseñe debe soportar Jakarta EE 11 (el proyecto usa
  Payara 7).
- **Tema:** `nemit-moderno` como base. El tema definitivo es propio y vive en `fplus-ui` (ADR-007), con tres modos de
  visualización (ADR-008) y un color de acento por módulo; reglas en `docs-equipo/Arquitectura/09-interfaz-y-tema.md`.
- **Marca del cliente:** <por definir>
- **Dispositivos y navegadores:** sin definir (NFR-022).
- **Accesibilidad:** NFR-023, ver documento 09.
- **Dónde se enseña el prototipo:** <servidor de demostración, ver Entornos>

## Seguridad — `nemit:appsec`

- **Nivel OWASP ASVS objetivo:** 2 (ASVS 5.0, niveles 1 y 2; vinculante según el `README.md` y el documento 10)
- **Lo que el cliente exige además:** <política de seguridad, auditoría, certificación, revisión por su área de TI; o «nada declarado»>
- **Contra qué entorno se corren los escaneos antes de liberar:** <el de pruebas; nunca producción sin confirmación>

## Stack y trampas de despliegue — `nemit:arquitecto` (stack), la ficha del esqueleto (comandos), `nemit:devops`

Java 25, Jakarta EE 11 sobre Payara 7.2026.8, PrimeFaces 16, Shiro 2.x por código, SQL Server 2022 con Flyway.
Dos WAR (`fplus-captura`, `fplus-reportes`) y dos JAR (`fplus-domain`, `fplus-ui`); detalle en `CLAUDE.md`.

<Cómo se compila y se despliega,
y lo que sale mal si se hace de otra forma (lo fija la ficha del esqueleto y lo mantiene devops). Van en todo prompt que
despliegue.>

## Entornos — `nemit:devops`

<Tabla en `docs/operacion/entornos.md`: por entorno, host, usuario, puertos, rutas y servicios. Nunca contraseñas: llaves
SSH, `~/.ssh/config` o variables de entorno. Qué acciones piden confirmación (por omisión: todo en producción).
**Con qué rol mide la base `nemit:dba`** (uno de solo lectura, si se puede) y si puede crear una base de prueba (con qué
prefijo). **¿Algún servidor o base lo comparte otro proyecto?** Si sí, qué no se detiene ni se reinicia desde una sesión, qué se
puede crear en él (una base, un pool, una aplicación) y quién tiene las credenciales para hacerlo.>

## Pruebas — `nemit:desarrollador`, `nemit:validador`

<Niveles de prueba, comandos de las unitarias y de extremo a extremo, dónde están las líneas base y los intermitentes
conocidos. Con qué cierra una ficha: sus casos, las suites de su zona y las unitarias de lo que toque.>

## Regresión diaria — `nemit:qa`

**Carpetas que indican trabajo en curso:** <rutas>

**Dónde quedó la última y dónde se escribe esta:** <ruta y sección>

**Poner HEAD en el entorno:** <pasos numerados, cada uno con su comprobación>

**Corridas:** <comandos en orden, cuáles no pueden ir en paralelo y por qué, cómo leer el resumen>

**Rojos conocidos con significado:** <si los hay>

## Informe de avance — `nemit:pmo`

<Dónde vive el informe y en qué formato, qué secciones tiene, de dónde sale cada cifra y cómo se cuentan las sesiones.>
