# Despliegue — FPlus

Cómo se pone una versión en un entorno. Lo sigue `nemit:devops`; los servidores están en [entornos.md](entornos.md).

## Antes de desplegar

- [ ] Las pruebas de la versión están en verde.
- [ ] Respaldo de la base si hay migraciones.
- [ ] Orden de despliegue cuando hay varios artefactos (por ejemplo: migraciones → API → web → app).

## Pasos por artefacto

### <artefacto>

1. <compilar> — comprobación: <…>
2. <copiar o publicar> — comprobación: <…>
3. <reiniciar o recargar> — comprobación: <la versión desplegada es la nueva>

## Configuración del servidor

<Variables, propiedades y servicios que el artefacto necesita. **Los valores secretos no van aquí**: se dice cómo se
configuran, no cuáles son.>

## Volver atrás

<Cómo se regresa a la versión anterior si algo sale mal.>
