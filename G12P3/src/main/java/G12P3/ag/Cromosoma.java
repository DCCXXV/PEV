package G12P3.ag;

import G12P3.arbol.NodoAst;
import G12P3.evaluacion.Contexto;
import G12P3.evaluacion.MapaLunar;
import G12P3.evaluacion.ResultadoSimulacion;

import java.util.List;
import java.util.Random;

public class Cromosoma {

    public NodoAst arbol;

    // resultados de la ultima evaluacion
    public double fitness;          // fitness final (con penalizacion de bloat)
    public double fitnessBase;      // media del fitness base en los 3 mapas
    public int nodos;               // numero de nodos del AST
    public int profundidadMax;      // profundidad maxima real del AST

    // detalles de cada mapa evaluado (para poder visualizar los 3 recorridos)
    public DatosMapa[] datosPorMapa;

    public static class DatosMapa {
        public int[][] mapaInicial;      // mapa antes de la simulacion
        public int[][] mapaFinal;        // mapa tras la simulacion (muestras recogidas quitadas)
        public boolean[][] visitado;
        public int posFila;
        public int posCol;
        public int direccion;
        public double energiaRestante;
        public int muestrasRecogidas;
        public int casillasExploradas;
        public int pisadasArena;
        public int colisiones;
        public double fitnessBase;       // fitness base especifico de este mapa
    }

    public Cromosoma(NodoAst arbol) {
        this.arbol = arbol;
    }

    public NodoAst getNodoAleatorio(Random rnd) {
        List<NodoAst> nodos = arbol.obtenerTodosNodos();
        return nodos.get(rnd.nextInt(nodos.size()));
    }

    public Cromosoma clonar() {
        Cromosoma copia = new Cromosoma(arbol.clonar());
        copia.fitness = this.fitness;
        copia.fitnessBase = this.fitnessBase;
        copia.nodos = this.nodos;
        copia.profundidadMax = this.profundidadMax;
        copia.datosPorMapa = this.datosPorMapa;
        return copia;
    }

    // evalua el cromosoma ejecutando su arbol-programa en varios mapas distintos
    // (generados con semillas diferentes) para que el fitness no dependa de un
    // unico escenario. devuelve el fitness final ya con la penalizacion por bloat.
    public double evaluar(long[] semillas, double coefBloat) {
        double suma = 0;
        this.datosPorMapa = new DatosMapa[semillas.length];
        for (int i = 0; i < semillas.length; i++) {
            // genera un mapa lunar distinto para cada semilla
            int[][] mapa = MapaLunar.generar(semillas[i]);
            // crea el entorno de simulacion (posicion inicial del rover, energia, etc.)
            Contexto ctx = new Contexto(mapa);
            // ejecuta el arbol como programa de control del rover en ese mapa
            ResultadoSimulacion res = ctx.simular(arbol);
            // acumula la puntuacion obtenida (muestras, exploracion, penalizaciones...)
            double fb = res.calcularFitnessBase();
            suma += fb;

            // guardamos el estado de cada mapa para la visualizacion
            DatosMapa dm = new DatosMapa();
            dm.mapaInicial = mapa;
            dm.mapaFinal = ctx.mapa;
            dm.visitado = ctx.getVisitado();
            dm.posFila = ctx.y;
            dm.posCol = ctx.x;
            dm.direccion = ctx.direccion;
            dm.energiaRestante = ctx.energia;
            dm.muestrasRecogidas = ctx.muestrasRecogidas;
            dm.casillasExploradas = ctx.casillasExploradas;
            dm.pisadasArena = ctx.pisadasArena;
            dm.colisiones = ctx.colisiones;
            dm.fitnessBase = fb;
            this.datosPorMapa[i] = dm;
        }
        // fitness base = media del rendimiento en los N mapas
        this.fitnessBase = suma / semillas.length;
        this.nodos = arbol.contarNodos();
        this.profundidadMax = arbol.calcularProfundidadMaxima();
        // penalizacion por bloating: arboles mas grandes reciben menos fitness
        // para evitar que crezcan sin mejorar el comportamiento real
        this.fitness = this.fitnessBase - (this.nodos * coefBloat);
        return this.fitness;
    }
}
