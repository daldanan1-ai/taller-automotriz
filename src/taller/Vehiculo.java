package taller;

/**
 * Vehículo que ingresa al taller automotriz. Su identificador es la PLACA.
 */
public class Vehiculo {

    // Estados posibles del vehículo dentro del taller
    public static final String EN_ESPERA = "EN ESPERA";   // en la cola de pendientes
    public static final String REPARADO = "REPARADO";     // procesado, en el historial (pila)
    public static final String CANCELADO = "CANCELADO";   // el cliente retiró el vehículo sin reparar

    public static final String[] SERVICIOS = {
        "Mantenimiento preventivo", "Cambio de aceite", "Frenos", "Suspensión",
        "Sistema eléctrico", "Motor", "Latonería y pintura"
    };

    private String placa;
    private String propietario;
    private String marca;
    private String servicio;
    private String descripcionFalla;
    private String estado;

    public Vehiculo(String placa, String propietario, String marca, String servicio, String descripcionFalla) {
        setPlaca(placa);
        setPropietario(propietario);
        setMarca(marca);
        setServicio(servicio);
        setDescripcionFalla(descripcionFalla);
        this.estado = EN_ESPERA;
    }

    /** Constructor privado usado solo por clave(): un vehículo que únicamente tiene placa. */
    private Vehiculo(String placa) {
        setPlaca(placa);
    }

    /**
     * Crea un objeto "clave" con solo la placa, para buscar con equals() en la
     * Lista, la Cola o la Pila (equals solo compara la placa).
     */
    public static Vehiculo clave(String placa) {
        return new Vehiculo(placa);
    }

    /**
     * Quita espacios y guiones y pasa a mayúsculas: " abc-123 " -> "ABC123".
     * Se usa también al BUSCAR, para que el usuario pueda escribir la placa como quiera.
     */
    public static String normalizarPlaca(String placa) {
        if (placa == null) {
            return "";
        }
        return placa.replace(" ", "").replace("-", "").toUpperCase();
    }

    /** Placa colombiana: carro ABC123 o moto ABC12D. */
    public static boolean esPlacaValida(String placa) {
        String p = normalizarPlaca(placa);
        return p.matches("[A-Z]{3}[0-9]{3}") || p.matches("[A-Z]{3}[0-9]{2}[A-Z]");
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        if (!esPlacaValida(placa)) {
            throw new IllegalArgumentException("Placa inválida: \"" + placa
                    + "\". Use el formato ABC123 (carro) o ABC12D (moto).");
        }
        this.placa = normalizarPlaca(placa);
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        String limpio = obligatorio(propietario, "propietario");
        if (!limpio.matches("[\\p{L} .'-]{3,60}")) {
            throw new IllegalArgumentException("El propietario solo puede tener letras y espacios (3 a 60 caracteres)");
        }
        this.propietario = limpio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = obligatorio(marca, "marca");
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = obligatorio(servicio, "servicio");
    }

    public String getDescripcionFalla() {
        return descripcionFalla;
    }

    public void setDescripcionFalla(String descripcionFalla) {
        String limpio = obligatorio(descripcionFalla, "descripción de la falla");
        if (limpio.length() < 5) {
            throw new IllegalArgumentException("Describa la falla con al menos 5 caracteres");
        }
        this.descripcionFalla = limpio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (!EN_ESPERA.equals(estado) && !REPARADO.equals(estado) && !CANCELADO.equals(estado)) {
            throw new IllegalArgumentException("Estado inválido: " + estado);
        }
        this.estado = estado;
    }

    private static String obligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo \"" + campo + "\" es obligatorio");
        }
        return valor.trim();
    }

    /**
     * Dos vehículos son el MISMO si tienen la misma placa (el identificador
     * principal). Lista, Cola y Pila usan este método en contiene() y buscar().
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Vehiculo)) {
            return false;
        }
        Vehiculo otro = (Vehiculo) obj;
        return placa.equals(otro.placa);
    }

    /** Coherente con equals(): mismo atributo (placa). */
    @Override
    public int hashCode() {
        return placa.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%-7s | %-20s | %-10s | %-24s | %-9s | %s",
                placa, propietario, marca, servicio, estado, descripcionFalla);
    }
}
