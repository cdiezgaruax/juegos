// src/main/java/base/Dialogos.java
package base.controlador;

import javax.swing.JOptionPane;

/**
 * Métodos comunes para pedir valores al usuario.
 */
public class Dialogos {
    /**
     * Pide un número entero con validación y rango.
     *
     * @param mensaje El texto que aparece en el diálogo.
     * @param min     Valor mínimo permitido (inclusive).
     * @param max     Valor máximo permitido (inclusive).
     * @return El entero válido, o null si el usuario pulsa Cancelar.
     */
    public static Integer pedirEntero(String mensaje, int min, int max) {
        while (true) {
            String input = JOptionPane.showInputDialog(mensaje);
            // Si pulsa cancelar devolvemos null
            if (input == null) return null;
            try {
                int v = Integer.parseInt(input);
                if (v < min || v > max) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Por favor ingresa un número entre " + min + " y " + max + "."
                    );
                } else {
                    return v;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Entrada inválida. Escribe un número entero."
                );
            }
        }
    }
}
