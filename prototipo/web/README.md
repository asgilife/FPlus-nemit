# Prototipo web — FPlus

Las pantallas del sistema **en su tecnología final** (Jakarta Faces 4.1 + PrimeFaces 16 sobre Java 25, el tema Nemit y la plantilla del
sistema), con datos simulados en memoria. El cliente ve exactamente lo que va a recibir: mismos componentes, mismo
comportamiento, mismo aspecto. Lo mantiene `nemit:disenador`.

## Compilar y desplegar

```bash
mvn package -DskipTests
# WAR: target/fplus-prototipo-web.war
```

Se compila con JDK 25 y se despliega en un Payara 7 (Jakarta EE 11) como cualquier WAR (no necesita base de datos ni recursos JNDI). En el servidor de
demostración lo hace `nemit:devops`; los datos de acceso están en `docs/operacion/entornos.md`.

## Qué trae

| Ruta | Qué es |
|---|---|
| `src/main/webapp/plantilla/` | Plantilla de página: barra (tema, «Reiniciar datos», rol simulado), menú lateral y pie |
| `src/main/webapp/resources/nemit-tema/` | Tokens y tema Nemit (tres variantes: Azul, Grafito, Claro) y `kit.css` con las piezas propias del kit |
| `src/main/webapp/vistas/kit/componentes.xhtml` | **Catálogo de componentes**: todo lo que usan las vistas, en el tema del proyecto |
| `src/main/webapp/vistas/ejemplo/registros.xhtml` | Historia de ejemplo: lista con filtros, detalle, alta validada por el servicio, cancelación con confirmación |
| `src/main/java/prototipo/comun/` | Tema, sesión simulada (rol), `ReglaException` y el evento de «Reiniciar datos» |
| `src/main/java/prototipo/ejemplo/` | El patrón de cada historia: entidad, **interfaz del servicio**, **servicio simulado** y vista |

## El tema

Este prototipo se generó con un **tema** (la piel), separado de la estructura. Lo que es del tema:

| Archivo | Qué decide |
|---|---|
| `src/main/resources/tema.properties` | Nombre del tema, sus variantes en orden (las que ofrece la barra), la de omisión, y por variante su etiqueta, su tema base de PrimeFaces (`saga-blue`, `arya-blue`…) y si es `claro` u `oscuro` |
| `resources/nemit-tema/tokens.css` | Paleta y medidas por variante (`[data-ime-theme="…"]`), con los nombres que leen la estructura y el kit (`--ime-color-*`, `--ime-radius`, `--ime-gap`…) |
| `resources/nemit-tema/theme.css` | Las variables de PrimeFaces apuntando a los tokens, tipografía y superficies |
| `resources/nemit-tema/ajustes.css` | Retoques de componentes y de la estructura. Se carga **después del CSS de componentes de PrimeFaces** (faceta `last` del `<h:head>`): aquí gana a igual especificidad |

La estructura (`layout.css`, `kit.css`, la plantilla) no conoce nombres de variantes: el logo y lo demás deciden por
`data-ime-modo` (`claro` u `oscuro`). **El tema del prototipo es el del sistema real**: al graduarse, estos archivos van
al módulo web del sistema. Para la marca de un cliente se ajustan los tokens, no se crea otro tema.

## El patrón: la vista habla con una interfaz, el servicio simulado se sustituye después

```
registros.xhtml ──► RegistrosView ──► RegistroService (interfaz)
                                          ▲
                        hoy:  RegistroServiceSimulado  (memoria, @ApplicationScoped)
                        mañana: la implementación real (servicio → facade → base), en el sistema
```

- **Cada historia** = una vista XHTML + su `View` (`@Named @ViewScoped`) + una interfaz de servicio + su implementación
  simulada. Copia la carpeta `ejemplo/` y renómbrala.
- **La vista nunca conoce la implementación**: solo la interfaz. Así, cuando la historia se construye de verdad, la vista
  y su `View` **se mueven tal cual** al módulo web del sistema y la ficha solo escribe la implementación real del
  servicio. La pantalla que aprobó el cliente es la que se entrega.
