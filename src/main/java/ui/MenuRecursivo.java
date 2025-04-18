package ui;

import base.MenuGrafico;
import caballo.LanzadorCaballo;
import hanoi.LanzadorTorresHanoi;
import nreinas.LanzadorNReinas;
import javax.swing.*;
import java.awt.*;

/**
 * MenuRecursivo: ventana principal que muestra un menú con opciones para:
 *  • Problema del Caballo
 *  • Torres de Hanoi
 *  • Problema de las N Reinas
 *  • Salir
 *
 * Extiende MenuGrafico, que ya configura la ventana base y llama a initMenu().
 */
public class MenuRecursivo extends MenuGrafico {

    /** Constructor: fija el título de la ventana. */
    public MenuRecursivo() {
        super("Menú Principal");   // Llama al constructor de MenuGrafico
    }

    /**
     * initMenu(): aquí definimos los botones y sus acciones.
     * Se ejecuta desde el constructor de MenuGrafico.
     */
    @Override
    protected void initMenu() {
        // 1) Creamos un panel con GridLayout: 4 filas, 1 columna, y espacio de 10px
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        // 2) Botón para lanzar el Problema del Caballo
        JButton b1 = new JButton("Problema del Caballo");
        b1.addActionListener(e -> {
            dispose();                           // Cierra el menú
            LanzadorCaballo.ejecutarSimulacion(); // Arranca la simulación del caballo
        });
        panel.add(b1);  // Añadimos el botón al panel

        // 3) Botón para lanzar las Torres de Hanoi
        JButton b2 = new JButton("Torres de Hanoi");
        b2.addActionListener(e -> {
            dispose();                                // Cierra el menú
            LanzadorTorresHanoi.ejecutarSimulacion(); // Arranca la simulación de Hanoi
        });
        panel.add(b2);  // Añadimos el botón al panel

        // 4) Botón para lanzar el Problema de las N Reinas
        JButton b3 = new JButton("Problema de las N Reinas");
        b3.addActionListener(e -> {
            dispose();                              // Cierra el menú
            LanzadorNReinas.ejecutarSimulacion();  // Arranca la simulación de N Reinas
        });
        panel.add(b3);  // Añadimos el botón al panel

        // 5) Botón para salir de la aplicación
        JButton b4 = new JButton("Salir");
        b4.addActionListener(e -> System.exit(0)); // Cierra la aplicación
        panel.add(b4);  // Añadimos el botón al panel

        // 6) Añadimos el panel con los cuatro botones al centro de la ventana
        add(panel, BorderLayout.CENTER);
    }
}
