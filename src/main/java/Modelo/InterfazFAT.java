package Modelo;


public interface InterfazFAT {
    void asignarCluster(int cluster, int siguienteCluster);
    void liberarCluster(int cluster);
    Integer obtenerSiguienteCluster(int cluster);
    boolean clusterLibre(int cluster);
    int obtenerTotalClusters();
}
