package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ProductoDAO {

    // Obtener todos los productos
    public static List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM productos")) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio")
                );
                productos.add(p);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener productos: " + e.getMessage());
        }

        return productos;
    }

    // Insertar nuevo producto
    public static void insertar(Producto producto) {
        String sql = "INSERT INTO productos(nombre, cantidad, precio) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getCantidad());
            stmt.setDouble(3, producto.getPrecio());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto agregado correctamente.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar producto: " + e.getMessage());
        }
    }

    public static void actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, cantidad = ?, precio = ? WHERE id = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getCantidad());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getId());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto actualizado correctamente.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar producto: " + e.getMessage());
        }
    }

    // Eliminar producto por ID
    public static void eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar producto: " + e.getMessage());
        }
    }
}