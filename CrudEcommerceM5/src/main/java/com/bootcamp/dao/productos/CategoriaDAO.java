package com.bootcamp.dao.productos;

import com.bootcamp.configuracion.DataBaseConnection;
import com.bootcamp.modelo.productos.CategoriaDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private final DataBaseConnection db = DataBaseConnection.getInstance();

    /**
     * Listar cat
     * @return
     */
    public List<CategoriaDTO> listarTodas() {
        List<CategoriaDTO> lista = new ArrayList<>();
        String sql = "SELECT idCategoria, nombreCategoria, imagenBanner FROM categorias ORDER BY nombreCategoria ASC";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new CategoriaDTO(
                        rs.getInt("idCategoria"),
                        rs.getString("nombreCategoria"),
                        rs.getString("imagenBanner")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }

    /**
     * guardar cat
     * @param cat
     */
    public void guardar(CategoriaDTO cat) {
        String sql = "INSERT INTO categorias (nombreCategoria, imagenBanner) VALUES (?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cat.getNombreCategoria());
            ps.setString(2, cat.getImagenBanner());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la categoría.");
        }
    }

    /**
     * Actualizar cat
     * @param cat
     */
    public void actualizar(CategoriaDTO cat) {
        String sql = "UPDATE categorias SET nombreCategoria = ?, imagenBanner = ? WHERE idCategoria = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cat.getNombreCategoria());
            ps.setString(2, cat.getImagenBanner());
            ps.setInt(3, cat.getIdCategoria());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la categoría.");
        }
    }

    /**
     * Eliminar cat
     * @param id
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM categorias WHERE idCategoria = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se puede eliminar la categoría. Verifique dependencias.");
        }
    }

    /**
     * Buscar por id
     * @param id
     * @return
     */
    public CategoriaDTO buscarPorId(int id) {
        String sql = "SELECT idCategoria, nombreCategoria, imagenBanner FROM categorias WHERE idCategoria = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CategoriaDTO(
                            rs.getInt("idCategoria"),
                            rs.getString("nombreCategoria"),
                            rs.getString("imagenBanner")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar categoría por ID: " + e.getMessage());
        }
        return null;
    }


    /**
     * Verifico si existe la categoria para no haber dualidad
     * @param nombre
     * @return
     */
    public boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM categorias WHERE nombreCategoria = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}