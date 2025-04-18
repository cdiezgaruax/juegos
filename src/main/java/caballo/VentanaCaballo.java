package caballo;

import base.VentanaJuego;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * VentanaCaballo: ventana que muestra la simulación del caballo en un tablero N×N.
 * • Hereda de VentanaJuego, por lo que ya tiene los botones “Simular” y “Volver al Menú”.
 */
public class VentanaCaballo extends VentanaJuego {
    private ProblemaCaballo problema;    // Modelo que contiene la lógica del caballo
    private BoardPanel boardPanel;       // Panel donde dibujaremos tablero y movimientos
    private Timer timer;                 // Temporizador para avanzar automáticamente
    private int stepIndex = 0;           // Índice del siguiente paso a dibujar
    private List<int[]> pasos;           // Lista de posiciones [fila, columna] del recorrido

    /**
     * Constructor:
     * @param titulo   Texto que aparece en la barra de la ventana
     * @param problema Instancia del modelo ya inicializado y ejecutado
     */
    public VentanaCaballo(String titulo, ProblemaCaballo problema) {
        super(titulo);                   // Llama al constructor de VentanaJuego
        this.problema = problema;        // Guarda el modelo
        this.pasos = problema.getPasos();// Obtiene el listado de pasos
        initComponents();                // Monta el panel y configura los botones
    }

    /**
     * Aquí configuramos todo lo específico de esta vista:
     * • Añadimos el panel central (BoardPanel).
     * • Ajustamos qué hacen btnSimular y btnVolverMenu al pulsarlos.
     */
    @Override
    protected void initComponents() {
        // 1) CREAR Y AÑADIR EL PANEL CENTRAL
        boardPanel = new BoardPanel(problema.getParametro()); // problema.getParametro() es N
        add(boardPanel, BorderLayout.CENTER);

        // 2) CONFIGURAR EL BOTÓN “Simular” (heredado de VentanaJuego)
        btnSimular.setText("Iniciar Simulación");
        btnSimular.addActionListener(e -> iniciarSimulacion());

        // 3) CONFIGURAR EL BOTÓN “Volver al Menú”
        btnVolverMenu.setText("Volver al Menú");
        btnVolverMenu.addActionListener(e -> {
            detenerTimerSiCorre(); // Si el timer estaba corriendo, lo paramos
            dispose();             // Cerramos esta ventana
            // new MenuRecursivo().mostrarMenu(); // Descomenta si quieres reabrir tu menú
        });
    }

    /**
     * Método que lanza la simulación:
     * • Cada 500ms avanza un paso y llama a repaint() para redibujar.
     */
    private void iniciarSimulacion() {
        timer = new Timer(500, e -> {
            if (stepIndex < pasos.size()) {
                // 1) Obtener la posición actual [fila,col]
                int[] pos = pasos.get(stepIndex);
                // 2) Marcar el número de paso en esa casilla
                boardPanel.marcarPaso(stepIndex, pos[0], pos[1]);
                // 3) Guardar la posición para pintar el óvalo rojo
                boardPanel.setCurrentMove(pos);
                // 4) Pedir que se redibuje el panel con los nuevos datos
                boardPanel.repaint();
                // 5) Avanzar al siguiente paso
                stepIndex++;
            } else {
                // Cuando terminen los pasos, paramos el timer
                detenerTimerSiCorre();
            }
        });
        timer.start(); // Iniciar el temporizador
    }

    /** Si el timer existe y sigue en marcha, lo detenemos. */
    private void detenerTimerSiCorre() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    /**
     * Clase interna que dibuja:
     * • La cuadrícula N×N
     * • Los números de paso en color azul
     * • Un óvalo rojo en la posición actual del caballo
     */
    private class BoardPanel extends JPanel {
        private int n;               // Cuántas casillas por fila/columna
        private int[][] simBoard;    // Matriz de simulación: -1=sin visitar, >=0 = número de paso
        private int[] currentMove;   // Coordenadas [fila, columna] del óvalo rojo

        /** Constructor: crea la matriz y la inicializa a -1 (todas sin visitar). */
        public BoardPanel(int n) {
            this.n = n;
            simBoard = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    simBoard[i][j] = -1; // Marcamos cada casilla como “no visitada”
                }
            }
        }

        /**
         * Guardamos en la matriz simulada el número de paso en la casilla (r,c).
         * @param number número de paso
         * @param r      fila
         * @param c      columna
         */
        public void marcarPaso(int number, int r, int c) {
            simBoard[r][c] = number;
        }

        /**
         * Guardamos la posición actual para que paintComponent pinte el óvalo rojo ahí.
         * @param pos array {fila, columna}
         */
        public void setCurrentMove(int[] pos) {
            currentMove = pos;
        }

        /**
         * Dibuja la cuadrícula, los números y el óvalo rojo.
         * Se llama automáticamente cuando hacemos repaint().
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 1) CALCULAR TAMAÑOS
            int w = getWidth();          // ancho total del panel
            int h = getHeight();         // alto total del panel
            int cellW = w / n;           // ancho de cada casilla
            int cellH = h / n;           // alto de cada casilla

            // 2) PINTAR CUADRÍCULA
            g.setColor(Color.BLACK);
            for (int i = 0; i <= n; i++) {
                // línea horizontal
                g.drawLine(0, i * cellH, w, i * cellH);
                // línea vertical
                g.drawLine(i * cellW, 0, i * cellW, h);
            }

            // 3) ESCRIBIR NÚMEROS DE PASO EN AZUL
            g.setColor(Color.BLUE);
            FontMetrics fm = g.getFontMetrics();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int val = simBoard[i][j];
                    if (val != -1) {
                        String txt = String.valueOf(val);
                        // calcular posición centrada del texto
                        int tx = j * cellW + (cellW - fm.stringWidth(txt)) / 2;
                        int ty = i * cellH + (cellH + fm.getAscent()) / 2;
                        g.drawString(txt, tx, ty);
                    }
                }
            }

            // 4) DIBUJAR ÓVALO ROJO EN currentMove
            if (currentMove != null) {
                int r = currentMove[0];
                int c = currentMove[1];
                g.setColor(Color.RED);
                // hacemos un óvalo ocupando la mitad de la casilla
                g.fillOval(
                        c * cellW + cellW / 4,
                        r * cellH + cellH / 4,
                        cellW / 2,
                        cellH / 2
                );
            }
        }
    }
}
