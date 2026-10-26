package prototipo.ensayes;

import prototipo.comun.BuzonDemo;
import prototipo.comun.DatosReiniciados;
import prototipo.comun.ReglaException;
import prototipo.comun.SesionDemo;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Implementación simulada de la captura de ERSA y de la revisión de Fresnillo: datos en memoria, reglas del negocio de
 * verdad. Las reglas salen de los requisitos (RF-036 a RF-057); donde un requisito no alcanza, el supuesto está anotado.
 * Datos de ejemplo con la forma de los del cliente (lotes Met-Mex P2026…/Z2026…, unidades y productos reales del
 * proyecto); los valores son ilustrativos.
 */
@ApplicationScoped
public class EnsayesSimulado implements CapturaEnsayesService, RevisionEnsayesService {

    static final String PLOMO = "Concentrado de plomo";
    static final String ZINC = "Concentrado de zinc";
    private static final String COMPANIA = "Fresnillo plc";
    private static final List<String> UNIDADES = List.of("Ciénega", "Fresnillo", "Juanicipio", "Saucito");
    private static final int MAX_OBSERVACION = 1000;

    /** El extracto vigente, simulado: elementos, condición y límite de partición por producto (RF-013, RF-014). */
    private static final Map<String, List<Elemento>> EXTRACTO = Map.of(
            PLOMO, List.of(
                    el("AU", "Au", "g/t", 2, Elemento.Condicion.PAGABLE, "0.30"),
                    el("AU_AJ", "Au ajustado", "g/t", 2, Elemento.Condicion.PAGABLE, "0.30"),
                    el("AG", "Ag", "g/t", 0, Elemento.Condicion.PAGABLE, "40"),
                    el("AG_AJ", "Ag ajustada", "g/t", 0, Elemento.Condicion.PAGABLE, "40"),
                    el("PB", "Pb", "%", 2, Elemento.Condicion.PAGABLE, "0.50"),
                    el("ZN", "Zn", "%", 2, Elemento.Condicion.CASTIGABLE, "0.40"),
                    el("CU", "Cu", "%", 2, Elemento.Condicion.CASTIGABLE, "0.15"),
                    el("AS", "As", "%", 3, Elemento.Condicion.CASTIGABLE, "0.050"),
                    el("SB", "Sb", "%", 3, Elemento.Condicion.CASTIGABLE, "0.050"),
                    el("CD", "Cd", "%", 3, Elemento.Condicion.CASTIGABLE, "0.010"),
                    el("FE", "Fe", "%", 2, Elemento.Condicion.CASTIGABLE, "0.30")),
            ZINC, List.of(
                    el("ZN", "Zn", "%", 2, Elemento.Condicion.PAGABLE, "0.50"),
                    el("AU", "Au", "g/t", 2, Elemento.Condicion.PAGABLE, "0.30"),
                    el("AU_AJ", "Au ajustado", "g/t", 2, Elemento.Condicion.PAGABLE, "0.30"),
                    el("AG", "Ag", "g/t", 0, Elemento.Condicion.PAGABLE, "40"),
                    el("AG_AJ", "Ag ajustada", "g/t", 0, Elemento.Condicion.PAGABLE, "40"),
                    el("PB", "Pb", "%", 2, Elemento.Condicion.CASTIGABLE, "0.30"),
                    el("CU", "Cu", "%", 2, Elemento.Condicion.CASTIGABLE, "0.15"),
                    el("FE", "Fe", "%", 2, Elemento.Condicion.CASTIGABLE, "0.30"),
                    el("AS", "As", "%", 3, Elemento.Condicion.CASTIGABLE, "0.050"),
                    el("CD", "Cd", "%", 3, Elemento.Condicion.CASTIGABLE, "0.020"),
                    el("INS", "Insoluble", "%", 2, Elemento.Condicion.CASTIGABLE, "0.30"),
                    el("MGO", "MgO", "%", 3, Elemento.Condicion.CASTIGABLE, "0.050")));

