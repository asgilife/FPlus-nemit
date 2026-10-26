# 08 · Convenciones de desarrollo

> Estado: PRELIMINAR · línea base de trabajo v0.1

## Paquetes

```
mx.fresnillo.fplus
├── dominio            entidades JPA, objetos de valor, reglas invariantes
│   ├── lote
│   ├── ensaye
│   ├── intercambio
│   └── contrato
├── repositorio        acceso a datos, JPA
├── servicio           servicios de aplicación, transaccionales
├── captura            solo en fplus-captura
│   ├── vista          backing beans CDI
│   └── seguridad      identidad y configuración de seguridad del módulo
└── reportes           solo en fplus-reportes
    ├── vista
    ├── ingesta        parsers de archivos externos
    └── seguridad
```

`dominio`, `repositorio` y `servicio` viven en `fplus-domain`. `captura` y `reportes` viven
cada uno en su WAR y **no se referencian entre sí**.

## Capas y transacciones

| Capa | Anotación | Responsabilidad |
| --- | --- | --- |
| Backing bean | `@Named @ViewScoped` | Orquestar y presentar. **Sin reglas de negocio** |
| Servicio de aplicación | `@ApplicationScoped @Transactional` | Caso de uso completo, límite transaccional |
| Repositorio | `@ApplicationScoped` | Consultas y persistencia. Sin reglas |
| Entidad | `@Entity` | Estado e invariantes del dominio |

Reglas:

- **El límite transaccional es el servicio de aplicación**, nunca el backing bean ni el
  repositorio. Un caso de uso, una transacción.
- La vista no inyecta repositorios ni el `EntityManager`.
- Sin `@Transactional` en métodos de solo lectura que no lo necesiten.
- Nada de lógica de negocio en `.xhtml`.

## Nombres

| Elemento | Convención | Ejemplo |
| --- | --- | --- |
| Tablas y columnas | `snake_case`, español, singular | `resultado_elemento`, `valor_aplicable` |
| Esquemas | `snake_case` corto | `ops`, `seg_cap` |
| Entidades | `PascalCase`, español | `ResultadoElemento` |
| Servicios | `PascalCase` + `Servicio` | `ConciliacionServicio` |
| Repositorios | `PascalCase` + `Repositorio` | `LoteRepositorio` |
| Backing beans | `PascalCase` + `Bean` | `CapturaEnsayeBean` |
| Permisos | `MAYUSCULAS_CON_GUION_BAJO` | `ENSAYE_AUTORIZAR` |
| Migraciones Flyway | `V<n>__descripcion.sql` | `V007__agrega_ciclo.sql` |

El dominio se nombra **en español**, igual que lo nombra el negocio. Un `ResultadoElemento`
es más claro para quien mantiene esto que un `ElementResult`. El código de infraestructura
sigue las convenciones de Java.

## Base de datos

- **`DECIMAL` con precisión y escala explícitas** para todo valor de ensaye. Nunca `FLOAT`
  ni `REAL`: el redondeo binario corrompe comparaciones contra Met-Mex.
- **`DATETIME2`**, no `DATETIME`. Marcas de tiempo en UTC; la conversión a hora local es
  responsabilidad de la vista.
- **Sin `SELECT *`** y sin concatenación de SQL. Consultas parametrizadas siempre.
- **Sin borrado físico** en `ops` ni en `aud`. Bajas lógicas.
- Todo valor de ensaye lleva **estado explícito** (`CERO_REAL`, `PENDIENTE`, `AUSENTE`,
  `NO_APLICA`) además del número. Ninguna consulta ni exportación puede colapsarlos.
- Toda entidad modificable lleva `@Version`.

## Migraciones

- Un script por cambio, numerado, **inmutable una vez aplicado en cualquier entorno**.
- Los `GRANT` y `DENY` de los principales de aplicación son parte de la migración, no
  configuración manual del DBA.
- Cambio de esquema implica redesplegar **ambos** módulos.
- La aplicación **no** ejecuta migraciones al arrancar, en ningún entorno ([ADR-018](adr/ADR-018-flyway-sin-migrar-al-arrancar.md)).

## Pruebas

