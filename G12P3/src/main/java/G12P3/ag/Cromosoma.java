package G12P3.ag;

import G12P3.arbol.GeneradorArbol;
import G12P3.arbol.NodoAst;
import java.util.List;
import java.util.Random;

public class Cromosoma {

    public NodoAst arbol;

    public Cromosoma(NodoAst arbol) {
        this.arbol = arbol;
    }

    public static Cromosoma aleatorio(int profMax, Random rnd) {
        return new Cromosoma(GeneradorArbol.rampedHalfAndHalf(profMax, rnd));
    }

    public NodoAst getNodoAleatorio(Random rnd) {
        List<NodoAst> nodos = arbol.obtenerTodosNodos();
        return nodos.get(rnd.nextInt(nodos.size()));
    }

    public Cromosoma clonar() {
        return new Cromosoma(arbol.clonar());
    }
}
