# ADR-004 · Identidad, roles y permisos segregados por módulo

**Estado:** Aceptada · 2026-09-11

## Contexto

ERSA es un **laboratorio representante**, no personal de Fresnillo. Su ciclo de alta, baja y
revocación de accesos es distinto, lo administra alguien distinto, y sus usuarios pueden
entrar desde fuera de la red corporativa.

NFR-005 exige restringir acciones según facultades manteniendo identidad individual.
NFR-008 exige separar la información comercial entre la captura de ERSA y el intercambio
externo.

La matriz concreta de roles depende de **P02**, que sigue abierto.

## Decisión

Cada módulo tiene su **propio padrón de identidad**: usuarios, contraseñas, roles, permisos
y asignaciones, en su propio esquema (`seg_cap` y `seg_rep`), con estructura idéntica y
contenido completamente disjunto.

Además:

- **El código verifica permisos, no roles.** El realm de Shiro devuelve
  códigos de permiso además de roles, y el código comprueba permisos. La matriz de roles
  vive como dato. Mecanismo en [ADR-009](ADR-009-apache-shiro-programatico.md).
- **No hay SSO entre los dos módulos.** Sesiones independientes.
- Las contraseñas se almacenan solo como hash **Argon2id** con sal por usuario, y se
  verifican exactamente como se reciben. **bcrypt queda prohibido**: trunca a 72 bytes.

## Consecuencias

**A favor**

- Comprometer `fplus-captura` **no** expone las credenciales de los usuarios internos: su
  principal tiene `DENY` sobre `seg_rep`.
- El ciclo de vida de los accesos de ERSA se administra por separado, como corresponde a un
  tercero.
- Cuando P02 cierre, la matriz de autoridades se configura **en datos**: sin recompilar, sin
  redesplegar. Y cuando cambie después —que va a cambiar— tampoco.
- Adoptar más adelante un proveedor de identidad corporativo afecta solo al **realm de Shiro**
  de cada módulo; la estructura de roles y permisos sobrevive, y la autoridad sigue sin
  derivarse de los grupos del proveedor.

**En contra**

- Una persona que legítimamente necesite ambos módulos tendrá **dos cuentas y dos
  contraseñas**. Si esto resulta frecuente, es señal de que la frontera entre módulos está
  mal trazada y hay que revisar P02.
- Dos pantallas de administración de usuarios. Mitigado: misma estructura, el código se
  escribe una vez y se parametriza por esquema.
- **La auditoría no puede tener llave foránea al usuario.** Se resuelve desnormalizando la
  identidad en el evento (`sistema`, `usuario`, `usuario_id`), lo cual además la hace
  legible tras una baja o un renombre.
- Dos padrones cuyas políticas hay que mantener alineadas. No hay sincronización de datos,
  solo de reglas documentadas.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Un padrón único con columna `sistema`** | Más cómodo y más frágil: una inyección SQL en el módulo expuesto alcanza los hashes de todos los usuarios internos. La protección dependería de que ninguna consulta olvide filtrar |
| **Identidad en un proveedor externo (LDAP, SSO)** | No hay SSO aprobado ni CyberArk. Se mantiene como evolución futura, compatible con esta estructura |
| **Verificar roles en el código** | Cada cambio de la matriz de autoridades sería un cambio de código. Con P02 abierto, sería un error de diseño |
| **Derivar permisos de los grupos de un SSO** | Prohibido por la línea base ASVS ([10](../10-controles-owasp-asvs.md)). El proveedor autentica; nunca otorga autoridad |
