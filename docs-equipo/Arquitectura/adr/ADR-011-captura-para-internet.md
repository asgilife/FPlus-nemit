# ADR-011 · `fplus-captura` se diseña para internet; la primera etapa entra por VPN

**Estado:** Aceptado con supuesto (P-061) · 2026-10-21
**Valida:** 2026-10-21 · `nemit:validador`
**Relación:** precisa [ADR-001](ADR-001-dos-modulos-desplegables.md) y cierra la decisión pendiente de
[03 · Vista de despliegue](../03-vista-de-despliegue.md) (escenario A frente a B).

## Contexto

El pendiente de mayor impacto de la arquitectura era si ERSA entra desde internet o por VPN
([01](../01-vision-y-alcance.md), «Pendientes»). Ya tiene respuesta:

- El cliente: «Este sistema es interno y ERSA lo va a usar porque le vamos a prestar incluso a nuestros equipos»
  (RNF-003, `docs/fuentes/juntas/2026-09-04-intercambio-y-terceria/transcripcion.md`, 01:40:39), y para externos,
  acceso «únicamente … al aplicativo como tal», con la sesión grabada (RNF-005, misma junta, 01:41:44).
- El usuario, el 2026-10-21 (respuesta a P-004 en `docs/requisitos/preguntas-abiertas.md`): al inicio, equipos
  prestados y VPN; **después pasará a internet**, y se diseña así desde el principio.
- **Decisión del usuario del 2026-10-21** (validación de la arquitectura): los analistas de ERSA entran con **segundo
  factor desde el primer día** («Sí, desde el primer día»), frente a construirlo y encenderlo al salir a internet o a
  no ofrecerlo con el riesgo aceptado. Es un código de una aplicación autenticadora (TOTP): la captura no envía correo
  ni SMS (abajo), y el código no necesita ninguna conexión saliente. Lo pide ASVS nivel 2 (AR-S-07).
- **Supuesto (P-061):** el «usuario externo» de ERSA (RNF-004) es una cuenta propia de FPlus en `seg_cap`, no una
  cuenta de invitado en el SSO corporativo. Si TI responde que es del SSO, la captura tendría que validar las
  identidades del proveedor y la regla «sin conexiones salientes» se revisa con otro ADR.

## Decisión

`fplus-captura` se construye para el **escenario A** (expuesto a internet) desde la primera ficha, aunque la primera
etapa se opere por VPN:

- Todos los controles del escenario A son del producto y no dependen de la red: los de [10](../10-controles-owasp-asvs.md),
  la identidad segregada ([ADR-004](ADR-004-identidad-segregada-por-modulo.md)) y el principal de base acotado.
- Lo que sí depende de la red (reverse proxy o WAF, DMZ, regla de firewall de un solo sentido) es **infraestructura de
  TI** y se agrega cuando el módulo salga a internet, sin cambiar el WAR.
- `fplus-captura` no inicia ninguna conexión hacia fuera de la base de datos: ni a PROVEM, ni a correo, ni a otro
  sistema ([ADR-013](ADR-013-provem-por-endpoint.md), [ADR-014](ADR-014-correo-por-bandeja.md)). Así, moverlo a una DMZ
  solo abre un puerto: el de SQL Server.

## Consecuencias

**A favor**

- Pasar a internet es una tarea de infraestructura, no un rediseño ni una migración de código.
- La primera etapa ya prueba los controles que internet exige.

**En contra**

- La primera etapa paga controles (bloqueo por intentos, cabeceras, sesión estricta) que la VPN haría menos urgentes.
  Se acepta: son los mismos que pide la línea base para cualquier módulo.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Diseñar para VPN y endurecer al salir a internet** (escenario B ahora) | El cliente ya dijo que sale a internet; endurecer después obliga a revisar todo el módulo con usuarios en operación |
| **No hacer nada** (dejar el pendiente abierto) | Bloquea la ficha del esqueleto: sin topología no se fijan la sesión ni la autenticación del módulo |
