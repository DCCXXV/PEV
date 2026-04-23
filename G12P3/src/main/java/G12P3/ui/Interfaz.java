package G12P3.ui;

import java.awt.*;
import javax.swing.*;

public class Interfaz extends JFrame {

    public Interfaz() {
        setTitle("Programación Genética, Rover Lunar");
        setSize(1400, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //se crea el tablero
        Tablero tablero = new Tablero();

        //se crea la grafica
        Grafica grafica = new Grafica();

        //se crea el panel del fenotipo (arbol con el codigo del cromosoma)
        PanelFenotipo fenotipo = new PanelFenotipo();

        //se crea el panel de la configuracion, se le pasan las referencias anteriores
        Configuracion configuracion = new Configuracion(
            tablero,
            grafica,
            fenotipo
        );

        //panel en el que estan la configuracion el tablero y la grafica
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        topPanel.add(configuracion);
        topPanel.add(tablero);
        topPanel.add(grafica);

        //se pone debajo del anterior panel el del fenotipo
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(fenotipo, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main() {
        SwingUtilities.invokeLater(() -> new Interfaz().setVisible(true));
    }
}
