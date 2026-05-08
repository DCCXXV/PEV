package G12P3.ui;

import java.awt.*;
import javax.swing.*;

public class Interfaz extends JFrame {

    public Interfaz() {
        setTitle("Programación Genética y Gramáticas Evolutivas, Rover Lunar");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Tablero tablero = new Tablero();
        PanelFenotipo fenotipo = new PanelFenotipo();

        Grafica graficaPG = new Grafica();
        Grafica graficaGE = new Grafica();

        Configuracion configuracionPG = new Configuracion(tablero, graficaPG, fenotipo);
        ConfiguracionGE configuracionGE = new ConfiguracionGE(tablero, graficaGE, fenotipo);
        EjemploPG ejemploPG = new EjemploPG(fenotipo);
        EjemploGE ejemploGE = new EjemploGE(fenotipo);

        JPanel graficaContainer = new JPanel(new CardLayout());
        graficaContainer.add(graficaPG, "PG");
        graficaContainer.add(graficaGE, "GE");

        // tabs para alternar entre PG y GE: cada uno con sus propios cruces y mutaciones
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("PG", configuracionPG);
        tabs.addTab("GE", configuracionGE);
        tabs.addTab("Ejemplo PG", ejemploPG);
        tabs.addTab("Ejemplo GE", ejemploGE);
        tabs.addChangeListener(e -> {
            CardLayout cl = (CardLayout) graficaContainer.getLayout();
            if (tabs.getSelectedComponent() == configuracionGE) {
                cl.show(graficaContainer, "GE");
                configuracionGE.inicializarVista();
            } else if (tabs.getSelectedComponent() == configuracionPG) {
                cl.show(graficaContainer, "PG");
            }
        });

        JPanel topPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        topPanel.add(tabs);
        topPanel.add(tablero);
        topPanel.add(graficaContainer);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(fenotipo, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main() {
        SwingUtilities.invokeLater(() -> new Interfaz().setVisible(true));
    }
}
