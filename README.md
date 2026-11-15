# Taller automotriz: gestión con Lista, Cola y Pila

**Actividad:** Estructura de Datos – Actividad de aprendizaje: *Sistema de gestión con Lista, Cola y Pila*.
**Caso asignado:** 1. Taller automotriz. Entidad `Vehiculo`, identificada por la **placa**.
**Estudiante:** Domingo Antonio Aldana Noel – Ingeniería de Software, Universidad de Cartagena.

## Descripción

Aplicación de consola en Java para un taller automotriz. Los vehículos **ingresan** al taller, **esperan** su turno de reparación en orden de llegada, se **reparan** y quedan en un **historial**. El sistema permite buscar un vehículo por placa, cancelar uno que esté en espera y deshacer la última reparación.

Las tres estructuras están **implementadas a mano con nodos doblemente enlazados**, partiendo de los ejemplos de clase de `Nodo`, `Lista`, `Cola` y `Pila` del docente. No se usa ninguna colección de Java.

| Estructura | Uso en el taller |
|---|---|
| **Lista** (`listaGeneral`) | Registro de **todos** los vehículos que han ingresado, en cualquier estado. |
| **Cola** (`colaPendientes`) | Vehículos **en espera** de reparación. Se atienden en orden de llegada (FIFO). |
| **Pila** (`historialProcesados`) | Vehículos **reparados**. El último reparado queda en la cima, lo que permite **deshacer** (LIFO). |

## Archivos

```
src/taller/
  Nodo.java           Nodo con dato y referencias izquierda/derecha
  Lista.java          Lista doblemente enlazada (primero, ultimo, cuenta)
  Cola.java           Cola FIFO (frente, fin, cuenta)
  Pila.java           Pila LIFO (cima, cuenta)
  Vehiculo.java       Entidad del caso: placa, propietario, marca, servicio, falla y estado
  SistemaTaller.java  Reglas del sistema y menú de consola (contiene el main)
test/
  PruebasTaller.java  60 pruebas automáticas
```

## Reglas implementadas

| Operación | Código |
|---|---|
| Registrar | `listaGeneral.agregar(vehiculo); colaPendientes.encolar(vehiculo);` |
| Procesar | `Vehiculo procesado = (Vehiculo) colaPendientes.desencolar(); historialProcesados.apilar(procesado);` |
| Deshacer | `Vehiculo ultimo = (Vehiculo) historialProcesados.desapilar(); colaPendientes.encolar(ultimo);` |
| Cancelar pendiente | Con una **cola auxiliar**: se desencolan todos, se separa el cancelado y los demás vuelven en el mismo orden. Nunca se elimina desde el centro de la cola. |
| Buscar | `listaGeneral.buscarDato(Vehiculo.clave(placa))` recorre la lista comparando con `equals()`, que compara por placa. |

## Métodos de las estructuras

- **Lista:** `agregar`, `agregarAlInicio`, `agregarEnPosicion`, `eliminarPrimero`, `eliminarUltimo`, `eliminarEnPosicion`, `buscarDato(int)`, `buscarDato(Object)`, `contiene`, `cuentaElementos`, `limpiar`, `mostrarAdelante`, `mostrarAtras`.
- **Cola:** `encolar`, `desencolar`, `peek`, `tamanio`, `esVacia`, `contiene`, `limpiar`, `mostrar`.
- **Pila:** `apilar`, `desapilar`, `peek`, `tamanio`, `esVacia`, `contiene`, `limpiar`, `mostrar`, `buscar` (distancia desde la cima, o -1).

## Requisitos

Java **17 o superior** (probado con Microsoft OpenJDK 21).

## Cómo ejecutar

### Windows

```bat
ejecutar.bat            :: compila y abre el menú
ejecutar.bat pruebas    :: compila y ejecuta las pruebas automáticas
```

### Cualquier sistema

```bash
javac -encoding UTF-8 -d out src/taller/*.java test/*.java
java -cp out taller.SistemaTaller   # menú
java -cp out PruebasTaller          # pruebas
```

### Visual Studio Code

**Ejecutar y depurar** (Ctrl+Shift+D) → **Taller automotriz (menú)** → **F5**.

Al iniciar, el programa pregunta si se desean cargar **8 vehículos de ejemplo**.

## Menú

```
 1. Registrar vehículo
 2. Ver todos los vehículos registrados
 3. Ver vehículos pendientes
 4. Procesar siguiente vehículo (reparar)
 5. Ver historial de vehículos reparados
 6. Buscar vehículo por placa
 7. Cancelar vehículo pendiente
 8. Deshacer último procesamiento
 9. Ver cantidad de vehículos (reporte)
10. Salir
```

## Validaciones

- La placa tiene formato colombiano (`ABC123` para carro, `ABC12D` para moto). Se normaliza, de modo que `abc-123` = `ABC123`.
- No se puede registrar una placa repetida. El sistema lo detecta apenas se escribe la placa.
- El propietario es obligatorio y solo admite letras. La marca es obligatoria. La falla debe tener al menos 5 caracteres. El servicio se elige de una lista.
- Hay mensajes claros cuando la cola o la pila están vacías, cuando la placa no existe y cuando se intenta cancelar un vehículo que no está en espera.
