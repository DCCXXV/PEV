package G12P3.ge.cruce;

import G12P3.ag.Cromosoma;
import G12P3.ag.cruce.Cruce;
import G12P3.ge.CromosomaGE;
import java.util.Random;

public class CruceBlxAlphaGE implements Cruce {

    private static final double ALPHA = 0.3;
    private final Random rnd;

    public CruceBlxAlphaGE(Random rnd) {
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
            int cmin = Math.min(g1[i], g2[i]);
            int cmax = Math.max(g1[i], g2[i]);
            double I = cmax - cmin;
            double inf = cmin - I * ALPHA;
            double sup = cmax + I * ALPHA;

            g1[i] = clipCodon((int) Math.round(inf + rnd.nextDouble() * (sup - inf)));
            g2[i] = clipCodon((int) Math.round(inf + rnd.nextDouble() * (sup - inf)));
        }
        p1.redecodificar();
        p2.redecodificar();
    }

    private int clipCodon(int v) {
        if (v < 0) return 0;
        if (v > 255) return 255;
        return v;
    }
}
