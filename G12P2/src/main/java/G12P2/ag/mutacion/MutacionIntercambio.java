package G12P2.ag.mutacion;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import java.util.Random;

// mutación por intercambio
public class MutacionIntercambio implements Mutacion {

    private Random rng = new Random();

    @Override
    public void mutar(Cromosoma cromosoma, double probMutacion) {
        CromosomaDrones c = (CromosomaDrones) cromosoma;
        int[] genes = c.getGenes();

        if (rng.nextDouble() < probMutacion) {
            int i = rng.nextInt(genes.length);
            int j = rng.nextInt(genes.length);

            int temp = genes[i];
            genes[i] = genes[j];
            genes[j] = temp;
        }
    }
}
