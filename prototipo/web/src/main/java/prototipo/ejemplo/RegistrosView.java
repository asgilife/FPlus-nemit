package prototipo.ejemplo;

import prototipo.comun.ReglaException;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import org.primefaces.PrimeFaces;

/**
 * Vista de la historia de ejemplo. Habla solo con {@link RegistroService}: al graduarse al sistema real, esta clase se
 * mueve tal cual y lo único que cambia es la implementación que CDI le inyecta.
 */
@Named
@ViewScoped
public class RegistrosView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RegistroService servicio;

    private String texto;
    private Registro.Estado estado;
    private List<Registro> registros;
    private Registro seleccionado;

    private String nuevoResponsable;
    private Integer nuevaCantidad;
    private String nuevasObservaciones;

    @PostConstruct
    void iniciar() {
        filtrar();
    }

    public void filtrar() {
        registros = servicio.buscar(texto, estado);
    }

    public void prepararNuevo() {
        nuevoResponsable = null;
        nuevaCantidad = null;
        nuevasObservaciones = null;
    }

    /** Guarda; si el servicio rechaza, el diálogo se queda abierto con lo capturado y el mensaje del rechazo. */
    public void guardar() {
        try {
            Registro r = servicio.crear(nuevoResponsable, nuevaCantidad, nuevasObservaciones);
            filtrar();
            PrimeFaces.current().executeScript("PF('dlgNuevo').hide()");
            mensaje(FacesMessage.SEVERITY_INFO, r.getFolio() + " guardado.");
        } catch (ReglaException e) {
            FacesContext.getCurrentInstance().validationFailed();
            mensaje(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public void cancelarSeleccionado() {
        if (seleccionado == null) {
            return;
        }
        try {
            servicio.cancelar(seleccionado.getFolio());
            filtrar();
            mensaje(FacesMessage.SEVERITY_INFO, seleccionado.getFolio() + " cancelado.");
        } catch (ReglaException e) {
            mensaje(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
    }

    public long contar(String nombreEstado) {
        Registro.Estado e = Registro.Estado.valueOf(nombreEstado);
        return servicio.buscar(null, null).stream().filter(r -> r.getEstado() == e).count();
    }

    public long getTotal() {
        return servicio.buscar(null, null).size();
    }

    public Registro.Estado[] getEstados() {
        return Registro.Estado.values();
    }

    public List<String> getResponsables() {
        return servicio.responsables();
    }

    private static void mensaje(FacesMessage.Severity severidad, String texto) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, texto, null));
    }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public Registro.Estado getEstado() { return estado; }
    public void setEstado(Registro.Estado estado) { this.estado = estado; }
    public List<Registro> getRegistros() { return registros; }
    public Registro getSeleccionado() { return seleccionado; }
    public void setSeleccionado(Registro seleccionado) { this.seleccionado = seleccionado; }
    public String getNuevoResponsable() { return nuevoResponsable; }
    public void setNuevoResponsable(String nuevoResponsable) { this.nuevoResponsable = nuevoResponsable; }
    public Integer getNuevaCantidad() { return nuevaCantidad; }
    public void setNuevaCantidad(Integer nuevaCantidad) { this.nuevaCantidad = nuevaCantidad; }
    public String getNuevasObservaciones() { return nuevasObservaciones; }
    public void setNuevasObservaciones(String nuevasObservaciones) { this.nuevasObservaciones = nuevasObservaciones; }
}
