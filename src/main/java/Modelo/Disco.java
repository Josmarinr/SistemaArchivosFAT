package Modelo;


import java.util.*;

public class Disco {
    private final int tamaño;
    private final int tamañoCluster;
    private final byte[] datos;
    private final InterfazFAT fat;
    private final List<EntradaDirectorio> directorioRaiz;

    public Disco(int tamaño, int tamañoCluster, InterfazFAT fat) {
        this.tamaño = tamaño;
        this.tamañoCluster = tamañoCluster;
        this.datos = new byte[tamaño];
        this.fat = fat;
        this.directorioRaiz = new ArrayList<>();
    }

    public InterfazFAT obtenerFAT() {
        return fat;
    }

    public List<EntradaDirectorio> obtenerDirectorioRaiz() {
        return directorioRaiz;
    }

    public byte[] obtenerDatos() {
        return datos;
    }

    public int obtenerTamañoCluster() {
        return tamañoCluster;
    }
}
