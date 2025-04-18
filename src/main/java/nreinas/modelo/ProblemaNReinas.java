package nreinas.modelo;

import base.modelo.AlgoritmoRecursivo;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * ProblemaNReinas: resuelve el clásico problema de colocar N reinas en un tablero
 * de N×N de modo que no se ataquen entre sí.
 * • Hereda de AlgoritmoRecursivo → ObjetoBase, así que debe implementar representar()
 */
public class ProblemaNReinas extends AlgoritmoRecursivo {
    private int n;           // Tamaño del tablero (N)
    private int[][] board;   // Matriz: 0 = casilla vacía, 1 = reina colocada
    private List<int[]> pasos; // Lista de posiciones donde se colocan las reinas

    /** Constructor: indica el nombre al padre y crea la lista vacía de pasos */
    public ProblemaNReinas() {
        super("Problema de las N Reinas");  // Le decimos a ObjetoBase el nombre
        pasos = new ArrayList<>();          // Inicializamos la lista de pasos
    }

    /**
     * setParametros: aquí recibimos N y preparamos la matriz.
     * @param n tamaño del tablero
     */
    @Override
    public void setParametros(int n) {
        // Validación: N debe ser >=4 (excepto N=1, trivial)
        if (n < 4 && n != 1) {
            throw new IllegalArgumentException(
                    "El tablero debe ser de al menos 4x4 (o 1x1).");
        }
        this.n = n;            // Guardamos N en el objeto
        this.parametro = n;    // También en el campo heredado
        board = new int[n][n]; // Creamos la matriz N×N
        // Inicializamos todas las casillas a 0 (vacías)
        for (int i = 0; i < n; i++) {
            Arrays.fill(board[i], 0);
        }
    }

    /**
     * ejecutar: lanza el backtracking empezando por la fila 0.
     */
    @Override
    public void ejecutar() {
        // Si solve(0) devuelve false, no hay solución (muy raro para N>=4)
        if (!solve(0)) {
            System.out.println(
                    "No se encontró solución para un tablero de " + n + "x" + n);
        }
    }

    /**
     * solve: método recursivo que coloca reinas fila a fila.
     * @param row índice de la fila donde intentamos colocar una reina
     * @return true si coloca todas con éxito, false para backtracking
     */
    private boolean solve(int row) {
        // Caso base: ya colocamos reinas en todas las filas
        if (row == n) {
            // Limpiamos la lista de pasos para llenarla con la solución final
            pasos.clear();
            // Recorrer la matriz y guardar cada posición de reina en pasos
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    if (board[r][c] == 1) {
                        pasos.add(new int[]{r, c});
                    }
                }
            }
            return true; // ¡Éxito!
        }
        // Intentar colocar una reina en cada columna de esta fila
        for (int col = 0; col < n; col++) {
            // isSafe devuelve true si es seguro poner reina aquí
            if (isSafe(row, col)) {
                board[row][col] = 1; // Colocamos la reina
                // Llamada recursiva para la siguiente fila
                if (solve(row + 1)) {
                    return true;      // Si la siguiente fila funciona, acabamos
                }
                board[row][col] = 0; // Si no funciona, quitamos la reina (backtracking)
            }
        }
        return false; // No hay posición válida en esta fila, retrocedemos
    }

    /**
     * isSafe: comprueba que no haya otra reina en la misma columna o diagonales.
     * @param row fila donde queremos colocar
     * @param col columna donde queremos colocar
     * @return true si está libre de ataques
     */
    private boolean isSafe(int row, int col) {
        // 1) Verificar columna hacia arriba
        for (int i = 0; i < row; i++) {
            if (board[i][col] == 1) return false;
        }
        // 2) Verificar diagonal superior izquierda
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 1) return false;
        }
        // 3) Verificar diagonal superior derecha
        for (int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++) {
            if (board[i][j] == 1) return false;
        }
        return true; // Si pasa todas, es seguro
    }

    /** Devuelve la lista de posiciones de las reinas colocadas (fila, columna). */
    @Override
    public List<int[]> getPasos() {
        return pasos;
    }

    /** Permite acceder a la matriz final con 0 y 1 */
    public int[][] getTablero() {
        return board;
    }

    /**
     * Guarda el tablero final en un archivo de texto.
     * 0 = celda libre, 1 = reina.
     * @param nombreArchivo ruta o nombre del fichero de salida
     */
    public void guardarTableroFinal(String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(nombreArchivo)) {
            writer.println("Tablero final (0: celda libre, 1: reina):");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // Escribimos cada valor con un espacio
                    writer.printf("%2d ", board[i][j]);
                }
                writer.println(); // Nueva línea al acabar la fila
            }
        } catch (IOException e) {
            e.printStackTrace(); // Si falla escribir el archivo, mostramos el error
        }
    }

    /**
     * representar: obligatorio por ObjetoBase.
     * Devuelve un texto descriptivo de este problema.
     */
    @Override
    public String representar() {
        return "Problema de las N Reinas en un tablero de " + n + "x" + n;
    }
}