    /** Valores típicos del ponderado de la mina, por producto: solo para que los ejemplos se vean creíbles. */
    private static final Map<String, Map<String, Double>> TIPICO = Map.of(
            PLOMO, Map.ofEntries(Map.entry("AU", 4.8), Map.entry("AU_AJ", 4.9), Map.entry("AG", 5320.0),
                    Map.entry("AG_AJ", 5410.0), Map.entry("PB", 52.4), Map.entry("ZN", 6.1), Map.entry("CU", 1.12),
                    Map.entry("AS", 0.214), Map.entry("SB", 0.187), Map.entry("CD", 0.042), Map.entry("FE", 8.65)),
            ZINC, Map.ofEntries(Map.entry("ZN", 51.8), Map.entry("AU", 1.35), Map.entry("AU_AJ", 1.38),
                    Map.entry("AG", 412.0), Map.entry("AG_AJ", 419.0), Map.entry("PB", 1.84), Map.entry("CU", 0.62),
                    Map.entry("FE", 7.95), Map.entry("AS", 0.118), Map.entry("CD", 0.391), Map.entry("INS", 2.15),
                    Map.entry("MGO", 0.084)));

    @Inject
    private SesionDemo sesion;

    @Inject
    private BuzonDemo buzon;

    private final List<Lote> lotes = new CopyOnWriteArrayList<>();

    private static Elemento el(String clave, String etiqueta, String unidad, int dec, Elemento.Condicion c, String lp) {
        return new Elemento(clave, etiqueta, unidad, dec, c, new BigDecimal(lp));
    }

    void reiniciar(@Observes DatosReiniciados evento) {
        sembrar();
    }

    // ── Datos de ejemplo ─────────────────────────────────────────────────────

    @PostConstruct
    void sembrar() {
        lotes.clear();
        // Autorizados por ERSA, en la revisión de Fresnillo
        Lote l1 = lote("Saucito", PLOMO, "152", "P20260943", 2, 314.208, 1);
        reportado(l1, Map.of("AG", 61.0), Lote.EstadoRevision.POR_REVISAR);
        Lote l2 = lote("Saucito", PLOMO, "153", "P20260944", 2, 298.551, 2);
        reportado(l2, Map.of(), Lote.EstadoRevision.POR_REVISAR);
        Lote l3 = lote("Fresnillo", PLOMO, "201", "P20260951", 4, 402.117, 3);
        reportado(l3, Map.of("AU", 0.52, "AU_AJ", 0.49), Lote.EstadoRevision.POR_REVISAR);
        Lote l4 = lote("Ciénega", PLOMO, "41", "P20260958", 5, 186.940, 4);
        reportado(l4, Map.of("AG", 118.0, "AG_AJ", 121.0), Lote.EstadoRevision.REGRESADO);
        l4.setDevuelto(true);
        l4.setObservacionFresnillo("Favor de revisar la plata normal y ajustada: está muy por arriba del ponderado de la mina.");
        l4.evento("Fresnillo · revisa ensayes", "Regresó el lote a ERSA con observación");
        Lote l5 = lote("Juanicipio", ZINC, "88-M", "Z20260554", 6, 352.776, 5);
        reportado(l5, Map.of(), Lote.EstadoRevision.CONFIRMADO);
        l5.setObservacionFresnillo("Confirmar el insoluble.");
        l5.evento("Fresnillo · revisa ensayes", "Regresó el lote a ERSA con observación");
        l5.evento("ERSA · capturista 2", "Confirmó los valores sin cambios");
        Lote l6 = lote("Juanicipio", ZINC, "89-M", "Z20260561", 6, 349.105, 6);
        reportado(l6, Map.of(), Lote.EstadoRevision.AUTORIZADO);
        l6.evento("Fresnillo · revisa ensayes", "Autorizó los ensayes de ERSA");
        // En el flujo de ERSA
        Lote l7 = lote("Fresnillo", ZINC, "117-M", "Z20260574", 8, 276.430, 7);
        capturado(l7, true);
        l7.setEstado(Lote.EstadoCaptura.REVISADO);
        l7.setReviso("ERSA · revisor");
        l7.evento("ERSA · revisor", "Marcó el lote como revisado");
        Lote l8 = lote("Saucito", ZINC, "64-M", "Z20260575", 9, 301.882, 8);
        capturado(l8, true);
        l8.setEstado(Lote.EstadoCaptura.ENVIADO);
        Lote l9 = lote("Fresnillo", PLOMO, "202", "P20260963", 10, 395.264, 9);
        capturado(l9, false);
        // Pendientes de capturar; el de Ciénega 42 sin fecha de Met-Mex (no llegó por la interfaz)
        lote("Saucito", PLOMO, "154", "P20260970", 12, 307.415, 10);
        Lote sinInterfaz = lote("Ciénega", PLOMO, "42", "P20260974", 13, 191.208, 11);
        sinInterfaz.sinInterfaz();
        lote("Juanicipio", PLOMO, "77", "P20260979", 14, 288.903, 12);
        lote("Fresnillo", ZINC, "118-M", "Z20260587", 15, 268.117, 13);
        lote("Ciénega", ZINC, "30", "Z20260590", 16, 142.560, 14);
        lote("Saucito", ZINC, "65-M", "Z20260593", 17, 310.004, 15);
        lote("Juanicipio", ZINC, "90-M", "Z20260598", 18, 341.772, 16);
    }

