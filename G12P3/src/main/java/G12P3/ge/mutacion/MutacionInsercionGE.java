package G12P3.ge.mutacion;

import G12P3.ag.Cromosoma;
import G12P3.ag.mutacion.Mutacion;
import G12P3.ge.CromosomaGE;
import java.util.Random;

// mutacion por insercion (P2): se elige un codon, se quita y se inserta
// en otra posicion desplazando el resto. desplazar codones cambia las
// reglas que se aplican en cada paso del decoder, alterando el fenotipo
public class MutacionInsercionGE implements Mutacion {

    private final Random rnd;

    public MutacionInsercionGE(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public void mutar(Cromosoma cromosoma) {
        CromosomaGE c = (CromosomaGE) cromosoma;
        int[] g = c.codones;
        if (g.length < 2) return;

        // dos desplazamientos como en P2
        for (int k = 0; k < 2; k++) {
            int origen = rnd.nextInt(g.length);
            int destino = rnd.nextInt(g.length);

            if (origen > destino) {
                int elem = g[origen];
                for (int i = origen; i > destino; i--) g[i] = g[i - 1];
                g[destino] = elem;
            } else if (origen < destino) {
                int elem = g[origen];
                for (int i = origen; i < destino; i++) g[i] = g[i + 1];
                g[destino] = elem;
            }
        }
        c.redecodificar();
    }
}
