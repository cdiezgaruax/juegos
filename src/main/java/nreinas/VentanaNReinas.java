package nreinas;

import base.VentanaJuego;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

/**
 * VentanaNReinas: ventana que muestra paso a paso la solución
 * del problema de las N Reinas en un tablero N×N.
 * Hereda de VentanaJuego, que ya proporciona:
 *   • btnSimular   — botón para iniciar la animación
 *   • btnVolverMenu — botón para cerrar esta ventana
 */
public class VentanaNReinas extends VentanaJuego {
    private ProblemaNReinas problema;        // Modelo con la lógica de N reinas
    private NReinasPanel nReinasPanel;       // Panel donde se dibuja el tablero
    private Timer timer;                     // Temporizador para la animación
    private int stepIndex = 0;               // Contador del paso actual
    private List<int[]> pasos;               // Lista de posiciones [fila,col] de cada reina

    /**
     * Constructor:
     * 1) Llama al constructor de VentanaJuego para fijar el título.
     * 2) Guarda el modelo y la lista de pasos resultante.
     * 3) Llama a initComponents() para montar la interfaz.
     */
    public VentanaNReinas(String titulo, ProblemaNReinas problema) {
        super(titulo);
        this.problema = problema;
        // Hacemos copia de la lista de pasos para no alterar el modelo original
        this.pasos = new ArrayList<>(problema.getPasos());
        initComponents();  // Configura panel y botones
    }

    /**
     * initComponents: método que monta los componentes específicos:
     * • El panel central con el tablero
     * • Configura los listeners de los botones heredados
     */
    @Override
    protected void initComponents() {
        // 1) CREAR Y AÑADIR EL PANEL DE DIBUJO EN EL CENTRO
        nReinasPanel = new NReinasPanel();
        add(nReinasPanel, BorderLayout.CENTER);

        // 2) CONFIGURAR el botón "Simular" (btnSimular viene de VentanaJuego)
        btnSimular.setText("Iniciar Simulación");
        btnSimular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSimulacion();  // Arranca el timer que dibuja reina por reina
            }
        });

        // 3) CONFIGURAR el botón "Volver al Menú"
        btnVolverMenu.setText("Volver al Menú");
        btnVolverMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Cierra esta ventana
                // Si tienes un menú principal, descomenta la línea:
                // new MenuRecursivo().mostrar();
            }
        });
    }

    /**
     * iniciarSimulacion: crea un Timer que cada 500 ms:
     * • Obtiene la siguiente posición de reina
     * • La añade al panel y repinta
     * • Aumenta stepIndex hasta acabar la lista
     */
    private void iniciarSimulacion() {
        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Si aún quedan posiciones por mostrar
                if (stepIndex < pasos.size()) {
                    // 1) Agregar la próxima reina al panel interno
                    nReinasPanel.agregarReina(pasos.get(stepIndex));
                    stepIndex++;           // 2) Avanzar al siguiente paso
                    nReinasPanel.repaint(); // 3) Redibujar el panel
                } else {
                    timer.stop();          // 4) Si ya no hay pasos, detener el timer
                }
            }
        });
        timer.start();  // Inicia la animación
    }

    /**
     * NReinasPanel: panel interno donde se dibuja el tablero y las reinas.
     */
    private class NReinasPanel extends JPanel {
        private int n;                       // Tamaño del tablero
        private int cellSize;                // Tamaño de cada casilla en píxeles
        private List<int[]> reinasDibujadas; // Posiciones de reinas ya mostradas

        /**
         * Constructor:
         *   • Obtiene N del modelo
         *   • Inicializa la lista de reinas dibujadas vacía
         */
        public NReinasPanel() {
            this.n = problema.getParametro();
            this.reinasDibujadas = new ArrayList<>();
        }

        /**
         * agregarReina: añade una nueva posición de reina para dibujarla
         * @param pos array {fila, columna}
         */
        public void agregarReina(int[] pos) {
            reinasDibujadas.add(pos);
        }

        /**
         * paintComponent: método llamado cada vez que repaint() se invoca.
         *   • Dibuja el tablero centrado
         *   • Dibuja las reinas colocadas hasta el momento
         *   • Dibuja un borde alrededor
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int w = getWidth(), h = getHeight();
            // Calcula el tamaño de cada casilla en función del menor lado
            cellSize = Math.min(w, h) / n;
            // Dimensiones totales del tablero
            int boardW = n * cellSize, boardH = n * cellSize;
            // Offset para centrar el tablero en el panel
            int offsetX = (w - boardW) / 2;
            int offsetY = (h - boardH) / 2;

            // 1) Dibujar las casillas del tablero
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // Alternar colores claro/oscuro como en un ajedrez
                    g.setColor((i + j) % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                    g.fillRect(offsetX + j * cellSize,
                            offsetY + i * cellSize,
                            cellSize,
                            cellSize);
                }
            }

            // 2) Dibujar las reinas que ya hemos agregado
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, cellSize / 2));
            FontMetrics fm = g.getFontMetrics();
            for (int[] pos : reinasDibujadas) {
                int r = pos[0], c = pos[1];
                String q = "Q";
                // Centrar la letra dentro de la casilla
                int sw = fm.stringWidth(q);
                int sh = fm.getAscent();
                int x = offsetX + c * cellSize + (cellSize - sw) / 2;
                int y = offsetY + r * cellSize + (cellSize + sh) / 2;
                g.drawString(q, x, y);
            }

            // 3) Dibujar un borde negro alrededor del tablero
            g.setColor(Color.BLACK);
            g.drawRect(offsetX, offsetY, boardW, boardH);
        }
    }
}