    private Lote lote(String unidad, String producto, String loteUnidad, String loteMetMex, int diaCierre,
                      double peso, int semilla) {
        LocalDate cierre = LocalDate.of(2026, 10, diaCierre);
        Map<String, BigDecimal> mina = new LinkedHashMap<>();
        for (Elemento e : EXTRACTO.get(producto)) {
            double base = TIPICO.get(producto).get(e.getClave());
            double variacion = 1 + ((semilla * 37 + e.getClave().length() * 11) % 13 - 6) / 100.0;
            mina.put(e.getClave(), BigDecimal.valueOf(base * variacion).setScale(e.getDecimales(), RoundingMode.HALF_UP));
        }
        Lote l = new Lote(loteMetMex, COMPANIA, unidad, producto, loteUnidad, loteMetMex, cierre,
                BigDecimal.valueOf(peso).setScale(3, RoundingMode.HALF_UP), "LF" + loteMetMex.substring(1),
                cierre.plusDays(1), mina);
        lotes.add(l);
        return l;
    }

    /** Ensayes de ERSA cercanos al ponderado de la mina; con {@code completo} = false, solo oro y plata. */
    private void capturado(Lote l, boolean completo) {
        LocalDateTime hora = l.getFechaCierre().plusDays(4).atTime(10, 30);
        int i = 0;
        for (Elemento e : EXTRACTO.get(l.getProducto())) {
            if (!completo && i >= 4) {
                break;
            }
            BigDecimal mina = l.getPonderadoMina().get(e.getClave());
            BigDecimal ajuste = e.getLimiteParticion().multiply(BigDecimal.valueOf(((i * 7) % 5 - 2) / 10.0));
            String quien = i < 6 ? "ERSA · capturista 1" : "ERSA · capturista 2";
            l.ensayesEditables().put(e.getClave(), new Lote.Captura(
                    mina.add(ajuste).setScale(e.getDecimales(), RoundingMode.HALF_UP), quien, hora.plusMinutes(i * 3L)));
            i++;
        }
        l.setFechaRecepcion(l.getFechaCierre().plusDays(2));
        l.setFechaEstimada(l.getFechaCierre().plusDays(7));
        l.setEstado(Lote.EstadoCaptura.GUARDADO);
        l.evento("ERSA · capturista 1", completo ? "Capturó los ensayes" : "Guardó una captura parcial");
        if (completo) {
            l.setEstado(Lote.EstadoCaptura.ENVIADO);
            l.evento("ERSA · capturista 2", "Envió el lote a revisión");
        }
    }

