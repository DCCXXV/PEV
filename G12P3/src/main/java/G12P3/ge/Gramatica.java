package G12P3.ge;

// gramatica BNF utilizada por el decoder de gramaticas evolutivas.
// el numero de alternativas de cada regla se mantiene SINCRONIZADO con Decoder.
// la estructura es analoga a la del generador de PG (GeneradorArbol#crearGrow):
// cada <expr> es directamente una accion, un condicional o un bloque,
// evitando NodoBloque vacios que infloran innecesariamente el AST.
public class Gramatica {

    public static final String BNF =
        "<prog>        ::= <bloque>\n" +
        "<expr>        ::= <accion> | <condicional> | <bloque>          (3)\n" +
        "<bloque>      ::= <expr> <expr> | <expr> <expr> <expr>           (2)\n" +
        "<condicional> ::= IF ( <sensor> <op> <umbral> ) <expr>\n" +
        "                  ELSE <expr>                                    (1)\n" +
        "<accion>      ::= AVANZAR | AVANZAR | AVANZAR\n" +
        "                  | GIRAR_IZQ | GIRAR_DER                        (5, sesgo 60%)\n" +
        "<sensor>      ::= DIST_MUESTRA | DIST_ARENA\n" +
        "                  | DIST_OBSTACULO | NIVEL_ENERGIA               (4)\n" +
        "<op>          ::= < | > | ==                                     (3)\n" +
        "<umbral>      ::= 10 | 50 | 100                                  (3)\n";
}
