package prototipo.comun;

/**
 * Rechazo de una regla de negocio, con su código y el mensaje que ve el usuario. Lo lanza el servicio —simulado hoy,
 * real mañana— y la vista lo convierte en mensaje: la validación vive en el servicio, nunca solo en la pantalla.
 */
public class ReglaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String codigo;

    public ReglaException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