    /** Lote ya autorizado por ERSA; {@code fuera} lleva las diferencias que se quieren ver por encima del límite. */
    private void reportado(Lote l, Map<String, Double> fuera, Lote.EstadoRevision revision) {
        capturado(l, true);
        for (Map.Entry<String, Double> f : fuera.entrySet()) {
            Lote.Captura c = l.getEnsayes().get(f.getKey());
            BigDecimal mina = l.getPonderadoMina().get(f.getKey());
            l.ensayesEditables().put(f.getKey(), new Lote.Captura(
                    mina.add(BigDecimal.valueOf(f.getValue())).setScale(mina.scale(), RoundingMode.HALF_UP),
                    c.getUsuario(), c.getCuando()));
        }
        l.setReviso("ERSA · revisor");
        l.evento("ERSA · revisor", "Marcó el lote como revisado");
        l.setAutorizo("ERSA · autorizador");
        l.setEstado(Lote.EstadoCaptura.AUTORIZADO);
        l.setFechaEntregaResultados(l.getFechaCierre().plusDays(6).atTime(17, 5));
        l.evento("ERSA · autorizador", "Autorizó: los ensayes pasaron a Fresnillo");
        l.setRevision(revision);
    }

    // ── Catálogos ────────────────────────────────────────────────────────────

    @Override
    public List<String> companias() {
        return List.of(COMPANIA);
    }

    @Override
    public List<String> unidades(String compania) {
        return compania == null || COMPANIA.equals(compania) ? UNIDADES : List.of();
    }

    @Override
    public List<String> productos() {
        return List.of(PLOMO, ZINC);
    }

    // ── Captura de ERSA ──────────────────────────────────────────────────────

    @Override
    public List<Lote> bandeja(Filtro filtro, boolean todos) {
        exigir(sesion.esDe("ERSA"), "Esta tarea es de ERSA.");
        return lotes.stream()
                .filter(l -> coincide(l, filtro))
                .filter(l -> todos || leToca(l))
                .sorted(Comparator.comparing(Lote::getFechaCierre).thenComparing(Lote::getLoteMetMex))
                .collect(Collectors.toList());
    }

    private boolean leToca(Lote l) {
        if (sesion.esDe("ERSA · capturista")) {
            return l.isDevuelto() || l.getEstado() == Lote.EstadoCaptura.PENDIENTE
                    || l.getEstado() == Lote.EstadoCaptura.GUARDADO;
        }
        if (sesion.es("ERSA · revisor")) {
            return l.getEstado() == Lote.EstadoCaptura.ENVIADO;
        }
        return sesion.es("ERSA · autorizador") && l.getEstado() == Lote.EstadoCaptura.REVISADO;
    }

    @Override
    public Lote porCodigo(String codigo) {
        exigir(sesion.esDe("ERSA"), "Esta tarea es de ERSA.");
        String c = codigo == null ? "" : codigo.trim();
        Lote l = lotes.stream().filter(x -> x.getCodigoSobre().equalsIgnoreCase(c)
                        || x.getLoteMetMex().equalsIgnoreCase(c)).findFirst()
                .orElseThrow(() -> new ReglaException("CODIGO_DESCONOCIDO",
                        "No hay ningún lote con el código «" + c + "»."));
        if (!leToca(l)) {
            throw new ReglaException("LOTE_NO_PENDIENTE",
                    "El lote " + l.getLoteMetMex() + " ya no está pendiente para tu rol (" + l.getEstado().getTexto() + ").");
        }
        return l;
    }

    @Override
    public List<Elemento> elementos(Lote lote) {
        return EXTRACTO.get(lote.getProducto()).stream().map(Elemento::paraErsa).collect(Collectors.toList());
    }

    @Override
    public void guardar(String clave, Map<String, BigDecimal> valores, Lote.TipoMuestra tipo, LocalDate entregaMetMex,
                        LocalDate recepcion, LocalDate estimada) {
        exigirCapturista();
        Lote l = buscar(clave);
        if (l.isDevuelto()) {
            throw new ReglaException("LOTE_DEVUELTO",
                    "Este lote lo regresó Fresnillo: confírmalo o corrígelo y vuelve a enviarlo.");
        }
        exigirEditable(l);
        aplicar(l, valores);
        l.setTipoMuestra(tipo == null ? Lote.TipoMuestra.ORIGINAL : tipo);
        if (!l.isEntregaPorInterfaz()) {
            l.setFechaEntregaMetMex(entregaMetMex);
        }
        l.setFechaRecepcion(recepcion);
        l.setFechaEstimada(estimada);
        l.setEstado(Lote.EstadoCaptura.GUARDADO);
        l.evento(sesion.getRol(), "Guardó la captura");
    }

