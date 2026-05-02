package G12P3.ge.mutacion;

import G12P3.ag.Cromosoma;
import G12P3.ag.mutacion.Mutacion;
import G12P3.ge.CromosomaGE;
import java.util.Random;

// intercambia dos codones aleatorios
public class MutacionIntercambioGE implements Mutacion {

    private final Random rnd;

    public MutacionIntercambioGE(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public void mutar(Cromosoma cromosoma) {
        CromosomaGE c = (CromosomaGE) cromosoma;
        int[] g = c.codones;
        if (g.length < 2) return;

        int i = rnd.nextInt(g.length);
        int j = rnd.nextInt(g.length);
        int t = g[i];
        g[i] = g[j];
        g[j] = t;
        c.redecodificar();
    }
}
