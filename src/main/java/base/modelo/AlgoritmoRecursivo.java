package base.modelo;

import java.util.List;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Clase base para cualquier algoritmo recursivo.
 * Guarda un nombre, un parámetro (como tamaño de tablero) y la lista de pasos realizados.
 */
public abstract class AlgoritmoRecursivo extends ObjetoBase {
    // Aquí se guarda el parámetro principal del algoritmo (por ejemplo, tamaño del tablero o número de discos).
    protected int parametro;

    // Lista donde almacenamos cada paso que hace el algoritmo.
    // Cada paso es un array de enteros: [fila, columna] o [origen, destino].
    protected List<int[]> pasos;

    /**
     * Constructor de la clase.
     * @param nombre Texto con el nombre del algoritmo (se pasa a la superclase ObjetoBase).
     */
    public AlgoritmoRecursivo(String nombre) {
        // Llama al constructor de ObjetoBase con un nombre y una descripción simple.
        super(nombre, "Algoritmo " + nombre);
    }

    /**
     * Debe definir cómo recibe el parámetro principal.
     * @param n el valor del parámetro (por ejemplo, el tamaño N).
     */
    public abstract void setParametros(int n);

    /**
     * Aquí va la lógica principal del algoritmo (la parte recursiva).
     */
    public abstract void ejecutar();

    /**
     * Devuelve la lista de pasos que se han almacenado.
     */
    public abstract List<int[]> getPasos();

    /**
     * Guarda en un archivo de texto la secuencia de pasos.
     * @param nombreArchivo ruta o nombre del fichero donde escribir.
     */
    public void guardarResultado(String nombreArchivo) {
        // Abrimos (o creamos) un PrintWriter para escribir líneas en un archivo.
        try (PrintWriter writer = new PrintWriter(nombreArchivo)) {
            writer.println("Secuencia de movimientos:");
            // Recorremos cada vector de la lista de pasos
            for (int[] vector : getPasos()) {
                // Convertimos el array a String (p.ej. "[0, 1]") y lo escribimos
                writer.println(vectorToString(vector));
            }
        } catch (IOException e) {
            // Si hay un error al escribir el archivo, lo mostramos por consola
            e.printStackTrace();
        }
    }

    /**
     * Convierte un array de enteros en un texto como "[a, b, c]".
     * @param vector el array a convertir.
     * @return la representación en cadena.
     */
    protected String vectorToString(int[] vector) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        // Añadimos cada posición del array, separada por comas
        for (int i = 0; i < vector.length; i++) {
            sb.append(vector[i]);
            if (i < vector.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Método para obtener el valor actual del parámetro.
     * @return el parámetro (p.ej. tamaño del tablero).
     */
    public int getParametro() {
        return parametro;
    }
}

