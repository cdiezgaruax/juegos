package caballo.modelo;

import base.modelo.AlgoritmoRecursivo;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * ProblemaCaballo: recorre todas las casillas de un tablero N×N con
 * un caballo, visitando cada casilla exactamente una vez.
 */
public class ProblemaCaballo extends AlgoritmoRecursivo {
    private int n;
    private int[][] tablero;
    private List<int[]> pasos;
    private int startX, startY;

    private int[] dx = {2,1,-1,-2,-2,-1,1,2};
    private int[] dy = {1,2,2,1,-1,-2,-2,-1};

    public ProblemaCaballo() {
        super("Problema del Caballo");
        pasos = new ArrayList<>();
    }

    @Override
    public void setParametros(int n) {
        this.n = n;
        this.parametro = n;
        tablero = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(tablero[i], -1);
        }
    }

    /** Fija la casilla de inicio seleccionada por el usuario. */
    public void setStart(int x, int y) {
        this.startX = x;
        this.startY = y;
    }

    @Override
    public void ejecutar() {
        // Marca la casilla inicial y arranca el backtracking
        tablero[startX][startY] = 0;
        pasos.add(new int[]{startX, startY});
        resolver(startX, startY, 1);
    }

    private boolean resolver(int x, int y, int mov) {
        if (mov == n * n) return true;
        Integer[] idx = {0,1,2,3,4,5,6,7};
        Arrays.sort(idx, (a,b) ->
                getDegree(x+dx[a], y+dy[a])
                        - getDegree(x+dx[b], y+dy[b])
        );
        for (int i : idx) {
            int nx = x + dx[i], ny = y + dy[i];
            if (nx>=0 && ny>=0 && nx<n && ny<n && tablero[nx][ny]==-1) {
                tablero[nx][ny] = mov;
                pasos.add(new int[]{nx, ny});
                if (resolver(nx, ny, mov+1)) return true;
                // backtrack
                tablero[nx][ny] = -1;
                pasos.remove(pasos.size()-1);
            }
        }
        return false;
    }

    private int getDegree(int x, int y) {
        int cnt = 0;
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i], ny = y + dy[i];
            if (nx>=0 && ny>=0 && nx<n && ny<n && tablero[nx][ny]==-1)
                cnt++;
        }
        return cnt;
    }

    @Override
    public List<int[]> getPasos() {
        return pasos;
    }

    /**
     * Guarda en un archivo la posición inicial y la secuencia de pasos.
     */
    @Override
    public void guardarResultado(String nombreArchivo) {
        try (PrintWriter w = new PrintWriter(nombreArchivo)) {
            w.println("Punto de inicio: [" + startX + ", " + startY + "]");
            w.println("Secuencia de movimientos:");
            for (int[] v : getPasos()) {
                w.println(vectorToString(v));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda en un archivo el tablero final mostrando el orden de visita.
     */
    public void guardarTablero(String nombreArchivo) {
        try (PrintWriter w = new PrintWriter(nombreArchivo)) {
            w.println("Tablero final (cada celda = número de paso):");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    w.printf("%4d", tablero[i][j]);
                }
                w.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String representar() {
        return "Problema del Caballo ("+n+"×"+n+"), empieza en ("+startX+","+startY+")";
    }
}