    @Override
    public void enviar(String clave) {
        exigirCapturista();
        Lote l = buscar(clave);
        exigirEditable(l);
        exigirCompleto(l);
        l.setEstado(Lote.EstadoCaptura.ENVIADO);
        l.setObservacionErsa(null);
        l.evento(sesion.getRol(), "Envió el lote a revisión");
    }

    @Override
    public void marcarRevisado(String clave) {
        exigir(sesion.es("ERSA · revisor"), "Solo el revisor de ERSA marca un lote como revisado.");
        Lote l = buscar(clave);
        exigirEstado(l, Lote.EstadoCaptura.ENVIADO);
        l.setReviso(sesion.getRol());
        l.setEstado(Lote.EstadoCaptura.REVISADO);
        l.evento(sesion.getRol(), "Marcó el lote como revisado");
    }

    @Override
    public void regresarEnErsa(String clave, String observacion) {
        Lote l = buscar(clave);
        boolean revisor = sesion.es("ERSA · revisor") && l.getEstado() == Lote.EstadoCaptura.ENVIADO;
        boolean autorizador = sesion.es("ERSA · autorizador") && l.getEstado() == Lote.EstadoCaptura.REVISADO;
        exigir(revisor || autorizador, "Solo el revisor o el autorizador de ERSA regresan un lote, y solo el que les toca.");
        String obs = recortar(observacion);
        l.setObservacionErsa(obs.isEmpty() ? null : obs);
        l.setEstado(Lote.EstadoCaptura.GUARDADO);
        l.evento(sesion.getRol(), "Regresó el lote a captura" + (obs.isEmpty() ? "" : " con observación"));
    }

    @Override
    public void autorizar(String clave) {
        exigir(sesion.es("ERSA · autorizador"), "Solo el autorizador de ERSA autoriza un lote.");
        Lote l = buscar(clave);
        exigirEstado(l, Lote.EstadoCaptura.REVISADO);
        l.setAutorizo(sesion.getRol());
        l.setEstado(Lote.EstadoCaptura.AUTORIZADO);
        l.setFechaEntregaResultados(LocalDateTime.now().withNano(0));
        l.evento(sesion.getRol(), "Autorizó: los ensayes pasaron a Fresnillo");
        if (l.getRevision() == Lote.EstadoRevision.REGRESADO) {
            l.setRevision(Lote.EstadoRevision.ACTUALIZADO);
            buzon.enviar("Usuarios de Fresnillo PLC", "Lote " + l.getLoteMetMex() + " actualizado por ERSA",
                    "ERSA corrigió y volvió a autorizar el lote " + l.getLoteMetMex() + " (" + l.getUnidad() + ", "
                            + l.getProducto() + ").");
        } else {
            l.setRevision(Lote.EstadoRevision.POR_REVISAR);
        }
    }

    @Override
    public void confirmar(String clave) {
        exigirCapturista();
        Lote l = buscar(clave);
        exigir(l.isDevuelto(), "Solo se confirma un lote que Fresnillo regresó.");
        l.setDevuelto(false);
        l.setRevision(Lote.EstadoRevision.CONFIRMADO);
        l.evento(sesion.getRol(), "Confirmó los valores sin cambios");
        buzon.enviar("Usuarios de Fresnillo PLC", "Lote " + l.getLoteMetMex() + " confirmado por ERSA",
                "ERSA confirmó sin cambios los valores del lote " + l.getLoteMetMex() + " (" + l.getUnidad() + ", "
                        + l.getProducto() + "). Confirmó: " + sesion.getRol() + ".");
    }

    @Override
    public void actualizarYEnviar(String clave, Map<String, BigDecimal> valores) {
        exigirCapturista();
        Lote l = buscar(clave);
        exigir(l.isDevuelto(), "Solo se corrige así un lote que Fresnillo regresó.");
        aplicar(l, valores);
        exigirCompleto(l);
        l.setDevuelto(false);
        l.setEstado(Lote.EstadoCaptura.ENVIADO);
        l.evento(sesion.getRol(), "Corrigió los valores y reenvió el lote a revisión");
    }

