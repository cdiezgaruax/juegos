package nreinas.vista;

import base.vista.VentanaJuego;
import nreinas.controlador.LanzadorNReinas;
import nreinas.modelo.ProblemaNReinas;
import ui.vista.MenuRecursivo;

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
 *   • 🔃 (recargar) para volver a pedir N
 *   • btnSimular   — botón para iniciar la animación
 *   • btnVolverMenu — botón para volver al menú
 */
public class VentanaNReinas extends VentanaJuego {
    private final ProblemaNReinas problema;   // Modelo con la lógica
    private NReinasPanel nReinasPanel;        // Panel donde dibujamos el tablero
    private Timer timer;                      // Temporizador para la animación
    private int stepIndex = 0;                // Índice del paso actual
    private final List<int[]> pasos;          // Posiciones [fila,col] de cada reina

    public VentanaNReinas(String titulo, ProblemaNReinas problema) {
        super(titulo);
        this.problema = problema;
        this.pasos    = new ArrayList<>(problema.getPasos());
        initComponents();
    }

    @Override
    protected void initComponents() {
        // 1) Crear y añadir el panel de dibujo
        nReinasPanel = new NReinasPanel();
        add(nReinasPanel, BorderLayout.CENTER);

        // 2) Configurar botón "Iniciar Simulación"
        btnSimular.setText("Iniciar Simulación");
        btnSimular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Si ya corría, reiniciar
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                    stepIndex = 0;
                    nReinasPanel.reset();
                }
                iniciarSimulacion();
            }
        });

        // 3) Configurar botón "Volver al Menú"
        btnVolverMenu.setText("Volver al Menú");
        btnVolverMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer != null && timer.isRunning()) timer.stop();
                dispose();
                new MenuRecursivo().mostrarMenu();
            }
        });
    }

    private void iniciarSimulacion() {
        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stepIndex < pasos.size()) {
                    nReinasPanel.agregarReina(pasos.get(stepIndex));
                    stepIndex++;
                    nReinasPanel.repaint();
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    /**
     * Recarga la ventana: cierra esta instancia y vuelve a pedir N.
     */
    @Override
    protected void onReload() {
        if (timer != null && timer.isRunning()) timer.stop();
        dispose();
        LanzadorNReinas.ejecutarSimulacion();
    }

    /**
     * Panel interno que dibuja el tablero y las reinas colocadas.
     */
    private class NReinasPanel extends JPanel {
        private final int n;
        private int cellSize;
        private final List<int[]> reinasDibujadas = new ArrayList<>();

        public NReinasPanel() {
            this.n = problema.getParametro();
        }

        public void agregarReina(int[] pos) {
            reinasDibujadas.add(pos);
        }

        public void reset() {
            reinasDibujadas.clear();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth(), h = getHeight();
            cellSize = Math.min(w, h) / n;
            int boardW = n * cellSize, boardH = n * cellSize;
            int offsetX = (w - boardW) / 2, offsetY = (h - boardH) / 2;

            // Dibujar tablero
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    g.setColor((i + j) % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                    g.fillRect(offsetX + j*cellSize, offsetY + i*cellSize, cellSize, cellSize);
                }
            }

            // Usar una fuente con glifos Unicode completos
            g.setFont(new Font("Serif", Font.BOLD, cellSize/2));
            g.setColor(Color.RED);
            FontMetrics fm = g.getFontMetrics();
            String reina = "\u2655";  // ♕

            // Dibujar reinas
            for (int[] pos : reinasDibujadas) {
                int r = pos[0], c = pos[1];
                int sw = fm.stringWidth(reina), sh = fm.getAscent();
                int x = offsetX + c*cellSize + (cellSize - sw)/2;
                int y = offsetY + r*cellSize + (cellSize + sh)/2;
                g.drawString(reina, x, y);
            }

            // Borde
            g.setColor(Color.BLACK);
            g.drawRect(offsetX, offsetY, boardW, boardH);
        }
    }
}
