package taller;

import java.util.Scanner;

/**
 * Sistema de gestión del taller automotriz.
 *
 *  - listaGeneral:        TODOS los vehículos que han ingresado (registro completo).
 *  - colaPendientes:      vehículos esperando reparación, en orden de llegada (FIFO).
 *  - historialProcesados: vehículos reparados; el último reparado queda en la cima (LIFO).
 *
 * Un mismo objeto Vehiculo puede estar en la lista y, además, en la cola o en
 * la pila: las estructuras guardan referencias al mismo objeto, no copias.
 */
public class SistemaTaller {

    private final Lista listaGeneral = new Lista();
    private final Cola colaPendientes = new Cola();
    private final Pila historialProcesados = new Pila();

    // ================================================== OPERACIONES

    /** Regla obligatoria: listaGeneral.agregar(...) y colaPendientes.encolar(...). */
    public void registrarVehiculo(Vehiculo vehiculo) throws Exception {
        if (vehiculo == null) {
            throw new Exception("El vehículo no puede ser nulo");
        }
        if (listaGeneral.contiene(vehiculo)) { // usa equals(): compara por placa
            throw new Exception("Ya existe un vehículo con placa " + vehiculo.getPlaca() + " en el registro");
        }
        listaGeneral.agregar(vehiculo);
        colaPendientes.encolar(vehiculo);
    }

    /**
     * Regla obligatoria: se saca el vehículo del FRENTE de la cola (el que llegó
     * primero) y se apila en el historial. Queda en la cima: es el más reciente.
     */
    public Vehiculo procesarSiguiente() throws Exception {
        if (colaPendientes.esVacia()) {
            throw new Exception("No hay vehículos pendientes por reparar");
        }
        Vehiculo procesado = (Vehiculo) colaPendientes.desencolar();
        procesado.setEstado(Vehiculo.REPARADO);
        historialProcesados.apilar(procesado);
        return procesado;
    }

    /**
     * Regla obligatoria: se desapila el ÚLTIMO vehículo reparado (LIFO) y vuelve
     * a la cola de pendientes. Como encolar agrega al final, vuelve a esperar
     * detrás de los que ya estaban en la cola.
     */
    public Vehiculo deshacerUltimoProcesamiento() throws Exception {
        if (historialProcesados.esVacia()) {
            throw new Exception("No hay procesamientos para deshacer: el historial está vacío");
        }
        Vehiculo ultimo = (Vehiculo) historialProcesados.desapilar();
        ultimo.setEstado(Vehiculo.EN_ESPERA);
        colaPendientes.encolar(ultimo);
        return ultimo;
    }

    /** true si la placa ya está en el registro general (recorre la lista con equals). */
    public boolean existePlaca(String placa) {
        return Vehiculo.esPlacaValida(placa) && listaGeneral.contiene(Vehiculo.clave(placa));
    }

    public Lista getListaGeneral() {
        return listaGeneral;
    }

    public Cola getColaPendientes() {
        return colaPendientes;
    }

    public Pila getHistorialProcesados() {
        return historialProcesados;
    }

    // ========================================================= MENÚ

    public static void main(String[] args) {
        SistemaTaller sistema = new SistemaTaller();
        sistema.ejecutarMenu(new Scanner(System.in));
    }

