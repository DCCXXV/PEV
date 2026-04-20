package G12P3.ag.mutacion;

import G12P3.ag.Cromosoma;
import java.util.Random;

public class MutacionAleatoria implements Mutacion {

    private final Mutacion[] estrategias;
    private final Random rnd;

    public MutacionAleatoria(Random rnd, int profMax) {
        this.rnd = rnd;
        this.estrategias = new Mutacion[] {
            new MutacionTerminal(rnd),
            new MutacionFuncional(rnd),
            new MutacionSubarbol(rnd, profMax),
            new MutacionHoist(rnd),
        };
    }

    @Override
    public void mutar(Cromosoma cromosoma) {
        estrategias[rnd.nextInt(estrategias.length)].mutar(cromosoma);
    }
}
