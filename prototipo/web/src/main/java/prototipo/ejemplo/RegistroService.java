package prototipo.ejemplo;

import java.util.List;

/**
 * El contrato que la vista usa. En el prototipo lo implementa {@link RegistroServiceSimulado}, con datos en memoria;
 * cuando la historia se construye de verdad, la ficha escribe la implementación real (servicio → facade → base) y la
 * vista no cambia. Por eso la vista nunca conoce la implementación: solo esta interfaz.
 */
public interface RegistroService {

    /** Registros que coinciden con el texto (folio o responsable) y el estado; null o vacío = sin filtro. */
    List<Registro> buscar(String texto, Registro.Estado estado);

    /** Crea un registro pendiente. Valida aquí, no en la pantalla: lanza ReglaException si algo no cumple. */
    Registro crear(String responsable, Integer cantidad, String observaciones);

    /** Cancela un registro pendiente o en curso; lanza ReglaException si ya no se puede. */
    void cancelar(String folio);

    /** Los responsables que se pueden elegir al crear. */
    List<String> responsables();
}
