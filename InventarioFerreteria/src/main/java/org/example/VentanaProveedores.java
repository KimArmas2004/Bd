package org.example;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VentanaProveedores extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private ProveedorDAO proveedorDAO;

    public VentanaProveedores(ProveedorDAO proveedorDAO) {
        this.proveedorDAO = proveedorDAO;

        setTitle("Lista de Proveedores");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Dirección");

        tabla = new JTable(modelo);
        add(new JScrollPane(tabla));

        cargarProveedores();

        setVisible(true);
    }

    private void cargarProveedores() {
        List<Proveedor> proveedores = proveedorDAO.obtenerProveedores();
        modelo.setRowCount(0); // limpia la tabla

        for (Proveedor p : proveedores) {
            modelo.addRow(new Object[]{p.getId(), p.getNombre(), p.getTelefono(), p.getDireccion()});
        }
    }
}
