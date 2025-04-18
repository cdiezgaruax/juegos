package caballo;

import javax.swing.JOptionPane;

/**
 * LanzadorCaballo: controla el flujo de la simulación del caballo.
 * 1) Pide al usuario el tamaño del tablero.
 * 2) Crea y configura el modelo ProblemaCaballo.
 * 3) Ejecuta la lógica recursiva.
 * 4) Guarda los resultados en archivos de texto.
 * 5) Lanza la ventana gráfica para ver la simulación.
 */
public class LanzadorCaballo {
    public static void ejecutarSimulacion() {
        int n;

        // 1) Pedir tamaño del tablero hasta que sea válido
        while (true) {
            String input = JOptionPane.showInputDialog("Introduce el tamaño del tablero (N):");
            try {
                n = Integer.parseInt(input);
                if (n < 5) {
                    JOptionPane.showMessageDialog(null,
                            "El tablero es demasiado pequeño. Debe ser de al menos 5x5.");
                } else if (n > 69) {
                    JOptionPane.showMessageDialog(null,
                            "El tablero es demasiado grande. Debe ser de máximo 69x69.");
                } else {
                    break; // Ya tenemos un N válido
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                        "Entrada inválida. Por favor, ingresa un número entero.");
            }
        }

        // 2) Crear el modelo
        //    ProblemaCaballo extiende AlgoritmoRecursivo → extiende ObjetoBase
        ProblemaCaballo problema = new ProblemaCaballo();
        problema.setParametros(n);  // Le decimos N
        problema.ejecutar();        // Arrancamos la recursión

        // 3) Preparar nombres de archivo con N
        String resultadoFile = "resultadoCaballo_" + n + "x" + n + ".txt";
        String tableroFile   = "tableroFinalCaballo_" + n + "x" + n + ".txt";

        // 4) Guardar la lista de movimientos y la matriz final
        problema.guardarResultado(resultadoFile); // Lista de vectores [fila,col]
        problema.guardarTablero(tableroFile);     // Matriz con el orden de visita

        // 5) Crear y mostrar la vista
        //    VentanaCaballo extiende VentanaJuego → extiende JFrame
        VentanaCaballo ventana = new VentanaCaballo("Problema del Caballo", problema);
        ventana.mostrar();  // setVisible(true)
    }
}
