package prototipo.ensayes;

import java.io.Serializable;
import java.math.BigDecimal;

/** Un renglón del análisis ERSA contra mina (RF-052): un elemento, sus dos ensayes, la diferencia y el límite. */
public class FilaAnalisis implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Elemento elemento;
    private final BigDecimal ersa;
    private final BigDecimal mina;
    private final BigDecimal diferencia;

    public FilaAnalisis(Elemento elemento, BigDecimal ersa, BigDecimal mina) {
        this.elemento = elemento;
        this.ersa = ersa;
        this.mina = mina;
        this.diferencia = ersa == null || mina == null ? null : ersa.subtract(mina);
    }

    /** Diferencia por encima del límite de partición del elemento: se marca, no obliga a nada (RF-052). */
    public boolean isFueraDeLimite() {
        return diferencia != null && elemento.getLimiteParticion() != null
                && diferencia.abs().compareTo(elemento.getLimiteParticion()) > 0;
    }

    public Elemento getElemento() { return elemento; }
    public BigDecimal getErsa() { return ersa; }
    public BigDecimal getMina() { return mina; }
    public BigDecimal getDiferencia() { return diferencia; }
}
