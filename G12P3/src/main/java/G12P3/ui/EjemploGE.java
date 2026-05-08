package G12P3.ui;

import G12P3.ag.mutacion.Mutacion;
import G12P3.ge.CromosomaGE;
import G12P3.ge.mutacion.MutacionBasicaGE;
import G12P3.ge.mutacion.MutacionGaussianaGE;
import G12P3.ge.mutacion.MutacionInsercionGE;
import G12P3.ge.mutacion.MutacionIntercambioGE;
import G12P3.ge.mutacion.MutacionInversionGE;
import G12P3.ge.mutacion.MutacionScrambleGE;
import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class EjemploGE extends JPanel {

    private final JSpinner semilla;
    private final JSpinner longCromosoma;
    private final JSpinner profDecoder;
    private final JComboBox<String> mutacion;
    private final JButton generar;
    private final JButton usar;
    private final JButton aplicar;
    private final JTextArea arrayOriginal;
    private final JTextArea arrayMutado;

    private final PanelFenotipo fenotipo;
    private CromosomaGE original;

    public EjemploGE(PanelFenotipo fenotipo) {
        this.fenotipo = fenotipo;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("Semilla:"), gbc);
        gbc.gridx = 1;
        semilla = new JSpinner(new SpinnerNumberModel(99, 1, Integer.MAX_VALUE, 1));
        add(semilla, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("Longitud cromosoma:"), gbc);
        gbc.gridx = 1;
        longCromosoma = new JSpinner(new SpinnerNumberModel(20, 1, 1000, 1));
        add(longCromosoma, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("Profundidad decoder:"), gbc);
        gbc.gridx = 1;
        profDecoder = new JSpinner(new SpinnerNumberModel(1000, 2, 100000, 1));
        add(profDecoder, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        generar = new JButton("Generar cromosoma");
        generar.addActionListener(e -> generarCromosoma());
        add(generar, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1; gbc.weighty = 1;
        arrayOriginal = new JTextArea(3, 30);
        arrayOriginal.setEditable(true);
        arrayOriginal.setLineWrap(true);
        arrayOriginal.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollOrig = new JScrollPane(arrayOriginal);
        scrollOrig.setBorder(BorderFactory.createTitledBorder("Cromosoma original"));
        add(scrollOrig, gbc);
        y++;

        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0;
        gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0; gbc.gridy = y;
        usar = new JButton("Usar cromosoma escrito");
        usar.addActionListener(e -> usarCromosomaEscrito());
        add(usar, gbc);
        y++;

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("Tipo de mutación:"), gbc);
        gbc.gridx = 1;
        mutacion = new JComboBox<>(new String[] {
            "Básica", "Inversión", "Intercambio", "Inserción", "Scramble", "Gaussiana"
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

        gbc.gridx = 0; gbc.gridy = y;
        gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        arrayMutado = new JTextArea(3, 30);
        arrayMutado.setEditable(false);
        arrayMutado.setLineWrap(true);
        arrayMutado.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollMut = new JScrollPane(arrayMutado);
        scrollMut.setBorder(BorderFactory.createTitledBorder("Cromosoma mutado"));
        add(scrollMut, gbc);
    }

    private void generarCromosoma() {
        long s = ((Number) semilla.getValue()).longValue();
        int lon = (int) longCromosoma.getValue();
        int prof = (int) profDecoder.getValue();
        Random rnd = new Random(s);
        int[] codones = new int[lon];
        for (int i = 0; i < lon; i++) codones[i] = rnd.nextInt(256);
        original = new CromosomaGE(codones, prof - 1);
        original.evaluar(new long[]{ s }, 0.0);
        arrayOriginal.setText(arrayAString(codones));
        arrayMutado.setText("");
        fenotipo.setMejor(original);
        aplicar.setEnabled(true);
    }

    private void usarCromosomaEscrito() {
        String texto = arrayOriginal.getText().trim();
        if (texto.isEmpty()) return;
        try {
            String[] partes = texto.split("[,\\s]+");
            int[] codones = new int[partes.length];
            for (int i = 0; i < partes.length; i++)
                codones[i] = Integer.parseInt(partes[i].trim()) & 0xFF;
            long s = ((Number) semilla.getValue()).longValue();
            int prof = (int) profDecoder.getValue();
            original = new CromosomaGE(codones, prof - 1);
            original.evaluar(new long[]{ s }, 0.0);
            arrayOriginal.setText(arrayAString(codones));
            arrayMutado.setText("");
            fenotipo.setMejor(original);
            aplicar.setEnabled(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Formato inválido. Escribe los codones separados por comas: 6, 130, 45, 200",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarMutacion() {
        if (original == null) return;
        CromosomaGE mutado = (CromosomaGE) original.clonar();
        long s = ((Number) semilla.getValue()).longValue();
        String tipo = (String) mutacion.getSelectedItem();

        Mutacion mut = switch (tipo) {
            case "Inversión"   -> new MutacionInversionGE(new Random());
            case "Intercambio" -> new MutacionIntercambioGE(new Random());
            case "Inserción"   -> new MutacionInsercionGE(new Random());
            case "Scramble"    -> new MutacionScrambleGE(new Random());
            case "Gaussiana"   -> new MutacionGaussianaGE(new Random());
            default            -> new MutacionBasicaGE(new Random());
        };

        mut.mutar(mutado);
        mutado.redecodificar();
        mutado.evaluar(new long[]{ s }, 0.0);
        arrayMutado.setText(arrayAString(mutado.codones));
        fenotipo.setMejor(mutado);
    }

    private String arrayAString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        return sb.toString();
    }
}
