package Modelo;


import java.util.*;

public class FAT12 implements InterfazFAT {
    private final Map<Integer, Integer> tabla = new HashMap<>();
    private final int totalClusters;

    public FAT12(int totalClusters) {
        this.totalClusters = totalClusters;
    }
    
    public void asignarCluster(int cluster, int siguienteCluster) {
        tabla.put(cluster, siguienteCluster);
    }
    
    public void liberarCluster(int cluster) {
        tabla.remove(cluster);
    }
    
    public Integer obtenerSiguienteCluster(int cluster) {
        return tabla.get(cluster);
    }
    
    public boolean clusterLibre(int cluster) {
        return !tabla.containsKey(cluster);
    }
    
    public int obtenerTotalClusters() {
        return totalClusters;
    }
}
