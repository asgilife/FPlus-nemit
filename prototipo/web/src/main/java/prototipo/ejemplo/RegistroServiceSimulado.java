package prototipo.ejemplo;

import prototipo.comun.DatosReiniciados;
import prototipo.comun.ReglaException;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementación simulada: los datos viven en memoria y vuelven a su estado inicial al redesplegar.
 * Las reglas sí son las del negocio (lo que el cliente va a ver rechazado en el sistema real se rechaza aquí igual).
 * Datos de ejemplo creíbles y sin nombres de personas reales: roles y folios con la forma de los del cliente.
 */
@ApplicationScoped
public class RegistroServiceSimulado implements RegistroService {

    private static final List<String> RESPONSABLES = List.of("Supervisor", "Operador", "Almacén");

    private final List<Registro> registros = new CopyOnWriteArrayList<>();

    /** Botón «Reiniciar datos»: vuelve a los datos de ejemplo iniciales. */
    void reiniciar(@Observes DatosReiniciados evento) {
        sembrar();
    }

    @PostConstruct
    void sembrar() {
        registros.clear();
        LocalDateTime hoy = LocalDateTime.now().withSecond(0).withNano(0);
        registros.add(new Registro("A-0005", hoy.minusMinutes(40), "Supervisor", 12, "", Registro.Estado.PENDIENTE));
        registros.add(new Registro("A-0004", hoy.minusHours(2), "Operador", 30, "Segundo turno", Registro.Estado.EN_CURSO));
        registros.add(new Registro("A-0003", hoy.minusHours(16), "Almacén", 25, "", Registro.Estado.COMPLETADO));
        registros.add(new Registro("A-0002", hoy.minusHours(22), "Supervisor", 8, "", Registro.Estado.COMPLETADO));
        registros.add(new Registro("A-0001", hoy.minusHours(26), "Operador", 4, "Capturado por error", Registro.Estado.CANCELADO));
    }

    @Override
    public List<Registro> buscar(String texto, Registro.Estado estado) {
        String t = texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);
        List<Registro> resultado = new ArrayList<>();
        for (Registro r : registros) {
            boolean coincideTexto = t.isEmpty()
                    || r.getFolio().toLowerCase(Locale.ROOT).contains(t)
                    || r.getResponsable().toLowerCase(Locale.ROOT).contains(t);
            if (coincideTexto && (estado == null || r.getEstado() == estado)) {
                resultado.add(r);
            }
        }
        resultado.sort(Comparator.comparing(Registro::getFecha).reversed());
        return resultado;
    }

    @Override
    public Registro crear(String responsable, Integer cantidad, String observaciones) {
        if (responsable == null || !RESPONSABLES.contains(responsable)) {
            throw new ReglaException("RESPONSABLE_REQUERIDO", "Elige al responsable.");
        }
        if (cantidad == null || cantidad <= 0) {
            throw new ReglaException("CANTIDAD_INVALIDA", "La cantidad debe ser mayor que cero.");
        }
        String folio = String.format("A-%04d", registros.size() + 1);
        Registro nuevo = new Registro(folio, LocalDateTime.now().withSecond(0).withNano(0), responsable, cantidad,
                observaciones == null ? "" : observaciones.trim(), Registro.Estado.PENDIENTE);
        registros.add(nuevo);
        return nuevo;
    }

    @Override
    public void cancelar(String folio) {
        Registro r = registros.stream().filter(x -> x.getFolio().equals(folio)).findFirst()
                .orElseThrow(() -> new ReglaException("REGISTRO_NO_ENCONTRADO", "El registro no existe."));
        if (!r.isCancelable()) {
            throw new ReglaException("REGISTRO_NO_CANCELABLE",
                    "Un registro " + r.getEstado().getTexto().toLowerCase(Locale.ROOT) + " ya no se puede cancelar.");
        }
        r.setEstado(Registro.Estado.CANCELADO);
    }

    @Override
    public List<String> responsables() {
        return RESPONSABLES;
    }
}
