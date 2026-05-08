package G12P3.ui;

import G12P3.ag.Cromosoma;
import G12P3.ag.mutacion.Mutacion;
import G12P3.ag.mutacion.MutacionAleatoria;
import G12P3.ag.mutacion.MutacionFuncional;
import G12P3.ag.mutacion.MutacionHoist;
import G12P3.ag.mutacion.MutacionSubarbol;
import G12P3.ag.mutacion.MutacionTerminal;
import G12P3.arbol.GeneradorArbol;
import G12P3.arbol.NodoAst;
import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class EjemploPG extends JPanel {

    private final JSpinner semilla;
    private final JSpinner profMax;
    private final JComboBox<String> mutacion;
    private final JButton generar;
    private final JButton aplicar;

    private final PanelFenotipo fenotipo;
    private Cromosoma original;

    public EjemploPG(PanelFenotipo fenotipo) {
        this.fenotipo = fenotipo;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("Semilla:"), gbc);
        gbc.gridx = 1;
        semilla = new JSpinner(new SpinnerNumberModel(173, 1, Integer.MAX_VALUE, 1));
        add(semilla, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("Profundidad máxima:"), gbc);
        gbc.gridx = 1;
        profMax = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));
        add(profMax, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        generar = new JButton("Generar cromosoma");
        generar.addActionListener(e -> generarCromosoma());
        add(generar, gbc);
        y++;

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("Tipo de mutación:"), gbc);
        gbc.gridx = 1;
        mutacion = new JComboBox<>(new String[] {
            "Aleatoria", "Sub-árbol", "Funcional", "Terminal", "Hoist"
        });
        add(mutacion, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        aplicar = new JButton("Aplicar mutación");
        aplicar.setEnabled(false);
        aplicar.addActionListener(e -> aplicarMutacion());
        add(aplicar, gbc);
        y++;

    }

    private void generarCromosoma() {
        long s = ((Number) semilla.getValue()).longValue();
        int prof = (int) profMax.getValue();
        NodoAst arbol = GeneradorArbol.crearGrow(0, prof - 1, new Random(s));
        original = new Cromosoma(arbol);
        original.evaluar(new long[]{ s }, 0.0);
        fenotipo.setMejor(original);
        aplicar.setEnabled(true);
    }

    private void aplicarMutacion() {
        if (original == null) return;
        Cromosoma mutado = original.clonar();
        long s = ((Number) semilla.getValue()).longValue();
        int prof = (int) profMax.getValue();
        Random rnd = new Random();
        String tipo = (String) mutacion.getSelectedItem();

        Mutacion mut = switch (tipo) {
            case "Sub-árbol" -> new MutacionSubarbol(rnd, prof - 1);
            case "Funcional"  -> new MutacionFuncional(rnd);
            case "Terminal"   -> new MutacionTerminal(rnd);
            case "Hoist"      -> new MutacionHoist(rnd);
            default           -> new MutacionAleatoria(rnd, prof - 1);
        };

        mut.mutar(mutado);
        mutado.evaluar(new long[]{ s }, 0.0);
        fenotipo.setMejor(mutado);
    }
}
