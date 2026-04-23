package G12P3.ag.seleccion;

import G12P3.ag.Cromosoma;

import java.util.Random;

public class Ranking implements Seleccion {

    private final Random rand;

    public Ranking(Random rand) {
        this.rand = rand;
    }

    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion) {
        int n = poblacion.length;

        // ordenar ascendente: peor fitness primero -> rank 1 al peor, rank N al mejor
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) indices[i] = i;
        java.util.Arrays.sort(indices, (a, b) -> Double.compare(poblacion[a].fitness, poblacion[b].fitness));

        double total = (n * (n + 1)) / 2.0;
        double[] prob = new double[n];
        for (int r = 0; r < n; r++) {
            prob[indices[r]] = (r + 1) / total;
        }

        Cromosoma[] nuevaPoblacion = new Cromosoma[n];
        for (int i = 0; i < n; i++) {
            double r = rand.nextDouble();
            int j = 0;
            double acumulado = prob[0];
            while (j < n - 1 && r > acumulado) {
                j++;
                acumulado += prob[j];
            }
            nuevaPoblacion[i] = poblacion[j].clonar();
        }

        return nuevaPoblacion;
    }
}
