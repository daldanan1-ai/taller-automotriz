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

    /** Agrega al inicio: el nuevo nodo queda a la izquierda del primero. */
    public void agregarAlInicio(Object dato) throws Exception {
        if (dato == null) {
            throw new Exception("El dato no puede ser nulo");
        }
        if (esVacia()) {
            agregar(dato);
            return;
        }
        Nodo nuevo = new Nodo(dato);
        nuevo.setDerecha(primero);
        primero.setIzquierda(nuevo);
        primero = nuevo;
        ++cuenta;
    }

    /**
     * Inserta en la posición indicada (0..cuenta). El nuevo nodo queda entre
     * "anterior" y "siguiente", y se ajustan las CUATRO referencias:
     *
     *   antes:   [anterior] <-> [siguiente]
     *   después: [anterior] <-> [nuevo] <-> [siguiente]
     */
    public void agregarEnPosicion(int indice, Object dato) throws Exception {
        if (dato == null) {
            throw new Exception("El dato no puede ser nulo");
        }
        if (indice < 0 || indice > cuenta) {
            throw new Exception("La posición " + indice + " está fuera del rango 0 - " + cuenta);
        }
        if (indice == 0) {
            agregarAlInicio(dato);
        } else if (indice == cuenta) {
            agregar(dato);
        } else {
            Nodo siguiente = buscar(indice);
            Nodo anterior = siguiente.getIzquierda();
            Nodo nuevo = new Nodo(dato);
            nuevo.setIzquierda(anterior);
            nuevo.setDerecha(siguiente);
            anterior.setDerecha(nuevo);
            siguiente.setIzquierda(nuevo);
            ++cuenta;
        }
    }

    public Object eliminarPrimero() throws Exception {
        if (esVacia()) {
            throw new Exception("La lista está vacía");
        }
        Object dato = primero.getDato();
        primero = primero.getDerecha();
        if (primero != null) {
            primero.setIzquierda(null);
        } else {
            ultimo = null; // la lista quedó vacía
        }
        --cuenta;
        return dato;
    }

    public Object eliminarUltimo() throws Exception {
        if (esVacia()) {
            throw new Exception("La lista está vacía");
        }
        Object dato = ultimo.getDato();
        ultimo = ultimo.getIzquierda();
        if (ultimo != null) {
            ultimo.setDerecha(null);
        } else {
            primero = null; // la lista quedó vacía
        }
        --cuenta;
        return dato;
    }

    /**
     * Elimina el nodo de la posición indicada "puenteándolo": su vecino de la
     * izquierda pasa a apuntar a su vecino de la derecha y viceversa.
     *
     *   antes:   [anterior] <-> [objetivo] <-> [siguiente]
     *   después: [anterior] <-> [siguiente]
     */
    public Object eliminarEnPosicion(int indice) throws Exception {
        Nodo objetivo = buscar(indice); // valida vacía y rango
        if (objetivo == primero) {
            return eliminarPrimero();
        }
        if (objetivo == ultimo) {
            return eliminarUltimo();
        }
        Nodo anterior = objetivo.getIzquierda();
        Nodo siguiente = objetivo.getDerecha();
        anterior.setDerecha(siguiente);
        siguiente.setIzquierda(anterior);
        objetivo.setIzquierda(null);
        objetivo.setDerecha(null);
        --cuenta;
        return objetivo.getDato();
    }

    /** true si algún nodo tiene un dato igual (equals) al recibido. No lanza excepción. */
    public boolean contiene(Object dato) {
        Nodo actual = primero;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return true;
            }
            actual = actual.getDerecha();
        }
        return false;
    }

    public void limpiar() {
        primero = null;
        ultimo = null;
        cuenta = 0;
    }

    /** Recorrido de primero a último siguiendo las referencias derecha. */
    public String mostrarAdelante() {
        if (esVacia()) {
            return "(lista vacía)";
        }
        StringBuilder sb = new StringBuilder();
        Nodo actual = primero;
        int i = 0;
        while (actual != null) {
            sb.append(String.format("%3d. %s%n", i++, actual.getDato()));
            actual = actual.getDerecha();
        }
        return sb.toString();
    }

    /** Recorrido de último a primero siguiendo las referencias izquierda. */
    public String mostrarAtras() {
        if (esVacia()) {
            return "(lista vacía)";
        }
        StringBuilder sb = new StringBuilder();
        Nodo actual = ultimo;
        int i = cuenta - 1;
        while (actual != null) {
            sb.append(String.format("%3d. %s%n", i--, actual.getDato()));
            actual = actual.getIzquierda();
        }
        return sb.toString();
    }
}
