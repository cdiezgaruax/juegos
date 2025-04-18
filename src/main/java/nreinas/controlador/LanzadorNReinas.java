package nreinas.controlador;

import base.controlador.Dialogos;
import nreinas.modelo.ProblemaNReinas;
import nreinas.vista.VentanaNReinas;
import ui.vista.MenuRecursivo;
import javax.swing.JOptionPane;

public class LanzadorNReinas {
    public static void ejecutarSimulacion() {
        // Para N Reinas, rango mínimo 4 (o 1), máximo digamos 20
        Integer n = Dialogos.pedirEntero(
                "Introduce el tamaño del tablero (N):", 1, 20
        );
        if (n == null) {
            new MenuRecursivo().mostrarMenu();
            return;
        }
        // Requerimos n>=4 o n==1
        if (n < 4 && n != 1) {
            JOptionPane.showMessageDialog(null,
                    "Para N Reinas, el tablero debe ser de al menos 4x4 (o 1x1).");
            new MenuRecursivo().mostrarMenu();
            return;
        }

        ProblemaNReinas problema = new ProblemaNReinas();
        problema.setParametros(n);
        problema.ejecutar();

        String tableroFile = "tableroFinalNReinas_" + n + "x" + n + ".txt";
        problema.guardarTableroFinal(tableroFile);

        new VentanaNReinas("Problema de las N Reinas", problema).mostrar();
    }
}
