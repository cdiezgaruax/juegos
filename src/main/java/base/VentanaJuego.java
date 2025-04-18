package base;

import javax.swing.*;
import java.awt.*;

/**
 * Clase base para todas las ventanas de juegos.
 * Proporciona botones comunes y un esqueleto para la vista.
 */
public abstract class VentanaJuego extends JFrame {

    // Botón para iniciar o simular la acción propia del juego/algoritmo
    protected JButton btnSimular;

    // Botón para volver al menú principal o cerrar la ventana actual
    protected JButton btnVolverMenu;

    /**
     * Constructor: configura la ventana con título, tamaño y posición.
     * @param titulo Texto que aparece en la barra de la ventana.
     */
    public VentanaJuego(String titulo) {
        super(titulo);                             // Pone el título de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Al cerrar la ventana, termina la aplicación
        setSize(600, 400);                         // Define un tamaño fijo de 600×400 píxeles
        setLocationRelativeTo(null);               // Centra la ventana en la pantalla
        initComponentesBase();                     // Añade los botones comunes al pie de la ventana
        // Nota: initComponents() se llamará desde la subclase
    }

    /**
     * Crea y añade los botones "Simular" y "Volver al Menú" en la parte inferior.
     */
    private void initComponentesBase() {
        // Creamos un panel con FlowLayout centrado para contener los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnSimular    = new JButton("Simular");      // Botón para arrancar la simulación
        btnVolverMenu = new JButton("Volver al Menú"); // Botón para cerrar o volver atrás

        // Añadimos los botones al panel
        panelBotones.add(btnSimular);
        panelBotones.add(btnVolverMenu);

        // Colocamos el panel de botones en la zona SUR (abajo) de la ventana
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Cada subclase debe implementar este método para:
     * - Añadir su panel central (por ejemplo, tablero)
     * - Configurar listeners en btnSimular y btnVolverMenu
     */
    protected abstract void initComponents();

    /**
     * Muestra la ventana en pantalla.
     * Llama internamente a setVisible(true).
     */
    public void mostrar() {
        setVisible(true);
    }
}
