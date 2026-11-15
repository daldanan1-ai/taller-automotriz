import taller.Cola;
import taller.Lista;
import taller.Pila;
import taller.SistemaTaller;
import taller.Vehiculo;

/**
 * Pruebas automáticas de Lista, Cola, Pila y de las reglas del sistema.
 * Ejecutar con: java -cp out PruebasTaller
 */
public class PruebasTaller {

    private static int aprobadas = 0;
    private static int fallidas = 0;

    public static void main(String[] args) throws Exception {
        probarLista();
        probarCola();
        probarPila();
        probarVehiculo();
        probarSistema();

        System.out.println();
        System.out.println("Pruebas aprobadas: " + aprobadas + " | fallidas: " + fallidas);
        if (fallidas > 0) {
            System.exit(1);
        }
    }

    // ------------------------------------------------------------ LISTA

    private static void probarLista() throws Exception {
        Lista l = new Lista();
        verificar("Lista: nueva lista vacía", l.esVacia() && l.cuentaElementos() == 0);
        l.agregar("B");
        l.agregar("C");
        l.agregarAlInicio("A");
        l.agregar("E");
        l.agregarEnPosicion(3, "D");
        verificar("Lista: agregar, agregarAlInicio y agregarEnPosicion -> A B C D E", contenido(l).equals("ABCDE"));
        verificar("Lista: buscarDato(int) en la primera mitad", l.buscarDato(1).equals("B"));
        verificar("Lista: buscarDato(int) en la segunda mitad (recorre desde el último)", l.buscarDato(3).equals("D"));
        verificar("Lista: buscarDato(Object) devuelve la posición", l.buscarDato("C") == 2);
        verificar("Lista: contiene", l.contiene("E") && !l.contiene("Z"));
        verificar("Lista: mostrarAtras recorre de último a primero",
                l.mostrarAtras().indexOf("E") < l.mostrarAtras().indexOf("A"));
        verificar("Lista: eliminarPrimero", l.eliminarPrimero().equals("A") && contenido(l).equals("BCDE"));
        verificar("Lista: eliminarUltimo", l.eliminarUltimo().equals("E") && contenido(l).equals("BCD"));
        verificar("Lista: eliminarEnPosicion del medio reconecta los vecinos",
                l.eliminarEnPosicion(1).equals("C") && contenido(l).equals("BD") && contenidoAtras(l).equals("DB"));
        verificar("Lista: agregarEnPosicion fuera de rango lanza excepción", lanza(() -> l.agregarEnPosicion(9, "X")));
        verificar("Lista: buscarDato de índice inválido lanza excepción", lanza(() -> l.buscarDato(5)));
        verificar("Lista: buscarDato de dato inexistente lanza excepción", lanza(() -> l.buscarDato("Z")));
        verificar("Lista: agregar null lanza excepción", lanza(() -> l.agregar(null)));
        l.eliminarPrimero();
        l.eliminarUltimo();
        verificar("Lista: al vaciarla con eliminaciones queda consistente", l.esVacia() && contenido(l).isEmpty());
        l.agregar("X");
        verificar("Lista: se puede volver a usar después de vaciarla", contenido(l).equals("X"));
        l.limpiar();
        verificar("Lista: limpiar", l.esVacia());
        verificar("Lista: eliminar en lista vacía lanza excepción", lanza(l::eliminarPrimero));
    }

    // ------------------------------------------------------------- COLA

    private static void probarCola() throws Exception {
        Cola c = new Cola();
        verificar("Cola: desencolar vacía lanza excepción", lanza(c::desencolar));
        verificar("Cola: peek vacía lanza excepción", lanza(c::peek));
        c.encolar("Toyota");
        c.encolar("Mazda");
        c.encolar("BMW");
        verificar("Cola: tamanio 3 y frente Toyota", c.tamanio() == 3 && c.peek().equals("Toyota"));
        verificar("Cola: FIFO, sale primero el primero que llegó",
                c.desencolar().equals("Toyota") && c.desencolar().equals("Mazda"));
        verificar("Cola: contiene", c.contiene("BMW") && !c.contiene("Toyota"));
        c.desencolar();
        verificar("Cola: vacía tras desencolar todo", c.esVacia());
        c.encolar("Kia");
        verificar("Cola: funciona después de vaciarse (fin se reinició bien)", c.peek().equals("Kia") && c.tamanio() == 1);
        c.limpiar();
        verificar("Cola: limpiar", c.esVacia() && c.mostrar().contains("vacía"));
    }

