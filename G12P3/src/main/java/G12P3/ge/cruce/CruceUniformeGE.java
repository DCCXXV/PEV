package G12P3.ge.cruce;

import G12P3.ag.Cromosoma;
import G12P3.ag.cruce.Cruce;
import G12P3.ge.CromosomaGE;
import java.util.Random;

// cruce uniforme: cada codon se intercambia con prob 0.5
public class CruceUniformeGE implements Cruce {

    private static final double PROB_INTERCAMBIO = 0.5;
    private final Random rnd;

    public CruceUniformeGE(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaGE p1 = (CromosomaGE) padre1;
        CromosomaGE p2 = (CromosomaGE) padre2;
        int[] g1 = p1.codones;
        int[] g2 = p2.codones;
        int len = Math.min(g1.length, g2.length);

        for (int i = 0; i < len; i++) {
            if (rnd.nextDouble() < PROB_INTERCAMBIO) {
                int t = g1[i];
                g1[i] = g2[i];
                g2[i] = t;
            }
        }
        p1.redecodificar();
        p2.redecodificar();
    }
}
