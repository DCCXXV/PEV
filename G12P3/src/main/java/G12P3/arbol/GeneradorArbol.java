package G12P3.arbol;

import G12P3.evaluacion.Operador;
import G12P3.evaluacion.TipoAccion;
import G12P3.evaluacion.TipoSensor;
import java.util.Random;

public class GeneradorArbol {

    private static final int[] UMBRALES = { 10, 50, 100 };
    private static final TipoSensor[] SENSORES = TipoSensor.values();
    private static final Operador[] OPERADORES = Operador.values();

    // genera n arboles distribuidos en grupos de profundidad 2 a profMax.
    // cada grupo tiene floor(n/numGrupos) individuos; los primeros grupos
    // reciben uno extra si hay resto. Dentro de cada grupo: mitad Full, mitad Grow.
    public static NodoAst[] rampedHalfAndHalf(int n, int profMax, Random rnd) {
        int numGrupos = profMax - 1;
        int porGrupo = n / numGrupos;
        int resto = n % numGrupos;

        NodoAst[] poblacion = new NodoAst[n];
        int idx = 0;

        for (int prof = 2; prof <= profMax; prof++) {
            int grupoIdx = prof - 2;
            int tam = porGrupo + (grupoIdx < resto ? 1 : 0);
            int mitad = tam / 2;

            for (int i = 0; i < mitad; i++)
                poblacion[idx++] = crearFull(0, prof, rnd);
            for (int i = mitad; i < tam; i++)
                poblacion[idx++] = crearGrow(0, prof, rnd);
        }

        return poblacion;
    }

    // grow: los nodos se eligen entre terminales y funciones hasta profMax.
    // en profMax se fuerza un terminal.
    // AVANZAR tiene el doble de probabilidad que GIRAR_IZQ y GIRAR_DER.
    public static NodoAst crearGrow(int profActual, int profMax, Random rnd) {
        if (profActual >= profMax) return crearTerminal(rnd, profActual);

        // terminal, condicional, bloque
        int tipo = rnd.nextInt(3);

        if (tipo == 0) return crearTerminal(rnd, profActual);
        else if (tipo == 1) return crearCondicional(
            profActual,
            profMax,
            rnd,
            false
        );
        else return crearBloque(profActual, profMax, rnd, false);
    }

    // full: solo nodos funcion hasta profMax-1, terminales en profMax.
    public static NodoAst crearFull(int profActual, int profMax, Random rnd) {
        if (profActual >= profMax) return crearTerminal(rnd, profActual);

        // solo nodos funcion: condicional o bloque
        if (rnd.nextBoolean()) return crearCondicional(
            profActual,
            profMax,
            rnd,
            true
        );
        else return crearBloque(profActual, profMax, rnd, true);
    }

    // crea un terminal con sesgo: AVANZAR tiene el doble de probabilidad.
    // probabilidades: AVANZAR=50%, GIRAR_IZQ=25%, GIRAR_DER=25%
    private static NodoAccion crearTerminal(Random rnd, int profActual) {
        int r = rnd.nextInt(4); // 0,1 -> AVANZAR, 2 -> GIRAR_IZQ, 3 -> GIRAR_DER
        TipoAccion accion;
        if (r <= 1) accion = TipoAccion.AVANZAR;
        else if (r == 2) accion = TipoAccion.GIRAR_IZQ;
        else accion = TipoAccion.GIRAR_DER;

        NodoAccion nodo = new NodoAccion(accion);
        nodo.setProfundidad(profActual);
        return nodo;
    }

    private static NodoCondicional crearCondicional(
        int profActual,
        int profMax,
        Random rnd,
        boolean full
    ) {
        TipoSensor sensor = SENSORES[rnd.nextInt(SENSORES.length)];
        Operador op = OPERADORES[rnd.nextInt(OPERADORES.length)];
        int umbral = UMBRALES[rnd.nextInt(UMBRALES.length)];

        NodoCondicional nodo = new NodoCondicional(sensor, op, umbral);
        nodo.setProfundidad(profActual);

        if (full) {
            nodo.setHijoTrue(crearFull(profActual + 1, profMax, rnd));
            nodo.setHijoFalse(crearFull(profActual + 1, profMax, rnd));
        } else {
            nodo.setHijoTrue(crearGrow(profActual + 1, profMax, rnd));
            nodo.setHijoFalse(crearGrow(profActual + 1, profMax, rnd));
        }
        return nodo;
    }

    private static NodoBloque crearBloque(
        int profActual,
        int profMax,
        Random rnd,
        boolean full
    ) {
        NodoBloque bloque = new NodoBloque();
        bloque.setProfundidad(profActual);
        int numHijos = rnd.nextInt(2) + 2; // 2 o 3 hijos
        for (int i = 0; i < numHijos; i++) {
            if (full) bloque.setHijo(
                crearFull(profActual + 1, profMax, rnd)
            );
            else bloque.setHijo(crearGrow(profActual + 1, profMax, rnd));
        }
        return bloque;
    }
}