    // ------------------------------------------------------------- PILA

    private static void probarPila() throws Exception {
        Pila p = new Pila();
        verificar("Pila: desapilar vacía lanza excepción", lanza(p::desapilar));
        p.apilar("Toyota");
        p.apilar("Mazda");
        p.apilar("BMW");
        verificar("Pila: cima es el último apilado", p.peek().equals("BMW") && p.tamanio() == 3);
        verificar("Pila: buscar devuelve la distancia desde la cima",
                p.buscar("BMW") == 1 && p.buscar("Toyota") == 3 && p.buscar("Kia") == -1);
        verificar("Pila: LIFO, sale primero el último que entró",
                p.desapilar().equals("BMW") && p.desapilar().equals("Mazda"));
        verificar("Pila: contiene", p.contiene("Toyota") && !p.contiene("BMW"));
        p.desapilar();
        verificar("Pila: vacía", p.esVacia());
        p.apilar("Kia");
        verificar("Pila: funciona después de vaciarse", p.peek().equals("Kia"));
        p.limpiar();
        verificar("Pila: limpiar", p.esVacia());
    }

    // --------------------------------------------------------- VEHICULO

    private static void probarVehiculo() {
        Vehiculo a = new Vehiculo("abc-123", "Juan Pérez", "Mazda", "Frenos", "Ruido al frenar");
        Vehiculo b = new Vehiculo("ABC123", "Otra Persona", "Kia", "Motor", "Otra falla");
        verificar("Vehiculo: la placa se normaliza (abc-123 -> ABC123)", a.getPlaca().equals("ABC123"));
        verificar("Vehiculo: equals compara solo la placa", a.equals(b) && a.hashCode() == b.hashCode());
        verificar("Vehiculo: placa de moto válida", Vehiculo.esPlacaValida("XYZ98K"));
        verificar("Vehiculo: placa inválida es rechazada",
                lanza(() -> new Vehiculo("AB1234", "Juan Pérez", "Mazda", "Frenos", "Ruido al frenar")));
        verificar("Vehiculo: propietario vacío es rechazado",
                lanza(() -> new Vehiculo("ABC123", "  ", "Mazda", "Frenos", "Ruido al frenar")));
        verificar("Vehiculo: propietario con números es rechazado",
                lanza(() -> new Vehiculo("ABC123", "Juan 123", "Mazda", "Frenos", "Ruido al frenar")));
        verificar("Vehiculo: falla muy corta es rechazada",
                lanza(() -> new Vehiculo("ABC123", "Juan Pérez", "Mazda", "Frenos", "mal")));
        verificar("Vehiculo: estado inválido es rechazado", lanza(() -> a.setEstado("PERDIDO")));
    }

    // ---------------------------------------------------------- SISTEMA

