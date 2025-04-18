package hanoi;

import javax.swing.JOptionPane;

/**
 * LanzadorTorresHanoi: clase “controlador” que:
 * 1) Pide al usuario cuántos discos quiere usar.
 * 2) Crea el modelo (ProblemaTorresHanoi) con ese número.
 * 3) Ejecuta la lógica recursiva para mover los discos.
 * 4) Guarda la lista de movimientos en un archivo.
 * 5) Lanza la ventana gráfica para ver la simulación.
 */
public class LanzadorTorresHanoi {

    /** Método principal que inicia todo el proceso. */
    public static void ejecutarSimulacion() {
        int n;  // Número de discos que vamos a usar

        // 1) Bucle para pedir al usuario un número válido de discos
        while (true) {
            // Mostrar un cuadro de diálogo pidiendo el valor
            String input = JOptionPane.showInputDialog("Introduce el número de discos:");
            try {
                // Intentar convertir el texto ingresado a un entero
                n = Integer.parseInt(input);

                // Validar que al menos haya 1 disco
                if (n < 1) {
                    JOptionPane.showMessageDialog(null, "Debe haber al menos 1 disco.");
                } else {
                    // Si es válido, salimos del bucle
                    break;
                }
            } catch (NumberFormatException ex) {
                // Si no era un número, mostrar error y volver a pedir
                JOptionPane.showMessageDialog(null,
                        "Entrada inválida. Por favor, ingresa un número entero.");
            }
        }

        // 2) Crear y preparar el modelo de las Torres de Hanoi
        //    ProblemaTorresHanoi hereda de AlgoritmoRecursivo
        ProblemaTorresHanoi problema = new ProblemaTorresHanoi();
        problema.setParametros(n);  // Le decimos cuántos discos usar
        problema.ejecutar();        // Ejecuta la lógica recursiva (almacena movimientos)

        // 3) Construir un nombre de archivo para guardar la secuencia de movimientos
        //    Ejemplo: "resultadoHanoi_3discos.txt"
        String resultadoFile = "resultadoHanoi_" + n + "discos.txt";
        // Guardar los movimientos en ese archivo (método heredado de AlgoritmoRecursivo)
        problema.guardarResultado(resultadoFile);

        // 4) Crear la vista gráfica que mostrará la simulación paso a paso
        VentanaTorresHanoi ventana =
                new VentanaTorresHanoi("Torres de Hanoi", problema);

        // 5) Mostrar la ventana en pantalla (llama a setVisible(true))
        ventana.mostrar();
    }
}
