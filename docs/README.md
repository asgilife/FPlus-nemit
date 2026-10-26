# Documentación — qué va dónde

Convención de FPlus. Si un documento nuevo no cabe en ninguna carpeta, **se reabre esta convención**; no se crea
una carpeta `varios/`.

## El recorrido

```
fuentes ──► requisitos ──► backlog ──► prototipo ──► ficha ──► código
(cliente)   (con origen)   (historias)  (prototipo/)  (spec)    (módulos)
                                           │            ▲
                                           └── la pantalla aprobada se gradúa al sistema
                              arquitectura ──────────────┘ (la ficha la respeta o propone un ADR)
```

Cada eslabón cita al anterior: un requisito cita su fuente, una historia sus requisitos, una pantalla del prototipo su
historia y una ficha su historia. Así se puede contestar «¿por qué existe esta pantalla?» sin preguntarle a nadie.

## Carpetas

| Carpeta | Qué va | Quién la escribe | Regla |
|---|---|---|---|
| `fuentes/` | Lo que entregó el cliente: juntas (audio + transcripción), documentos, fotos, formatos en papel | Quien recibe el material | **No se edita.** Se agrega, nunca se corrige |
| `negocio/` | Visión, proceso de negocio, actores | `nemit:analista` | Describe el negocio, no el sistema |
| `glosario.md` | Términos del dominio | Todos | Un término, una definición |
| `requisitos/` | Requisitos con cita y origen; preguntas abiertas | `nemit:analista` | Todo requisito dice si es norma, cliente o deducción nuestra |
| `backlog.md` | Índice de las historias: prioridad, estado, prototipo y ficha de cada una; restricciones que aplican a todo | `nemit:product-owner` (el diseñador y el líder técnico actualizan la fila) | Prioridad, estado, prototipo y ficha viven solo aquí |
| `historias/` | Las historias de usuario, una por archivo (`HU-NNN.md`) | `nemit:product-owner` | Cada historia cita sus requisitos; ningún otro documento la copia, todos la citan por su número |
| `../docs-equipo/Arquitectura/` | Arquitectura, modelo de datos, ADR (vive fuera de `docs/`, ver su `README.md`) | `nemit:arquitecto` | Una decisión que cambia la arquitectura deja un ADR |
| `seguridad/` | Modelo de amenazas (la matriz ASVS es `../docs-equipo/Arquitectura/10-controles-owasp-asvs.md`), informes de seguridad de cada liberación | `nemit:appsec` | Cada control dice dónde se cumple y cómo se verificó; nunca un identificador de memoria |
| `planning/` | El plan y las fichas | `nemit:lider-tecnico`, `nemit:coordinador` | Sin ficha validada no se implementa |
| `operacion/` | Instalación, despliegue, entornos | `nemit:devops` | Sin secretos |
| `informes/` | Informes de avance | `nemit:pmo` | Lo cierto pesa más que lo actual |
| `private/` | Notas internas, borradores comerciales | Quien quiera | Fuera de git |

Fuera de `docs/`: `prototipo/` (las pantallas en la tecnología final, con datos simulados: se despliegan y se navegan)
y las carpetas de código de cada módulo, cada una con su `CLAUDE.md`.

## Preguntas frecuentes

| ¿Dónde pongo…? | Respuesta |
|---|---|
| La grabación de una junta | `fuentes/juntas/AAAA-MM-DD-tema/` (el audio queda fuera de git; la transcripción no) |
| Un PDF o una foto que mandó el cliente | `fuentes/documentos/` |
| Una pregunta que hay que hacerle al cliente | `requisitos/preguntas-abiertas.md` |
| Una historia de usuario | `historias/HU-NNN.md`, y su fila en `backlog.md` |
| Una decisión de arquitectura | `../docs-equipo/Arquitectura/adr/ADR-NNN-titulo.md` |
| La pantalla de una historia | `../prototipo/web/` o `../prototipo/movil/` (una vista por historia, ver su `README.md`) |
| Cómo se instala en la máquina de un desarrollador | `operacion/setup.md` |
| Los servidores de pruebas y producción | `operacion/entornos.md` (sin contraseñas) |
