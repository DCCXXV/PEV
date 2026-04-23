package G12P3.ag.seleccion;

import G12P3.ag.Cromosoma;

import java.util.Random;

public class Restos implements Seleccion {

    private final Random rand;

    public Restos(Random rand) {
        this.rand = rand;
    }

    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion) {
        int n = poblacion.length;

        double min = Double.MAX_VALUE;
        for (Cromosoma c : poblacion) if (c.fitness < min) min = c.fitness;

        double total = 0;
        double[] ajustado = new double[n];
        for (int i = 0; i < n; i++) {
            ajustado[i] = poblacion[i].fitness - min;
            total += ajustado[i];
        }

        // si todos valen lo mismo, devolver copias
        if (total == 0) {
            Cromosoma[] copia = new Cromosoma[n];
            for (int i = 0; i < n; i++) copia[i] = poblacion[i].clonar();
            return copia;
        }

        double media = total / n;

        int[] garantizados = new int[n];
        double[] fraccion = new double[n];
        for (int i = 0; i < n; i++) {
            double esperado = ajustado[i] / media;
            garantizados[i] = (int) esperado;
            fraccion[i] = esperado - garantizados[i];
        }

        Cromosoma[] nuevaPoblacion = new Cromosoma[n];
        int num = 0;
        for (int i = 0; i < n && num < n; i++)
            for (int j = 0; j < garantizados[i] && num < n; j++)
                nuevaPoblacion[num++] = poblacion[i].clonar();

        if (num < n) {
            double totalFrac = 0;
            for (double f : fraccion) totalFrac += f;

            while (num < n) {
                double r = rand.nextDouble() * totalFrac;
                double acumulado = fraccion[0];
                int j = 0;
                while (j < n - 1 && r > acumulado) {
                    j++;
                    acumulado += fraccion[j];
                }
                nuevaPoblacion[num++] = poblacion[j].clonar();
            }
        }

        return nuevaPoblacion;
    }
}
