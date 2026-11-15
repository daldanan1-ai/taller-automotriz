package taller;

/**
 * Nodo doblemente enlazado: guarda un dato y dos referencias a sus vecinos.
 *
 *        izquierda          derecha
 *   [nodo anterior] <-- [ dato ] --> [nodo siguiente]
 *
 * Basado en el ejemplo de clase de John Carlos Arrieta Arrieta.
 * Lo usan las tres estructuras:
 *  - Lista: izquierda = anterior, derecha = siguiente.
 *  - Cola:  del frente hacia el fin se avanza por la derecha.
 *  - Pila:  desde la cima, el nodo de "abajo" está a la izquierda.
 */
public class Nodo {

    private Object dato;
    private Nodo izquierda;
    private Nodo derecha;

    public Nodo() {
    }

    public Nodo(Object dato) {
        this.dato = dato;
    }

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public Nodo getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(Nodo vecino) {
        this.izquierda = vecino;
    }

    public Nodo getDerecha() {
        return derecha;
    }

    public void setDerecha(Nodo vecino) {
        this.derecha = vecino;
    }
}
