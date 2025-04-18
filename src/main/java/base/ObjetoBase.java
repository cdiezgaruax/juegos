package base;

/**
 * Clase base para cualquier objeto de la aplicación.
 * Todas las entidades (algoritmos, problemas, etc.) pueden heredar de aquí.
 */
public abstract class ObjetoBase {

    // Un identificador único para este objeto (puede servir como clave o nombre interno).
    protected String id;

    // Una descripción o nombre legible para mostrar al usuario o en archivos.
    protected String descripcion;

    /**
     * Constructor: crea el objeto con un id y una descripción.
     * @param id          Texto corto que identifica este objeto.
     * @param descripcion Texto más largo o descriptivo del objeto.
     */
    public ObjetoBase(String id, String descripcion) {
        this.id = id;                   // Guarda el id
        this.descripcion = descripcion; // Guarda la descripción
    }

    // -------------------- GETTERS Y SETTERS --------------------

    /** @return el id de este objeto */
    public String getId() {
        return id;
    }

    /** @param id nuevo identificador para este objeto */
    public void setId(String id) {
        this.id = id;
    }

    /** @return la descripción actual del objeto */
    public String getDescripcion() {
        return descripcion;
    }

    /** @param descripcion nueva descripción para el objeto */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // ---------------- MÉTODO ABSTRACTO ----------------

    /**
     * Obliga a que cada clase hija defina cómo convertir el objeto a texto.
     * Por ejemplo: “Problema de las N Reinas 8x8” o “Torres de Hanoi con 3 discos”.
     * @return una cadena que describa este objeto
     */
    public abstract String representar();

    // ---------------- OVERRIDE toString ----------------

    /**
     * Cuando hagas System.out.println(miObjeto), llamará a este toString(),
     * que a su vez invoca representar().
     */
    @Override
    public String toString() {
        return representar();
    }
}
