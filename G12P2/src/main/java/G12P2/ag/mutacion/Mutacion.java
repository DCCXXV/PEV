package G12P2.ag.mutacion;

import G12P2.cromosomas.Cromosoma;

public interface Mutacion {
    void mutar(Cromosoma cromosoma, double probMutacion);
}
