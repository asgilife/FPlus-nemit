# FICHA-NNN — <Qué debe ser cierto al terminar, en una línea>

**Estado:** `[F] REDACTADA — AAAA-MM-DD`. **Preguntas abiertas:** <ninguna | lista>
**Historia:** [HU-NNN](../../historias/HU-NNN.md) · **Prototipo:** `prototipo/<web|movil>/…` (la vista aprobada que esta ficha gradúa)
**Prioridad:** <P1 | P2 | P3, y por qué>
**Tamaño:** <S | M | L>, <módulos que toca>
**Orden:** <qué tiene que cerrar antes y qué desbloquea>

## Zona

Lo que esta ficha toca. Decide qué fichas pueden ir en paralelo con ella (dos zonas que no se cruzan) y cuáles no.

| Qué | Cuál |
|---|---|
| Módulos y archivos | <rutas o carpetas> |
| Migraciones | <ninguna | una (el número lo reserva el coordinador al lanzar)> |
| Contratos entre componentes | <DTO, endpoint, evento, formato de archivo que cambia, o ninguno> |
| Estado global | <parámetros, catálogos, configuración compartida que cambia, o ninguno> |
| Recursos compartidos | <servidor, base, dispositivo que sus pruebas ocupan en exclusiva, o ninguno> |

## De dónde sale

| Requisito | Origen |
|---|---|
| <qué se pide> | <Norma / Cliente (fuente) / Deducción nuestra / Usuario, fecha> |

## Qué hay hoy

<Lo verificado contra el código y la base, con archivos y líneas, y cómo se midió. Para que la sesión de implementación
no lo vuelva a deducir.>

## Decisiones

**D1 — <título>.** <Qué se decide.> *Por qué:* <razón>. *Descartado:* <alternativa y por qué no>.

## Seguridad y base de datos

<«No aplica» si la ficha no toca ninguna de las dos. Si toca:>

- **Controles ASVS que aplican:** <identificadores de la matriz `docs-equipo/Arquitectura/10-controles-owasp-asvs.md` y qué significan aquí, o
  «ninguno»>. **Pruebas negativas:** <casos #N…: sin permiso, con el identificador de otro, con entrada maliciosa; cada
  una afirma el rechazo y que no se escribió nada>.
- **Base de datos:** <tablas y columnas nuevas (llaves `id` + `uuid`, auditoría), índices con la consulta que los
  justifica, cómo se lee cada lista (proyección, `JOIN FETCH`, paginación) y el caso que cuenta las consultas>.
- **Datos y configuración:** <cada campo nuevo con su clasificación (AR-G-24) y cada valor configurable con su lugar
  (AR-G-23), citando `modelo-datos.md` o diciendo que la ficha lo agrega; o «ninguno»>.
- **Consultados:** <`nemit:dba` / `nemit:appsec`, si la ficha lo necesitó, con fecha>.

## Cambios

| # | Dónde | Qué |
|---|---|---|
| 1 | `<archivo>` | <cambio concreto> |

## Casos

| # | Caso | ¿Falla hoy? | Cómo se prueba |
|---|---|---|---|
| 1 | <comportamiento esperado> | Sí / No (y por qué) | <unitaria, extremo a extremo, manual> |

## Criterios de aceptación

- [ ] <verificable por alguien que no estuvo en la sesión>

## Prompt de implementación

<Lo arma nemit:coordinador al lanzar, verificado contra el código de ese día.>

## Resultado

<Lo escribe la sesión de implementación al cerrar: commits, casos con sus números, lo corregido en el camino y lo que
queda.>
