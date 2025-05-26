package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;

public class FormularioProveedor extends JFrame {
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JButton btnGuardar;

    public FormularioProveedor(Connection connection) {
        setTitle("Registrar Proveedor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblTelefono = new JLabel("Teléfono:");
        JLabel lblDireccion = new JLabel("Dirección:");

        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtDireccion = new JTextField();
        btnGuardar = new JButton("Guardar");

        add(lblNombre);
        add(txtNombre);
        add(lblTelefono);
        add(txtTelefono);
        add(lblDireccion);
        add(txtDireccion);
        add(new JLabel());  // Espacio vacío
        add(btnGuardar);

        ProveedorDAO dao = new ProveedorDAO(connection);

        btnGuardar.addActionListener((ActionEvent e) -> {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String direccion = txtDireccion.getText().trim();

            if (nombre.isEmpty() || direccion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Dirección son obligatorios.");
                return;
            }

            Proveedor proveedor = new Proveedor(0, nombre, telefono, direccion);
            if (dao.insertarProveedor(proveedor)) {
                JOptionPane.showMessageDialog(this, "Proveedor guardado con éxito.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el proveedor.");
            }
        });

        setVisible(true);
    }
}
