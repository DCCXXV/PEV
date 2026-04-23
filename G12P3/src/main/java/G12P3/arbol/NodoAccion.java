package G12P3.arbol;

import G12P3.evaluacion.Contexto;
import G12P3.evaluacion.TipoAccion;

import java.util.ArrayList;
import java.util.List;

public class NodoAccion implements NodoAst {
    private TipoAccion tipoAccion;
    private int profundidad;

    public NodoAccion(TipoAccion tipo) {
        this.tipoAccion = tipo;
    }

    @Override
    public void ejecutar(Contexto ctx) {
        if (!ctx.vivo || ctx.ticks >= ctx.MAX_TICKS) return;

        switch (tipoAccion) {
            case AVANZAR -> ctx.avanzar();
            case GIRAR_IZQ -> ctx.girarIzquierda();
            case GIRAR_DER -> ctx.girarDerecha();
        }

        ctx.ticks++;
    }

    @Override
    public int getProfundidad() { return profundidad; }

    @Override
    public void setProfundidad(int profundidad) { this.profundidad = profundidad; }

    @Override
    public NodoAst clonar() {
        NodoAccion copia = new NodoAccion(this.tipoAccion);
        copia.profundidad = this.profundidad;
        return copia;
    }

    @Override
    public int contarNodos() {
        return 1;
    }

    @Override
    public int calcularProfundidadMaxima() {
        return 1;
    }

    @Override
    public List<NodoAst> obtenerTodosNodos() {
        List<NodoAst> lista = new ArrayList<>();
        lista.add(this);
        return lista;
    }

    @Override
    public boolean isConditional() {
        return false;
    }

    public TipoAccion getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(TipoAccion tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    @Override
    public String toString() {
        return tipoAccion.name() + "()";
    }
}
