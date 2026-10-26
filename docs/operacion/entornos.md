# Entornos — FPlus

Dónde corre el sistema. Lo lee `nemit:devops` para conectarse y desplegar.

**Ninguna contraseña, llave ni token en este archivo.** El acceso es con llaves SSH (`~/.ssh/config` de cada persona) o
variables de entorno. Aquí se dice el host y el usuario, nunca el secreto.

| Entorno | Host | Acceso | Servicios y puertos | Rutas | ¿Confirmar antes de actuar? |
|---|---|---|---|---|---|
| Local | `localhost` | — | <servidor de aplicaciones, base, puertos> | <carpetas> | No |
| Pruebas | <host> | `ssh <alias>` | <…> | <…> | Reinicios y migraciones |
| Producción | <host> | `ssh <alias>` | <…> | <…> | **Siempre** |

## Bitácora

Cada acción en un entorno remoto deja una línea: fecha, entorno, qué se hizo, quién lo pidió y cómo se comprobó.

| Fecha | Entorno | Acción | Pedido por | Comprobación |
|---|---|---|---|---|