    /** Registra autor y hora solo de lo que cambió (RF-039); un campo vaciado se quita. */
    private void aplicar(Lote l, Map<String, BigDecimal> valores) {
        Map<String, Lote.Captura> ensayes = l.ensayesEditables();
        LocalDateTime ahora = LocalDateTime.now().withNano(0);
        for (Elemento e : EXTRACTO.get(l.getProducto())) {
            BigDecimal nuevo = valores.get(e.getClave());
            Lote.Captura actual = ensayes.get(e.getClave());
            if (nuevo == null) {
                ensayes.remove(e.getClave());
            } else if (actual == null || actual.getValor().compareTo(nuevo) != 0) {
                ensayes.put(e.getClave(), new Lote.Captura(
                        nuevo.setScale(e.getDecimales(), RoundingMode.HALF_UP), sesion.getRol(), ahora));
            }
        }
    }

    private void exigirCompleto(Lote l) {
        List<String> faltan = EXTRACTO.get(l.getProducto()).stream()
                .filter(e -> !l.getEnsayes().containsKey(e.getClave()))
                .map(Elemento::getEtiqueta).collect(Collectors.toList());
        if (!faltan.isEmpty()) {
            throw new ReglaException("ELEMENTOS_FALTANTES",
                    "No se puede enviar: faltan por capturar " + String.join(", ", faltan) + ".");
        }
    }

    private void exigirCapturista() {
        exigir(sesion.esDe("ERSA · capturista"), "Solo un capturista de ERSA captura ensayes.");
    }

    private static void exigirEditable(Lote l) {
        if (l.getEstado() != Lote.EstadoCaptura.PENDIENTE && l.getEstado() != Lote.EstadoCaptura.GUARDADO) {
            throw new ReglaException("LOTE_ENVIADO",
                    "El lote ya se envió (" + l.getEstado().getTexto().toLowerCase() + "): solo se modifica si te lo regresan.");
        }
    }

    private static void exigirEstado(Lote l, Lote.EstadoCaptura esperado) {
        if (l.getEstado() != esperado) {
            throw new ReglaException("ESTADO_INVALIDO",
                    "El lote está " + l.getEstado().getTexto().toLowerCase() + "; esta acción es para lotes "
                            + esperado.getTexto().toLowerCase() + "s.");
        }
    }

    // ── Revisión de Fresnillo ────────────────────────────────────────────────

    @Override
    public List<Lote> consulta(Filtro filtro) {
        exigirFresnillo();
        return lotes.stream().filter(l -> coincide(l, filtro))
                .sorted(Comparator.comparing(Lote::getFechaCierre).thenComparing(Lote::getLoteMetMex))
                .collect(Collectors.toList());
    }

    @Override
    public List<FilaAnalisis> analisis(String clave) {
        exigirFresnillo();
        Lote l = buscar(clave);
        if (!l.isAutorizadoPorErsa()) {
            throw new ReglaException("SIN_AUTORIZAR",
                    "ERSA todavía no autoriza el lote " + l.getLoteMetMex() + ": sus valores no se ven hasta entonces.");
        }
        List<FilaAnalisis> filas = new ArrayList<>();
        for (Elemento e : EXTRACTO.get(l.getProducto())) {
            Lote.Captura c = l.getEnsayes().get(e.getClave());
            filas.add(new FilaAnalisis(e, c == null ? null : c.getValor(), l.getPonderadoMina().get(e.getClave())));
        }
        return filas;
    }

    @Override
    public long fueraDeLimite(String clave) {
        Lote l = buscar(clave);
        return l.isAutorizadoPorErsa() ? analisis(clave).stream().filter(FilaAnalisis::isFueraDeLimite).count() : 0;
    }

