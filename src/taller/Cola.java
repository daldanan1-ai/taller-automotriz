package taller;

/**
 * Cola FIFO (First In, First Out): el primero en llegar es el primero en salir.
 *
 *   frente                       fin
 *     |                           |
 *   [V1] <-> [V2] <-> [V3] <-> [V4]
 *   sale por aquí           entra por aquí
 *
 * Basada en el ejemplo de clase de John Carlos Arrieta Arrieta.
 */
public class Cola {

    private int cuenta;
    private Nodo frente; // primer elemento (el que se atiende)
    private Nodo fin;    // último elemento (el que llegó de último)

    public Cola() {
        cuenta = 0;
        frente = null;
        fin = null;
    }

    public boolean esVacia() {
        return cuenta == 0;
    }

    /** Encolar: agrega al final (después del fin actual). */
    public void encolar(Object dato) throws Exception {
        if (dato == null) {
            throw new Exception("El dato no puede ser nulo");
        }
        Nodo nuevo = new Nodo(dato);
        if (esVacia()) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.setDerecha(nuevo);
            nuevo.setIzquierda(fin);
            fin = nuevo;
        }
        ++cuenta;
    }

    /** Desencolar: saca y devuelve el elemento del frente. */
    public Object desencolar() throws Exception {
        if (esVacia()) {
            throw new Exception("La cola está vacía");
        }
        Object dato = frente.getDato();
        frente = frente.getDerecha();
        if (frente != null) {
            frente.setIzquierda(null);
        } else {
            fin = null; // la cola quedó vacía
        }
        --cuenta;
        return dato;
    }

    /** Consulta el frente sin sacarlo. */
    public Object peek() throws Exception {
        if (esVacia()) {
            throw new Exception("La cola está vacía");
        }
        return frente.getDato();
    }

    public int tamanio() {
        return cuenta;
    }

    /** Recorre de frente a fin comparando con equals. No modifica la cola. */
    public boolean contiene(Object dato) {
        Nodo actual = frente;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return true;
            }
            actual = actual.getDerecha();
        }
        return false;
    }

    public void limpiar() {
        frente = null;
        fin = null;
        cuenta = 0;
    }

    /** Muestra la cola en orden de atención (del frente al fin). */
    public String mostrar() {
        if (esVacia()) {
            return "(cola vacía)";
        }
        StringBuilder sb = new StringBuilder();
        Nodo actual = frente;
        int turno = 1;
        while (actual != null) {
            sb.append(String.format("%3d. %s%n", turno++, actual.getDato()));
            actual = actual.getDerecha();
        }
        return sb.toString();
    }
}
