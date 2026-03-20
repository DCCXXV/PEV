package G12P2.ag.cruce;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

// cruce por emparejamiento parcial
public class CrucePMX implements Cruce {

    private Random rng = new Random();

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaDrones p1 = (CromosomaDrones) padre1;
        CromosomaDrones p2 = (CromosomaDrones) padre2;

        int[] genes1 = p1.getGenes();
        int[] genes2 = p2.getGenes();
        int n = genes1.length;

        // puntos de corte elegidos al azar (al menos 1 elemento de segmento)
        int c1 = rng.nextInt(n);
        int c2 = rng.nextInt(n);
        if (c1 > c2) {
            int t = c1;
            c1 = c2;
            c2 = t;
        }
        if (c1 == c2) c2 = Math.min(c2 + 1, n - 1);

        // copias originales para seguir las cadenas de resolución
        int[] orig1 = genes1.clone();
        int[] orig2 = genes2.clone();

        // mapas valor→posición en el segmento (para resolver conflictos)
        Map<Integer, Integer> posEnSeg1 = new HashMap<>();
        Map<Integer, Integer> posEnSeg2 = new HashMap<>();
        Set<Integer> seg1 = new HashSet<>();
        Set<Integer> seg2 = new HashSet<>();

        for (int i = c1; i <= c2; i++) {
            seg1.add(orig1[i]);
            seg2.add(orig2[i]);
            posEnSeg1.put(orig1[i], i);
            posEnSeg2.put(orig2[i], i);
            // intercambio del segmento central
            genes1[i] = orig2[i];
            genes2[i] = orig1[i];
        }

        // resolver conflictos fuera del segmento
        for (int i = 0; i < n; i++) {
            if (i >= c1 && i <= c2) continue;

            // hijo1; si el valor original de padre1 ya está en el segmento de padre2,
            // seguimos la cadena: buscamos qué tenía padre1 en la posición donde padre2 tenía ese valor
            int v1 = orig1[i];
            while (seg2.contains(v1)) {
                v1 = orig1[posEnSeg2.get(v1)];
            }
            genes1[i] = v1;

            // hijo2; análogo con padre2
            int v2 = orig2[i];
            while (seg1.contains(v2)) {
                v2 = orig2[posEnSeg1.get(v2)];
            }
            genes2[i] = v2;
        }
    }
}
