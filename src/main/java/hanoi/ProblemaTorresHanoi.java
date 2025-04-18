package hanoi;

import base.AlgoritmoRecursivo;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * ProblemaTorresHanoi:
 * • Modelo que resuelve el clásico problema de las Torres de Hanoi
 * • Hereda de AlgoritmoRecursivo → ObjetoBase, por lo que debe implementar representar()
 */
public class ProblemaTorresHanoi extends AlgoritmoRecursivo {
    // ------------------ ATRIBUTOS ------------------
    private int n;  // Número de discos que vamos a mover

    // Pilas que representan cada torre:
    // • torreOrigen contiene inicialmente todos los discos
    // • torreAuxiliar y torreDestino empiezan vacías
    private Stack<Integer> torreOrigen;
    private Stack<Integer> torreAuxiliar;
    private Stack<Integer> torreDestino;

    /**
     * Constructor:
     * • Llama a super() para darle un nombre al algoritmo
     * • Inicializa la lista de pasos vacía
     */
    public ProblemaTorresHanoi() {
        super("Torres de Hanoi");
        pasos = new ArrayList<>();  // Lista de movimientos [origen, destino]
    }

    // ------------- setParametros(int n) -------------
    @Override
    public void setParametros(int n) {
        // 1) Validación: debe haber al menos 1 disco
        if (n < 1) {
            throw new IllegalArgumentException("Debe haber al menos 1 disco.");
        }
        // 2) Guardar n en este objeto y en el atributo heredado 'parametro'
        this.n = n;
        this.parametro = n;

        // 3) Crear y llenar la pila de origen con discos de mayor a menor
        torreOrigen   = new Stack<>();
        torreAuxiliar = new Stack<>();
        torreDestino  = new Stack<>();
        //  Discos: n (el más grande) ... 1 (el más pequeño)
        for (int i = n; i >= 1; i--) {
            torreOrigen.push(i);
        }
    }

    // ------------- ejecutar() -------------
    @Override
    public void ejecutar() {
        // Arranca el proceso recursivo: mueve n discos de torreOrigen a torreDestino
        moverDiscos(n, torreOrigen, torreDestino, torreAuxiliar,
                /*numOrigen=*/1, /*numDestino=*/3, /*numAuxiliar=*/2);
    }

    /**
     * moverDiscos: método recursivo que implementa el algoritmo de las Torres de Hanoi
     *
     * @param n           Cantidad de discos a mover en esta llamada
     * @param origen      Pila de donde sacamos discos
     * @param destino     Pila donde ponemos discos
     * @param auxiliar    Pila auxiliar para apoyar discos
     * @param numOrigen   Identificador numérico de la torre origen (1,2 o 3)
     * @param numDestino  Identificador numérico de la torre destino
     * @param numAuxiliar Identificador numérico de la torre auxiliar
     */
    private void moverDiscos(int n,
                             Stack<Integer> origen,
                             Stack<Integer> destino,
                             Stack<Integer> auxiliar,
                             int numOrigen,
                             int numDestino,
                             int numAuxiliar) {
        // Caso base: si solo queda 1 disco, muévelo directamente
        if (n == 1) {
            int disco = origen.pop();  // Sacamos el disco de la pila origen
            // Verificar que no pongamos un disco grande sobre uno más pequeño
            if (!destino.isEmpty() && destino.peek() < disco) {
                throw new IllegalStateException(
                        "Movimiento ilegal: mover disco " + disco +
                                " sobre disco " + destino.peek()
                );
            }
            destino.push(disco);  // Colocamos disco en la pila destino
            pasos.add(new int[]{numOrigen, numDestino});  // Registramos el movimiento
            return;  // Salimos del método
        }

        // Paso 1: mover n-1 discos de origen a auxiliar
        moverDiscos(n - 1,
                origen,    // origen sigue siendo origen
                auxiliar,  // ahora destino temporal es la pila auxiliar
                destino,   // y el destino real pasa a ser auxiliar
                numOrigen,
                numAuxiliar,
                numDestino);

        // Paso 2: mover el disco restante (el más grande) de origen a destino
        int disco = origen.pop();
        if (!destino.isEmpty() && destino.peek() < disco) {
            throw new IllegalStateException(
                    "Movimiento ilegal: mover disco " + disco +
                            " sobre disco " + destino.peek()
            );
        }
        destino.push(disco);
        pasos.add(new int[]{numOrigen, numDestino});  // Registramos este movimiento

        // Paso 3: mover los n-1 discos de auxiliar a destino
        moverDiscos(n - 1,
                auxiliar,  // ahora origen es la pila auxiliar
                destino,   // destino final
                origen,    // auxiliar pasa a ser la pila origen
                numAuxiliar,
                numDestino,
                numOrigen);
    }

    // ------------- getPasos() -------------
    @Override
    public List<int[]> getPasos() {
        return pasos;  // Devuelve la lista de movimientos registrados
    }

    /**
     * getEstadoTorres():
     * • Permite obtener un texto con el estado final de las pilas
     * • Útil para depuración o para mostrar en consola
     */
    public String getEstadoTorres() {
        StringBuilder sb = new StringBuilder();
        sb.append("Torre Origen:   ").append(torreOrigen).append("\n");
        sb.append("Torre Auxiliar: ").append(torreAuxiliar).append("\n");
        sb.append("Torre Destino:  ").append(torreDestino);
        return sb.toString();
    }

    // ------------- representar() (abstracto en ObjetoBase) -------------
    @Override
    public String representar() {
        // Texto descriptivo para este objeto
        return "Torres de Hanoi con " + n + " disco" + (n > 1 ? "s" : "");
    }
}
