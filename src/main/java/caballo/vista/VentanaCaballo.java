package caballo.vista;

import base.vista.VentanaJuego;
import caballo.controlador.LanzadorCaballo;
import caballo.modelo.ProblemaCaballo;
import ui.vista.MenuRecursivo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * VentanaCaballo: grid de botones estilo tablero de ajedrez,
 * selección de casilla de inicio y simulación del recorrido.
 * Incluye:
 *  • 🔃 (recargar) para volver a pedir N
 *  • Iniciar (simulación paso a paso)
 *  • Volver al Menú
 * Los números fijos se pintan en rojo vivo.
 */
public class VentanaCaballo extends VentanaJuego {
    private final ProblemaCaballo problema;
    private final int n;
    private final JButton[][] casillas;
    private int startX = -1, startY = -1;
    private Timer timer;
    private int stepIndex;
    private final List<int[]> pasos;

    public VentanaCaballo(String titulo, ProblemaCaballo problema) {
        super(titulo);
        this.problema = problema;
        this.n        = problema.getParametro();
        this.pasos    = problema.getPasos();
        this.casillas = new JButton[n][n];
        initComponents();
    }

    @Override
    protected void initComponents() {
        // 1) Tablero central: N×N botones en patrón ajedrez
        JPanel tableroPanel = new JPanel(new GridLayout(n, n));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                JButton b = new JButton();
                b.setMargin(new Insets(0,0,0,0));
                b.setOpaque(true);
                b.setBorderPainted(false);
                b.setBackground((i + j) % 2 == 0
                        ? Color.WHITE
                        : new Color(160,160,160));
                final int fx = i, fy = j;
                b.addActionListener(e -> {
                    // restaurar color de la casilla previa
                    if (startX >= 0) {
                        JButton old = casillas[startX][startY];
                        old.setBackground(((startX + startY) % 2 == 0)
                                ? Color.WHITE
                                : new Color(160,160,160));
                    }
                    startX = fx; startY = fy;
                    b.setBackground(Color.ORANGE);
                });
                casillas[i][j] = b;
                tableroPanel.add(b);
            }
        }
        add(tableroPanel, BorderLayout.CENTER);

        // 2) Configurar botón "Iniciar"
        btnSimular.setText("Iniciar");
        btnSimular.addActionListener(e -> {
            if (startX < 0) {
                JOptionPane.showMessageDialog(this,
                        "Por favor selecciona la casilla de inicio.",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            problema.setStart(startX, startY);
            problema.ejecutar();

            // si no hay tour completo
            if (pasos.size() != n * n) {
                JOptionPane.showMessageDialog(this,
                        "No tiene solución completa desde esa casilla.",
                        "Sin solución", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // limpiar textos anteriores
            for (JButton[] fila: casillas) {
                for (JButton btn: fila) {
                    btn.setText("");
                    btn.setForeground(Color.BLACK);
                    // restaurar listeners: opcional, pero aquí asumimos que
                    // no volverán a querer seleccionar
                }
            }

            stepIndex = 0;
            timer = new Timer(300, ev -> {
                // convertir anterior en número rojo
                if (stepIndex > 0) {
                    int[] prev = pasos.get(stepIndex - 1);
                    JButton p = casillas[prev[0]][prev[1]];
                    p.setText(String.valueOf(stepIndex - 1));
                    p.setForeground(Color.RED);
                    p.setFont(p.getFont().deriveFont(Font.BOLD, 14f));
                    // quitar listeners para que ya no responda
                    for (ActionListener al : p.getActionListeners()) {
                        p.removeActionListener(al);
                    }
                }
                // pintar caballo en casilla actual
                if (stepIndex < pasos.size()) {
                    int[] cur = pasos.get(stepIndex);
                    JButton c = casillas[cur[0]][cur[1]];
                    c.setText("♞");
                    c.setForeground(Color.BLACK);
                    c.setFont(c.getFont().deriveFont(Font.BOLD, 16f));
                } else {
                    timer.stop();
                }
                stepIndex++;
            });
            timer.start();
        });

        // 3) Configurar botón "Volver al Menú"
        btnVolverMenu.setText("Volver al Menú");
        btnVolverMenu.addActionListener(e -> {
            if (timer != null && timer.isRunning()) timer.stop();
            dispose();
            new MenuRecursivo().mostrarMenu();
        });
    }

    @Override
    protected void onReload() {
        if (timer != null && timer.isRunning()) timer.stop();
        dispose();
        LanzadorCaballo.ejecutarSimulacion();
    }
}
