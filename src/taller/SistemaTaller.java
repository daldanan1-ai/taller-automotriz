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
