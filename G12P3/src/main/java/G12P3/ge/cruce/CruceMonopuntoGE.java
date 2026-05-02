package G12P3.ge.cruce;

import G12P3.ag.Cromosoma;
import G12P3.ag.cruce.Cruce;
import G12P3.ge.CromosomaGE;
import java.util.Random;

// cruce de un punto sobre el array de codones (clasico para representacion lineal)
public class CruceMonopuntoGE implements Cruce {

    private final Random rnd;

    public CruceMonopuntoGE(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaGE p1 = (CromosomaGE) padre1;
        CromosomaGE p2 = (CromosomaGE) padre2;
        int[] g1 = p1.codones;
        int[] g2 = p2.codones;
        int len = Math.min(g1.length, g2.length);
        if (len <= 1) return;

        int corte = 1 + rnd.nextInt(len - 1);
        for (int i = corte; i < len; i++) {
            int t = g1[i];
            g1[i] = g2[i];
            g2[i] = t;
        }
        p1.redecodificar();
        p2.redecodificar();
    }
}
