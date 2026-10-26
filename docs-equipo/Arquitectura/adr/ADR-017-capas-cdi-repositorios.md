# ADR-017 · Capas con servicios CDI transaccionales y repositorios, no EJB `@Stateless` con facades

**Estado:** Aceptado · 2026-10-21
**Valida:** 2026-10-21 · `nemit:validador` · para la ficha del esqueleto: los temporizadores con `persistent = false`
(AR-JF-12). Jakarta EE 11 trae Concurrency 3.1 (`@Asynchronous(runAt = @Schedule(...))` en beans CDI), que evitaría el
EJB `@Singleton`; si el esqueleto lo usa, se anota como precisión
**Se aparta de AR-JF-01** (Arquitectura de Referencia Nemit 0.9, borrador) en el mecanismo, no en la regla.

## Contexto

AR-JF-01 pide vista → servicio EJB `@Stateless` → `{Entidad}Facade`. La arquitectura de FPlus ya decidió otra forma
en [02](../02-vista-de-modulos.md) («Capas dentro de cada módulo») y [08](../08-convenciones-de-desarrollo.md) («Capas y
transacciones»): servicios CDI `@ApplicationScoped` con `@Transactional` y repositorios sobre `EntityManager`. Ningún
ADR lo había registrado frente a la referencia.

## Decisión

- Se conserva la regla de fondo de AR-JF-01: **la vista nunca toca el acceso a datos**; vista → backing bean → servicio
  → repositorio.
- El mecanismo es CDI: servicios `@ApplicationScoped @Transactional` y repositorios. La transacción aparte de AR-JF-02
  se hace con `@Transactional(REQUIRES_NEW)` en **otro** bean, nunca por autoinvocación.
- Lo que CDI no da se toma de Jakarta EE sin agregar dependencias: las tareas programadas
  ([ADR-013](ADR-013-provem-por-endpoint.md), [ADR-014](ADR-014-correo-por-bandeja.md)) son un EJB `@Singleton` con
  `@Schedule`, que cumple AR-JF-12 junto con el candado en la base.
- Los servicios de `fplus-domain` no dependen de Shiro: comprueban permisos a través de una interfaz (`Autorizador`)
  que cada WAR implementa con su sesión. Así se cumple que el dominio no conoce el módulo en el que corre
  ([02](../02-vista-de-modulos.md), «Reglas de dependencia»).

## Consecuencias

**A favor**

- Un solo modelo de componentes (CDI) para servicios, vistas y pruebas.
- Los repositorios se prueban con Testcontainers sin contenedor EJB.

**En contra**

- Las reglas de AR-JF que hablan de `@Stateless` se leen traducidas a CDI; cada ficha que las cite lo dice.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Adoptar AR-JF-01 tal cual** (EJB `@Stateless` + facades) | Contradice 02 y 08 sin una fuerza que lo pida; dos modelos de componentes mezclados |
| **No hacer nada** | La desviación existiría sin ADR, y por la regla de la referencia una desviación sin ADR no existe |
