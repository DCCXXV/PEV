package G12P3.ag.seleccion;

import G12P3.ag.Cromosoma;

import java.util.Random;

public class Estocastico implements Seleccion {

    private final Random rand;

    public Estocastico(Random rand) {
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

        Cromosoma[] nuevaPoblacion = new Cromosoma[n];

        if (total == 0) {
            for (int i = 0; i < n; i++) nuevaPoblacion[i] = poblacion[rand.nextInt(n)].clonar();
            return nuevaPoblacion;
        }

        double distMax = total / n;
        double current = rand.nextDouble() * distMax;

        double acumulado = 0;
        int i = 0;
        int numEncontrados = 0;
        while (i < n && numEncontrados < n) {
            acumulado += ajustado[i];
            while (current < acumulado && numEncontrados < n) {
                nuevaPoblacion[numEncontrados] = poblacion[i].clonar();
                numEncontrados++;
                current += distMax;
            }
            i++;
        }

        // relleno de seguridad
        while (numEncontrados < n) {
            nuevaPoblacion[numEncontrados] = poblacion[n - 1].clonar();
            numEncontrados++;
        }

        return nuevaPoblacion;
    }
}
