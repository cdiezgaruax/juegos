package hanoi.controlador;

import base.controlador.Dialogos;
import hanoi.modelo.ProblemaTorresHanoi;
import hanoi.vista.VentanaTorresHanoi;
import ui.vista.MenuRecursivo;

public class LanzadorTorresHanoi {
    public static void ejecutarSimulacion() {
        // Pedimos número de discos entre 1 y, por ejemplo, 28
        Integer n = Dialogos.pedirEntero(
                "Introduce el número de discos:", 1, 28
        );
        if (n == null) {
            new MenuRecursivo().mostrarMenu();
            return;
        }

        ProblemaTorresHanoi problema = new ProblemaTorresHanoi();
        problema.setParametros(n);
        problema.ejecutar();

        String resultadoFile = "resultadoHanoi_" + n + "discos.txt";
        problema.guardarResultado(resultadoFile);

        new VentanaTorresHanoi("Torres de Hanoi", problema).mostrar();
    }
}
