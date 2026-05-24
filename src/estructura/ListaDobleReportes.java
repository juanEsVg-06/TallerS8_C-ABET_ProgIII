package estructura;

import modelo.Reporte;
import modelo.EstadoReporte;
import servicios.GestorReportes;

public class ListaDobleReportes implements GestorReportes {

    // Punteros
    private NodoDoble cabeza;
    private NodoDoble cola;

    public ListaDobleReportes() {
        this.cabeza = null; //Lista está vacía
        this.cola = null;
    }

    @Override
    public void agregarReporte(Reporte reporte) {
        // Nodo nuevo
        NodoDoble nuevoNodo = new NodoDoble(reporte);

        // Evalua si es el primer elemento de la lista
        if (cabeza == null) {
            cabeza = nuevoNodo;
            cola = nuevoNodo; //Unico, es la cabeza y la cola a la vez
        } else {
            // 3. Si ya hay elementos, lo conectamos al final
            cola.setSiguiente(nuevoNodo); // El puntero derecho del último nodo mira al nuevo
            nuevoNodo.setAnterior(cola);  // El puntero izquierdo del nuevo mira al que antes era el último
            cola = nuevoNodo;             //Nuevo nodo es la nueva cola
        }
    }

    @Override
    public String mostrarTodos() {
        if (cabeza == null) {
            return "El historial de reportes está vacío.";
        }

        StringBuilder sb = new StringBuilder();
        NodoDoble actual = cabeza;

        while (actual != null) {
            sb.append(actual.getReporte().toString()).append("\n");
            sb.append("------------------------------------------------\n");
            actual = actual.getSiguiente();
        }
        return sb.toString();
    }

    @Override
    public Reporte obtenerReporte(String id) {
        // Validacion de la lista si esta vacía antes
        if (cabeza == null) {
            System.out.println("El historial de reportes esta vacío");
            return null;
        }

        NodoDoble actual = cabeza;

        //El bucle se ejecuta mientras el nodo actual exista en memoria
        while (actual != null) {
            if (actual.getReporte().getId().equals(id)) {
                return actual.getReporte();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    @Override
    public String mostrarPendientes() {
        if (cabeza == null) {
            return "El historial de reportes está vacio";
        }

        StringBuilder sb = new StringBuilder();
        NodoDoble actual = cabeza;
        boolean pendientes = false;

        while (actual != null) {
            if (actual.getReporte().getEstado() == EstadoReporte.PENDIENTE) {
                sb.append(actual.getReporte().toString()).append("\n");
                sb.append("------------------------------------------------\n");
                pendientes = true;
            }
            actual = actual.getSiguiente();
        }

        return pendientes ? sb.toString() : "No existen reportes con estado PENDIENTE";
    }

    @Override
    public String mostrarPorGravedad(int gravedad) {
        if (cabeza == null) {
            return "El historial de reportes está vacío.";
        }

        StringBuilder sb = new StringBuilder();
        NodoDoble actual = cabeza;
        boolean coincide = false;

        while (actual != null) {
            if (actual.getReporte().getNivelGravedad() == gravedad) {
                sb.append(actual.getReporte().toString()).append("\n");
                sb.append("------------------------------------------------\n");
                coincide = true;
            }
            actual = actual.getSiguiente();
        }

        return coincide ? sb.toString() : "No hay reportes con nivel de gravedad " + gravedad + ".";
    }

    @Override
    public boolean actualizarEstado(String id, EstadoReporte nuevoEstado) {
        // Usamos el método que ya creaste para buscar el reporte
        Reporte reporteEncontrado = obtenerReporte(id);

        // Si no es nulo, significa que sí existe en la lista
        if (reporteEncontrado != null) {
            reporteEncontrado.setEstado(nuevoEstado);
            return true;
        }

        // Si era nulo, devolvemos false
        return false;
    }

    @Override
    public void eliminarReporte(String id) {
        // Validación inicial: si está vacía, no hay nada que borrar
        if (cabeza == null) {
            System.out.println("El historial de reportes está vacío.");
            return;
        }

        NodoDoble actual = cabeza;

        // Recorremos la lista para buscar el nodo específico
        while (actual != null) {
            if (actual.getReporte().getId().equals(id)) {
                // CASO 1: Es el único nodo de la lista
                if (actual == cabeza && actual == cola) {
                    cabeza = null;
                    cola = null;
                }
                // CASO 2: Es el primer nodo (cabeza)
                else if (actual == cabeza) {
                    cabeza = actual.getSiguiente();
                    cabeza.setAnterior(null);
                }
                // CASO 3: Es el último nodo (cola)
                else if (actual == cola) {
                    cola = actual.getAnterior();
                    cola.setSiguiente(null);
                }
                // CASO 4: Está en el medio
                else {
                    actual.getAnterior().setSiguiente(actual.getSiguiente());
                    actual.getSiguiente().setAnterior(actual.getAnterior());
                }
                System.out.println("Reporte eliminado exitosamente");
                return; // Importante para romper el método ya que cumplió su cometido
            }
            actual = actual.getSiguiente();
        }
        System.out.println("Reporte con ID " + id + " no fue encontrado");
    }
}