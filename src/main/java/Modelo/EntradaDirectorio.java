package Modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntradaDirectorio {
    private String nombre;
    private String extension;
    private boolean esDirectorio;
    private int inicioCluster;
    private Date fechaCreacion;
    private int longitudClusters;
    private List<EntradaDirectorio> contenido; // Para contenido del directorio

    public EntradaDirectorio(String nombre, String extension, boolean esDirectorio, int inicioCluster, int longitudClusters) {
        this.nombre = nombre;
        this.extension = extension;
        this.esDirectorio = esDirectorio;
        this.inicioCluster = inicioCluster;
        this.fechaCreacion = new Date();
        this.longitudClusters = longitudClusters;
        if (esDirectorio) {
            this.contenido = new ArrayList<>();
        }
    }

    public String obtenerNombre() {
        return nombre;
    }

    public String obtenerExtension() {
        return extension;
    }

    public boolean esDirectorio() {
        return esDirectorio;
    }

    public int obtenerInicioCluster() {
        return inicioCluster;
    }

    public Date obtenerFechaCreacion() {
        return fechaCreacion;
    }

    public int obtenerLongitudClusters() {
        return longitudClusters;
    }

    public List<EntradaDirectorio> obtenerContenido() {
        return contenido;
    }

    public void agregarEntrada(EntradaDirectorio entrada) {
        if (esDirectorio) {
            contenido.add(entrada);
        }
    }
}
