package G12P2.ag.seleccion;

import G12P2.cromosomas.Cromosoma;
import java.util.Random;

public class Ruleta implements Seleccion {

    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion, double[] fitness) {
        double max = -Double.MAX_VALUE;
        for (double fit : fitness) if (fit > max) max = fit;

        int total = 0;
        double fitnessAjustado[] = new double[fitness.length];
        for (int i = 0; i < fitness.length; i++) {
            fitnessAjustado[i] = max - fitness[i];
            total += fitnessAjustado[i];
        }

        //se saca el porcentaje de cada fitness correspondiente con el acumulado
        double puntuacion[] = new double[fitness.length];
        for (int i = 0; i < fitnessAjustado.length; i++) {
            puntuacion[i] = (double) fitnessAjustado[i] / total;
        }

        //se saca la nueva poblacion
        Cromosoma nuevaPoblacion[] = new Cromosoma[poblacion.length];
        Random rand = new Random();
        for (int i = 0; i < poblacion.length; i++) {
            //se saca un random de 0 a 1
            double random = rand.nextDouble();

            //se va sumando las puntuacion y cuando el random pase a ser menor significa que
            //que entra en el rango de la ultima puntuacion sumada
            int j = 0;
            double acumulado = puntuacion[0];
            while (j < puntuacion.length - 1 && random > acumulado) {
                j++;
                acumulado += puntuacion[j];
            }
            nuevaPoblacion[i] = poblacion[j].copia();
        }

        return nuevaPoblacion;
    }
}
