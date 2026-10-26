package prototipo.comun;

/**
 * Evento CDI: alguien pidió volver los datos de ejemplo a su estado inicial (botón «Reiniciar datos» de la barra).
 * Cada servicio simulado lo observa y vuelve a sembrar lo suyo: void reiniciar(@Observes DatosReiniciados e).
 * Sirve para empezar cada demostración con los mismos datos.
 */
public final class DatosReiniciados {
}
