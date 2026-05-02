package G12P3.ge;

import G12P3.arbol.NodoAccion;
import G12P3.arbol.NodoAst;
import G12P3.arbol.NodoBloque;
import G12P3.arbol.NodoCondicional;
import G12P3.evaluacion.Operador;
import G12P3.evaluacion.TipoAccion;
import G12P3.evaluacion.TipoSensor;

// mapeo genotipo -> fenotipo: convierte un cromosoma de codones (enteros 0..255)
// en un AST aplicando las reglas de produccion BNF mediante regla = c % r.
//
// la gramatica esta deliberadamente alineada con el generador de PG
// (GeneradorArbol#crearGrow) para que los individuos GE compitan en igualdad
// de condiciones: cada <expr> es directamente accion, condicional o bloque,
// sin envolver acciones sueltas en NodoBloque vacios.
public class Decoder {

    private static final int[] UMBRALES = { 10, 50, 100 };
    private static final TipoSensor[] SENSORES = TipoSensor.values();
    private static final Operador[] OPERADORES = Operador.values();

    public static final int MAX_WRAPS = 3;

    private final int[] codones;
    private final int profMax;
    private int idx;
    private int wraps;

    private Decoder(int[] codones, int profMax) {
        this.codones = codones;
        this.profMax = profMax;
        this.idx = 0;
        this.wraps = 0;
    }

    public static NodoAst decodificar(int[] codones, int profMax) {
        if (codones == null || codones.length == 0) {
            NodoAccion fallback = new NodoAccion(TipoAccion.AVANZAR);
            fallback.setProfundidad(0);
            return fallback;
        }
        Decoder d = new Decoder(codones, profMax);
        // arrancamos siempre por un bloque para garantizar varios statements
        // a nivel raiz y reducir probabilidad de programas triviales (1 accion)
        return d.bloque(0);
    }

    private int siguiente() {
        if (idx >= codones.length) {
            idx = 0;
            wraps++;
        }
        if (wraps > MAX_WRAPS) return 0;
        return codones[idx++] & 0xFF;
    }

    private boolean wrapsAgotados() {
        return wraps > MAX_WRAPS;
    }

    // <expr> ::= <accion> | <condicional> | <bloque>     (3 alternativas)
    private NodoAst expr(int prof) {
        if (prof >= profMax || wrapsAgotados()) return accion(prof);
        int c = siguiente();
        if (wrapsAgotados()) return accion(prof);

        int alt = c % 3;
        if (alt == 0) return accion(prof);
        if (alt == 1) return condicional(prof);
        return bloque(prof);
    }

    // <bloque> ::= <expr> <expr> | <expr> <expr> <expr>   (2 alternativas)
    private NodoAst bloque(int prof) {
        if (prof >= profMax) return accion(prof);

        int c = siguiente();
        int numHijos = (c % 2 == 0) ? 2 : 3;

        NodoBloque b = new NodoBloque();
        b.setProfundidad(prof);
        for (int i = 0; i < numHijos; i++) {
            b.setHijo(expr(prof + 1));
        }
        return b;
    }

    // <condicional> ::= IF (<sensor> <op> <umbral>) <expr> ELSE <expr>   (1 alt)
    private NodoAst condicional(int prof) {
        if (prof >= profMax - 1) return accion(prof);

        TipoSensor s = SENSORES[siguiente() % SENSORES.length];
        Operador op = OPERADORES[siguiente() % OPERADORES.length];
        int umb = UMBRALES[siguiente() % UMBRALES.length];

        NodoCondicional cond = new NodoCondicional(s, op, umb);
        cond.setProfundidad(prof);
        cond.setHijoTrue(expr(prof + 1));
        cond.setHijoFalse(expr(prof + 1));
        return cond;
    }

    // <accion> ::= AVANZAR | AVANZAR | AVANZAR | GIRAR_IZQ | GIRAR_DER
    // sesgo 60% a AVANZAR (vs 50% en PG) porque la pereza penaliza fuerte y
    // la estructura mas equilibrada del decoder produce arboles algo menos
    // "moviles" que ramped half-and-half si no se compensa
    private NodoAst accion(int prof) {
        int c = siguiente() % 5;
        TipoAccion a;
        if (c <= 2) a = TipoAccion.AVANZAR;
        else if (c == 3) a = TipoAccion.GIRAR_IZQ;
        else a = TipoAccion.GIRAR_DER;

        NodoAccion na = new NodoAccion(a);
        na.setProfundidad(prof);
        return na;
    }
}
