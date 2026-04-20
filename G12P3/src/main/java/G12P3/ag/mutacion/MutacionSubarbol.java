package G12P3.ag.mutacion;

import G12P3.ag.Cromosoma;
import G12P3.arbol.GeneradorArbol;
import G12P3.arbol.NodoAst;
import G12P3.arbol.NodoBloque;
import G12P3.arbol.NodoCondicional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class MutacionSubarbol implements Mutacion {

    private final Random rnd;
    private final int profMax;

    public MutacionSubarbol(Random rnd, int profMax) {
        this.rnd = rnd;
        this.profMax = profMax;
    }

    @Override
    public void mutar(Cromosoma cromosoma) {
        // recolecta "huecos", cada uno es un setter que reemplaza un nodo del arbol
        List<Consumer<NodoAst>> huecos = new ArrayList<>();

        // hueco raiz
        huecos.add(nuevo -> cromosoma.arbol = nuevo);

        recolectarHuecos(cromosoma.arbol, huecos);

        Consumer<NodoAst> hueco = huecos.get(rnd.nextInt(huecos.size()));
        NodoAst nuevoSubarbol = GeneradorArbol.rampedHalfAndHalf(profMax, rnd);
        hueco.accept(nuevoSubarbol);
    }

    private void recolectarHuecos(
        NodoAst nodo,
        List<Consumer<NodoAst>> huecos
    ) {
        if (nodo instanceof NodoCondicional cond) {
            huecos.add(cond::setHijoTrue);
            recolectarHuecos(cond.getHijoTrue(), huecos);
            if (cond.getHijoFalse() != null) {
                huecos.add(cond::setHijoFalse);
                recolectarHuecos(cond.getHijoFalse(), huecos);
            }
        } else if (nodo instanceof NodoBloque bloque) {
            List<NodoAst> hijos = bloque.getHijos();
            for (int i = 0; i < hijos.size(); i++) {
                final int idx = i;
                huecos.add(nuevo -> hijos.set(idx, nuevo));
                recolectarHuecos(hijos.get(i), huecos);
            }
        }
    }
}
