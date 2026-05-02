package G12P3.ge.mutacion;

import G12P3.ag.Cromosoma;
import G12P3.ag.mutacion.Mutacion;
import G12P3.ge.CromosomaGE;
import java.util.Random;

// mutacion gaussiana (P1, adaptada): a cada codon se le suma ruido gaussiano
// con probabilidad PROB_GEN. permite "saltos pequenos" en el espacio de
// busqueda, util cuando el codon esta cerca de un cambio de regla (c % r)
public class MutacionGaussianaGE implements Mutacion {

    private static final double PROB_GEN = 0.05;
    private static final double SIGMA = 25.0; // ~10% del rango 0..255

    private final Random rnd;

    public MutacionGaussianaGE(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public void mutar(Cromosoma cromosoma) {
        CromosomaGE c = (CromosomaGE) cromosoma;
        boolean cambiado = false;
        for (int i = 0; i < c.codones.length; i++) {
            if (rnd.nextDouble() < PROB_GEN) {
                int nuevo = (int) Math.round(c.codones[i] + rnd.nextGaussian() * SIGMA);
                if (nuevo < 0) nuevo = 0;
                if (nuevo > 255) nuevo = 255;
                c.codones[i] = nuevo;
                cambiado = true;
            }
        }
        if (cambiado) c.redecodificar();
    }
}
