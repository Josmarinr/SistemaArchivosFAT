package Controlador;

import Modelo.Disco;
import Modelo.EntradaDirectorio;
import java.util.*;

public class SistemaArchivos {

    private final Disco disco;

    public SistemaArchivos(Disco disco) {
        this.disco = disco;
        EntradaDirectorio directorioRaiz = new EntradaDirectorio("", "", true, 0, 1);
        disco.obtenerDirectorioRaiz().add(directorioRaiz);
    }

    public void crearArchivo(String nombre, String extension, int tamañoBytes, String rutaDirectorio) {
        EntradaDirectorio directorio = obtenerDirectorio(rutaDirectorio);
        if (directorio == null) {
            System.out.println("Directorio no encontrado.");
            return;
        }

        int tamañoCluster = disco.obtenerTamañoCluster();
        int longitudClusters = (int) Math.ceil((double) tamañoBytes / tamañoCluster);

        int inicioCluster = buscarClusterLibre();
        if (inicioCluster == -1) {
            System.out.println("No hay clusters libres.");
            return;
        }

        if (!asignarClusters(inicioCluster, longitudClusters)) {
            System.out.println("No hay clusters suficientes para asignar el archivo completo.");
            return;
        }

        EntradaDirectorio entrada = new EntradaDirectorio(nombre, extension, false, inicioCluster, longitudClusters);
        directorio.agregarEntrada(entrada);
    }

    public void crearDirectorio(String nombre, String rutaDirectorio) {
        EntradaDirectorio directorio = obtenerDirectorio(rutaDirectorio);
        if (directorio == null) {
            System.out.println("Directorio no encontrado.");
            return;
        }

        int inicioCluster = buscarClusterLibre();
        if (inicioCluster == -1) {
            System.out.println("No hay clusters libres.");
            return;
        }

        EntradaDirectorio entrada = new EntradaDirectorio(nombre, "", true, inicioCluster, 1);
        directorio.agregarEntrada(entrada);

        // Agregar entrada del directorio en la FAT
        disco.obtenerFAT().asignarCluster(inicioCluster, -1); // -1 indica el final del directorio
    }

    public String listarArchivos(String rutaDirectorio) {
        EntradaDirectorio directorio = obtenerDirectorio(rutaDirectorio);
        if (directorio == null || !directorio.esDirectorio()) {
            return "Directorio no encontrado.";
        }

        return listarEntradas(directorio, "");
    }

    private String listarEntradas(EntradaDirectorio directorio, String indentacion) {
        StringBuilder resultado = new StringBuilder();
        for (EntradaDirectorio entrada : directorio.obtenerContenido()) {
            resultado.append(indentacion)
                    .append(entrada.obtenerNombre())
                    .append(entrada.esDirectorio() ? " [DIR]" : " [FILE]")
                    .append(" | Ext: ").append(entrada.obtenerExtension())
                    .append(" | Creado: ").append(entrada.obtenerFechaCreacion())
                    .append(" | Inicio Cluster: ").append(entrada.obtenerInicioCluster())
                    .append(" | Longitud Clusters: ").append(entrada.obtenerLongitudClusters())
                    .append("\n");
            if (entrada.esDirectorio()) {
                resultado.append(listarEntradas(entrada, indentacion + "  "));
            }
        }
        return resultado.toString();
    }

    public void eliminarArchivo(String nombre, String rutaDirectorio) {
        EntradaDirectorio directorio = obtenerDirectorio(rutaDirectorio);
        if (directorio == null || !directorio.esDirectorio()) {
            System.out.println("Directorio no encontrado.");
            return;
        }

        Iterator<EntradaDirectorio> iterator = directorio.obtenerContenido().iterator();
        while (iterator.hasNext()) {
            EntradaDirectorio entrada = iterator.next();
            if (entrada.obtenerNombre().equals(nombre)) {
                liberarClusters(entrada.obtenerInicioCluster());
                iterator.remove();
                return;
            }
        }
        System.out.println("Archivo o directorio no encontrado.");
    }

