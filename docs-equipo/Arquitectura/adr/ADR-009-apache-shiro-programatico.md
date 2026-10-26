# ADR-009 · Apache Shiro configurado por código

**Estado:** Aceptada · 2026-09-11
**Relación:** precisa [ADR-004](ADR-004-identidad-segregada-por-modulo.md), que asumía
Jakarta Security como mecanismo.

## Contexto

[ADR-004](ADR-004-identidad-segregada-por-modulo.md) fijó la identidad segregada por módulo
y la verificación por permisos en lugar de roles. Al redactarlo se asumió Jakarta Security
(`@DatabaseIdentityStoreDefinition`) como mecanismo de implementación.

El equipo tiene experiencia previa con **Apache Shiro** y opta por él. Además se adopta
**OWASP ASVS 5.0 niveles 1 y 2** como línea base obligatoria
([10 · Controles OWASP ASVS](../10-controles-owasp-asvs.md)), lo que impone requisitos
concretos sobre sesión y contraseñas que el mecanismo debe poder satisfacer.

## Decisión

**Apache Shiro**, configurado **íntegramente por código**. **No se usa `shiro.ini`.**

- La configuración vive en un `WebEnvironment` propio por módulo, declarado por parámetro de
  contexto. Ahí se arman `SecurityManager`, realm, gestor de sesión y cadenado de filtros.
- **Se requiere Shiro 2.x.** Jakarta EE 11 usa el espacio `jakarta.*`; Shiro 1.x depende de
  `javax.servlet` y no sirve. La versión exacta se verifica al fijar la dependencia.
- El realm de cada módulo lee **solo su propio esquema** de identidad: `RealmCaptura` contra
  `seg_cap`, `RealmReportes` contra `seg_rep`.
- **Gestor de sesión nativo de Shiro**, no el del contenedor, con un `SessionIdGenerator`
  propio sobre `SecureRandom` de 32 bytes.
- **`RememberMe` deshabilitado** explícitamente.
- **Argon2id** mediante un `CredentialsMatcher` propio.

## Consecuencias

**A favor**

- Configuración en código: versionada, revisable en el diff, verificable con pruebas, sin un
  archivo de texto paralelo que se desincroniza.
- Sin `shiro.ini` no hay un segundo lugar donde alguien afloje una regla sin que se note en
  la revisión.
- Los permisos con formato `dominio:accion` de Shiro encajan con la verificación por permiso
  de ADR-004, que se conserva sin cambios.
- Control total sobre generación de identificadores de sesión y verificación de contraseñas,
  que es justo lo que la línea base ASVS exige.
- Experiencia previa del equipo.

**En contra**

- **Hay que sustituir dos valores por omisión de Shiro que no cumplen la línea base**:
  `JavaUuidSessionIdGenerator` (un UUID no aporta la entropía exigida) y el servicio de hash
  por omisión. Olvidar cualquiera de los dos deja un control incumplido sin que nada falle
  visiblemente.
- El gestor de sesión nativo desplaza al del contenedor. Shiro respalda
  `HttpServletRequest.getSession()` y JSF sigue funcionando, pero es una pieza más que
  entender al depurar.
- Las anotaciones de Shiro requieren el interceptor CDI cableado. Mientras no se verifique en
  Payara, la comprobación programática en el servicio es la vía obligatoria.
- Una dependencia externa más que mantener y vigilar en avisos de seguridad.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Jakarta Security** (`@DatabaseIdentityStoreDefinition`) | Era el supuesto de ADR-004. Viable, pero el equipo tiene experiencia con Shiro y prefiere su control explícito sobre sesión y credenciales |
| **Shiro con `shiro.ini`** | Descartado expresamente. Configuración de seguridad fuera del código: no se revisa en el diff, no se prueba, y se desincroniza del despliegue |
| **Seguridad del contenedor Payara** | Ata la aplicación al servidor y dificulta las pruebas de integración de los controles |
| **`RememberMe` de Shiro habilitado** | Cookie con estado serializado; origen histórico de vulnerabilidades de deserialización. Ningún caso de uso lo justifica aquí |
| **bcrypt como algoritmo de contraseña** | **Prohibido.** Trunca a 72 bytes en silencio, lo que viola directamente el requisito de verificar la contraseña exactamente como se recibe |

## Lo que NO cambia

ADR-004 se conserva íntegro: identidad segregada por módulo, `DENY` cruzado sobre el esquema
de identidad del otro, verificación por permisos y no por roles, y **la autoridad nunca
derivada de los grupos de un proveedor de identidad**. Shiro es el mecanismo; la estructura
de control de acceso es la misma.
