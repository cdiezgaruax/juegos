package ui.vista;

import base.vista.MenuGrafico;
import caballo.controlador.LanzadorCaballo;
import hanoi.controlador.LanzadorTorresHanoi;
import nreinas.controlador.LanzadorNReinas;
import javax.swing.*;
import java.awt.*;

public class MenuRecursivo extends MenuGrafico {

    public MenuRecursivo() {
        super("Menú Principal");
    }

    @Override
    protected void initMenu() {
        // Creamos un solo panel de 4 filas, 1 columna (exactamente 3 botones)
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton b1 = new JButton("Problema del Caballo");
        b1.addActionListener(e -> {
            dispose();
            LanzadorCaballo.ejecutarSimulacion();
        });
        panel.add(b1);

        JButton b2 = new JButton("Torres de Hanoi");
        b2.addActionListener(e -> {
            dispose();
            LanzadorTorresHanoi.ejecutarSimulacion();
        });
        panel.add(b2);

        JButton b3 = new JButton("Problema de las N Reinas");
        b3.addActionListener(e -> {
            dispose();
            LanzadorNReinas.ejecutarSimulacion();
        });
        panel.add(b3);

        // Un único add al CENTER: no debe haber más adds en este frame
        add(panel, BorderLayout.CENTER);
    }
}
