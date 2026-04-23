package G12P3.arbol;

import G12P3.evaluacion.Contexto;
import java.util.List;

public interface NodoAst {
    void ejecutar(Contexto ctx);
    NodoAst clonar();
    int contarNodos();
    int calcularProfundidadMaxima();
    List<NodoAst> obtenerTodosNodos();
    boolean isConditional();
    int getProfundidad();
    void setProfundidad(int profundidad);
}
