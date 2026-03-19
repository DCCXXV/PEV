package G12P2.ag.seleccion;

import G12P2.cromosomas.Cromosoma;
import java.util.*;

public class Torneo implements Seleccion {

    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion, double[] fitness) {
        Random rand = new Random();

        //nueva poblacion
        Cromosoma nuevaPoblacion[] = new Cromosoma[poblacion.length];
        for (int i = 0; i < poblacion.length; i++) {
            //se sacan 3 numeros aleatorios
            Set<Integer> numeros = new HashSet<>();
            while (numeros.size() < 3) numeros.add(
                rand.nextInt(poblacion.length)
            );

            //se miran 3 y se queda con el mejor
            double min = Double.MAX_VALUE;
            int selected = -1;
            for (int num : numeros) {
                if (fitness[num] < min) {
                    min = fitness[num];
                    selected = num;
                }
            }

            //se guarda el ganador
            nuevaPoblacion[i] = poblacion[selected].copia();
        }

        return nuevaPoblacion;
    }
}
