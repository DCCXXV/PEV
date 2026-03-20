package G12P2.ag;

import G12P2.ag.cruce.Cruce;
import G12P2.ag.mutacion.Mutacion;
import G12P2.ag.seleccion.Seleccion;
import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import G12P2.evaluacion.ResEvaluacion;
import G12P2.ui.Grafica;
import G12P2.ui.Tablero;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Simulator {

    private int maxGeneraciones;
    private int generacionActual;

    private Seleccion seleccion;
    private Cruce cruce;
    private Mutacion mutacion;

    private double probCruce;
    private double probMutacion;
    private double elitismo;

    private Cromosoma[] poblacion;
    private int tamPoblacion;
    private Supplier<Cromosoma> factoriaCromosomas;
    private double[] fitness;
    private ResEvaluacion[] resEvaluacion;

    private Cromosoma[] elite;
    private double[] fitnessElite;
    private int[] idxElite;

    private boolean memetico;
    private boolean memeticoElite;
    private double porcentajeMemetico;

    // generaciones consecutivas sin mejora del mejor absoluto antes de suspender la búsqueda memética
    private static final int STAGNATION_LIMIT = 15;
    private int generacionesSinMejora = 0;

    boolean terminoEjecucion = false;
    private List<double[]> optimosPareto;
    private List<ResEvaluacion> solucionesPareto;

    public Simulator(
        int maxGeneraciones,
        int tamPoblacion,
        double probCruce,
        double probMutacion,
        double elitismo,
        Seleccion seleccion,
        Cruce cruce,
        Mutacion mutacion,
        Supplier<Cromosoma> factoriaCromosomas,
        boolean memetico,
        boolean memeticoElite,
        double porcentajeMemetico,
        Tablero tablero,
        Grafica grafica
    ) {
        this.memetico = memetico;
        this.memeticoElite = memeticoElite;
        this.porcentajeMemetico = porcentajeMemetico;
        this.maxGeneraciones = maxGeneraciones;
        this.tamPoblacion = tamPoblacion;
        this.probCruce = probCruce;
        this.probMutacion = probMutacion;
        this.elitismo = elitismo;
        this.factoriaCromosomas = factoriaCromosomas;
        this.generacionActual = 0;

        this.seleccion = seleccion;
        this.cruce = cruce;
        this.mutacion = mutacion;

        // arrays para la gráfica de evolución
        double[] mejoresPorGeneracion = new double[maxGeneraciones];
        double[] mejoresAbsolutos = new double[maxGeneraciones];
        double[] mediaPorGeneracion = new double[maxGeneraciones];

        // inicializar población y evaluarla
        iniciarPoblacion();
        evaluarPoblacion();

        //mejor y optimos
        double mejorFitnessAbsoluto = Integer.MAX_VALUE;
        this.optimosPareto = new ArrayList<>();
        this.solucionesPareto = new ArrayList<>();

        Cromosoma mejorCromosomaAbsoluto = null;
        ResEvaluacion mejorEvaluacion = null;
        for (int i = 0; i < tamPoblacion; i++) {
            if (fitness[i] < mejorFitnessAbsoluto) {
                mejorFitnessAbsoluto = fitness[i];
                mejorCromosomaAbsoluto = poblacion[i].copia();
                mejorEvaluacion = this.resEvaluacion[i];
            }
        }

        while (this.generacionActual < this.maxGeneraciones && !Thread.currentThread().isInterrupted()) {
            generaElite();
            poblacion = seleccion.seleccionar(poblacion, fitness);
            cruce(probCruce);
            mutacion(probMutacion);
            introducirElite();
            if (memetico && generacionesSinMejora < STAGNATION_LIMIT) {
                if (memeticoElite)
                    busquedaSobreElite();
                else
                    busuedaSobrePoblacion();
            }
            evaluarPoblacion();

            // recoger stats de esta generacion
            double mejorGen = Double.MAX_VALUE;
            double suma = 0;
            double fitAntes = mejorFitnessAbsoluto;
            for (int i = 0; i < tamPoblacion; i++) {
                suma += fitness[i];
                if (fitness[i] < mejorGen) {
                    mejorGen = fitness[i];
                }
                if (fitness[i] < mejorFitnessAbsoluto) {
                    mejorFitnessAbsoluto = fitness[i];
                    mejorCromosomaAbsoluto = poblacion[i].copia();
                    mejorEvaluacion = this.resEvaluacion[i];
                }
                comprobarOptimosPareto(this.resEvaluacion[i]);
            }

            // actualizar contador de estancamiento
            if (mejorFitnessAbsoluto < fitAntes) {
                generacionesSinMejora = 0;
            } else {
                generacionesSinMejora++;
                // reactivar periódicamente por si el AG logra escapar del óptimo local
                if (generacionesSinMejora >= STAGNATION_LIMIT * 2)
                    generacionesSinMejora = 0;
            }

            //SE MANDA EL RESULTADO A LA GRAFICA
            double media = suma / tamPoblacion;
            grafica.actualizarGrafica(generacionActual, mejorGen, mejorFitnessAbsoluto, media);

            //SE MANDA EL RESULTADO AL TABLERO
            tablero.setMejor(
                    mejorEvaluacion.getFitness(),
                    mejorEvaluacion.getEnergia(),
                    mejorEvaluacion.getTiemposDrones(),
                    mejorEvaluacion.getEnergiaDrones(),
                    mejorEvaluacion.getCaminos(),
                    mejorEvaluacion.getCromosoma().getGenes()
            );
            System.out.println(generacionActual + " | " + maxGeneraciones + " | " + mejorFitnessAbsoluto);

            generacionActual++;
        }

        terminoEjecucion = !Thread.currentThread().isInterrupted();
    }

    private void comprobarOptimosPareto(ResEvaluacion solucionPareto) {

        double desplazamiento = solucionPareto.getFitness();
        double energia = solucionPareto.getEnergia();
        double[] nuevoSol = {desplazamiento, energia};

        // Comprobar si el nuevo punto esta dominado por alguno existente
        for (double[] optimo : this.optimosPareto) {
            if (optimo[0] <= desplazamiento && optimo[1] <= energia) {
                return;
            }
        }

        // Eliminar los que ahora quedan dominados por el nuevo punto
        for (int i = optimosPareto.size() - 1; i >= 0; i--) {
            double[] optimo = optimosPareto.get(i);
            if (optimo[0] >= desplazamiento && optimo[1] >= energia) {
                optimosPareto.remove(i);
                solucionesPareto.remove(i);
            }
        }

        solucionesPareto.add(solucionPareto);
        optimosPareto.add(nuevoSol);
    }

    private void iniciarPoblacion() {
        poblacion = new Cromosoma[tamPoblacion];
        for (int i = 0; i < tamPoblacion; i++) {
            poblacion[i] = factoriaCromosomas.get();
        }
    }

    private void evaluarPoblacion() {
        this.fitness = new double[tamPoblacion];
        this.resEvaluacion = new ResEvaluacion[tamPoblacion];
        for (int i = 0; i < tamPoblacion; i++) {
            ResEvaluacion res = poblacion[i].evaluar();
            this.resEvaluacion[i] = res;
            fitness[i] = res.getFitness();
        }
    }

    private void cruce(double probCruce) {
        Random rng = new Random();
        ArrayList<Integer> seleccionados = new ArrayList<>();
        for (int i = 0; i < tamPoblacion; i++) {
            if (rng.nextDouble() < probCruce) {
                seleccionados.add(i);
            }
        }
        if (seleccionados.size() % 2 != 0) {
            seleccionados.remove(seleccionados.size() - 1);
        }
        for (int i = 0; i < seleccionados.size(); i += 2) {
            cruce.cruzar(
                poblacion[seleccionados.get(i)],
                poblacion[seleccionados.get(i + 1)]
            );
        }
    }

    private void mutacion(double probMutacion) {
        for (int i = 0; i < tamPoblacion; i++) {
            mutacion.mutar(poblacion[i], probMutacion);
        }
    }

    private void generaElite() {
        double[][] paresFitness = new double[tamPoblacion][2];
        for (int i = 0; i < tamPoblacion; i++) {
            paresFitness[i][0] = i;
            paresFitness[i][1] = fitness[i];
        }
        Arrays.sort(paresFitness, (a, b) -> Double.compare(a[1], b[1]));

        elite = new Cromosoma[(int)(elitismo * tamPoblacion)];
        fitnessElite = new double[(int)(elitismo * tamPoblacion)];
        idxElite = new int[(int)(elitismo * tamPoblacion)];
        for (int i = 0; i < (int)(elitismo * tamPoblacion); i++) {
            double idx = paresFitness[i][0];
            elite[i] = poblacion[(int)idx].copia();
            fitnessElite[i] = fitness[(int)idx];
            idxElite[i] = (int)idx;
        }
    }

    private void introducirElite() {
        double[][] paresFitness = new double[tamPoblacion][2];
        for (int i = 0; i < tamPoblacion; i++) {
            paresFitness[i][0] = i;
            paresFitness[i][1] = fitness[i];
        }
        Arrays.sort(paresFitness, (a, b) -> Double.compare(b[1], a[1]));

        for (int i = 0; i < (int)(elitismo * tamPoblacion); i++) {
            double idx = paresFitness[i][0];
            poblacion[(int)idx] = elite[i];
            fitness[(int)idx] = fitnessElite[i];
        }
    }

    private void busuedaSobrePoblacion() {
        IntStream.range(0, poblacion.length).parallel().forEach(k -> {
            if (Math.random() >= this.porcentajeMemetico) return;
            if (!(poblacion[k] instanceof CromosomaDrones)) return;
            aplicarBusquedaLocal(k);
        });
    }

    private void busquedaSobreElite() {
        IntStream.range(0, idxElite.length).parallel().forEach(k -> {
            if (Math.random() >= this.porcentajeMemetico) return;
            if (!(poblacion[idxElite[k]] instanceof CromosomaDrones)) return;
            aplicarBusquedaLocal(idxElite[k]);
        });
    }

    /**
     * aplica búsqueda local 2-Opt al 10% de la población
     *
     * por cada individuo seleccionado, prueba todas las inversiones posibles
     * de subrutas y como nos quedamos con la mejor se "desenredan" las rutas
     */
    private void aplicarBusquedaLocal(int idx) {
        CromosomaDrones ind = (CromosomaDrones) poblacion[idx];
        double fitActual = fitness[idx];
        int[] genesActuales = ind.getGenes();

        // probar todas las inversiones posibles de tramos (i, j)
        for (int i = 0; i < genesActuales.length - 1; i++) {
            for (int j = i + 2; j < genesActuales.length; j++) {
                // invertir el subtramo entre i+1 y j
                int[] nuevaRuta = invertirSubRuta(genesActuales, i + 1, j);

                // crear un clon con esa nueva ruta para evaluarla
                CromosomaDrones clon = (CromosomaDrones) ind.copia();
                clon.setGenes(nuevaRuta);

                // si "desenredar" la ruta mejora el fitness, nos lo quedamos
                double nuevaFit = clon.evaluar().getFitness();
                if (nuevaFit < fitActual) {
                    poblacion[idx] = clon;
                    fitness[idx] = nuevaFit;
                    ind = clon;
                    genesActuales = nuevaRuta;
                    fitActual = nuevaFit;
                }
            }
        }
    }

    // devuelve una copia de genes con el tramo [desde, hasta] invertido.
    private int[] invertirSubRuta(int[] genes, int desde, int hasta) {
        int[] nueva = genes.clone();
        int lo = desde,
            hi = hasta;
        while (lo < hi) {
            int tmp = nueva[lo];
            nueva[lo] = nueva[hi];
            nueva[hi] = tmp;
            lo++;
            hi--;
        }
        return nueva;
    }

    public List<ResEvaluacion> getOptimos() {
        if (!this.terminoEjecucion)
            return null;

        this.solucionesPareto.sort(Comparator.comparingDouble(a -> a.getFitness()));
        return this.solucionesPareto;
    }
}
