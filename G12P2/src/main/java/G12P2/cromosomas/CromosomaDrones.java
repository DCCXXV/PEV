package G12P2.cromosomas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import G12P2.Scene;

public class CromosomaDrones implements Cromosoma {

    private ArrayList<Double> velocidadDronI = new ArrayList<Double>(
        // veloz, estándar, pesado, ágil, tanque
        java.util.Arrays.asList(1.5, 1.0, 0.7, 1.2, 0.5)
    );

    private int[] genes;

    //numero de drones y numero de camaras
    private int D, C;

    private Scene scene;

    private static final Random rng = new Random();

    public CromosomaDrones(int D, int C, int[][] posCamaras, Scene scene) {
        this.D = D;
        this.C = C;
        this.scene = scene;
        this.genes = generarGenesAleatorios();
    }

    //copia
    public CromosomaDrones(CromosomaDrones other) {
        this.genes = other.getGenes().clone();
        this.D = other.getD();
        this.C = other.getC();
        this.scene = other.getScene();
    }

    // tamanio = C + (D-1).
    private int[] generarGenesAleatorios() {
        List<Integer> valores = new ArrayList<>(C + D - 1);
        // camaras
        for (int i = 1; i <= C; i++) valores.add(i);
        // separadores
        for (int i = C + 1; i <= C + D - 1; i++) valores.add(i);

        Collections.shuffle(valores, rng);

        return valores.stream().mapToInt(Integer::intValue).toArray();
    }

    public int[] getGenes() {
        return genes;
    }

    public void setGenes(int[] genes) {
        this.genes = genes;
    }

    public int getC() {
        return C;
    }

    public int getD() {
        return D;
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public int getRows() {
        return scene.getRows();
    }

    @Override
    public int getCols() {
        return scene.getCols();
    }

    @Override
    public int[][] generarMapa() {
        return null; // TODO
    }

    @Override
    public int evaluar() {
        return 0; // TODO
    }

    @Override
    public Cromosoma copia() {
        return new CromosomaDrones(this);
    }
}
