package prototipo.ensayes;

import java.util.List;

/**
 * Lo que Fresnillo hace con lo que ERSA reporta (HU-006 y su parte de HU-007). En el prototipo lo implementa
 * {@link EnsayesSimulado}; la vista solo conoce esta interfaz.
 */
public interface RevisionEnsayesService {

    /** Todos los lotes con su estado y fechas; los valores solo de lo que ERSA ya autorizó (RF-051). */
    List<Lote> consulta(Filtro filtro);

    /** Por elemento: ensaye de ERSA, ponderado de la mina, diferencia y límite (RF-052). */
    List<FilaAnalisis> analisis(String lote);

    /** Cuántos elementos del lote pasan el límite de partición. */
    long fueraDeLimite(String lote);

    /** Regresa los lotes a ERSA con observación (hasta mil caracteres) y avisa por correo (RF-053). */
    void regresar(List<String> lotes, String observacion);

    /** Da por buenos los ensayes de ERSA de esos lotes (RF-052). */
    void autorizar(List<String> lotes);
}