    private static void probarSistema() throws Exception {
        SistemaTaller s = new SistemaTaller();
        s.cargarDatosDeEjemplo();
        verificar("Sistema: registro agrega a la lista y a la cola",
                s.getListaGeneral().cuentaElementos() == 8 && s.getColaPendientes().tamanio() == 8);
        verificar("Sistema: placa duplicada (con otro formato) es rechazada", lanza(() -> s.registrarVehiculo(
                new Vehiculo("abc 123", "Otra Persona", "Kia", "Motor", "Otra falla"))));
        verificar("Sistema: el duplicado no alteró las estructuras", s.getListaGeneral().cuentaElementos() == 8);

        Vehiculo p1 = s.procesarSiguiente();
        Vehiculo p2 = s.procesarSiguiente();
        verificar("Sistema: procesa en orden de llegada (FIFO)",
                p1.getPlaca().equals("ABC123") && p2.getPlaca().equals("XYZ98K"));
        verificar("Sistema: procesados quedan en la pila con estado REPARADO",
                s.getHistorialProcesados().peek().equals(p2) && p2.getEstado().equals(Vehiculo.REPARADO));

        Vehiculo deshecho = s.deshacerUltimoProcesamiento();
        verificar("Sistema: deshacer saca el ÚLTIMO procesado (LIFO)", deshecho.getPlaca().equals("XYZ98K"));
        verificar("Sistema: el deshecho vuelve al FINAL de la cola y EN ESPERA",
                ultimoDeCola(s.getColaPendientes()).equals(deshecho) && deshecho.getEstado().equals(Vehiculo.EN_ESPERA));

        Vehiculo cancelado = s.cancelarPendiente("GHI789");
        verificar("Sistema: cancelar cambia el estado y saca de la cola",
                cancelado.getEstado().equals(Vehiculo.CANCELADO) && !s.getColaPendientes().contiene(cancelado));
        verificar("Sistema: el cancelado sigue en la lista general", s.getListaGeneral().contiene(cancelado));
        verificar("Sistema: cancelar con cola auxiliar conserva el orden de los demás",
                ordenCola(s.getColaPendientes()).equals("DEF456,JKL321,MNO654,PQR11B,STU852,XYZ98K"));
        verificar("Sistema: no se cancela un vehículo ya reparado", lanza(() -> s.cancelarPendiente("ABC123")));
        verificar("Sistema: no se cancela una placa inexistente", lanza(() -> s.cancelarPendiente("QQQ111")));
        verificar("Sistema: no se cancela dos veces", lanza(() -> s.cancelarPendiente("GHI789")));

        verificar("Sistema: buscar por placa (sin importar el formato)",
                s.buscarPorPlaca("def-456").getPropietario().equals("Luis Mora"));
        verificar("Sistema: buscar placa inexistente lanza excepción", lanza(() -> s.buscarPorPlaca("ZZZ999")));
        verificar("Sistema: reporte coherente", s.reporteCantidades().contains("✓"));

        SistemaTaller vacio = new SistemaTaller();
        verificar("Sistema: procesar sin pendientes lanza excepción", lanza(vacio::procesarSiguiente));
        verificar("Sistema: deshacer sin historial lanza excepción", lanza(vacio::deshacerUltimoProcesamiento));
    }

    // -------------------------------------------------------- AUXILIARES

    private static String contenido(Lista l) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < l.cuentaElementos(); i++) {
            sb.append(l.buscarDato(i));
        }
        return sb.toString();
    }

    private static String contenidoAtras(Lista l) {
        // mostrarAtras numera "  1. D" ... se toma el último carácter de cada línea
        StringBuilder sb = new StringBuilder();
        for (String linea : l.mostrarAtras().split("\\R")) {
            if (!linea.isBlank()) {
                sb.append(linea.charAt(linea.length() - 1));
            }
        }
        return sb.toString();
    }

    /** Recorre la cola desencolando y reencolando: al final queda igual. */
    private static String ordenCola(Cola c) throws Exception {
        StringBuilder sb = new StringBuilder();
        int n = c.tamanio();
        for (int i = 0; i < n; i++) {
            Vehiculo v = (Vehiculo) c.desencolar();
            sb.append(sb.length() == 0 ? "" : ",").append(v.getPlaca());
            c.encolar(v);
        }
        return sb.toString();
    }

    private static Object ultimoDeCola(Cola c) throws Exception {
        Object ultimo = null;
        int n = c.tamanio();
        for (int i = 0; i < n; i++) {
            ultimo = c.desencolar();
            c.encolar(ultimo);
        }
        return ultimo;
    }

    interface Accion {
        void ejecutar() throws Exception;
    }

    private static boolean lanza(Accion accion) {
        try {
            accion.ejecutar();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private static void verificar(String descripcion, boolean condicion) {
        if (condicion) {
            aprobadas++;
            System.out.println("[OK]    " + descripcion);
        } else {
            fallidas++;
            System.out.println("[FALLA] " + descripcion);
        }
    }
}
