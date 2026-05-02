package G12P3.ui;

import java.awt.*;
import javax.swing.*;

public class Interfaz extends JFrame {

    public Interfaz() {
        setTitle("Programación Genética y Gramáticas Evolutivas, Rover Lunar");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // tablero, grafica y panel de fenotipo se comparten entre los dos modos
        Tablero tablero = new Tablero();
        Grafica grafica = new Grafica();
        PanelFenotipo fenotipo = new PanelFenotipo();

        Configuracion configuracionPG = new Configuracion(tablero, grafica, fenotipo);
        ConfiguracionGE configuracionGE = new ConfiguracionGE(tablero, grafica, fenotipo);

        // tabs para alternar entre PG y GE: cada uno con sus propios cruces y mutaciones
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Programación Genética", configuracionPG);
        tabs.addTab("Gramáticas Evolutivas", configuracionGE);
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedComponent() == configuracionGE) {
                configuracionGE.inicializarVista();
            }
        });

        JPanel topPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        topPanel.add(tabs);
        topPanel.add(tablero);
        topPanel.add(grafica);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(fenotipo, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main() {
        SwingUtilities.invokeLater(() -> new Interfaz().setVisible(true));
    }
}
