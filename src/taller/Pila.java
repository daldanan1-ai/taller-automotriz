package taller;

/**
 * Pila LIFO (Last In, First Out): el último en entrar es el primero en salir.
 *
 *   cima -> [V3]      <- se apila y se desapila solo por aquí
 *            | izquierda (abajo)
 *           [V2]
 *            |
 *           [V1]      <- el más antiguo, en el fondo
 *
 * Basada en el ejemplo de clase de John Carlos Arrieta Arrieta.
 */
public class Pila {

    private int cuenta;
    private Nodo cima; // el tope de la pila

    public Pila() {
        cuenta = 0;
        cima = null;
    }

    public boolean esVacia() {
        return cuenta == 0;
    }

    /** Apilar (push): el nuevo nodo queda encima de la cima actual. */
    public void apilar(Object dato) throws Exception {
        if (dato == null) {
            throw new Exception("El dato no puede ser nulo");
        }
        Nodo nuevo = new Nodo(dato);
        if (esVacia()) {
            cima = nuevo;
        } else {
            nuevo.setIzquierda(cima); // abajo del nuevo queda la cima anterior
            cima.setDerecha(nuevo);
            cima = nuevo;
        }
        ++cuenta;
    }

    /** Desapilar (pop): saca y devuelve el elemento de la cima. */
    public Object desapilar() throws Exception {
        if (esVacia()) {
            throw new Exception("La pila está vacía");
        }
        Object dato = cima.getDato();
        cima = cima.getIzquierda(); // el nodo de abajo pasa a ser la cima
        if (cima != null) {
            cima.setDerecha(null);
        }
        --cuenta;
        return dato;
    }

    /** Consulta la cima sin sacarla. */
    public Object peek() throws Exception {
        if (esVacia()) {
            throw new Exception("La pila está vacía");
        }
        return cima.getDato();
    }

    public int tamanio() {
        return cuenta;
    }

    public boolean contiene(Object dato) {
        return buscar(dato) != -1;
    }

    /**
     * Distancia desde la cima hasta el dato: 1 si está en la cima, 2 si está
     * justo debajo, etc. Devuelve -1 si no está. No modifica la pila.
     */
    public int buscar(Object dato) {
        Nodo actual = cima;
        int distancia = 1;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return distancia;
            }
            actual = actual.getIzquierda();
            distancia++;
        }
        return -1;
    }

    public void limpiar() {
        cima = null;
        cuenta = 0;
    }

    /** Muestra de la cima (más reciente) al fondo (más antiguo). */
    public String mostrar() {
        if (esVacia()) {
            return "(pila vacía)";
        }
        StringBuilder sb = new StringBuilder();
        Nodo actual = cima;
        int posicion = 1;
        while (actual != null) {
            sb.append(String.format("%3d. %s%s%n", posicion, actual.getDato(), posicion == 1 ? "   <- cima" : ""));
            actual = actual.getIzquierda();
            posicion++;
        }
        return sb.toString();
    }
}
