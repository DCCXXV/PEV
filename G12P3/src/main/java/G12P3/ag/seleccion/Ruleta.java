package G12P3.ag.seleccion;

import G12P3.ag.Cromosoma;

import java.util.Random;

public class Ruleta implements Seleccion {

    private final Random rand;

    public Ruleta(Random rand) {
        this.rand = rand;
    }

    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion) {
        int n = poblacion.length;

        // desplazamos para que todos sean >= 0 (mayor fitness = mejor)
        double min = Double.MAX_VALUE;
        for (Cromosoma c : poblacion) if (c.fitness < min) min = c.fitness;

        double total = 0;
        double[] ajustado = new double[n];
        for (int i = 0; i < n; i++) {
            ajustado[i] = poblacion[i].fitness - min;
            total += ajustado[i];
        }

        Cromosoma[] nuevaPoblacion = new Cromosoma[n];

        // si todos tienen el mismo fitness, seleccion uniforme
        if (total == 0) {
            for (int i = 0; i < n; i++) nuevaPoblacion[i] = poblacion[rand.nextInt(n)].clonar();
            return nuevaPoblacion;
        }

        double[] prob = new double[n];
        for (int i = 0; i < n; i++) prob[i] = ajustado[i] / total;

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
