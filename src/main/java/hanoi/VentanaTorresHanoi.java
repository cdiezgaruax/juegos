package hanoi;

import base.VentanaJuego;
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
 * • Hereda de VentanaJuego, que ya proporciona los botones “Simular”
 *   y “Volver al Menú”.
 */
public class VentanaTorresHanoi extends VentanaJuego {
    private ProblemaTorresHanoi problema;   // Modelo con la lógica recursiva
    private HanoiPanel hanoiPanel;          // Panel donde se dibujan las torres
    private Timer timer;                    // Temporizador para animar cada paso
    private int stepIndex = 0;              // Índice del paso actual en la lista
    private List<int[]> pasos;              // Lista de movimientos [origen, destino]

    // Representación interna de las tres torres como listas de discos
    private List<Integer> torreOrigen;
    private List<Integer> torreAuxiliar;
    private List<Integer> torreDestino;

    /**
     * Constructor:
     * @param titulo   Texto en la barra de la ventana
     * @param problema Instancia del modelo ya ejecutado
     */
    public VentanaTorresHanoi(String titulo, ProblemaTorresHanoi problema) {
        super(titulo);                     // Invoca al constructor de VentanaJuego
        this.problema = problema;          // Guarda el modelo
        this.pasos = problema.getPasos();  // Obtiene la lista de movimientos

        // Inicializar las torres con discos en la torre origen
        int n = problema.getParametro();   // Número de discos
        torreOrigen   = new ArrayList<>();
        torreAuxiliar = new ArrayList<>();
        torreDestino  = new ArrayList<>();
        // Disco más grande (n) al fondo, luego n-1, …, el más pequeño (1) arriba
        for (int i = n; i >= 1; i--) {
            torreOrigen.add(i);
        }

        initComponents();                  // Monta el panel y configura los botones
    }

    /**
     * Inicializa los componentes gráficos específicos:
     * • Añade el panel de dibujo (centro).
     * • Configura los botones “Simular” y “Volver al Menú”.
     */
    @Override
    protected void initComponents() {
        // 1) Crear y añadir el panel de simulación en el centro
        hanoiPanel = new HanoiPanel();
        add(hanoiPanel, BorderLayout.CENTER);

        // 2) Configurar el botón “Simular” (heredado de VentanaJuego)
        btnSimular.setText("Iniciar Simulación");
        btnSimular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSimulacion();      // Arranca la animación
            }
        });

        // 3) Configurar el botón “Volver al Menú”
        btnVolverMenu.setText("Volver al Menú");
        btnVolverMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();                // Cierra esta ventana
                // new MenuRecursivo().mostrar(); // Descomenta para reabrir el menú
            }
        });
    }

    /**
     * Arranca un Timer que cada segundo ejecuta un paso de la lista:
     * • Aplica el movimiento en la vista interna.
     * • Repinta el panel.
     */
    private void iniciarSimulacion() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Si quedan pasos por mostrar
                if (stepIndex < pasos.size()) {
                    int[] movimiento = pasos.get(stepIndex);
                    // 1) Aplicar en la representación interna
                    aplicarMovimiento(movimiento[0], movimiento[1]);
                    stepIndex++;
                    // 2) Repintar la vista con el nuevo estado
                    hanoiPanel.repaint();
                } else {
                    timer.stop();           // Si no quedan pasos, parar el Timer
                }
            }
        });
        timer.start();                     // Inicia el Timer
    }

    /**
     * Mueve un disco de una torre a otra en las listas internas:
     * • Comprueba que el movimiento sea legal (no poner disco grande sobre uno pequeño).
     * @param numOrigen  Identificador de la torre origen (1,2 o 3)
     * @param numDestino Identificador de la torre destino
     */
    private void aplicarMovimiento(int numOrigen, int numDestino) {
        List<Integer> origen  = getTorre(numOrigen);
        List<Integer> destino = getTorre(numDestino);
        if (!origen.isEmpty()) {
            int disco = origen.remove(origen.size() - 1);  // Saca el disco de arriba
            // Verificar regla: disco más grande no sobre disco más pequeño
            if (!destino.isEmpty() && destino.get(destino.size() - 1) < disco) {
                throw new IllegalStateException(
                        "Movimiento ilegal: disco " + disco +
                                " sobre disco " + destino.get(destino.size() - 1)
                );
            }
            destino.add(disco);        // Coloca el disco en la torre destino
        }
    }

    /**
     * Devuelve la lista que corresponde al número de torre:
     * 1→origen, 2→auxiliar, 3→destino
     */
    private List<Integer> getTorre(int num) {
        switch (num) {
            case 1: return torreOrigen;
            case 2: return torreAuxiliar;
            case 3: return torreDestino;
            default: return null;
        }
    }

    /**
     * Panel interno que dibuja:
     * • Fondo blanco
     * • Las tres torres (líneas verticales)
     * • Los discos coloreados en cada torre
     */
    private class HanoiPanel extends JPanel {
        private HashMap<Integer, Color> colorMap = new HashMap<>();
        private Random random = new Random();

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // 1) Pinta el fondo de blanco
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            // 2) Calcula dimensiones
            int panelWidth  = getWidth();
            int panelHeight = getHeight();
            int towerWidth  = panelWidth / 3;  // Cada torre ocupa un tercio del ancho
            int baseY       = panelHeight - 50; // Altura de la base
            int discHeight  = 20;               // Altura fija de cada disco

            // 3) Dibuja las líneas que representan las torres
            g.setColor(Color.BLACK);
            for (int i = 0; i < 3; i++) {
                int xCenter = towerWidth * i + towerWidth / 2;
                g.drawLine(xCenter, baseY, xCenter, 50);
            }

            // 4) Dibuja los discos en cada torre
            dibujarTorre(g, torreOrigen,   1, towerWidth, baseY, discHeight);
            dibujarTorre(g, torreAuxiliar, 2, towerWidth, baseY, discHeight);
            dibujarTorre(g, torreDestino,  3, towerWidth, baseY, discHeight);
        }

        /**
         * Dibuja todos los discos de una torre:
         * • Los discos más grandes (n) se dibujan abajo,
         *   los más pequeños (1) arriba.
         * • Asigna un color aleatorio a cada tamaño la primera vez.
         */
        private void dibujarTorre(Graphics g,
                                  List<Integer> torre,
                                  int numTorre,
                                  int towerWidth,
                                  int baseY,
                                  int discHeight) {
            int xCenter = towerWidth * (numTorre - 1) + towerWidth / 2;
            for (int i = 0; i < torre.size(); i++) {
                int disco = torre.get(i);                   // Tamaño del disco
                int discWidth = 30 + disco * 20;            // Ancho proporcional
                int x = xCenter - discWidth / 2;            // X para centrar disco
                int y = baseY - discHeight * (i + 1);       // Y según posición en la pila

                // Si no tenía color, generamos uno aleatorio y lo guardamos
                if (!colorMap.containsKey(disco)) {
                    colorMap.put(disco, new Color(
                            random.nextInt(256),
                            random.nextInt(256),
                            random.nextInt(256)
                    ));
                }
                g.setColor(colorMap.get(disco)); // Color fijo por tamaño
                g.fillRect(x, y, discWidth, discHeight);  // Rellenar disco
                g.setColor(Color.BLACK);
                g.drawRect(x, y, discWidth, discHeight);  // Borde negro
            }
        }
    }
}
