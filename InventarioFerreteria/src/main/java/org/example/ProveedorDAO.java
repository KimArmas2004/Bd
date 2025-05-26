package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {
    private final Connection connection;

    public ProveedorDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean insertarProveedor(Proveedor proveedor) {
        String sql = "INSERT INTO proveedores (nombre, telefono, direccion) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getTelefono());
            stmt.setString(3, proveedor.getDireccion());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar proveedor: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarProveedor(Proveedor proveedor) {
        String sql = "UPDATE proveedores SET nombre = ?, telefono = ?, direccion = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getTelefono());
            stmt.setString(3, proveedor.getDireccion());
            stmt.setInt(4, proveedor.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar proveedor: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProveedor(int id) {
        String sql = "DELETE FROM proveedores WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar proveedor: " + e.getMessage());
            return false;
        }
    }

    public List<Proveedor> obtenerProveedores() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedores";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Proveedor p = new Proveedor();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setTelefono(rs.getString("telefono"));
                p.setDireccion(rs.getString("direccion"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener proveedores: " + e.getMessage());
        }
        return lista;
    }

    public Proveedor obtenerPorId(int id) {
        String sql = "SELECT * FROM proveedores WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Proveedor(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("direccion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener proveedor por ID: " + e.getMessage());
        }
        return null;
    }
}
