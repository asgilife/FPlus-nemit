# ADR-014 · Los avisos por correo salen de una bandeja en la base; los envía `fplus-reportes`

**Estado:** Propuesta · 2026-10-21 · con supuesto de P-058 (servidor de correo) · **objeción abierta** (abajo)
**Valida:** 2026-10-21 · `nemit:validador` · no se acepta. Objeción: «los destinatarios salen de los usuarios y roles»
no se puede cumplir con la identidad segregada de [ADR-004](ADR-004-identidad-segregada-por-modulo.md). El correo de
RF-053 lo escribe `fplus-reportes` para ERSA, y las direcciones de ERSA viven en `seg_cap.usuario.correo`, sobre el
que `fplus-reportes` tiene `DENY` ([05](../05-seguridad-y-control-de-acceso.md)). El de RF-054 lo escribe
`fplus-captura`, que no puede leer `seg_rep`; ese sí se puede resolver al enviar, porque quien envía es reportes. El
ADR tiene que decir cómo se resuelven los destinatarios de cada sentido sin abrir `seg_cap` a reportes (por ejemplo, la
dirección de ERSA como dato de la integración en `cfg`, o una vista acotada sin credenciales con su porqué). P-027
(quién recibe cada correo) sigue abierta.

## Contexto

El flujo pide correos automáticos en los dos sentidos:

- Fresnillo regresa un lote a ERSA → correo a ERSA con los lotes y la observación (RF-053, desde `fplus-reportes`).
- ERSA confirma un lote regresado → correo a Fresnillo (RF-054, desde `fplus-captura`).

`fplus-captura` no inicia conexiones salientes ([ADR-011](ADR-011-captura-para-internet.md)), y un correo que se
manda dentro de la transacción del negocio se pierde o se duplica si el servidor de correo falla a la mitad.

## Decisión

- Cualquier módulo que necesite avisar **escribe el correo en una bandeja de salida** (`ops.correo_saliente`) en la
  **misma transacción** del cambio que lo origina: si el cambio no se guarda, el correo no existe.
- **Solo `fplus-reportes` envía**: una tarea programada, con el mismo candado de una sola ejecución que
  [ADR-013](ADR-013-provem-por-endpoint.md), toma los pendientes, los manda por SMTP y marca cada uno como enviado o
  fallido, con reintentos acotados.
- El servidor SMTP, el remitente y los reintentos son **configuración de integración** con pantalla y prueba de envío
  (AR-G-23). Los destinatarios salen de los usuarios y roles, no de texto escrito en el código.
- Cada valor que entra a una cabecera de correo se valida contra retorno de carro y salto de línea, también las
  direcciones leídas de la base (regla del `README.md`).

## Consecuencias

**A favor**

- `fplus-captura` sigue sin salir de su red; su correo lo envía el módulo interno.
- Ningún aviso se pierde ni sale de un cambio que no se guardó; los fallidos se ven y se pueden reintentar.
- La bandeja es también la evidencia de qué se avisó, a quién y cuándo (RNF-001).

**En contra**

- El aviso no es instantáneo: tarda lo que tarde la siguiente pasada de la tarea (segundos o un minuto).

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Cada módulo manda su correo por SMTP** | `fplus-captura` abriría una salida desde la DMZ; un fallo de SMTP rompería la transacción del negocio o dejaría el aviso sin enviar |
| **Cola de mensajes** | Una pieza más que operar para unos cuantos correos al día |
| **No hacer nada** (solo aviso dentro del sistema) | El cliente pidió correo explícitamente (RF-053, RF-054) |
