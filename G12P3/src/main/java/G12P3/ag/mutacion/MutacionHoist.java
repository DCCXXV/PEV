package G12P3.ag.mutacion;

import G12P3.ag.Cromosoma;
import G12P3.arbol.NodoAst;
import java.util.List;
import java.util.Random;

public class MutacionHoist implements Mutacion {

    private final Random rnd;

    public MutacionHoist(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public void mutar(Cromosoma cromosoma) {
        List<NodoAst> nodos = cromosoma.arbol.obtenerTodosNodos();

        // descarta la raiz (hoist asciende una subrama interna)
        if (nodos.size() <= 1) return;

        NodoAst interno = nodos.get(rnd.nextInt(nodos.size() - 1) + 1);
        cromosoma.arbol = interno;
    }
}
