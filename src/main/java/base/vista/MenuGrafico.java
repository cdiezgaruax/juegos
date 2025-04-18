package base.vista;

import javax.swing.*;

/**
 * Clase abstracta que crea una ventana que será nuestro menú principal.
 * Todas las pantallas de selección (caballo, Hanoi, NReinas…) vendrán de aquí.
 */
public abstract class MenuGrafico extends JFrame {

    /**
     * Constructor del menú.
     * @param titulo Texto que aparecerá en la barra de la ventana.
     */
    public MenuGrafico(String titulo) {
        super(titulo);                       // Llama al padre (JFrame) para poner el título
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra todo al pulsar la X
        setSize(400, 300);                   // Le da un tamaño fijo a la ventana (400×300 px)
        setLocationRelativeTo(null);         // La centra en la pantalla automáticamente
        initMenu();                          // Llama al método que crea botones y opciones
    }

    /**
     * Método que debe implementar cada menú concreto (por ejemplo, el de caballo o el de Hanoi).
     * Aquí pones todos los botones para elegir juego, tamaño de tablero, etc.
     */
    protected abstract void initMenu();

    /**
     * Llama a setVisible(true) para mostrar la ventana en pantalla.
     * Úsalo cuando quieras que el menú aparezca.
     */
    public void mostrarMenu() {
        setVisible(true);
    }
}
