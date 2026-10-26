package prototipo.ensayes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Un lote cerrado y lo que ERSA reporta de él. Los datos del lote vienen de PROVEM (RF-001); los ensayes, la muestra y
 * las fechas los captura ERSA (RF-036 a RF-046); la revisión es de Fresnillo (RF-051 a RF-055).
 * Clase con getters y no record: el EL no lee los accesores de un record.
 */
public class Lote implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Estado de la captura en ERSA (RF-042). Lista fija: el flujo decide según el valor. */
    public enum EstadoCaptura {
        PENDIENTE("Pendiente", "secondary"),
        GUARDADO("Guardado", "info"),
        ENVIADO("Enviado", "warning"),
        REVISADO("Revisado", "warning"),
        AUTORIZADO("Autorizado", "success");

        private final String texto;
        private final String severidad;

        EstadoCaptura(String texto, String severidad) {
            this.texto = texto;
            this.severidad = severidad;
        }

        public String getTexto() { return texto; }
        public String getSeveridad() { return severidad; }
    }

    /** Lo que Fresnillo hizo con lo que ERSA autorizó (RF-052 a RF-056). */
    public enum EstadoRevision {
        POR_REVISAR("Por revisar", "info"),
        REGRESADO("Regresado a ERSA", "danger"),
        CONFIRMADO("Confirmado por ERSA", "success"),
        ACTUALIZADO("Actualizado por ERSA", "warning"),
        AUTORIZADO("Autorizado por Fresnillo", "success");

        private final String texto;
        private final String severidad;

        EstadoRevision(String texto, String severidad) {
            this.texto = texto;
            this.severidad = severidad;
        }

        public String getTexto() { return texto; }
        public String getSeveridad() { return severidad; }
    }

    /** Tipo de muestra (RF-043). Lista fija: cerrada por naturaleza (hasta dos remuestreos, RF-074). */
    public enum TipoMuestra {
        ORIGINAL("Original"),
        PRIMER_REMUESTREO("Primer remuestreo"),
        SEGUNDO_REMUESTREO("Segundo remuestreo");

        private final String texto;

        TipoMuestra(String texto) {
            this.texto = texto;
        }

        public String getTexto() { return texto; }
    }

    /** Un valor capturado: quién y cuándo, para que «te diga qué elemento capturó quién» (RF-039). */
    public static class Captura implements Serializable {
        private static final long serialVersionUID = 1L;
        private final BigDecimal valor;
        private final String usuario;
        private final LocalDateTime cuando;

        public Captura(BigDecimal valor, String usuario, LocalDateTime cuando) {
            this.valor = valor;
            this.usuario = usuario;
            this.cuando = cuando;
        }

        public BigDecimal getValor() { return valor; }
        public String getUsuario() { return usuario; }
        public LocalDateTime getCuando() { return cuando; }
    }

    /** Una línea de la historia del lote (RNF-001): quién hizo qué y cuándo. */
    public static class Evento implements Serializable {
        private static final long serialVersionUID = 1L;
        private final LocalDateTime cuando;
        private final String quien;
        private final String que;

        public Evento(LocalDateTime cuando, String quien, String que) {
            this.cuando = cuando;
            this.quien = quien;
            this.que = que;
        }

        public LocalDateTime getCuando() { return cuando; }
        public String getQuien() { return quien; }
        public String getQue() { return que; }
    }

    // De PROVEM
    private final String clave;
    private final String compania;
    private final String unidad;
    private final String producto;
    private final String loteUnidad;
    private final String loteMetMex;
    private final LocalDate fechaCierre;
    private final BigDecimal pesoSeco;
    private final String codigoSobre;
    private final Map<String, BigDecimal> ponderadoMina;

    // De Met-Mex, si comparte la interfaz (RF-007); si no, la captura ERSA
    private LocalDate fechaEntregaMetMex;
    private boolean entregaPorInterfaz;

    // De ERSA
    private TipoMuestra tipoMuestra = TipoMuestra.ORIGINAL;
    private LocalDate fechaRecepcion;
    private LocalDate fechaEstimada;
    private LocalDateTime fechaEntregaResultados;
    private final Map<String, Captura> ensayes = new LinkedHashMap<>();
    private EstadoCaptura estado = EstadoCaptura.PENDIENTE;
    private String reviso;
    private String autorizo;
    private String observacionErsa;

    // De Fresnillo
    private EstadoRevision revision;
    private String observacionFresnillo;
    private boolean devuelto;
    private final List<Evento> historia = new ArrayList<>();

    public Lote(String clave, String compania, String unidad, String producto, String loteUnidad, String loteMetMex,
                LocalDate fechaCierre, BigDecimal pesoSeco, String codigoSobre, LocalDate fechaEntregaMetMex,
                Map<String, BigDecimal> ponderadoMina) {
        this.clave = clave;
        this.compania = compania;
        this.unidad = unidad;
        this.producto = producto;
        this.loteUnidad = loteUnidad;
        this.loteMetMex = loteMetMex;
        this.fechaCierre = fechaCierre;
        this.pesoSeco = pesoSeco;
        this.codigoSobre = codigoSobre;
        this.fechaEntregaMetMex = fechaEntregaMetMex;
        this.entregaPorInterfaz = fechaEntregaMetMex != null;
        this.ponderadoMina = ponderadoMina;
    }

    /** Quiénes capturaron, en orden, sin repetir (RF-039). */
    public String getCapturaron() {
        return String.join(", ", ensayes.values().stream().map(Captura::getUsuario).distinct().toList());
    }

    public boolean isAutorizadoPorErsa() {
        return estado == EstadoCaptura.AUTORIZADO;
    }

    void evento(String quien, String que) {
        historia.add(new Evento(LocalDateTime.now().withNano(0), quien, que));
    }

    public String getClave() { return clave; }
    public String getCompania() { return compania; }
    public String getUnidad() { return unidad; }
    public String getProducto() { return producto; }
    public String getLoteUnidad() { return loteUnidad; }
    public String getLoteMetMex() { return loteMetMex; }
    public LocalDate getFechaCierre() { return fechaCierre; }
    public BigDecimal getPesoSeco() { return pesoSeco; }
    String getCodigoSobre() { return codigoSobre; }
    Map<String, BigDecimal> getPonderadoMina() { return ponderadoMina; }
    public LocalDate getFechaEntregaMetMex() { return fechaEntregaMetMex; }
    void setFechaEntregaMetMex(LocalDate f) { this.fechaEntregaMetMex = f; }
    /** Met-Mex no compartió la fecha por la interfaz: la escribe ERSA (RF-007). */
    void sinInterfaz() { this.entregaPorInterfaz = false; this.fechaEntregaMetMex = null; }
    public boolean isEntregaPorInterfaz() { return entregaPorInterfaz; }
    public TipoMuestra getTipoMuestra() { return tipoMuestra; }
    void setTipoMuestra(TipoMuestra t) { this.tipoMuestra = t; }
    public LocalDate getFechaRecepcion() { return fechaRecepcion; }
    void setFechaRecepcion(LocalDate f) { this.fechaRecepcion = f; }
    public LocalDate getFechaEstimada() { return fechaEstimada; }
    void setFechaEstimada(LocalDate f) { this.fechaEstimada = f; }
    public LocalDateTime getFechaEntregaResultados() { return fechaEntregaResultados; }
    void setFechaEntregaResultados(LocalDateTime f) { this.fechaEntregaResultados = f; }
    public Map<String, Captura> getEnsayes() { return Collections.unmodifiableMap(ensayes); }
    Map<String, Captura> ensayesEditables() { return ensayes; }
    public EstadoCaptura getEstado() { return estado; }
    void setEstado(EstadoCaptura e) { this.estado = e; }
    public String getReviso() { return reviso; }
    void setReviso(String r) { this.reviso = r; }
    public String getAutorizo() { return autorizo; }
    void setAutorizo(String a) { this.autorizo = a; }
    public String getObservacionErsa() { return observacionErsa; }
    void setObservacionErsa(String o) { this.observacionErsa = o; }
    public EstadoRevision getRevision() { return revision; }
    void setRevision(EstadoRevision r) { this.revision = r; }
    public String getObservacionFresnillo() { return observacionFresnillo; }
    void setObservacionFresnillo(String o) { this.observacionFresnillo = o; }
    public boolean isDevuelto() { return devuelto; }
    void setDevuelto(boolean d) { this.devuelto = d; }
    public List<Evento> getHistoria() { return Collections.unmodifiableList(historia); }
}
