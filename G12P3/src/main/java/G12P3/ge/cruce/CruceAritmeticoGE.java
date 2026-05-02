package G12P3.ge.cruce;

import G12P3.ag.Cromosoma;
import G12P3.ag.cruce.Cruce;
import G12P3.ge.CromosomaGE;

// cruce aritmetico (P1, adaptado a enteros): h[i] = (p1[i] + p2[i]) / 2
// para cada codon. al ser determinista produce dos hijos identicos pero
// genera codones "intermedios" que no estaban en ninguno de los dos padres
public class CruceAritmeticoGE implements Cruce {

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaGE p1 = (CromosomaGE) padre1;
        CromosomaGE p2 = (CromosomaGE) padre2;
        int[] g1 = p1.codones;
        int[] g2 = p2.codones;
        int len = Math.min(g1.length, g2.length);

        for (int i = 0; i < len; i++) {
            int media = (g1[i] + g2[i]) / 2;
            g1[i] = media;
            g2[i] = media;
        }
        p1.redecodificar();
        p2.redecodificar();
    }
}
