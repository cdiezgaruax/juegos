import ui.vista.MenuRecursivo;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // SwingUtilities.invokeLater se usa para arrancar
        // el código de la interfaz gráfica en el Event Dispatch Thread (EDT),
        // que es el hilo seguro para todas las operaciones de Swing.
        SwingUtilities.invokeLater(() ->
                // Aquí creamos una instancia de nuestro menú principal
                // y llamamos a setVisible(true) para mostrar la ventana.
                new MenuRecursivo().setVisible(true)
        );
    }
}
