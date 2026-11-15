package taller;

/**
 * Lista doblemente enlazada.
 *
 *   primero                                   ultimo
 *     |                                         |
 *   [A] <-> [B] <-> [C] <-> [D]  (izquierda <-, -> derecha)
 *
 * Basada en el ejemplo de clase de John Carlos Arrieta Arrieta.
 */
public class Lista {

    private int cuenta;
    private Nodo primero;
    private Nodo ultimo;

    public boolean esVacia() {
        return cuenta == 0;
    }

    /** Agrega al final: el nuevo nodo queda a la derecha del último. */
    public void agregar(Object dato) throws Exception {
        if (dato == null) {
            throw new Exception("El dato no puede ser nulo");
        }
        Nodo objetivo = new Nodo(dato);
        if (esVacia()) {
            objetivo.setIzquierda(null);
            objetivo.setDerecha(null);
            primero = objetivo;
            ultimo = objetivo;
        } else {
            ultimo.setDerecha(objetivo);
            objetivo.setIzquierda(ultimo);
            objetivo.setDerecha(null);
            ultimo = objetivo;
        }
        ++cuenta;
    }

    /**
     * Devuelve el NODO de la posición indicada.
     * Como la lista es doble, si el índice está en la primera mitad se recorre
     * desde el primero hacia la derecha; si está en la segunda, desde el último
     * hacia la izquierda. Así se recorren como máximo cuenta/2 nodos.
     */
    private Nodo buscar(int indice) throws Exception {
        if (esVacia()) {
            throw new Exception("La lista está vacía");
        }
        if (indice < 0 || indice >= cuenta) {
            throw new Exception("La posición " + indice + " está fuera del rango 0 - " + (cuenta - 1));
        }
        Nodo actual;
        if (indice < cuenta / 2) {
            actual = primero;
            for (int i = 0; i < indice; i++) {
                actual = actual.getDerecha();
            }
        } else {
            actual = ultimo;
            for (int i = cuenta - 1; i > indice; i--) {
                actual = actual.getIzquierda();
            }
        }
        return actual;
    }

    /** Devuelve la posición del dato (comparando con equals) o lanza excepción si no está. */
    private int buscar(Object dato) throws Exception {
        if (esVacia()) {
            throw new Exception("La lista está vacía");
        }
        Nodo actual = primero;
        for (int i = 0; i < cuenta; i++) {
            if (actual.getDato().equals(dato)) {
                return i;
            }
            actual = actual.getDerecha();
        }
        throw new Exception("El elemento no está en la lista");
    }

    public Object buscarDato(int indice) throws Exception {
        Nodo objetivo = buscar(indice);
        return objetivo.getDato();
    }

    public int buscarDato(Object dato) throws Exception {
        return buscar(dato);
    }

    public int cuentaElementos() {
        return cuenta;
    }
}
