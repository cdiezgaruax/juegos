package nreinas;

import javax.swing.JOptionPane;

/**
 * LanzadorNReinas: clase “controlador” que coordina toda la simulación de N Reinas:
 * 1) Pide al usuario el tamaño del tablero.
 * 2) Crea y configura el modelo (ProblemaNReinas).
 * 3) Ejecuta el algoritmo para encontrar una solución.
 * 4) Guarda el tablero final en un archivo.
 * 5) Lanza la ventana gráfica para visualizar la solución paso a paso.
 */
public class LanzadorNReinas {

    /** Método principal que inicia el flujo de la simulación. */
    public static void ejecutarSimulacion() {
        int n;  // Aquí guardaremos el tamaño N del tablero

        // 1) Bucle para pedir al usuario un número de tablero válido
        while (true) {
            // Mostrar cuadro de diálogo pidiendo N
            String input = JOptionPane.showInputDialog("Introduce el tamaño del tablero (N):");
            try {
                // Intentar convertir la cadena ingresada a un entero
                n = Integer.parseInt(input);

                // Para N Reinas, N debe ser >=4 (o igual a 1, caso trivial)
                if (n < 4 && n != 1) {
                    // Mostrar mensaje de error si N no cumple
                    JOptionPane.showMessageDialog(null,
                            "El tablero debe ser de al menos 4x4 (o 1x1).");
                } else {
                    // N es válido, salimos del bucle
                    break;
                }
            } catch (NumberFormatException ex) {
                // Si la conversión falla (no es un número), mostramos error y repetimos
                JOptionPane.showMessageDialog(null,
                        "Entrada inválida. Por favor, ingresa un número entero.");
            }
        }

        // 2) Crear y preparar el modelo para N Reinas
        ProblemaNReinas problema = new ProblemaNReinas();
        problema.setParametros(n);  // Le pasamos N al modelo
        problema.ejecutar();        // Ejecuta el backtracking y guarda la solución internamente

        // 3) Construir el nombre del archivo para guardar el tablero final
        //    Ejemplo: "tableroFinalNReinas_8x8.txt"
        String tableroFile = "tableroFinalNReinas_" + n + "x" + n + ".txt";
        // Guardar la matriz final en ese archivo
        problema.guardarTableroFinal(tableroFile);

        // 4) Crear la ventana que mostrará la simulación gráfica
        VentanaNReinas ventana = new VentanaNReinas(
                "Problema de las N Reinas",  // Título de la ventana
                problema                     // Modelo con la solución
        );

        // 5) Mostrar la ventana en pantalla
        ventana.mostrar();  // Internamente llama a setVisible(true)
    }
}
