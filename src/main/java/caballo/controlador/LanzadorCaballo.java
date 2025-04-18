package caballo.controlador;

import base.controlador.Dialogos;
import caballo.modelo.ProblemaCaballo;
import caballo.vista.VentanaCaballo;
import ui.vista.MenuRecursivo;

/**
 * Ahora ya NO ejecutamos el algoritmo en el lanzador, sino que
 * dejamos que la ventana lo haga cuando el usuario pulse “Iniciar”.
 */
public class LanzadorCaballo {
    public static void ejecutarSimulacion() {
        Integer n = Dialogos.pedirEntero(
                "Introduce el tamaño del tablero (N):", 5, 69
        );
        if (n == null) {
            new MenuRecursivo().mostrarMenu();
            return;
        }

        ProblemaCaballo problema = new ProblemaCaballo();
        problema.setParametros(n);

        // Lanzamos la ventana: en ella el usuario elegirá la casilla y pulsará "Iniciar"
        new VentanaCaballo("Problema del Caballo", problema).mostrar();
    }
}
