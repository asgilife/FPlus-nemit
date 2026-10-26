package prototipo.comun;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Sesión simulada del prototipo: no hay login, se elige el rol desde la barra para enseñar qué ve cada actor.
 * Los roles son los del glosario del proyecto; se cambian aquí.
 */
@Named("sesionDemo")
@SessionScoped
public class SesionDemo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Los actores del proyecto, en el orden en que se ofrecen. El primero es el rol con el que abre el prototipo.
     * Dos capturistas de ERSA para enseñar la captura repartida (RF-039). El rol de Fresnillo lleva un nombre
     * provisional mientras el cliente dice cómo se llama el puesto (P-050).
     */
    private static final List<String> ROLES = List.of(
            "ERSA · capturista 1", "ERSA · capturista 2", "ERSA · revisor", "ERSA · autorizador",
            "Fresnillo · revisa ensayes");

    private String rol = ROLES.get(0);

    @Inject
    private Event<DatosReiniciados> reinicio;

    /**
     * Vuelve todos los datos de ejemplo a su estado inicial y recarga la página (la redirección rehace las vistas,
     * que si no conservarían sus listas viejas). El aviso lo pinta la plantilla al ver ?reiniciado=true: el flash de
     * JSF no llega con una redirección cuando la respuesta ya se envió.
     */
    public String reiniciarDatos() {
        reinicio.fire(new DatosReiniciados());
        return FacesContext.getCurrentInstance().getViewRoot().getViewId() + "?faces-redirect=true&reiniciado=true";
    }

    /** True si el rol actual es uno de los indicados, separados por coma: es('Supervisor,Administrador'). */
    public boolean es(String roles) {
        return Arrays.stream(roles.split(",")).map(String::trim).anyMatch(rol::equals);
    }

    /** True si el rol actual empieza con el prefijo: esDe('ERSA') para todos los de ERSA. */
    public boolean esDe(String prefijo) {
        return rol.startsWith(prefijo);
    }

    public List<String> getRoles() {
        return ROLES;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        if (ROLES.contains(rol)) {
            this.rol = rol;
        }
    }
}