| Nivel | Alcance | Herramienta |
| --- | --- | --- |
| Unitaria | Reglas de dominio, sin base de datos | JUnit 5 |
| Integración | Repositorios y servicios contra SQL Server real | Testcontainers |
| Seguridad | Que `fplus-captura` **no** pueda leer `seg_rep` | Prueba de integración dedicada |
| Seguridad | Que toda autenticación **emita un identificador de sesión nuevo** | Prueba de integración obligatoria |
| Seguridad | Que toda respuesta lleve el juego de cabeceras de [10](10-controles-owasp-asvs.md) | Prueba de integración obligatoria |

La prueba de seguridad no es opcional. Es la verificación de que la segregación de
[05](05-seguridad-y-control-de-acceso.md) realmente está aplicada y no se perdió en una
migración posterior. Debe fallar la construcción si el `DENY` desaparece.


## Interfaz

- **Ningún estilo se escribe en una pantalla.** Todo lo visual vive en `fplus-ui`.
- Colores, espaciados y tipografía siempre por variable CSS, nunca literales dispersos.
- **Los tokens son semánticos, nunca literales**: `--superficie-base`, no `--gris-claro`. Hay tres modos visuales y el nombre debe seguir siendo cierto en los tres.
- No eliminar el indicador de foco sin sustituirlo por uno propio con contraste suficiente.
- El color nunca es el único portador de información.
- Un valor tiene **cuatro estados** que deben verse distintos: cero real, pendiente,
  ausente y no aplica ([09](09-interfaz-y-tema.md)). Nunca colapsarlos en una celda vacía.


## Seguridad

La línea base vinculante es **[10 · Controles OWASP ASVS](10-controles-owasp-asvs.md)**.
Resumen operativo de lo que aplica al escribir código:

- **Nunca concatenar** un valor externo en SQL, en un nombre JNDI, en un filtro LDAP, en una
  cabecera de correo ni en una línea de registro.
- **Nunca** `escape="false"` sobre una salida que pueda contener un valor de origen externo.
- Los backing beans exponen **objetos de formulario**, nunca una entidad `@Entity`.
- La comprobación de permiso va en el **servicio de aplicación**, no solo en la vista.
- **Ningún secreto** en el código, en el artefacto ni con valor por omisión: si falta, la
  aplicación **falla al arrancar**.
- **Ninguna clase, endpoint o configuración de prueba** dentro del artefacto de producción.
- Prohibido desactivar la validación de certificados, **también en desarrollo**.
- Todo valor externo se **sanea antes de registrarlo**. Nunca se registran contraseñas,
  hashes, tokens ni identificadores de sesión.
- La dirección del cliente sale de la conexión, salvo que un proxy de confianza sobrescriba
  la cabecera de reenvío.

## Registro de eventos

- **Logging técnico** (`java.util.logging` / SLF4J) y **auditoría funcional** (`aud.evento`)
  son cosas distintas. El log no sustituye la auditoría.
- **Nunca** registrar contraseñas, hashes, ni valores de ensaye completos en el log técnico.
- Todo error atrapado se registra con contexto suficiente para diagnosticarlo.

## Control de versiones

- Rama `main` protegida.
- Una rama por cambio; integración por Pull Request.
- El mensaje de commit explica **por qué**, no qué: el diff ya dice qué.
- Las decisiones de arquitectura se registran como [ADR](adr/), no como comentario en el
  código.

## Lo que no se hace

- No introducir REST hasta que exista un consumidor real y concreto.
- No agregar dependencias sin justificar el costo de mantenerlas.
- No resolver un TBD por criterio del equipo de desarrollo: los TBD de estos documentos
  requieren decisión de negocio o de TI.
- No usar números de los ejemplos de los documentos como configuración real.

## Exportaciones (RNF-010)

Toda exportación a Excel o CSV sale lista para analizarse:

- **Un solo renglón de encabezados**, sin encabezados repetidos por página ni celdas combinadas.
- **Cada columna numérica dice su unidad** en el encabezado (g/t, %, t secas o húmedas); el valor va como número, no
  como texto con formato.
- **Los cuatro estados del valor se exportan explícitos** ([04](04-modelo-de-datos.md)): un pendiente o un no aplica
  nunca sale como cero ni como celda vacía indistinguible.
- Las cédulas y los formatos que pide un tercero (la cédula para Met-Mex, el formato de ERSA) siguen su plantilla, pero
  sus datos salen de las mismas consultas.
