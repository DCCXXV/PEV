package G12P2;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import java.util.function.Supplier;

public class SupplierFactory {

    /*
    mapas:
    - Museo
    - Pasillos
    - Supermercado
    */
    public static Supplier<Cromosoma> getMapa(
        String nombre,
        boolean ponderado,
        boolean real
    ) {
        if (!ponderado) return mapasSinPonderar(nombre);
        else return mapasPonderados(nombre);
    }

    private static Supplier<Cromosoma> mapasSinPonderar(String nombre) {
        switch (nombre) {
            case "Museo": {
                Scene scene = new Scene(Mapas.getMapa(nombre), false);
                int[][] camaras = {};
                return () -> new CromosomaDrones(4, 3, camaras, scene);
            }
            case "Pasillo": {
                Scene scene = new Scene(Mapas.getMapa(nombre), false);
                int[][] camaras = {};
                return () -> new CromosomaDrones(7, 5, camaras, scene);
            }
            case "SuperMercado": {
                Scene scene = new Scene(Mapas.getMapa(nombre), false);
                int[][] camaras = {};
                return () -> new CromosomaDrones(8, 7, camaras, scene);
            }
        }
        return null;
    }

    private static Supplier<Cromosoma> mapasPonderados(String nombre) {
        switch (nombre) {
            case "Museo": {
                Scene scene = new Scene(
                    Mapas.getMapa(nombre + "Ponderado"),
                    true
                );
                int[][] camaras = {};
                return () -> new CromosomaDrones(4, 3, camaras, scene);
            }
            case "Pasillo": {
                Scene scene = new Scene(
                    Mapas.getMapa(nombre + "Ponderado"),
                    true
                );
                int[][] camaras = {};
                return () -> new CromosomaDrones(7, 5, camaras, scene);
            }
            case "SuperMercado": {
                Scene scene = new Scene(
                    Mapas.getMapa(nombre + "Ponderado"),
                    true
                );
                int[][] camaras = {};
                return () -> new CromosomaDrones(8, 7, camaras, scene);
            }
        }
        return null;
    }
}
