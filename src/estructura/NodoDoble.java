package estructura;

import modelo.Reporte;

public class NodoDoble {
    private Reporte reporte;
    private NodoDoble anterior;
    private NodoDoble siguiente;

    public NodoDoble(Reporte reporte) {
        this.reporte = reporte;
        this.anterior = null; //Sin enlace
        this.siguiente = null; //Sin enlace
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    public NodoDoble getAnterior() {
        return anterior;
    }

    public void setAnterior(NodoDoble anterior) {
        this.anterior = anterior;
    }

    public NodoDoble getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoDoble siguiente) {
        this.siguiente = siguiente;
    }
}
