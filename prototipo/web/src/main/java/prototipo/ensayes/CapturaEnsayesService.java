package prototipo.ensayes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Lo que ERSA hace con los lotes (HU-001, HU-002, HU-003 y su parte de HU-007). En el prototipo lo implementa
 * {@link EnsayesSimulado}; al construirse, la ficha escribe la implementación real y la vista no cambia.
 * El usuario no se pasa: lo sabe el servicio (hoy por la sesión simulada; en el sistema, por la seguridad).
 */
public interface CapturaEnsayesService {

    List<String> companias();

    List<String> unidades(String compania);

    List<String> productos();

    /**
     * Los lotes que le tocan al rol actual: capturista, los pendientes, guardados y devueltos; revisor, los enviados;
     * autorizador, los revisados (RF-049). Con {@code todos}, la consulta de todo lo reportado.
     */
    List<Lote> bandeja(Filtro filtro, boolean todos);

    /** Localiza el lote por el código de barras del sobre (RF-044); ReglaException si no existe o no le toca. */
    Lote porCodigo(String codigo);

    /** Los elementos que el extracto vigente pide para el lote, en su orden y sin condición (RF-014, RF-048). */
    List<Elemento> elementos(Lote lote);

    /** Guarda lo capturado; solo registra autor y hora de lo que cambió (RF-039, RF-040). */
    void guardar(String lote, Map<String, BigDecimal> valores, Lote.TipoMuestra tipo, LocalDate entregaMetMex,
                 LocalDate recepcion, LocalDate estimada);

    /** Envía a revisión; rechaza si falta algún elemento (RF-040) o si ya se envió (RF-042). */
    void enviar(String lote);

    void marcarRevisado(String lote);

    /** Revisión o autorización regresan el lote a captura, con observación opcional (RF-041). */
    void regresarEnErsa(String lote, String observacion);

    /** Autoriza: los ensayes pasan a Fresnillo y se fija la fecha de entrega de resultados (RF-046). */
    void autorizar(String lote);

    /** Lote devuelto por Fresnillo: confirma sin cambios (RF-054, RF-055). */
    void confirmar(String lote);

    /** Lote devuelto por Fresnillo: corrige y lo reenvía al flujo (RF-054). */
    void actualizarYEnviar(String lote, Map<String, BigDecimal> valores);
}
