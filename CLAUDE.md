# CLAUDE.md — FPlus

Sistema de seguimiento a ensayes de lotes de concentrado, desde el muestreo en mina hasta el ensaye aplicable acordado
con la contraparte.

Este archivo lleva las **reglas que aplican a todo el repositorio**. Cada módulo de código tiene su propio `CLAUDE.md`
con sus patrones; si se contradicen, gana el del módulo (ver `AGENTS.md`). Los datos de proceso del proyecto (rutas,
entornos, cómo se prueba) están en `.claude/perfil.md`.

---

## Qué es el sistema

Concentra en un solo sistema, con historia, autoría y auditabilidad, el seguimiento que hoy vive en Excel, correos y
PDF: el ensaye de cada lote por elemento, la comparación entre el ensaye de Fresnillo PLC (vía ERSA) y el de Met-Mex,
la negociación, la tercería y la resolución del valor aplicable. Detalle en `README.md` y en
`docs-equipo/Arquitectura/`.

## Regla crítica — quién usa qué

Dos desplegables independientes contra una misma base: **`fplus-captura`** (analistas de ERSA; primera etapa por VPN,
construido para internet —ADR-011—; identidad local en `seg_cap`) y **`fplus-reportes`** (Fresnillo PLC, red interna,
identidad en `seg_rep`). Sin API entre ellos: se integran por la base de datos. Ningún módulo toca la identidad del otro.

- **`fplus-captura` no abre ninguna conexión fuera de su base**: ni PROVEM, ni correo, ni otro sistema (ADR-011).
- **Solo `fplus-reportes` habla con el exterior**: lee PROVEM por su endpoint, con una sola ejecución a la vez
  (ADR-013), y envía los correos que cualquier módulo deja en la bandeja `ops.correo_saliente` dentro de su
  transacción (ADR-014).
- **El lote lo escribe solo la ingesta de PROVEM**; ERSA no captura datos del lote (RF-038).
- **Las evidencias viven en la base y nunca se borran** (ADR-015).
- Los ADR están en `docs-equipo/Arquitectura/adr/`; los marcados «Propuesta» todavía no los valida otra sesión.
- **Decisiones de producto del usuario, 2026-10-21** (validación de la arquitectura; no se vuelven a preguntar):
  - Los analistas de ERSA entran con **segundo factor desde el primer día**, con un código de aplicación autenticadora
    (TOTP), también mientras se entre por VPN (ADR-011).
  - Mientras no exista el SSO corporativo, **Fresnillo entra por LDAP corporativo** (ADR-010 como etapa provisional;
    ADR-012 la sustituirá cuando TI responda P-057).

## Reglas vinculantes

Las **«Reglas vinculantes para el código»** del `README.md` (OWASP ASVS 5.0 niveles 1 y 2, identidad, directorio,
salida y entrada, datos, prohibiciones, pruebas obligatorias y puerta de revisión) aplican a todo código, también al
que produzcan los agentes. **No se repiten aquí para que no diverjan**: se leen allí antes de tocar código. El detalle
está en `docs-equipo/Arquitectura/10-controles-owasp-asvs.md` y `11-checklist-de-validacion.md`.

## Stack

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 25 |
| Plataforma | Jakarta EE 11 sobre Payara Server 7.2026.8 |
| Interfaz | JSF + PrimeFaces 16, tema propio en `fplus-ui` (ADR-007, ADR-008) |
| Seguridad | Apache Shiro 2.x configurado por código (ADR-009, ADR-010) |
| Base de datos | SQL Server 2022, base única con esquemas `ops`, `cfg`, `aud`, `seg_cap`, `seg_rep`; migraciones Flyway |
| Build | Maven multi-módulo: `fplus-domain`, `fplus-ui`, `fplus-captura`, `fplus-reportes` |

Decisiones aún no tomadas (eventos y colas, alojamiento, DW/BI, SSO concreto, acceso de terceros): no se infieren.

## Documentos — dónde está cada cosa

| Qué | Dónde |
|---|---|
| Lo que entregó el cliente (no se edita) | `docs/fuentes/` |
| Visión, proceso de negocio | `docs/negocio/` |
| Glosario del dominio | `docs/glosario.md` |
| Requisitos con cita y origen | `docs/requisitos/` |
| Requerimientos preliminares v0.1 (material nuestro, sin baseline) | `docs-equipo/*.docx` |
| Historias de usuario: una por archivo, y su índice (prioridad, estado, prototipo, ficha) | `docs/historias/`, `docs/backlog.md` |
| Arquitectura, modelo de datos, ADR | `docs-equipo/Arquitectura/` |
| Matriz OWASP ASVS | `docs-equipo/Arquitectura/10-controles-owasp-asvs.md` |
| Modelo de amenazas | `docs/seguridad/amenazas.md` |
| Prototipo navegable (en la tecnología final, datos simulados) | `prototipo/` |
| Plan y fichas | `docs/planning/` |
| Instalación, despliegue, entornos | `docs/operacion/` |
| Perfil del proyecto (para los agentes) | `.claude/perfil.md` |

## Cómo trabajamos

- **Nada se programa sin ficha validada.** El ciclo es REDACTAR → VALIDAR (sesión aparte) → IMPLEMENTAR (sesión
  aparte) → CERRAR. Reglas en `docs/planning/fichas/README.md`.
- **Todo requisito dice de dónde salió**: norma, cliente o deducción nuestra. Lo que deducimos no se presenta como
  pedido del cliente.
- **Los pendientes abiertos del `README.md` («Estado y alcance») no se convierten en supuestos.**
- **Lo que se encuentra en el camino se corrige**, con un caso que primero falle y en commit propio; se para y se
  consulta solo si pide decisión de negocio, migración o cambio de contrato entre componentes.
- **Ninguna sesión hace push**: lo hace una persona. Commits con pathspec explícito (`git commit -m "…" -- rutas`),
  nunca `git add` seguido de `git commit` a secas.
- **Ningún secreto en el repositorio**: contraseñas, llaves y tokens viven en el servidor o en variables de entorno.

## Convenciones

- Idioma de documentos y respuestas: español. Identificadores y términos técnicos, en su forma original.
- Paquetes, capas y transacciones: `docs-equipo/Arquitectura/08-convenciones-de-desarrollo.md`.
