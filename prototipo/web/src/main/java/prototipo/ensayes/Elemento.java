package prototipo.ensayes;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Un elemento que se ensaya, tal como lo define el extracto vigente del lote (RF-014): símbolo, unidad, si es pagable o
 * castigable y su límite de partición. ERSA lo recibe sin condición (RF-048): no debe saber qué se paga ni qué se castiga.
 */
public class Elemento implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Pagable o castigable, como lo marca el extracto. Lista fija: el código decide según el valor. */
    public enum Condicion {
        PAGABLE("Pagable", "P"),
        CASTIGABLE("Castigable", "C");

        private final String texto;
        private final String sigla;

        Condicion(String texto, String sigla) {
            this.texto = texto;
            this.sigla = sigla;
        }

        public String getTexto() { return texto; }
        public String getSigla() { return sigla; }
    }

    private final String clave;
    private final String etiqueta;
    private final String unidad;
    private final int decimales;
    private final Condicion condicion;
    private final BigDecimal limiteParticion;

    public Elemento(String clave, String etiqueta, String unidad, int decimales, Condicion condicion,
                    BigDecimal limiteParticion) {
        this.clave = clave;
        this.etiqueta = etiqueta;
        this.unidad = unidad;
        this.decimales = decimales;
        this.condicion = condicion;
        this.limiteParticion = limiteParticion;
    }

    /** La misma definición sin lo que ERSA no debe ver: condición ni límite de partición. */
    Elemento paraErsa() {
        return new Elemento(clave, etiqueta, unidad, decimales, null, null);
    }

    public String getClave() { return clave; }
    public String getEtiqueta() { return etiqueta; }
    public String getUnidad() { return unidad; }
    public int getDecimales() { return decimales; }
    public Condicion getCondicion() { return condicion; }
    public BigDecimal getLimiteParticion() { return limiteParticion; }
}
