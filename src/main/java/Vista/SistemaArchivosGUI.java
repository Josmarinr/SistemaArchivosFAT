package Vista;

import Controlador.SistemaArchivos;
import Modelo.Disco;
import Modelo.FAT12;
import Modelo.FAT16;
import Modelo.FAT32;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SistemaArchivosGUI extends JFrame {

    private JTextField txtTamañoDisco;
    private JTextField txtTamañoCluster;
    private JComboBox<String> cmbSistemaArchivos;
    private JTextArea txtAreaDirectorios;
    private JTextArea txtAreaClusters;
    private JTextArea txtAreaFAT;
    private JScrollPane scpDirectorios;
    private JScrollPane scpClusters;
    private JScrollPane scpFAT;
    private JTextField txtNombre;
    private JTextField txtExtension;
    private JCheckBox chkEsDirectorio;
    private JTextField txtTamañoBytes;
    private JTextField txtDireccion;
    private SistemaArchivos sistemaArchivos;

    public SistemaArchivosGUI() {
        super("Sistema de Archivos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear panel superior
        JPanel pnlSuperior = new JPanel();
        pnlSuperior.setLayout(new FlowLayout());
        pnlSuperior.add(new JLabel("Tamaño de disco:"));
        txtTamañoDisco = new JTextField(5);
        pnlSuperior.add(txtTamañoDisco);
        pnlSuperior.add(new JLabel("Tamaño de cluster:"));
        txtTamañoCluster = new JTextField(5);
        pnlSuperior.add(txtTamañoCluster);
        pnlSuperior.add(new JLabel("Sistema de archivos:"));
        cmbSistemaArchivos = new JComboBox<>(new String[]{"FAT12", "FAT16", "FAT32"});
        pnlSuperior.add(cmbSistemaArchivos);
        JButton btnIniciar = new JButton("Iniciar");
        pnlSuperior.add(btnIniciar);

        add(pnlSuperior, BorderLayout.NORTH);

        // Crear panel central con dos divisores
        JSplitPane pnlCentral = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        add(pnlCentral, BorderLayout.CENTER);

// Crear área de texto para directorios
        txtAreaDirectorios = new JTextArea();
        txtAreaDirectorios.setEditable(false);
        txtAreaDirectorios.setPreferredSize(new Dimension(400, 400)); // Establecer tamaño preferido
        scpDirectorios = new JScrollPane(txtAreaDirectorios);
        pnlCentral.setLeftComponent(scpDirectorios);

// Crear un segundo JSplitPane para dividir clusters y FAT
        JSplitPane pnlDerecho = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

// Crear área de texto para clusters
        txtAreaClusters = new JTextArea();
        txtAreaClusters.setEditable(false);
        txtAreaClusters.setPreferredSize(new Dimension(200, 400)); // Establecer tamaño preferido
        scpClusters = new JScrollPane(txtAreaClusters);
        pnlDerecho.setLeftComponent(scpClusters);

// Crear área de texto para FAT
        txtAreaFAT = new JTextArea();
        txtAreaFAT.setEditable(false);
        txtAreaFAT.setPreferredSize(new Dimension(200, 400)); // Establecer tamaño preferido
        scpFAT = new JScrollPane(txtAreaFAT);
        pnlDerecho.setRightComponent(scpFAT);

// Añadir el segundo JSplitPane al componente derecho del panel central
        pnlCentral.setRightComponent(pnlDerecho);

// Ajustar las proporciones de las divisiones
        pnlCentral.setDividerLocation(400); // División inicial entre directorios y clusters/FAT
        pnlDerecho.setDividerLocation(350); // División inicial entre clusters y FAT

        // Crear panel inferior
        JPanel pnlInferior = new JPanel();
        pnlInferior.setLayout(new FlowLayout());
        pnlInferior.add(new JLabel("Nombre:"));
        txtNombre = new JTextField(10);
        pnlInferior.add(txtNombre);
        pnlInferior.add(new JLabel("Extensión:"));
        txtExtension = new JTextField(5);
        pnlInferior.add(txtExtension);
        pnlInferior.add(new JLabel("Es directorio:"));
        chkEsDirectorio = new JCheckBox();
        pnlInferior.add(chkEsDirectorio);
        pnlInferior.add(new JLabel("Tamaño (bytes):"));
        txtTamañoBytes = new JTextField(5);
        pnlInferior.add(txtTamañoBytes);
        pnlInferior.add(new JLabel("Direccion:"));
        txtDireccion = new JTextField(10);
        pnlInferior.add(txtDireccion);
        JButton btnAgregar = new JButton("Agregar");
        pnlInferior.add(btnAgregar);
        JButton btnBorrar = new JButton("Borrar");
        pnlInferior.add(btnBorrar);
        JButton btnActualizar = new JButton("Actualizar");
        pnlInferior.add(btnActualizar);
        add(pnlInferior, BorderLayout.SOUTH);

        pack();
        setVisible(true);

        // Acción para cambiar estado de campos según sea directorio o archivo
        chkEsDirectorio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean isSelected = chkEsDirectorio.isSelected();
                txtExtension.setEnabled(!isSelected);
                txtTamañoBytes.setEnabled(!isSelected);
                if (isSelected) {
                    txtExtension.setText("");
                    txtTamañoBytes.setText("");
                }
            }
        });

        // Acción para iniciar el sistema de archivos
        btnIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int tamañoDisco = Integer.parseInt(txtTamañoDisco.getText());
                    int tamañoCluster = Integer.parseInt(txtTamañoCluster.getText());
                    String sistemaArchivosTipo = (String) cmbSistemaArchivos.getSelectedItem();
                    if (sistemaArchivosTipo.equals("FAT12")) {
                        int totalClusters = tamañoDisco / tamañoCluster;
                        FAT12 fat = new FAT12(totalClusters);
                        Disco disco = new Disco(tamañoDisco, tamañoCluster, fat);
                        sistemaArchivos = new SistemaArchivos(disco);
                        System.out.println("Sistema FAT12 iniciado");
                    } else if (sistemaArchivosTipo.equals("FAT16")) {
                        int totalClusters = tamañoDisco / tamañoCluster;
                        FAT16 fat = new FAT16(totalClusters);
                        Disco disco = new Disco(tamañoDisco, tamañoCluster, fat);
                        sistemaArchivos = new SistemaArchivos(disco);
                        System.out.println("Sistema FAT16 iniciado");
                    } else if (sistemaArchivosTipo.equals("FAT32")) {
                        int totalClusters = tamañoDisco / tamañoCluster;
                        FAT32 fat = new FAT32(totalClusters);
                        Disco disco = new Disco(tamañoDisco, tamañoCluster, fat);
                        sistemaArchivos = new SistemaArchivos(disco);
                        System.out.println("Sistema FAT32 iniciado");
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SistemaArchivosGUI.this, "Por favor, introduce valores numéricos válidos.");
                }
            }
        });

        // Acción para agregar archivos o directorios
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (sistemaArchivos == null) {
                    JOptionPane.showMessageDialog(SistemaArchivosGUI.this, "Primero inicia el sistema de archivos.");
                    return;
                }

                String nombre = txtNombre.getText();
                String extension = txtExtension.getText();
                String direccion = txtDireccion.getText();
                boolean esDirectorio = chkEsDirectorio.isSelected();
                try {
                    int tamañoBytes = esDirectorio ? 0 : Integer.parseInt(txtTamañoBytes.getText());
                    if (esDirectorio) {
                        sistemaArchivos.crearDirectorio(nombre, direccion);
                        String rutaDirectorio = txtDireccion.getText();
                        String listado = sistemaArchivos.listarArchivos(rutaDirectorio);
                        txtAreaDirectorios.setText(listado);
                        String arbol = sistemaArchivos.obtenerArbolDirectorio();
                        txtAreaClusters.setText(arbol);
                        String fat = sistemaArchivos.mostrarEstadoFAT();
                        txtAreaFAT.setText(fat);
                    } else {
                        sistemaArchivos.crearArchivo(nombre, extension, tamañoBytes, direccion);
                        String rutaDirectorio = txtDireccion.getText();
                        String listado = sistemaArchivos.listarArchivos(rutaDirectorio);
                        txtAreaDirectorios.setText(listado);
                        String arbol = sistemaArchivos.obtenerArbolDirectorio();
                        txtAreaClusters.setText(arbol);
                        String fat = sistemaArchivos.mostrarEstadoFAT();
                        txtAreaFAT.setText(fat);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SistemaArchivosGUI.this, "Por favor, introduce un tamaño válido en bytes.");
                }
            }
        });

        // Acción para listar archivos y directorios
        btnBorrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreArchivo = JOptionPane.showInputDialog(
                        null,
                        "Nombre del archivo a eliminar:",
                        "Eliminar Archivo",
                        JOptionPane.QUESTION_MESSAGE
                );
                String rutaArchivo = JOptionPane.showInputDialog(
                        null,
                        "Ruta del archivo a eliminar:",
                        "Eliminar Archivo",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
                    // Realizar la eliminación del archivo (aquí debes agregar tu lógica)
                    sistemaArchivos.eliminarArchivo(nombreArchivo, rutaArchivo);
                    String listado = sistemaArchivos.listarArchivos(rutaArchivo);
                    txtAreaDirectorios.setText(listado);
                    String arbol = sistemaArchivos.obtenerArbolDirectorio();
                    txtAreaClusters.setText(arbol);
                    String fat = sistemaArchivos.mostrarEstadoFAT();
                    txtAreaFAT.setText(fat);
                } else {
                    // Mostrar mensaje si no se ingresó un nombre válido
                    JOptionPane.showMessageDialog(
                            null,
                            "Nombre de archivo no válido.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SistemaArchivosGUI();
            }
        });
    }
}
