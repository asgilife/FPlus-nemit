package prototipo.ensayes;

import prototipo.comun.ReglaException;
import prototipo.comun.SesionDemo;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.primefaces.PrimeFaces;

/**
 * Revisión de Fresnillo de lo que ERSA reporta (HU-006 y la parte de Fresnillo de HU-007). Habla solo con
 * {@link RevisionEnsayesService}.
 */
@Named
@ViewScoped
public class RevisionEnsayesView implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DIA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Inject
    private RevisionEnsayesService servicio;

    @Inject
    private CapturaEnsayesService catalogos;

    @Inject
    private SesionDemo sesion;

    private final Filtro filtro = new Filtro();
    private List<Lote> lotes;
    private List<Lote> seleccionados = new ArrayList<>();
    private Lote detalle;
    private List<FilaAnalisis> filas;
    private String observacion;

    @PostConstruct
    void iniciar() {
        if (sesion.esDe("Fresnillo")) {
            filtrar();
        }
    }

    public void filtrar() {
        lotes = servicio.consulta(filtro);
        seleccionados = new ArrayList<>();
    }

    public void limpiarFiltros() {
        filtro.setCompania(null);
        filtro.setUnidad(null);
        filtro.setProducto(null);
        filtro.setDesde(null);
        filtro.setHasta(null);
        filtrar();
    }

    public void verDetalle(Lote l) {
        detalle = l;
        try {
            filas = servicio.analisis(l.getClave());
        } catch (ReglaException e) {
            filas = List.of();
        }
    }

    /** Antes de abrir el diálogo: sin lotes no hay nada que regresar. */
    public void prepararRegreso() {
        observacion = null;
        if (getSeleccionCount() == 0) {
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().addMessage("form:avisos",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecciona al menos un lote para regresarlo.", null));
        }
    }

    /**
     * Un ensaye con los decimales de su elemento. Se formatea aquí porque f:convertNumber dentro de una tabla evalúa
     * sus atributos al construir la vista, cuando la fila todavía no existe. Un cero nunca sale «-0».
     */
    public String numero(BigDecimal v, int decimales) {
        if (v == null) {
            return "—";
        }
        BigDecimal r = v.setScale(decimales, RoundingMode.HALF_UP);
        if (r.signum() == 0) {
            r = r.abs();
        }
        DecimalFormat f = new DecimalFormat(decimales == 0 ? "#,##0" : "#,##0." + "0".repeat(decimales),
                DecimalFormatSymbols.getInstance(Locale.of("es", "MX")));
        return f.format(r);
    }

    public String fecha(LocalDate f) {
        return f == null ? "—" : f.format(DIA);
    }

    public String fechaHora(LocalDateTime f) {
        return f == null ? "—" : f.format(DIA_HORA);
    }

    public void regresar() {
        try {
            servicio.regresar(claves(), observacion);
            String texto = "Regresados a ERSA: " + nombres() + ". ERSA recibe el aviso por correo.";
            filtrar();
            PrimeFaces.current().executeScript("PF('dlgRegresar').hide()");
            info(texto);
        } catch (ReglaException e) {
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void autorizar() {
        try {
            servicio.autorizar(claves());
            String texto = "Autorizados: " + nombres() + ".";
            filtrar();
            info(texto);
        } catch (ReglaException e) {
            FacesContext.getCurrentInstance().addMessage("form:avisos",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    private List<String> claves() {
        return seleccionados == null ? List.of() : seleccionados.stream().map(Lote::getClave).collect(Collectors.toList());
    }

    private String nombres() {
        return seleccionados.stream().map(Lote::getLoteMetMex).collect(Collectors.joining(", "));
    }

    private static void info(String texto) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, null));
    }

    /** Cuántos elementos del lote pasan el límite; 0 si ERSA todavía no lo autoriza. */
    public long fuera(Lote l) {
        return servicio.fueraDeLimite(l.getClave());
    }

    public long getPorRevisar() {
        return lotes == null ? 0 : lotes.stream().filter(l -> l.isAutorizadoPorErsa() && !l.isDevuelto()
                && l.getRevision() != Lote.EstadoRevision.AUTORIZADO).count();
    }

    public long getConDiferencias() {
        return lotes == null ? 0 : lotes.stream().filter(l -> fuera(l) > 0).count();
    }

    public long getEnErsa() {
        return lotes == null ? 0 : lotes.stream().filter(l -> !l.isAutorizadoPorErsa() || l.isDevuelto()).count();
    }

    public int getSeleccionCount() {
        return seleccionados == null ? 0 : seleccionados.size();
    }

    public List<String> getCompanias() { return catalogos.companias(); }
    public List<String> getUnidades() { return catalogos.unidades(filtro.getCompania()); }
    public List<String> getProductos() { return catalogos.productos(); }
    public Filtro.TipoBusqueda[] getTiposBusqueda() { return Filtro.TipoBusqueda.values(); }

    public Filtro getFiltro() { return filtro; }
    public List<Lote> getLotes() { return lotes; }
    public List<Lote> getSeleccionados() { return seleccionados; }
    public void setSeleccionados(List<Lote> seleccionados) { this.seleccionados = seleccionados; }
    public Lote getDetalle() { return detalle; }
    public List<FilaAnalisis> getFilas() { return filas; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
