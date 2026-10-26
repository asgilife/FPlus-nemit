package prototipo.ensayes;

import java.io.Serializable;
import java.time.LocalDate;

/** La pantalla de entrada de la captura y de las consultas (RF-037): compañía, unidad, producto y fechas. */
public class Filtro implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Por qué fecha se busca (RF-037). Lista fija. */
    public enum TipoBusqueda {
        CIERRE("Fecha de cierre del lote"),
        RECEPCION("Fecha de recepción de la muestra");

        private final String texto;

        TipoBusqueda(String texto) {
            this.texto = texto;
        }

        public String getTexto() { return texto; }
    }

    private String compania;
    private String unidad;
    private String producto;
    private TipoBusqueda tipoBusqueda = TipoBusqueda.CIERRE;
    private LocalDate desde;
    private LocalDate hasta;

    public String getCompania() { return compania; }
    public void setCompania(String compania) { this.compania = compania; }
    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }
    public TipoBusqueda getTipoBusqueda() { return tipoBusqueda; }
    public void setTipoBusqueda(TipoBusqueda tipoBusqueda) { this.tipoBusqueda = tipoBusqueda; }
    public LocalDate getDesde() { return desde; }
    public void setDesde(LocalDate desde) { this.desde = desde; }
    public LocalDate getHasta() { return hasta; }
    public void setHasta(LocalDate hasta) { this.hasta = hasta; }
}