    public void ejecutarMenu(Scanner sc) {
        while (true) {
            System.out.println();
            System.out.println("=========== TALLER AUTOMOTRIZ — GESTIÓN DE VEHÍCULOS ===========");
            System.out.println(" 1. Registrar vehículo");
            System.out.println(" 2. Ver todos los vehículos registrados");
            System.out.println(" 3. Ver vehículos pendientes");
            System.out.println(" 4. Procesar siguiente vehículo (reparar)");
            System.out.println(" 5. Ver historial de vehículos reparados");
            System.out.println(" 8. Deshacer último procesamiento");
            System.out.println("10. Salir");
            System.out.print("Seleccione una opción: ");
            if (!sc.hasNextLine()) {
                return;
            }
            String opcion = sc.nextLine().trim();
            System.out.println("-".repeat(100));
            try {
                switch (opcion) {
                    case "1" -> menuRegistrar(sc);
                    case "2" -> {
                        System.out.println("REGISTRO GENERAL (Lista) — " + listaGeneral.cuentaElementos() + " vehículos");
                        System.out.print(listaGeneral.mostrarAdelante());
                    }
                    case "3" -> {
                        System.out.println("PENDIENTES (Cola, orden de llegada) — " + colaPendientes.tamanio());
                        System.out.print(colaPendientes.mostrar());
                    }
                    case "4" -> {
                        Vehiculo v = procesarSiguiente();
                        System.out.println("✓ Reparado (desencolado del frente y apilado en el historial):");
                        System.out.println("  " + v);
                        System.out.println("  Quedan " + colaPendientes.tamanio() + " vehículo(s) pendiente(s).");
                    }
                    case "5" -> {
                        System.out.println("HISTORIAL DE REPARADOS (Pila: el más reciente arriba) — "
                                + historialProcesados.tamanio());
                        System.out.print(historialProcesados.mostrar());
                    }
                    case "8" -> {
                        Vehiculo v = deshacerUltimoProcesamiento();
                        System.out.println("✓ Se deshizo la reparación de " + v.getPlaca()
                                + " (desapilado de la cima y encolado al final).");
                        System.out.println("  Ahora está en el turno " + colaPendientes.tamanio()
                                + " de la cola, con estado " + v.getEstado() + ".");
                    }
                    case "10" -> {
                        System.out.println("Gracias por usar el sistema del taller.");
                        return;
                    }
                    default -> System.out.println("Opción no válida. Escriba un número del menú.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("✗ Dato inválido: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("✗ " + e.getMessage());
            }
        }
    }

    private void menuRegistrar(Scanner sc) throws Exception {
        System.out.println("REGISTRAR VEHÍCULO");
        String placa = leer(sc, "Placa (ABC123 o ABC12D): ");
        if (!Vehiculo.esPlacaValida(placa)) {   // se valida enseguida para no pedir los demás datos
            throw new IllegalArgumentException("Placa inválida: \"" + placa + "\"");
        }
        if (existePlaca(placa)) {               // duplicado: se avisa antes de pedir los demás datos
            throw new Exception("Ya existe un vehículo con placa " + Vehiculo.normalizarPlaca(placa)
                    + " en el registro");
        }
        String propietario = leer(sc, "Propietario: ");
        String marca = leer(sc, "Marca: ");
        for (int i = 0; i < Vehiculo.SERVICIOS.length; i++) {
            System.out.println("   " + (i + 1) + ". " + Vehiculo.SERVICIOS[i]);
        }
        String servicio = Vehiculo.SERVICIOS[leerOpcion(sc, "Tipo de servicio (número): ", Vehiculo.SERVICIOS.length) - 1];
        String falla = leer(sc, "Descripción de la falla: ");

        Vehiculo vehiculo = new Vehiculo(placa, propietario, marca, servicio, falla);
        registrarVehiculo(vehiculo);
        System.out.println("✓ Registrado: " + vehiculo);
        System.out.println("  Quedó en la lista general (posición " + (listaGeneral.cuentaElementos() - 1)
                + ") y en la cola de pendientes (turno " + colaPendientes.tamanio() + ").");
    }

    private static String leer(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        return sc.hasNextLine() ? sc.nextLine() : "";
    }

    private static int leerOpcion(Scanner sc, String mensaje, int maximo) {
        String texto = leer(sc, mensaje).trim();
        try {
            int n = Integer.parseInt(texto);
            if (n < 1 || n > maximo) {
                throw new NumberFormatException();
            }
            return n;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Debe escribir un número entre 1 y " + maximo);
        }
    }
}
