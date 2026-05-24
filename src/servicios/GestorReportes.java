package servicios;

import modelo.Reporte;
import modelo.EstadoReporte;

public interface GestorReportes {
    // Operaciones base
    void agregarReporte(Reporte reporte);
    void eliminarReporte(String id);
    Reporte obtenerReporte(String id);

    // Filtros y actualizaciones
    String mostrarTodos();
    String mostrarPendientes();
    String mostrarPorGravedad(int gravedad);
    boolean actualizarEstado(String id, EstadoReporte nuevoEstado);
}