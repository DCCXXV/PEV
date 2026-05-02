package G12P3.ge.mutacion;

import G12P3.ag.Cromosoma;
import G12P3.ag.mutacion.Mutacion;
import G12P3.ge.CromosomaGE;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// mutacion scramble (P2): se eligen dos puntos i,j y se barajan los
// codones del segmento [i,j]. mas exploratorio que la inversion porque
// aplica una permutacion aleatoria en lugar de invertir el orden
public class MutacionScrambleGE implements Mutacion {

    private final Random rnd;

    public MutacionScrambleGE(Random rnd) {
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
        List<Integer> segmento = new ArrayList<>();
        for (int k = i; k <= j; k++) segmento.add(g[k]);
        Collections.shuffle(segmento, rnd);
        for (int k = i; k <= j; k++) g[k] = segmento.get(k - i);

        c.redecodificar();
    }
}