    @Override
    public void regresar(List<String> claves, String observacion) {
        exigirFresnillo();
        if (claves == null || claves.isEmpty()) {
            throw new ReglaException("SIN_LOTES", "Selecciona al menos un lote.");
        }
        String obs = recortar(observacion);
        // Supuesto: la observación es obligatoria al regresar a ERSA, porque es lo que el correo le explica (P-056)
        if (obs.isEmpty()) {
            throw new ReglaException("OBSERVACION_REQUERIDA", "Escribe qué tiene que revisar ERSA.");
        }
        if (obs.length() > MAX_OBSERVACION) {
            throw new ReglaException("OBSERVACION_LARGA",
                    "La observación no puede pasar de " + MAX_OBSERVACION + " caracteres (lleva " + obs.length() + ").");
        }
        List<Lote> seleccion = claves.stream().map(this::buscar).collect(Collectors.toList());
        for (Lote l : seleccion) {
            exigirRevisable(l);
        }
        for (Lote l : seleccion) {
            l.setDevuelto(true);
            l.setRevision(Lote.EstadoRevision.REGRESADO);
            l.setObservacionFresnillo(obs);
            l.evento(sesion.getRol(), "Regresó el lote a ERSA con observación");
        }
        Lote primero = seleccion.get(0);
        buzon.enviar("Capturistas de ERSA",
                "Lotes por revisar: " + seleccion.stream().map(Lote::getLoteMetMex).collect(Collectors.joining(", ")),
                "Fresnillo PLC regresó para revisión los lotes "
                        + seleccion.stream().map(l -> l.getLoteMetMex() + " (" + l.getUnidad() + ", " + l.getProducto() + ")")
                        .collect(Collectors.joining("; "))
                        + ". Compañía: " + primero.getCompania() + ". Observaciones: " + obs);
    }

    @Override
    public void autorizar(List<String> claves) {
        exigirFresnillo();
        if (claves == null || claves.isEmpty()) {
            throw new ReglaException("SIN_LOTES", "Selecciona al menos un lote.");
        }
        List<Lote> seleccion = claves.stream().map(this::buscar).collect(Collectors.toList());
        for (Lote l : seleccion) {
            exigirRevisable(l);
        }
        for (Lote l : seleccion) {
            l.setRevision(Lote.EstadoRevision.AUTORIZADO);
            l.evento(sesion.getRol(), "Autorizó los ensayes de ERSA");
        }
    }

    private static void exigirRevisable(Lote l) {
        if (!l.isAutorizadoPorErsa()) {
            throw new ReglaException("SIN_AUTORIZAR", "El lote " + l.getLoteMetMex() + " todavía está en ERSA.");
        }
        if (l.isDevuelto()) {
            throw new ReglaException("YA_REGRESADO", "El lote " + l.getLoteMetMex() + " ya está regresado a ERSA.");
        }
        if (l.getRevision() == Lote.EstadoRevision.AUTORIZADO) {
            throw new ReglaException("YA_AUTORIZADO", "El lote " + l.getLoteMetMex() + " ya está autorizado.");
        }
    }

    private void exigirFresnillo() {
        exigir(sesion.esDe("Fresnillo"), "Esta tarea es de Fresnillo.");
    }

    // ── Comunes ──────────────────────────────────────────────────────────────

    private Lote buscar(String clave) {
        return lotes.stream().filter(l -> l.getClave().equals(clave)).findFirst()
                .orElseThrow(() -> new ReglaException("LOTE_DESCONOCIDO", "El lote no existe."));
    }

    private static boolean coincide(Lote l, Filtro f) {
        if (f == null) {
            return true;
        }
        LocalDate fecha = f.getTipoBusqueda() == Filtro.TipoBusqueda.RECEPCION ? l.getFechaRecepcion() : l.getFechaCierre();
        boolean conFechas = f.getDesde() != null || f.getHasta() != null;
        return (vacio(f.getCompania()) || Objects.equals(f.getCompania(), l.getCompania()))
                && (vacio(f.getUnidad()) || Objects.equals(f.getUnidad(), l.getUnidad()))
                && (vacio(f.getProducto()) || Objects.equals(f.getProducto(), l.getProducto()))
                && (!conFechas || (fecha != null
                        && (f.getDesde() == null || !fecha.isBefore(f.getDesde()))
                        && (f.getHasta() == null || !fecha.isAfter(f.getHasta()))));
    }

    private static boolean vacio(String s) {
        return s == null || s.isBlank();
    }

    private static String recortar(String s) {
        return s == null ? "" : s.strip();
    }

    private static void exigir(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new ReglaException("SIN_PERMISO", mensaje);
        }
    }
}
