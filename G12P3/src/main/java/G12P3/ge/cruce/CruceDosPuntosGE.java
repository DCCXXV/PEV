package G12P3.ge.cruce;

import G12P3.ag.Cromosoma;
import G12P3.ag.cruce.Cruce;
import G12P3.ge.CromosomaGE;
import java.util.Random;

// cruce de dos puntos: intercambia el segmento central de los codones
public class CruceDosPuntosGE implements Cruce {

    private final Random rnd;

    public CruceDosPuntosGE(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaGE p1 = (CromosomaGE) padre1;
        CromosomaGE p2 = (CromosomaGE) padre2;
        int[] g1 = p1.codones;
        int[] g2 = p2.codones;
        int len = Math.min(g1.length, g2.length);
        if (len < 3) return;

        int a = rnd.nextInt(len - 1);
        int b = a + 1 + rnd.nextInt(len - a - 1);
        for (int i = a; i < b; i++) {
            int t = g1[i];
            g1[i] = g2[i];
            g2[i] = t;
        }
        p1.redecodificar();
        p2.redecodificar();
    }
}
