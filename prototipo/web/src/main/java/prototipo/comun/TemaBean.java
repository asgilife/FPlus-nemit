package prototipo.comun;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Variante visual de la sesión. Las variantes las declara el tema del proyecto en {@code tema.properties} (su clave,
 * su etiqueta y el tema base de PrimeFaces de cada una); web.xml lee el tema base en {@code primefaces.THEME}. Así el
 * mismo kit sirve con cualquier tema sin tocar código.
 */
@Named("temaBean")
@SessionScoped
public class TemaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Una variante del tema: su clave (valor de data-ime-theme), su etiqueta y su tema base de PrimeFaces. */
    public static final class Variante implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String clave;
        private final String etiqueta;
        private final String primefaces;
        private final String modo;

        Variante(String clave, String etiqueta, String primefaces, String modo) {
            this.clave = clave;
            this.etiqueta = etiqueta;
            this.primefaces = primefaces;
            this.modo = modo;
        }

        public String getClave() { return clave; }
        public String getEtiqueta() { return etiqueta; }
        public String getPrimefaces() { return primefaces; }
        public String getModo() { return modo; }
    }

    private final String nombre;
    private final List<Variante> variantes = new ArrayList<>();
    private String tema;

    public TemaBean() {
        Properties p = new Properties();
        try (InputStream in = TemaBean.class.getResourceAsStream("/tema.properties")) {
            if (in == null) {
                throw new IllegalStateException("Falta tema.properties en src/main/resources: lo trae el tema del proyecto.");
            }
            p.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        nombre = p.getProperty("nombre", "");
        for (String clave : p.getProperty("variantes", "").split(",")) {
            String c = clave.trim();
            if (!c.isEmpty()) {
                String base = p.getProperty("variante." + c + ".primefaces", "saga-blue");
                // Sin declararlo, el modo sale del tema base: los oscuros de PrimeFaces son arya y luna-*
                String modo = p.getProperty("variante." + c + ".modo",
                        base.startsWith("arya") || base.startsWith("luna") ? "oscuro" : "claro");
                variantes.add(new Variante(c, p.getProperty("variante." + c + ".etiqueta", c), base, modo));
            }
        }
        if (variantes.isEmpty()) {
            throw new IllegalStateException("tema.properties no declara ninguna variante.");
        }
        tema = p.getProperty("omision", variantes.get(0).getClave());
    }

    /** Cambia de variante y devuelve null para que la página se recargue completa (el tema base va en el head). */
    public String cambiar(String nueva) {
        if (variantes.stream().anyMatch(v -> v.getClave().equals(nueva))) {
            tema = nueva;
        }
        return null;
    }

    /** Sin prefijo «is»: EL lo tomaría por la propiedad booleana y no resolvería el parámetro. */
    public boolean esActivo(String t) {
        return tema.equals(t);
    }

    public String getPrimefaces() {
        return variantes.stream().filter(v -> v.getClave().equals(tema)).findFirst()
                .map(Variante::getPrimefaces).orElse("saga-blue");
    }

    /** «claro» u «oscuro» (data-ime-modo): lo que la estructura necesita saber, sea cual sea el nombre de la variante. */
    public String getModo() {
        return variantes.stream().filter(v -> v.getClave().equals(tema)).findFirst()
                .map(Variante::getModo).orElse("claro");
    }

    public String getNombre() { return nombre; }
    public List<Variante> getVariantes() { return variantes; }
    public String getTema() { return tema; }
    public void setTema(String tema) { cambiar(tema); }
}
