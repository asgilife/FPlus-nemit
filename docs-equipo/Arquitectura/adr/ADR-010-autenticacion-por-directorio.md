# ADR-010 · Autenticación por directorio: un realm, N directorios por configuración

**Estado:** Aceptada · 2026-09-11 · rige como etapa provisional de `fplus-reportes` mientras no haya SSO (decisión del
usuario del 2026-10-21, anotada en [ADR-012](ADR-012-sso-corporativo.md)); su lista de directorios en un archivo de
propiedades espera su ADR de desviación de AR-G-23 y AR-G-03
**Relación:** extiende [ADR-009](ADR-009-apache-shiro-programatico.md).

## Contexto

Fresnillo autentica contra LDAP habitualmente. Hay **tres directorios en juego**:

| Directorio | Naturaleza |
| --- | --- |
| Fresnillo · sitio 1 | Corporativo |
| Fresnillo · sitio 2 | Corporativo, otro sitio. Misma organización y población |
| Met-Mex | **Otra organización: la contraparte del intercambio** |

La práctica previa era **un realm por LDAP**. El análisis del equipo concluyó que es
innecesario: basta leer la lista de un archivo de propiedades, mantenerla en memoria y
conectarse. Correcto, y ahorra código.

La propuesta original añadía que **da igual cuál valide**: si alguno valida, se da acceso.
Eso es adecuado entre los dos sitios de Fresnillo, pero no a través de la frontera con
Met-Mex.

## Decisión

**Un solo realm**, con los directorios declarados en un archivo de propiedades y cargados en
memoria. Un **directorio lógico** agrupa varios servidores, que son conmutación por error
entre sí.

Con una precisión sobre la propuesta original: **el directorio se resuelve antes de
conectar, no probando uno por uno.** La cuenta local en `seg_rep.usuario` declara a qué
directorio pertenece el usuario, y el bind se hace solo contra ese.

Además:

- La identidad local se identifica por el par **(directorio, usuario)**, no por el usuario
  suelto.
- **`fplus-captura` no usa LDAP.** ERSA es laboratorio externo, no está en el directorio, y
  conserva credenciales locales con Argon2id.
- El directorio **autentica y nada más**. Roles y permisos siguen resolviéndose contra
  `seg_rep`. Sin cuenta local provisionada, validar no otorga acceso.

## Consecuencias

**A favor**

- Se conserva el ahorro buscado: un realm en lugar de tres, y agregar un cuarto directorio es
  configuración, no código.
- **Un bind por intento en lugar de hasta tres.** Resolver antes de conectar es más barato
  que probar.
- La contraseña de un usuario **nunca viaja a un directorio que no es el suyo**. Con
  recorrido ciego, las credenciales de empleados de Fresnillo llegarían al directorio de la
  contraparte comercial en cada intento.
- Sin colisión de identidad: un `jperez` de Fresnillo y un `jperez` de Met-Mex son cuentas
  distintas y no pueden aterrizar una en la otra.
- Sin amplificación del bloqueo: un intento fallido no dispara el contador en tres
  directorios a la vez.
- Adoptar un cuarto directorio, o migrar uno, no toca el código.

**En contra**

- Una cuenta local debe existir **antes** del primer acceso. No hay alta automática al
  autenticar por directorio. Es deliberado: es la compuerta que sostiene todo el esquema,
  pero implica un paso de provisión que alguien debe operar.
- Un campo más que mantener por cuenta (`directorio`), y la unicidad pasa a ser por par.
- Si un usuario cambia de directorio, hay que actualizarlo en su cuenta local.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Un realm por LDAP** | La práctica previa. Fija en código lo que es configuración: un cuarto directorio obligaría a recompilar |
| **Un realm, recorriendo todos hasta que alguno valide** | La propuesta original. Envía la contraseña a directorios ajenos al usuario, incluido el de la contraparte comercial; permite colisión de identidad entre directorios; y amplifica el bloqueo de cuenta. Además es más lento |
| **Confiar en los grupos del directorio para los permisos** | Prohibido por la línea base ([10](../10-controles-owasp-asvs.md), patrón 6). El directorio autentica; nunca otorga autoridad |
| **LDAP también para ERSA** | No están en el directorio de Fresnillo. Forzarlo implicaría darles cuenta corporativa, que es una decisión mayor y no pedida |

## Situación del tercer directorio

El directorio de Met-Mex se pidió **hace años, por una reestructura del grupo**. Información
del equipo, **por confirmar**: probablemente **ya no sea necesario**.

Si se confirma que se retira, los tres directorios pasan a ser todos de Fresnillo y
desaparece la frontera organizacional que motivó la precisión de esta decisión.

**La decisión se mantiene igual aunque eso ocurra**, por dos razones que no dependen de
Met-Mex:

- Resolver antes de conectar cuesta **un bind por intento** en lugar de hasta tres. Es más
  barato, no más caro.
- Evita la amplificación del bloqueo de cuenta: un intento fallido no dispara el contador en
  varios directorios a la vez.

Lo que sí desaparece es el riesgo de entregar credenciales a la contraparte y el de colisión
de identidad entre organizaciones. Dicho de otro modo: **la justificación más fuerte puede
evaporarse, y el diseño sigue siendo el correcto por economía.**

Mientras no se confirme, el directorio queda **configurado pero sin cuentas locales
asociadas**, lo que en este diseño equivale a sin acceso.

> **Por confirmar con TI:** si el directorio de Met-Mex se retira del alcance. Si se retira,
> se elimina de las propiedades y este apartado se cierra. Si se mantiene, sigue vigente lo
> anterior: el acceso técnico a Met-Mex no está aprobado (TR M01/E03) y NFR-008 exige
> mantener separada la información comercial.
