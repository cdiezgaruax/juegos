package hanoi.vista;

import base.vista.VentanaJuego;
import hanoi.controlador.LanzadorTorresHanoi;
import hanoi.modelo.ProblemaTorresHanoi;
import ui.vista.MenuRecursivo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * VentanaTorresHanoi: ventana que muestra paso a paso el movimiento
 * de los discos en el problema de las Torres de Hanoi.
 * • Hereda de VentanaJuego, que ya proporciona los botones “🔃”, “Simular” y “Volver al Menú”.
 */
public class VentanaTorresHanoi extends VentanaJuego {
    private final ProblemaTorresHanoi problema;   // Modelo con la lógica recursiva
    private HanoiPanel hanoiPanel;                // Panel donde se dibujan las torres
    private Timer timer;                          // Temporizador para animar cada paso
    private int stepIndex = 0;                    // Índice del paso actual en la lista
    private final List<int[]> pasos;              // Lista de movimientos [origen, destino]

    // Representación interna de las tres torres como listas de discos
    private final List<Integer> torreOrigen;
    private final List<Integer> torreAuxiliar;
    private final List<Integer> torreDestino;

    /**
     * Constructor:
     * @param titulo   Texto en la barra de la ventana
     * @param problema Instancia del modelo ya preparado (setParametros + ejecutar ya llamados)
     */
    public VentanaTorresHanoi(String titulo, ProblemaTorresHanoi problema) {
        super(titulo);
        this.problema = problema;
        this.pasos    = problema.getPasos();

        // Inicializar las torres con discos en la torre origen
        int n = problema.getParametro();
        torreOrigen   = new ArrayList<>();
        torreAuxiliar = new ArrayList<>();
        torreDestino  = new ArrayList<>();
        for (int i = n; i >= 1; i--) {
            torreOrigen.add(i);
        }

        initComponents();
    }

    @Override
    protected void initComponents() {
        // 1) Panel central de simulación
        hanoiPanel = new HanoiPanel();
        add(hanoiPanel, BorderLayout.CENTER);

        // 2) Botón “Simular”
        btnSimular.setText("Iniciar Simulación");
        btnSimular.addActionListener(e -> iniciarSimulacion());

        // 3) Botón “Volver al Menú”
        btnVolverMenu.setText("Volver al Menú");
        btnVolverMenu.addActionListener(e -> {
            if (timer != null && timer.isRunning()) timer.stop();
            dispose();
            // reabre el menú principal:
            new MenuRecursivo().mostrarMenu();
        });
    }

    /**
     * Al pulsar 🔃 (recargar): detiene animación, cierra esta ventana
     * y vuelve a pedir el número de discos.
     */
    @Override
    protected void onReload() {
        if (timer != null && timer.isRunning()) timer.stop();
        dispose();
        LanzadorTorresHanoi.ejecutarSimulacion();
    }

    /**
     * Arranca un Timer que cada segundo ejecuta un paso de la lista:
     * • Aplica el movimiento en la vista interna.
     * • Repinta el panel.
     */
    private void iniciarSimulacion() {
        stepIndex = 0;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stepIndex < pasos.size()) {
                    int[] movimiento = pasos.get(stepIndex);
                    aplicarMovimiento(movimiento[0], movimiento[1]);
                    hanoiPanel.repaint();
                    stepIndex++;
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    /**
     * Mueve un disco de una torre a otra en las listas internas:
     * • Comprueba que el movimiento sea legal (no poner disco grande sobre uno pequeño).
     */
    private void aplicarMovimiento(int numOrigen, int numDestino) {
        List<Integer> origen  = getTorre(numOrigen);
        List<Integer> destino = getTorre(numDestino);
        if (!origen.isEmpty()) {
            int disco = origen.remove(origen.size() - 1);
            if (!destino.isEmpty() && destino.get(destino.size() - 1) < disco) {
                throw new IllegalStateException(
                        "Movimiento ilegal: disco " + disco +
                                " sobre disco " + destino.get(destino.size() - 1)
                );
            }
            destino.add(disco);
        }
    }

    /** Devuelve la lista que corresponde al número de torre: 1→origen, 2→auxiliar, 3→destino */
    private List<Integer> getTorre(int num) {
        switch (num) {
            case 1: return torreOrigen;
            case 2: return torreAuxiliar;
            case 3: return torreDestino;
            default: return null;
        }
    }

    /** Panel interno que dibuja las torres y los discos */
    private class HanoiPanel extends JPanel {
        private final HashMap<Integer,Color> colorMap = new HashMap<>();
        private final Random random = new Random();

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Fondo blanco
            g.setColor(Color.WHITE);
            g.fillRect(0,0,getWidth(),getHeight());

            int w = getWidth(), h = getHeight();
            int towerW = w / 3;
            int baseY = h - 50;
            int discH = 20;

            // Dibujar varillas
            g.setColor(Color.BLACK);
            for (int i = 0; i < 3; i++) {
                int x = towerW*i + towerW/2;
                g.drawLine(x, baseY, x, 50);
            }

            // Dibujar discos en cada torre
            dibujarTorre(g, torreOrigen,   1, towerW, baseY, discH);
            dibujarTorre(g, torreAuxiliar, 2, towerW, baseY, discH);
            dibujarTorre(g, torreDestino,  3, towerW, baseY, discH);
        }

        private void dibujarTorre(Graphics g,
                                  List<Integer> torre,
                                  int num,
                                  int towerW,
                                  int baseY,
                                  int discH) {
            int xC = towerW*(num-1) + towerW/2;
            for (int i = 0; i < torre.size(); i++) {
                int disco = torre.get(i);
                int discW = 30 + disco*20;
                int x = xC - discW/2;
                int y = baseY - discH*(i+1);
                colorMap.computeIfAbsent(disco,
                        d -> new Color(random.nextInt(256),
                                random.nextInt(256),
                                random.nextInt(256)));
                g.setColor(colorMap.get(disco));
                g.fillRect(x,y,discW,discH);
                g.setColor(Color.BLACK);
                g.drawRect(x,y,discW,discH);
            }
        }
    }
}
