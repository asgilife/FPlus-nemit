# Modelo de amenazas — FPlus

Lo mantienen `nemit:appsec` y `nemit:arquitecto` (Arquitectura de Referencia Nemit, AR-S-02). Se actualiza cuando una ficha
agrega una frontera de confianza, un actor o un dato sensible.

## Fronteras de confianza

```
[navegador] ──TLS──► [servidor de aplicaciones] ──► [base de datos]
[app móvil] ──TLS──►          │
                              └──TLS──► [sistemas externos]
```

<Ajustar a los componentes de `arquitectura/arquitectura.md`.>

## Actores

| Actor | Qué puede hacer | De dónde viene |
|---|---|---|
| <usuario legítimo, usuario malintencionado con cuenta, alguien en la red, administrador del servidor…> | <…> | <…> |

## Activos

| Activo | Por qué importa | Clasificación propuesta |
|---|---|---|
| <credenciales, datos personales, datos del negocio, disponibilidad…> | <…> | <sensible · interno · público> (pasa a `modelo-datos.md`, AR-S-11) |

## Fronteras que vendrán

<Las que agregan historias del backlog todavía sin ficha (una app, una integración): se modelan cuando su ficha llegue.>

## Amenazas

STRIDE como guía: suplantación, alteración, repudio, divulgación, denegación de servicio, elevación de privilegios.

| # | Frontera / componente | Amenaza (quién, cómo) | Qué protege | Mitigación (AR, ADR, ficha) | Estado |
|---|---|---|---|---|---|
| A-01 | <…> | <…> | <dato o función> | <…> | <mitigada · pendiente (dueño) · riesgo aceptado (quién, fecha, ADR)> |

**Un riesgo aceptado lo acepta el usuario**, nunca el equipo: queda con su nombre de rol, la fecha y el ADR.
