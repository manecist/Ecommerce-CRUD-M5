package com.bootcamp.dao.productos;
import com.bootcamp.configuracion.DataBaseConnection;
import com.bootcamp.modelo.productos.SubcategoriaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubcategoriaDAO {
    private final DataBaseConnection db = DataBaseConnection.getInstance();

    /**
     * Mapa para ahorrar codigo
     * @param rs
     * @return
     * @throws SQLException
     */
    private SubcategoriaDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        return new SubcategoriaDTO(
                rs.getInt("idSubcategoria"),      // idSubcategoria
                rs.getInt("idCategoriaAsociada"),  // idCategoriaAsociada
                rs.getString("nombreSubcategoria"),// nombreSubcategoria
                rs.getString("nombreCategoria")    // nombreCategoria (viene del JOIN)
        );
    }

    /**
     * Listar sub
     * @return
     */
    public List<SubcategoriaDTO> listarTodas() {
        List<SubcategoriaDTO> lista = new ArrayList<>();
        // JOIN para traer el nombre de la categoría padre
        String sql = "SELECT s.idSubcategoria, s.idCategoriaAsociada, s.nombreSubcategoria, c.nombreCategoria " +
                "FROM subcategorias s " +
                "INNER JOIN categorias c ON s.idCategoriaAsociada = c.idCategoria " +
                "ORDER BY c.nombreCategoria ASC, s.nombreSubcategoria ASC";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // USAMOS EL MAPEO AQUÍ
                lista.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar subcategorías: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Listar por cat
     * @param idCat
     * @return
     */
    public List<SubcategoriaDTO> listarPorCategoria(int idCat) {
        List<SubcategoriaDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM subcategorias WHERE idCategoriaAsociada = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCat);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new SubcategoriaDTO(
                            rs.getInt("idSubcategoria"),
                            rs.getInt("idCategoriaAsociada"),
                            rs.getString("nombreSubcategoria")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Guardar sub
     * @param sub
     */
    public void guardar(SubcategoriaDTO sub) {
        String sql = "INSERT INTO subcategorias (idCategoriaAsociada, nombreSubcategoria) VALUES (?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sub.getIdCategoriaAsociada());
            ps.setString(2, sub.getNombreSubcategoria());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar subcategoría: " + e.getMessage());
        }
    }

    /**
     * Actualizar sub
     * @param sub
     */
    public void actualizar(SubcategoriaDTO sub) {
        String sql = "UPDATE subcategorias SET idCategoriaAsociada = ?, nombreSubcategoria = ? WHERE idSubcategoria = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sub.getIdCategoriaAsociada());
            ps.setString(2, sub.getNombreSubcategoria());
            ps.setInt(3, sub.getIdSubcategoria());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar subcategoría.");
        }
    }

    /**
     * Buscar por id sub
     * @param id
     * @return
     */
    public SubcategoriaDTO buscarPorId(int id) {
        String sql = "SELECT s.*, c.nombreCategoria FROM subcategorias s " +
                "INNER JOIN categorias c ON s.idCategoriaAsociada = c.idCategoria " +
                "WHERE s.idSubcategoria = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // REUTILIZAMOS EL MAPEO AQUÍ TAMBIÉN
                    return mapResultSetToDTO(rs);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Eliminar sub
     * @param id
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM subcategorias WHERE idSubcategoria = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar subcategoría.");
        }
    }
}