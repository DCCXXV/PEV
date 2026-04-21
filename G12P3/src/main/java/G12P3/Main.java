package G12P3;

import G12P3.arbol.GeneradorArbol;
import G12P3.arbol.NodoAst;
import G12P3.evaluacion.Contexto;
import G12P3.evaluacion.MapaLunar;
import G12P3.evaluacion.ResultadoSimulacion;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Contexto contexto = new Contexto(MapaLunar.generar(3000));

        Random rnd = new Random();
        NodoAst[] arbol = GeneradorArbol.rampedHalfAndHalf(1, 5, rnd);

        ResultadoSimulacion res = contexto.simular(arbol[0]);
        System.out.println(res.toString());
    }
}