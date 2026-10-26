# ADR-001 · Dos módulos desplegables independientes

**Estado:** Aceptada · 2026-09-11

## Contexto

FPlus atiende dos audiencias distintas:

- Los analistas de **ERSA**, laboratorio representante de Fresnillo, que capturan ensayes.
  Pueden necesitar acceso desde fuera de la red corporativa.
- Los usuarios de **Fresnillo PLC**, en red interna, que concilian, intercambian con
  Met-Mex, negocian y consultan.

Además, NFR-008 exige mantener separada la información comercial entre la captura de ERSA y
el intercambio externo.

## Decisión

El sistema se construye como **dos aplicaciones web desplegables por separado**:
`fplus-captura` y `fplus-reportes`, cada una un WAR sobre su propia instancia de Payara.

El código común vive en una biblioteca (`fplus-domain`, un JAR), no en un contenedor
compartido.

## Consecuencias

**A favor**

- Cada módulo se despliega, reinicia y dimensiona sin afectar al otro.
- Permite colocar solo el módulo expuesto en una DMZ. **Un EAR no puede partirse entre dos
  redes**: esta es la razón técnica dura de la decisión.
- Superficies de ataque separadas: comprometer el módulo expuesto no entrega el interno.
- Permite privilegios de base de datos distintos por módulo, que es lo que habilita
  [ADR-003](ADR-003-bd-unica-esquemas-separados.md) y
  [ADR-004](ADR-004-identidad-segregada-por-modulo.md).

**En contra**

- Dos instancias de Payara que operar, monitorear y actualizar.
- Un cambio en `fplus-domain` obliga a recompilar y redesplegar ambos.
- Código de infraestructura duplicado si no se cuida qué va en la biblioteca.

## Alternativas consideradas

| Alternativa | Por qué no |
| --- | --- |
| **Un solo WAR monolítico** | Imposible exponer parte a internet sin exponerlo todo. Rompe NFR-008 desde el diseño |
| **Un EAR con dos módulos web** | No se puede desplegar medio EAR en una DMZ. Comparten ciclo de vida y principal de base de datos |
| **Microservicios** | Sobredimensionado: un equipo, un dominio cohesionado, sin necesidad de escalado independiente. Agregaría latencia y complejidad operativa sin beneficio |
