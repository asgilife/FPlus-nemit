package prototipo.ejemplo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Un registro de ejemplo. Clase con getters y no record: el EL de Jakarta EE 10 no lee los accesores de un record.
 */
public class Registro implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Estado {
        PENDIENTE("Pendiente", "warning"),
        EN_CURSO("En curso", "info"),
        COMPLETADO("Completado", "success"),
        CANCELADO("Cancelado", "danger");

        private final String texto;
        private final String severidad;

        Estado(String texto, String severidad) {
            this.texto = texto;
            this.severidad = severidad;
        }

        public String getTexto() {
            return texto;
        }

        /** Severidad de p:tag: success, info, warning, danger. */
        public String getSeveridad() {
            return severidad;
        }
    }

    private final String folio;
    private final LocalDateTime fecha;
    private final String responsable;
    private final int cantidad;
    private final String observaciones;
    private Estado estado;

    public Registro(String folio, LocalDateTime fecha, String responsable, int cantidad, String observaciones,
                    Estado estado) {
        this.folio = folio;
        this.fecha = fecha;
        this.responsable = responsable;
        this.cantidad = cantidad;
        this.observaciones = observaciones;
        this.estado = estado;
    }

    public boolean isCancelable() {
        return estado == Estado.PENDIENTE || estado == Estado.EN_CURSO;
    }

    public String getFolio() { return folio; }
    public LocalDateTime getFecha() { return fecha; }
    public String getResponsable() { return responsable; }
    public int getCantidad() { return cantidad; }
    public String getObservaciones() { return observaciones; }
    public Estado getEstado() { return estado; }
    void setEstado(Estado estado) { this.estado = estado; }
}