    public void modificarArchivo(String nombre, String nuevoNombre, String rutaDirectorio) {
        EntradaDirectorio directorio = obtenerDirectorio(rutaDirectorio);
        if (directorio == null || !directorio.esDirectorio()) {
            System.out.println("Directorio no encontrado.");
            return;
        }

        for (EntradaDirectorio entrada : directorio.obtenerContenido()) {
            if (entrada.obtenerNombre().equals(nombre) && !entrada.esDirectorio()) {
                entrada = new EntradaDirectorio(nuevoNombre, entrada.obtenerExtension(), false, entrada.obtenerInicioCluster(), entrada.obtenerLongitudClusters());
                System.out.println("Archivo modificado.");
                return;
            }
        }
        System.out.println("Archivo no encontrado.");
    }

    private EntradaDirectorio obtenerDirectorio(String rutaDirectorio) {
        String[] partesRuta = rutaDirectorio.split("/");
        List<EntradaDirectorio> directorioRaiz = disco.obtenerDirectorioRaiz();
        EntradaDirectorio directorioActual = directorioRaiz.get(0); // Inicializar con el directorio raíz principal
        List<EntradaDirectorio> contenidoActual = directorioActual.obtenerContenido();

        for (String parte : partesRuta) {
            if (parte.isEmpty()) {
                continue;
            }
            boolean encontrado = false;
            for (EntradaDirectorio entrada : contenidoActual) {
                if (entrada.esDirectorio() && entrada.obtenerNombre().equals(parte)) {
                    directorioActual = entrada;
                    contenidoActual = entrada.obtenerContenido();
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                return null;
            }
        }
        return directorioActual;
    }

    private int buscarClusterLibre() {
        int totalClusters = disco.obtenerFAT().obtenerTotalClusters();
        for (int i = 0; i < totalClusters; i++) {
            if (disco.obtenerFAT().clusterLibre(i)) {
                return i;
            }
        }
        return -1;
    }

    private boolean asignarClusters(int inicioCluster, int longitudClusters) {
        int clusterActual = inicioCluster;
        for (int i = 0; i < longitudClusters - 1; i++) {
            int siguienteCluster = buscarClusterLibre();
            if (siguienteCluster == -1) {
                liberarClusters(inicioCluster);
                return false;
            }
            disco.obtenerFAT().asignarCluster(clusterActual, siguienteCluster);
            clusterActual = siguienteCluster;
        }
        disco.obtenerFAT().asignarCluster(clusterActual, -1); // -1 indica el final del archivo
        return true;
    }

    private void liberarClusters(int inicioCluster) {
        Integer cluster = inicioCluster;
        while (cluster != null && cluster != -1) {
            Integer siguienteCluster = disco.obtenerFAT().obtenerSiguienteCluster(cluster);
            disco.obtenerFAT().liberarCluster(cluster);
            cluster = siguienteCluster;
        }
    }

    public String mostrarEstadoFAT() {
        StringBuilder estadoFAT = new StringBuilder();
        estadoFAT.append("Estado de la FAT:\n");
        for (int i = 0; i < disco.obtenerFAT().obtenerTotalClusters(); i++) {
            Integer siguienteCluster = disco.obtenerFAT().obtenerSiguienteCluster(i);
            if (siguienteCluster != null) {
                estadoFAT.append("Cluster ").append(i).append(" -> ").append(siguienteCluster).append("\n");
            }
        }
        return estadoFAT.toString();
    }

    public void mostrarArbolDirectorio() {
        System.out.println(mostrarArbolDirectorioRecursivo(disco.obtenerDirectorioRaiz(), ""));
    }

    public String obtenerArbolDirectorio() {
        return mostrarArbolDirectorioRecursivo(disco.obtenerDirectorioRaiz(), "");
    }

    private String mostrarArbolDirectorioRecursivo(List<EntradaDirectorio> directorio, String rutaActual) {
        StringBuilder resultado = new StringBuilder();
        for (EntradaDirectorio entrada : directorio) {
            if (entrada.esDirectorio()) {
                resultado.append(rutaActual).append(entrada.obtenerNombre()).append("/\n");
                resultado.append(mostrarArbolDirectorioRecursivo(entrada.obtenerContenido(), rutaActual + entrada.obtenerNombre() + "/"));
            } else {
                resultado.append(rutaActual).append(entrada.obtenerNombre()).append(".").append(entrada.obtenerExtension()).append("\n");
            }
        }
        return resultado.toString();
    }
}
