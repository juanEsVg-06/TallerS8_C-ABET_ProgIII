package modelo;

import java.time.LocalDate;

public class Reporte {

    //Atributos
    private String id;
    private String nombreUser;
    private String descripcion;
    private int nivelGravedad;
    private LocalDate fechaReporte;
    private EstadoReporte estado;

    //Constructor
    public Reporte (String id, String nombreUser, String descripcion, int nivelGravedad, EstadoReporte estado) {
        this.id = id;
        this.nombreUser = nombreUser;
        this.descripcion = descripcion;
        this.nivelGravedad = nivelGravedad;
        this.fechaReporte = LocalDate.now(); // Se asigna automáticamente al crearse
        this.estado = estado;
    }

    //Getters y Setters
    public String getId() {
        return id;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNivelGravedad() {
        return nivelGravedad;
    }

    public void setNivelGravedad(int nivelGravedad) {
        this.nivelGravedad = nivelGravedad;
    }

    public LocalDate getFechaReporte() {
        return fechaReporte;
    }

    public EstadoReporte getEstado() {
        return estado;
    }

    public void setEstado(EstadoReporte estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "|| Reporte - ID > " + id + " ||" +
                "Nombre: " + nombreUser  + "\n" +
                "Descripcion: " + descripcion + "\n" +
                "\nNivel de Gravedad > " + nivelGravedad + " | " + "Fecha de Reporte > " + fechaReporte + "\n" +
                "Estado > " + estado;
    }
}
