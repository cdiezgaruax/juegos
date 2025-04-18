// src/main/java/base/VentanaJuego.java
package base.vista;

import javax.swing.*;
import java.awt.*;
import ui.vista.MenuRecursivo;

public abstract class VentanaJuego extends JFrame {
    protected JButton btnReload;      // 🔃
    protected JButton btnSimular;
    protected JButton btnVolverMenu;

    public VentanaJuego(String titulo) {
        super(titulo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        initComponentesBase();
        // initComponents() lo llamará la subclase
    }

    private void initComponentesBase() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // 1) 🔃 Recargar / volver a pedir N
        btnReload = new JButton("🔃");
        btnReload.setToolTipText("Volver a pedir el tamaño y reiniciar");
        btnReload.addActionListener(e -> onReload());
        panelBotones.add(btnReload);

        // 2) Simular / Iniciar
        btnSimular = new JButton("Simular");
        panelBotones.add(btnSimular);

        // 3) Volver al menú
        btnVolverMenu = new JButton("Volver al Menú");
        btnVolverMenu.addActionListener(e -> {
            dispose();
            new MenuRecursivo().mostrarMenu();
        });
        panelBotones.add(btnVolverMenu);

        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Método que se ejecuta al pulsar 🔃.
     * Por defecto no hace nada; las subclases lo pueden
     * sobrescribir para reiniciar el flujo (p.ej. volver al lanzador).
     */
    protected void onReload() {
        // noop
    }

    /** Cada subclase monta su panel central y configura btnSimular */
    protected abstract void initComponents();

    /** Hace visible la ventana */
    public void mostrar() {
        setVisible(true);
    }
}
