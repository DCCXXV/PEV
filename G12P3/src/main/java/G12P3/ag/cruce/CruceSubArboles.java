package G12P3.ag.cruce;

import G12P3.ag.Cromosoma;
import G12P3.arbol.NodoAst;

import java.util.List;
import java.util.Random;

public class CruceSubArboles implements Cruce{
    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {

        Random rand = new Random();

        NodoAst nodosPadre1 = padre1.getNodoAleatorio(rand);
        while (!nodosPadre1.isConditional()) {

        }

        NodoAst nodosPadre2 = padre2.getNodoAleatorio(rand);
        //TODO
    }
}
