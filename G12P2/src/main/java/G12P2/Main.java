package G12P2;

import G12P2.evaluacion.EvaluacionDrones;

public class Main {
    public static void main(String[] args) {

        Scene escena = new Scene(Mapas.getMapa("SuperMercadoPonderado"));
        EvaluacionDrones.evaluar(escena, null);
    }
}
