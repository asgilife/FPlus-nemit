# AGENTS.md — FPlus

Gobernanza y routing para asistentes de IA (Claude Code, Codex, Cursor u otros). Las reglas del código están en los
`CLAUDE.md`; este archivo solo dice **cuál leer y en qué orden**.

## 1. Routing por módulo

Antes de modificar código, identifica el módulo afectado y lee su `CLAUDE.md`, además del de la raíz.

| Si trabajas en… | Lee |
|---|---|
| `/<modulo>/**` | `/CLAUDE.md` + `/<modulo>/CLAUDE.md` |
| `/docs/**` | `/docs/README.md` (qué va en cada carpeta) |
| `/prototipo/**` | el `README.md` de su carpeta (`web/`, `movil/`) |

<Una fila por módulo de código, cuando existan. Todo módulo nuevo nace con su `CLAUDE.md`.>

Si una tarea toca varios módulos: uno por uno, leyendo el `CLAUDE.md` de cada uno, sin mezclar sus convenciones.

## 2. Precedencia

1. El `CLAUDE.md` del módulo.
2. El `CLAUDE.md` de la raíz.
3. Este archivo, que solo define routing.

## 3. Documentos que mandan

- `docs/requisitos/`, `docs/backlog.md` y `docs/historias/`: qué se pidió y de dónde salió.
- `docs-equipo/Arquitectura/` (y sus ADR en `adr/`): cómo está construido; un cambio que lo contradiga necesita un ADR.
- `docs/planning/fichas/`: la tarea en curso. **Sin ficha validada no se implementa.**

No inventes requisitos, endpoints ni tablas que no estén en esos documentos: si faltan, dilo.

## 4. Al terminar una tarea, reporta

- Archivos modificados.
- Qué cambió y por qué, en dos líneas.
- Impacto en datos, en contratos entre componentes y en despliegue, si lo hay.
- Cómo se verificó: pruebas corridas y su resultado.
