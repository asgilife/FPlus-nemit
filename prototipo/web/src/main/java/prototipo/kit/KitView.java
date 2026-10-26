package prototipo.kit;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

/** Datos fijos para el catálogo de componentes del kit. */
@Named
@ViewScoped
public class KitView implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Un hito de una línea de tiempo vertical. */
    public static class Hito implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String hora;
        private final String titulo;
        private final String detalle;
        private final String severidad;

        public Hito(String hora, String titulo, String detalle, String severidad) {
            this.hora = hora;
            this.titulo = titulo;
            this.detalle = detalle;
            this.severidad = severidad;
        }

        public String getHora() { return hora; }
        public String getTitulo() { return titulo; }
        public String getDetalle() { return detalle; }
        public String getSeveridad() { return severidad; }
    }

    private final List<Hito> hitos = List.of(
            new Hito("08:30", "Paso cumplido", "A tiempo", "success"),
            new Hito("10:00", "Paso por vencer", "Quedan 30 min", "warning"),
            new Hito("12:15", "Paso que ya no se alcanza", "Fuera de tiempo", "danger"),
            new Hito("14:00", "Paso pendiente", "Sin iniciar", "info"));

    private String texto;
    private String opcion = "A";
    private Integer cantidad = 0;
    private java.time.LocalDate fecha;
    private String eleccion = "recomendada";

    public void notificar() {
        jakarta.faces.context.FacesContext.getCurrentInstance().addMessage(null,
                new jakarta.faces.application.FacesMessage("Registro guardado."));
    }

    public List<Hito> getHitos() { return hitos; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getOpcion() { return opcion; }
    public void setOpcion(String opcion) { this.opcion = opcion; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public java.time.LocalDate getFecha() { return fecha; }
    public void setFecha(java.time.LocalDate fecha) { this.fecha = fecha; }
    public String getEleccion() { return eleccion; }
    public void setEleccion(String eleccion) { this.eleccion = eleccion; }
}
