package G12P3.ge;

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
