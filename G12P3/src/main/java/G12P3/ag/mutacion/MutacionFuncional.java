package G12P3.ag.mutacion;

import G12P3.ag.Cromosoma;
import G12P3.arbol.NodoAst;
import G12P3.arbol.NodoCondicional;
import G12P3.evaluacion.Operador;
import G12P3.evaluacion.TipoSensor;
import java.util.List;
import java.util.Random;

public class MutacionFuncional implements Mutacion {

    private static final int[] UMBRALES = { 10, 50, 100 };
    private static final TipoSensor[] SENSORES = TipoSensor.values();
    private static final Operador[] OPERADORES = Operador.values();

    private final Random rnd;

    public MutacionFuncional(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public void mutar(Cromosoma cromosoma) {
        List<NodoAst> condicionales = cromosoma.arbol
            .obtenerTodosNodos()
            .stream()
            .filter(n -> n instanceof NodoCondicional)
            .toList();

        if (condicionales.isEmpty()) return;

        NodoCondicional nodo = (NodoCondicional) condicionales.get(
            rnd.nextInt(condicionales.size())
        );

        // altera sensor, operador o umbral sin modificar la estructura
        switch (rnd.nextInt(3)) {
            case 0 -> {
                TipoSensor actual = nodo.getSensor();
                TipoSensor nuevo;
                do {
                    nuevo = SENSORES[rnd.nextInt(SENSORES.length)];
                } while (nuevo == actual);
                nodo.setSensor(nuevo);
            }
            case 1 -> {
                Operador actual = nodo.getOperador();
                Operador nuevo;
                do {
                    nuevo = OPERADORES[rnd.nextInt(OPERADORES.length)];
                } while (nuevo == actual);
                nodo.setOperador(nuevo);
            }
            default -> {
                int actual = nodo.getUmbral();
                int nuevo;
                do {
                    nuevo = UMBRALES[rnd.nextInt(UMBRALES.length)];
                } while (nuevo == actual);
                nodo.setUmbral(nuevo);
            }
        }
    }
}
