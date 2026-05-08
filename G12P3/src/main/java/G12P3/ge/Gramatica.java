package G12P3.ge;

public class Gramatica {

    public static final String BNF =
        "<prog>        ::= <bloque> | <condicional>\n" +
        "<expr>        ::= <accion> | <condicional> | <bloque>\n" +
        "<bloque>      ::= <expr> <expr> | <expr> <expr> <expr>\n" +
        "<condicional> ::= IF ( <sensor> <op> <umbral> ) <expr>\n" +
        "                  ELSE <expr>\n" +
        "<accion>      ::= AVANZAR | AVANZAR\n" +
        "                  | GIRAR_IZQ | GIRAR_DER\n" +
        "<sensor>      ::= DIST_MUESTRA | DIST_ARENA\n" +
        "                  | DIST_OBSTACULO | NIVEL_ENERGIA\n" +
        "<op>          ::= < | > | ==\n" +
        "<umbral>      ::= 10 | 50 | 100\n";
}
