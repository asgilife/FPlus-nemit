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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.PrimeFaces;

/**
 * Captura de ensayes de ERSA (HU-001, HU-002, HU-003 y la parte de ERSA de HU-007). Habla solo con
 * {@link CapturaEnsayesService}: al graduarse al sistema, esta clase se mueve tal cual.
 */
@Named
@ViewScoped
public class CapturaEnsayesView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CapturaEnsayesService servicio;

    @Inject
    private SesionDemo sesion;

    private final Filtro filtro = new Filtro();
    private boolean todos;
    private List<Lote> lotes;
    private String codigo;

    private Lote lote;
    private List<Elemento> elementos;
    private final Map<String, Object> valores = new HashMap<>();
    private Lote.TipoMuestra tipoMuestra;
    private LocalDate entregaMetMex;
    private LocalDate recepcion;
    private LocalDate estimada;
    private boolean corrigiendo;
    private String observacion;

    @PostConstruct
    void iniciar() {
        if (sesion.esDe("ERSA")) {
            filtrar();
        }
    }

    public void filtrar() {
        lotes = servicio.bandeja(filtro, todos);
    }

    public void limpiarFiltros() {
        filtro.setCompania(null);
        filtro.setUnidad(null);
        filtro.setProducto(null);
        filtro.setDesde(null);
        filtro.setHasta(null);
        filtrar();
    }

    /** Lector de código de barras: el lector «teclea» el código y un Enter; aquí se busca el lote. */
    public void leerCodigo() {
        try {
            abrir(servicio.porCodigo(codigo));
            codigo = null;
            // Supuesto (P-022, C-005): leer el sobre propone la fecha de recepción de hoy si no la tiene
            if (recepcion == null && isEditable()) {
                recepcion = LocalDate.now();
            }
            PrimeFaces.current().executeScript("PF('dlgLote').show()");
        } catch (ReglaException e) {
            error(e);
        }
    }

    public void abrir(Lote l) {
        lote = l;
        elementos = servicio.elementos(l);
        valores.clear();
        l.getEnsayes().forEach((k, c) -> valores.put(k, c.getValor()));
        tipoMuestra = l.getTipoMuestra();
        entregaMetMex = l.getFechaEntregaMetMex();
        recepcion = l.getFechaRecepcion();
        estimada = l.getFechaEstimada();
        corrigiendo = false;
        observacion = null;
    }

    public void guardar() {
        ejecutar(() -> servicio.guardar(lote.getClave(), numericos(), tipoMuestra, entregaMetMex, recepcion, estimada),
                "Captura guardada.", false);
    }

    /** Guarda y envía: lo que se ve en pantalla es lo que se envía. */
    public void enviar() {
        ejecutar(() -> {
            servicio.guardar(lote.getClave(), numericos(), tipoMuestra, entregaMetMex, recepcion, estimada);
            servicio.enviar(lote.getClave());
        }, "Lote enviado a revisión.", true);
    }

    public void marcarRevisado() {
        ejecutar(() -> servicio.marcarRevisado(lote.getClave()), "Lote marcado como revisado.", true);
    }

    public void regresar() {
        ejecutar(() -> servicio.regresarEnErsa(lote.getClave(), observacion), "Lote regresado a captura.", true);
    }

    public void autorizar() {
        ejecutar(() -> servicio.autorizar(lote.getClave()), "Lote autorizado: los ensayes ya están en Fresnillo.", true);
    }

    public void confirmar() {
        ejecutar(() -> servicio.confirmar(lote.getClave()), "Valores confirmados: Fresnillo recibe el aviso.", true);
    }

    public void corregir() {
        corrigiendo = true;
    }

    public void actualizarYEnviar() {
        ejecutar(() -> servicio.actualizarYEnviar(lote.getClave(), numericos()), "Lote corregido y enviado a revisión.", true);
    }

    /** El componente numérico ligado a un mapa puede entregar Double o texto: al servicio va siempre BigDecimal. */
    private Map<String, BigDecimal> numericos() {
        Map<String, BigDecimal> m = new HashMap<>();
        valores.forEach((k, v) -> {
            if (v instanceof BigDecimal b) {
                m.put(k, b);
            } else if (v instanceof Number n) {
                m.put(k, new BigDecimal(n.toString()));
            } else if (v != null && !v.toString().isBlank()) {
                m.put(k, new BigDecimal(v.toString().trim()));
            }
        });
        return m;
    }

    private void ejecutar(Runnable accion, String exito, boolean cerrar) {
        String clave = lote.getClave();
        try {
            accion.run();
            filtrar();
            if (cerrar) {
                PrimeFaces.current().executeScript("PF('dlgLote').hide()");
            } else {
                lotes.stream().filter(l -> l.getClave().equals(clave)).findFirst().ifPresent(this::abrir);
            }
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, lote.getLoteMetMex() + ": " + exito, null));
        } catch (ReglaException e) {
            FacesContext.getCurrentInstance().validationFailed();
            error(e);
        }
    }

    private static void error(ReglaException e) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
    }

    // ── Lo que la pantalla deja hacer, según el rol y el estado (el servicio lo vuelve a revisar) ──

    public boolean isCapturista() {
        return sesion.esDe("ERSA · capturista");
    }

    public boolean isEditable() {
        return lote != null && isCapturista() && !lote.isDevuelto()
                && (lote.getEstado() == Lote.EstadoCaptura.PENDIENTE || lote.getEstado() == Lote.EstadoCaptura.GUARDADO);
    }

    public boolean isValoresEditables() {
        return isEditable() || (lote != null && lote.isDevuelto() && isCapturista() && corrigiendo);
    }

    public boolean isPuedeRevisar() {
        return lote != null && sesion.es("ERSA · revisor") && lote.getEstado() == Lote.EstadoCaptura.ENVIADO;
    }

    public boolean isPuedeAutorizar() {
        return lote != null && sesion.es("ERSA · autorizador") && lote.getEstado() == Lote.EstadoCaptura.REVISADO;
    }

    public boolean isPuedeAtenderDevolucion() {
        return lote != null && lote.isDevuelto() && isCapturista();
    }

    public Lote.Captura captura(String clave) {
        return lote == null ? null : lote.getEnsayes().get(clave);
    }

    public int getCapturados() {
        return lote == null ? 0 : lote.getEnsayes().size();
    }

    public String getTitulo() {
        if (sesion.esDe("ERSA · capturista")) {
            return "Lotes por capturar";
        }
        return sesion.es("ERSA · revisor") ? "Lotes por revisar" : "Lotes por autorizar";
    }

    public Lote.TipoMuestra[] getTiposMuestra() { return Lote.TipoMuestra.values(); }
    public Filtro.TipoBusqueda[] getTiposBusqueda() { return Filtro.TipoBusqueda.values(); }
    public List<String> getCompanias() { return servicio.companias(); }
    public List<String> getUnidades() { return servicio.unidades(filtro.getCompania()); }
    public List<String> getProductos() { return servicio.productos(); }

    public Filtro getFiltro() { return filtro; }
    public boolean isTodos() { return todos; }
    public void setTodos(boolean todos) { this.todos = todos; }
    public List<Lote> getLotes() { return lotes; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public Lote getLote() { return lote; }
    public void setLote(Lote l) { if (l != null) { abrir(l); } }
    public int total(Lote l) { return servicio.elementos(l).size(); }
    public List<Elemento> getElementos() { return elementos; }
    public Map<String, Object> getValores() { return valores; }
    public Lote.TipoMuestra getTipoMuestra() { return tipoMuestra; }
    public void setTipoMuestra(Lote.TipoMuestra tipoMuestra) { this.tipoMuestra = tipoMuestra; }
    public LocalDate getEntregaMetMex() { return entregaMetMex; }
    public void setEntregaMetMex(LocalDate entregaMetMex) { this.entregaMetMex = entregaMetMex; }
    public LocalDate getRecepcion() { return recepcion; }
    public void setRecepcion(LocalDate recepcion) { this.recepcion = recepcion; }
    public LocalDate getEstimada() { return estimada; }
    public void setEstimada(LocalDate estimada) { this.estimada = estimada; }
    public boolean isCorrigiendo() { return corrigiendo; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