- **Las reglas viven en el servicio, no en la pantalla**: el simulado valida y lanza `ReglaException(código, mensaje)`
  igual que lo hará el real; la vista la convierte en mensaje. Sin `required` de JSF para reglas de negocio.
- **Datos de ejemplo creíbles**, con la forma y el vocabulario del cliente, sin nombres de personas reales. Cada
  servicio simulado observa `DatosReiniciados` para volver a su estado inicial.
- **Lo que ve cada rol**: `rendered="#{sesionDemo.es('Rol1,Rol2')}"`. Los roles se declaran en `SesionDemo`.

## Reglas que el kit ya resolvió (no las repitas)

- Todo componente de PrimeFaces que envía va dentro de un `h:form`, también los de un diálogo de confirmación.
- Los enlaces y botones de la plantilla llevan **id fijo**: en una plantilla compartida, un id automático puede no ser
  el mismo componente en cada página.
- Un `update` desde dentro de un contenedor con nombre (pestañas, tablas) usa la ruta completa (`:formulario:id`).
- Las tablas llevan `reflow="true"`: en pantallas angostas cada fila se apila en vez de recortar columnas.
- Los mensajes de éxito van a la notificación (`p:growl severity="info"`) y los rechazos junto a donde se capturó.
- El cambio de rol recarga la página completa: `update="@all"` no vuelve a evaluar lo que cada rol ve.
- PrimeFaces carga su `components.css` después de las hojas del `<h:head>`: un retoque de un componente que no gana va
  en `ajustes.css` (que se pinta al final), no subiendo especificidad en `theme.css`.
- Las clases de PrimeFaces son `ui-*` (`ui-datatable`, `ui-dialog`, `ui-tag`…), no `p-*`, que son de otras librerías.
- Desde PrimeFaces 15 los temas base se llaman con su color (`saga-blue`, `arya-blue`) y, salvo `saga-blue`, vienen en
  el artefacto `primefaces-themes`: con el nombre viejo (`saga`, `arya`) la página se pinta sin el CSS del tema.
- En PrimeFaces 16 la selección de `p:dataTable` cambió de nombre: `selectionDisabled` (antes `disabledSelection`),
  `selectionRowMode` (antes `rowSelectMode`) y la columna de casillas es `<p:column selectionBox="true"/>`.
- `f:convertNumber` y `f:convertDateTime` dentro de una tabla o un `ui:repeat` evalúan sus atributos al construir la
  vista, cuando la variable de la fila todavía no existe: unos decimales que dependen de la fila se formatean en el
  controlador de vista (y un cero nunca se muestra como «-0»).
- En `proto-campos` cada valor es un elemento (`<span>`): un `h:outputText` sin id no genera elemento y desalinea las
  etiquetas de los valores.
- Un `p:inputNumber` ligado a un `Map` no sabe a qué tipo convertir: el controlador de vista convierte a `BigDecimal`
  antes de llamar al servicio.
- Una tabla con más de seis o siete columnas lleva `styleClass="… proto-tabla-ancha"` además de `reflow="true"`: las
  columnas toman el ancho de su contenido y, por debajo de 1200 px, cada fila se apila. Varias etiquetas en una celda
  van dentro de `<div class="proto-etiquetas">`; las etiquetas nunca se parten en renglones.
- `ime-table-dense` apunta a las clases `ui-datatable-*` (antes apuntaba a `p-datatable-*` y no se aplicaba).
- El selector de rol de la barra lleva `autoWidth="false"`; su ancho lo fija `ajustes.css` (con `autoWidth`, PrimeFaces
  lo ensancha al rol más largo y en teléfono tapa el botón del menú).
- Las pruebas de pantalla excluyen de «desborde» un texto cortado a propósito con puntos suspensivos, siempre que el
  nombre completo esté a la mano (un `title`, la lista desplegable).
