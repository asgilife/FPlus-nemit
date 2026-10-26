package prototipo.comun;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Solo para demostrar: los correos que el sistema enviaría quedan aquí en lugar de salir. Las vistas los enseñan en un
 * panel rotulado como control de demostración. No es parte del producto: en el sistema, el servicio real envía.
 */
@Named("buzonDemo")
@ApplicationScoped
public class BuzonDemo {

    /** Un correo que el sistema habría enviado. */
    public static class Correo implements Serializable {
        private static final long serialVersionUID = 1L;
        private final LocalDateTime cuando;
        private final String para;
        private final String asunto;
        private final String cuerpo;

        public Correo(LocalDateTime cuando, String para, String asunto, String cuerpo) {
            this.cuando = cuando;
            this.para = para;
            this.asunto = asunto;
            this.cuerpo = cuerpo;
        }

        public LocalDateTime getCuando() { return cuando; }
        public String getPara() { return para; }
        public String getAsunto() { return asunto; }
        public String getCuerpo() { return cuerpo; }
    }

    private final List<Correo> correos = new CopyOnWriteArrayList<>();

    void reiniciar(@Observes DatosReiniciados evento) {
        correos.clear();
    }

    public void enviar(String para, String asunto, String cuerpo) {
        correos.add(0, new Correo(LocalDateTime.now().withNano(0), para, asunto, cuerpo));
    }

    /** Del más reciente al más antiguo. */
    public List<Correo> getCorreos() {
        return new ArrayList<>(correos);
    }
}
