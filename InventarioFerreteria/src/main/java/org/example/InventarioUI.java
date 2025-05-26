package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class InventarioUI extends JFrame {
    private JTable tablaProductos;
    private DefaultTableModel modeloTablaProductos;

    private JTable tablaProveedores;
    private DefaultTableModel modeloTablaProveedores;

    private ProductoDAO productoDAO;
    private ProveedorDAO proveedorDAO;

    public InventarioUI(Connection connection) {
        productoDAO = new ProductoDAO(); // sin conexión si ya está manejada internamente
        proveedorDAO = new ProveedorDAO(connection);

        setTitle("Ferretería Kristal");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel productos
        JPanel panelProductos = new JPanel(new BorderLayout());

        modeloTablaProductos = new DefaultTableModel(new Object[]{"ID", "Nombre", "Cantidad", "Precio"}, 0);
        tablaProductos = new JTable(modeloTablaProductos);
        JScrollPane scrollProductos = new JScrollPane(tablaProductos);

        JPanel panelBotonesProductos = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");

        panelBotonesProductos.add(btnAgregar);
        panelBotonesProductos.add(btnEditar);
        panelBotonesProductos.add(btnEliminar);
        panelBotonesProductos.add(btnActualizar);

        panelProductos.add(scrollProductos, BorderLayout.CENTER);
        panelProductos.add(panelBotonesProductos, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> {
            JTextField campoNombre = new JTextField();
            JTextField campoCantidad = new JTextField();
            JTextField campoPrecio = new JTextField();

            Object[] mensaje = {
                    "Nombre:", campoNombre,
                    "Cantidad:", campoCantidad,
                    "Precio:", campoPrecio
            };

            int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                try {
                    String nombre = campoNombre.getText();
                    int cantidad = Integer.parseInt(campoCantidad.getText());
                    double precio = Double.parseDouble(campoPrecio.getText());

                    Producto nuevo = new Producto(nombre, cantidad, precio);
                    productoDAO.insertar(nuevo);
                    cargarProductos();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Cantidad y precio deben ser válidos.");
                }
            }
        });

        btnEditar.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Selecciona un producto para editar.");
                return;
            }

            int id = (int) modeloTablaProductos.getValueAt(fila, 0);
            String nombre = (String) modeloTablaProductos.getValueAt(fila, 1);
            int cantidad = (int) modeloTablaProductos.getValueAt(fila, 2);
            double precio = (double) modeloTablaProductos.getValueAt(fila, 3);

            JTextField campoNombre = new JTextField(nombre);
            JTextField campoCantidad = new JTextField(String.valueOf(cantidad));
            JTextField campoPrecio = new JTextField(String.valueOf(precio));

            Object[] mensaje = {
                    "Nombre:", campoNombre,
                    "Cantidad:", campoCantidad,
                    "Precio:", campoPrecio
            };

            int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Editar Producto", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                try {
                    String nuevoNombre = campoNombre.getText();
                    int nuevaCantidad = Integer.parseInt(campoCantidad.getText());
                    double nuevoPrecio = Double.parseDouble(campoPrecio.getText());

                    Producto actualizado = new Producto(id, nuevoNombre, nuevaCantidad, nuevoPrecio);
                    productoDAO.actualizar(actualizado);
                    cargarProductos();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Valores inválidos.");
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Selecciona un producto para eliminar.");
                return;
            }

            int id = (int) modeloTablaProductos.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "¿Deseas eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                productoDAO.eliminar(id);
                cargarProductos();
            }
        });

        btnActualizar.addActionListener(e -> cargarProductos());

        // Panel proveedores
        JPanel panelProveedores = new JPanel(new BorderLayout());

        modeloTablaProveedores = new DefaultTableModel(new Object[]{"ID", "Nombre", "Teléfono", "Dirección"}, 0);
        tablaProveedores = new JTable(modeloTablaProveedores);
        JScrollPane scrollProveedores = new JScrollPane(tablaProveedores);

        JPanel panelBotonesProveedores = new JPanel();
        JButton btnAgregarProv = new JButton("Agregar");
        JButton btnEditarProv = new JButton("Editar");
        JButton btnEliminarProv = new JButton("Eliminar");
        JButton btnActualizarProv = new JButton("Actualizar");

        panelBotonesProveedores.add(btnAgregarProv);
        panelBotonesProveedores.add(btnEditarProv);
        panelBotonesProveedores.add(btnEliminarProv);
        panelBotonesProveedores.add(btnActualizarProv);

        panelProveedores.add(scrollProveedores, BorderLayout.CENTER);
        panelProveedores.add(panelBotonesProveedores, BorderLayout.SOUTH);

        btnAgregarProv.addActionListener(e -> {
            JTextField campoNombre = new JTextField();
            JTextField campoTelefono = new JTextField();
            JTextField campoDireccion = new JTextField();

            Object[] mensaje = {
                    "Nombre:", campoNombre,
                    "Teléfono:", campoTelefono,
                    "Dirección:", campoDireccion
            };

            int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Agregar Proveedor", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                Proveedor proveedor = new Proveedor(0, campoNombre.getText(), campoTelefono.getText(), campoDireccion.getText());
                proveedorDAO.insertarProveedor(proveedor);
                cargarProveedores();
            }
        });

        btnEditarProv.addActionListener(e -> {
            int fila = tablaProveedores.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Selecciona un proveedor para editar.");
                return;
            }

            int id = (int) modeloTablaProveedores.getValueAt(fila, 0);
            String nombre = (String) modeloTablaProveedores.getValueAt(fila, 1);
            String telefono = (String) modeloTablaProveedores.getValueAt(fila, 2);
            String direccion = (String) modeloTablaProveedores.getValueAt(fila, 3);

            JTextField campoNombre = new JTextField(nombre);
            JTextField campoTelefono = new JTextField(telefono);
            JTextField campoDireccion = new JTextField(direccion);

            Object[] mensaje = {
                    "Nombre:", campoNombre,
                    "Teléfono:", campoTelefono,
                    "Dirección:", campoDireccion
            };

            int opcion = JOptionPane.showConfirmDialog(null, mensaje, "Editar Proveedor", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                Proveedor proveedor = new Proveedor(id, campoNombre.getText(), campoTelefono.getText(), campoDireccion.getText());
                proveedorDAO.actualizarProveedor(proveedor);
                cargarProveedores();
            }
        });

        btnEliminarProv.addActionListener(e -> {
            int fila = tablaProveedores.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Selecciona un proveedor para eliminar.");
                return;
            }

            int id = (int) modeloTablaProveedores.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "¿Deseas eliminar este proveedor?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                proveedorDAO.eliminarProveedor(id);
                cargarProveedores();
            }
        });

        btnActualizarProv.addActionListener(e -> cargarProveedores());

        tabbedPane.addTab("Productos", panelProductos);
        tabbedPane.addTab("Proveedores", panelProveedores);

        add(tabbedPane);

        cargarProductos();
        cargarProveedores();
    }

    private void cargarProductos() {
        modeloTablaProductos.setRowCount(0);
        List<Producto> productos = productoDAO.obtenerTodos();
        for (Producto p : productos) {
            modeloTablaProductos.addRow(new Object[]{p.getId(), p.getNombre(), p.getCantidad(), p.getPrecio()});
        }
    }

    private void cargarProveedores() {
        modeloTablaProveedores.setRowCount(0);
        List<Proveedor> proveedores = proveedorDAO.obtenerProveedores();
        for (Proveedor p : proveedores) {
            modeloTablaProveedores.addRow(new Object[]{p.getId(), p.getNombre(), p.getTelefono(), p.getDireccion()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/ferreteria?serverTimezone=UTC",
                        "root",
                        "Pantostado12"
                );
                new InventarioUI(connection).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
