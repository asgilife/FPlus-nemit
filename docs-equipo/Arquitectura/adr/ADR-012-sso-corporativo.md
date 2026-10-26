# ADR-012 · Fresnillo entra con el inicio de sesión único corporativo; la autoridad sigue en las cuentas locales

**Estado:** Propuesta · 2026-10-21 · **espera la respuesta de P-057** (qué es el SSO corporativo) y de P-061; **vuelve
a `nemit:arquitecto`** por la decisión del usuario del 2026-10-21 (abajo)
**Valida:** 2026-10-21 · `nemit:validador` · no se acepta: depende de P-057 y la decisión del usuario cambia su
«No hacer nada» y su relación con ADR-010
**Relación:** si se acepta, sustituye a [ADR-010](ADR-010-autenticacion-por-directorio.md) en `fplus-reportes`.
No toca [ADR-004](ADR-004-identidad-segregada-por-modulo.md) ni [ADR-009](ADR-009-apache-shiro-programatico.md).

## Contexto

ADR-010 decidió que Fresnillo se autentica contra sus directorios LDAP con un solo realm. Después de esa decisión, TI de
Fresnillo dijo en junta:

> «Es un single sign-on, entras por tu usuario de Oracle. Y eso sí ya es política de la empresa.» — RNF-004,
> `docs/fuentes/juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 02:00:21

Un inicio de sesión único (el usuario ya autenticado en la empresa no vuelve a escribir su contraseña) no es lo mismo
que validar usuario y contraseña contra LDAP. Si es política de la empresa, ADR-010 la contradice. Lo que no sabemos es
**qué es** ese SSO: un proveedor de identidad con OpenID Connect o SAML, un agente de Oracle Access Manager delante de
la aplicación, o el mismo directorio con otra puerta.

**Decisión del usuario del 2026-10-21** (validación de la arquitectura): si TI no entrega la configuración del SSO
antes de la entrega de la primera etapa, Fresnillo entra **mientras tanto por LDAP corporativo** ([ADR-010](ADR-010-autenticacion-por-directorio.md))
(«LDAP corporativo mientras tanto»), frente a cuentas locales provisionales y frente a esperar al SSO. Consecuencias
para este ADR: la ficha del esqueleto **sí** implementa ADR-010 (contradice el «No hacer nada» de abajo y la
consecuencia «el código de ADR-010 no se escribe»); ADR-010 deja de ser solo el respaldo y pasa a ser la etapa
provisional; y su lista de directorios en un archivo de propiedades necesita su ADR de desviación de AR-G-23 y AR-G-03
(ver `../README.md`, «Arquitectura de referencia»). Lo que TI tiene que entregar para esa etapa está en P-062.

**Para cuando se rehaga:** la configuración del SSO (emisor, audiencia, claves) está como «arranque» en
[04](../04-modelo-de-datos.md), y AR-G-23 pone el directorio de usuarios entre las integraciones con pantalla. Si se
queda en el servidor (cambiar el emisor entrega todas las cuentas a quien controle el nuevo), este ADR debe decir que
se aparta de AR-G-23 y por qué.

## Decisión (propuesta)

- `fplus-reportes` autentica a los usuarios de Fresnillo **con el SSO corporativo**, con el protocolo que TI indique
  (P-057). La aplicación recibe una identidad ya autenticada y la valida (firma, emisor, audiencia, vigencia).
- **La autoridad no cambia**: roles y permisos se resuelven contra las cuentas locales de `seg_rep`. Sin cuenta local
  provisionada no hay acceso, aunque el SSO diga quién es la persona. Es la regla 6 de los prohibidos del `README.md` y
  [10](../10-controles-owasp-asvs.md).
- `fplus-captura` no cambia: ERSA no tiene usuario corporativo («Buscaríamos un usuario externo», misma junta,
  02:00:51) y conserva sus credenciales locales con Argon2id.
- ADR-010 queda como respaldo **solo si** TI responde que el SSO es, en la práctica, el mismo directorio LDAP.

## Consecuencias

**A favor**

- Cumple la política de la empresa; el usuario de Fresnillo no maneja otra contraseña.
- La contraseña corporativa nunca pasa por FPlus.
- La compuerta de acceso (la cuenta local) y la segregación por módulo se conservan.

**En contra**

- Depende de que TI registre la aplicación en su proveedor de identidad y entregue su configuración (emisor, claves,
  audiencia) para cada entorno.
- Shiro necesita un realm de token en lugar del realm de directorio de ADR-010; el código de ADR-010 no se escribe.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Mantener ADR-010 (LDAP con contraseña)** | Contradice lo que TI llamó política de la empresa; la contraseña corporativa pasaría por FPlus |
| **Que el SSO también otorgue roles** (grupos del proveedor) | Prohibido: la autoridad nunca viene del proveedor de identidad (regla 6, [10](../10-controles-owasp-asvs.md)) |
| **SSO también para ERSA** | ERSA no tiene usuario corporativo; darle uno es una decisión mayor que nadie pidió |
| **No hacer nada** hasta tener el detalle | Se sigue así para el código: la ficha del esqueleto deja la autenticación de `fplus-reportes` detrás de una interfaz y no implementa ADR-010 hasta que P-057 se responda |
