package G12P3.ui;

import G12P3.ag.Cromosoma;
import G12P3.arbol.NodoAccion;
import G12P3.arbol.NodoAst;
import G12P3.arbol.NodoBloque;
import G12P3.arbol.NodoCondicional;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

public class PanelFenotipo extends JPanel {

    private final JTextArea textoCodigo;
    private final JTree arbolVisual;
    private final JLabel resumen;

    public PanelFenotipo() {
        setLayout(new BorderLayout());
        setBorder(
            BorderFactory.createTitledBorder(
                "Estrategia del mejor individuo global"
            )
        );

        resumen = new JLabel(" ");
        resumen.setFont(new Font("Arial", Font.BOLD, 13));
        resumen.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        add(resumen, BorderLayout.NORTH);

        textoCodigo = new JTextArea(10, 50);
        textoCodigo.setEditable(false);
        textoCodigo.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textoCodigo.setBackground(new Color(250, 250, 245));

        arbolVisual = new JTree(new DefaultMutableTreeNode("(vacío)"));
        arbolVisual.setFont(new Font("Monospaced", Font.PLAIN, 12));
        arbolVisual.setRootVisible(true);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        arbolVisual.setCellRenderer(renderer);

        JScrollPane scrollCodigo = new JScrollPane(textoCodigo);
        scrollCodigo.setPreferredSize(new Dimension(0, 160));
        JScrollPane scrollArbol = new JScrollPane(arbolVisual);
        scrollArbol.setPreferredSize(new Dimension(0, 160));

        JSplitPane split = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            scrollCodigo,
            scrollArbol
        );
        split.setResizeWeight(0.5);
        split.setDividerLocation(0.5);
        add(split, BorderLayout.CENTER);
    }

    public void setMejor(Cromosoma c) {
        SwingUtilities.invokeLater(() -> {
            resumen.setText(
                String.format(
                    "Fitness: %.2f   |   Fitness base: %.2f   |   Profundidad: %d   |   Nodos: %d",
                    c.fitness,
                    c.fitnessBase,
                    c.profundidadMax,
                    c.nodos
                )
            );
            textoCodigo.setText(c.arbol.toString());
            textoCodigo.setCaretPosition(0);
            DefaultMutableTreeNode raiz = nodoATreeNode(c.arbol);
            arbolVisual.setModel(new DefaultTreeModel(raiz));
        });
    }

    public void limpiar() {
        SwingUtilities.invokeLater(() -> {
            resumen.setText(" ");
            textoCodigo.setText("");
            arbolVisual.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("(vacío)")));
        });
    }

    private DefaultMutableTreeNode nodoATreeNode(NodoAst nodo) {
        if (nodo instanceof NodoAccion) {
            return new DefaultMutableTreeNode("accion");
        }

        if (nodo instanceof NodoCondicional cond) {
            DefaultMutableTreeNode tn = new DefaultMutableTreeNode("condicional");
            DefaultMutableTreeNode ifNode = new DefaultMutableTreeNode("if");
            ifNode.add(nodoATreeNode(cond.getHijoTrue()));
            tn.add(ifNode);
            if (cond.getHijoFalse() != null) {
                DefaultMutableTreeNode elseNode = new DefaultMutableTreeNode("else");
                elseNode.add(nodoATreeNode(cond.getHijoFalse()));
                tn.add(elseNode);
            }
            return tn;
        }

        if (nodo instanceof NodoBloque bloque) {
            DefaultMutableTreeNode tn = new DefaultMutableTreeNode("bloque");
            for (NodoAst hijo : bloque.getHijos()) {
                tn.add(nodoATreeNode(hijo));
            }
            return tn;
        }

        return new DefaultMutableTreeNode("desconocido");
    }


}
