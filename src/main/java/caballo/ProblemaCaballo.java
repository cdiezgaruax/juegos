package caballo;

import base.AlgoritmoRecursivo;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * ProblemaCaballo: recorre todas las casillas de un tablero N×N con
 * un caballo de ajedrez, visitando cada casilla exactamente una vez.
 *
 * Hereda de AlgoritmoRecursivo → ObjetoBase, por lo que:
 * - Debe implementar representar().
 * - Ya hereda guardarResultado(), getParametro(), etc.
 */
public class ProblemaCaballo extends AlgoritmoRecursivo {
    // ------------- ATRIBUTOS -------------
    private int n;                // Tamaño del tablero (N)
    private int[][] tablero;      // Matriz que guarda el orden de visita
    private List<int[]> pasos;    // Lista de movimientos [fila, columna]

    // Estas dos arrays guardan los 8 saltos posibles del caballo
    private int[] dx = {2,1,-1,-2,-2,-1,1,2};
    private int[] dy = {1,2,2,1,-1,-2,-2,-1};

    // ------------- CONSTRUCTOR -------------
    public ProblemaCaballo() {
        super("Problema del Caballo");  // Le decimos a la superclase el nombre
        pasos = new ArrayList<>();      // Inicializamos la lista de pasos vacía
    }

    // ------------- SET PARAMETROS -------------
    @Override
    public void setParametros(int n) {
        // 1) Validamos que N sea al menos 5 (para que exista solución)
        if (n < 5) {
            throw new IllegalArgumentException("N mínimo 5 para que exista solución.");
        }
        // 2) Guardamos N en nuestro campo y en el campo heredado 'parametro'
        this.n = n;
        this.parametro = n;
        // 3) Creamos la matriz de N×N y la llenamos de -1 (no visitado)
        tablero = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(tablero[i], -1);
        }
    }

    // ------------- EJECUTAR ALGORITMO -------------
    @Override
    public void ejecutar() {
        // 1) Marcamos la primera casilla (0,0) con el paso 0
        tablero[0][0] = 0;
        // 2) Añadimos ese primer paso a la lista
        pasos.add(new int[]{0, 0});
        // 3) Llamamos al método recursivo que hará el backtracking
        resolver(0, 0, 1);
    }

    // ------------- MÉTODO RECURSIVO (BACKTRACKING) -------------
    /**
     * Trata de mover el caballo desde (x,y) usando mov como número de paso.
     * @return true si completa el recorrido, false si no hay solución desde aquí.
     */
    private boolean resolver(int x, int y, int mov) {
        // Si ya dimos N*N pasos, hemos visitado todo el tablero
        if (mov == n * n) {
            return true;
        }

        // 1) Creamos un array de índices 0..7 para ordenar por heurística Warnsdorff
        Integer[] idx = {0,1,2,3,4,5,6,7};
        // 2) Ordenamos esos índices según el grado (menor primero)
        Arrays.sort(idx, (a, b) ->
                getDegree(x + dx[a], y + dy[a])
                        - getDegree(x + dx[b], y + dy[b])
        );

        // 3) Probamos cada movimiento en ese nuevo orden
        for (int i : idx) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            // 4) Verificamos que la siguiente casilla exista y esté libre (-1)
            if (nx >= 0 && ny >= 0 && nx < n && ny < n && tablero[nx][ny] == -1) {
                // 5) Marcamos ese paso
                tablero[nx][ny] = mov;
                pasos.add(new int[]{nx, ny});
                // 6) Llamamos recursivamente con mov+1
                if (resolver(nx, ny, mov + 1)) {
                    return true;  // Si esa rama funciona, propagamos el éxito
                }
                // 7) Si no funciona, deshacemos el paso (backtracking)
                tablero[nx][ny] = -1;
                pasos.remove(pasos.size() - 1);
            }
        }
        // Si ninguno de los movimientos conduce a solución, devolvemos false
        return false;
    }

    // ------------- HEURÍSTICA WARNDSORFF -------------
    /**
     * Cuenta cuántos movimientos válidos hay desde (x,y).
     * Sirve para elegir primero casillas con menos salidas.
     */
    private int getDegree(int x, int y) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx >= 0 && ny >= 0 && nx < n && ny < n && tablero[nx][ny] == -1) {
                count++;
            }
        }
        return count;
    }

    // ------------- GETTERS -------------
    @Override
    public List<int[]> getPasos() {
        return pasos;    // Devuelve la lista de movimientos
    }

    /**
     * Si alguien quiere ver la matriz final con el orden de visita:
     */
    public int[][] getTablero() {
        return tablero;
    }

    // ------------- GUARDAR TABLERO EN TXT -------------
    /**
     * Escribe en un archivo la matriz final (cada celda con su número de paso).
     */
    public void guardarTablero(String nombreArchivo) {
        try (PrintWriter w = new PrintWriter(nombreArchivo)) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    w.printf("%4d", tablero[i][j]);
                }
                w.println();
            }
        } catch (IOException e) {
            e.printStackTrace();  // En caso de error al escribir archivo
        }
    }

    // ------------- MéTODO OBLIGATORIO representar() -------------
    /**
     * Método que pide ObjetoBase: devuelve un texto que describa este problema.
     */
    @Override
    public String representar() {
        return "Problema del Caballo (" + n + "×" + n + ")";
    }
}
