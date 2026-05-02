package G12P3.ge.mutacion;

import G12P3.ag.Cromosoma;
import G12P3.ag.mutacion.Mutacion;
import G12P3.ge.CromosomaGE;
import java.util.Random;

// invierte el segmento de codones [i, j]
public class MutacionInversionGE implements Mutacion {

    private final Random rnd;

    public MutacionInversionGE(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public void mutar(Cromosoma cromosoma) {
        CromosomaGE c = (CromosomaGE) cromosoma;
        int[] g = c.codones;
        if (g.length < 2) return;

        int i = rnd.nextInt(g.length);
        int j = rnd.nextInt(g.length);
        if (i > j) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        while (i < j) {
            int t = g[i];
            g[i] = g[j];
            g[j] = t;
            i++;
            j--;
        }
        c.redecodificar();
    }
}
