package G12P3.ge;

import G12P3.ag.Cromosoma;

// cromosoma para gramaticas evolutivas. el genotipo es un array de codones
// (enteros 0..255) y el fenotipo es el AST que se obtiene al decodificarlos
public class CromosomaGE extends Cromosoma {

    public int[] codones;
    public final int profMaxDecoder;

    public CromosomaGE(int[] codones, int profMaxDecoder) {
        super(null);
        this.codones = codones;
        this.profMaxDecoder = profMaxDecoder;
        this.arbol = Decoder.decodificar(codones, profMaxDecoder);
    }

    // se llama tras cada cruce/mutacion para reflejar el cambio en el fenotipo
    public void redecodificar() {
        this.arbol = Decoder.decodificar(codones, profMaxDecoder);
    }

    @Override
    public Cromosoma clonar() {
        CromosomaGE copia = new CromosomaGE(codones.clone(), profMaxDecoder);
        copia.fitness = this.fitness;
        copia.fitnessBase = this.fitnessBase;
        copia.nodos = this.nodos;
        copia.profundidadMax = this.profundidadMax;
        copia.datosPorMapa = this.datosPorMapa;
        return copia;
    }
}
