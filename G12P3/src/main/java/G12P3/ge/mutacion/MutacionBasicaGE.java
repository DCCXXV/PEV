package G12P3.ge.mutacion;

import G12P3.ag.Cromosoma;
import G12P3.ag.mutacion.Mutacion;
import G12P3.ge.CromosomaGE;
import java.util.Random;

// mutacion clasica de cadena de enteros: por cada codon, con probabilidad
// fija de gen, se sustituye por un valor 0..255 aleatorio
public class MutacionBasicaGE implements Mutacion {

    // ~1/L para una cadena de 50 codones: una mutacion esperada por individuo
    private static final double PROB_GEN = 0.02;
    private final Random rnd;

    public MutacionBasicaGE(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public void mutar(Cromosoma cromosoma) {
        CromosomaGE c = (CromosomaGE) cromosoma;
        boolean cambiado = false;
        for (int i = 0; i < c.codones.length; i++) {
            if (rnd.nextDouble() < PROB_GEN) {
                c.codones[i] = rnd.nextInt(256);
                cambiado = true;
            }
        }
        if (cambiado) c.redecodificar();
    }
}
