package com.bootcamp.dao.productos;

import com.bootcamp.configuracion.DataBaseConnection;
import com.bootcamp.modelo.productos.ProductoDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    /**
     * Conexion MySql por instance
     */
    private final DataBaseConnection db = DataBaseConnection.getInstance();

    /**
     * Logica de listar productos
     * @param busqueda
     * @param catId
     * @param subId
     * @param criterioOrden
     * @return
     */
    public List<ProductoDTO> listar(String busqueda, Integer catId, Integer subId, String criterioOrden) {
        List<ProductoDTO> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, s.nombreSubcategoria, c.nombreCategoria " +
                        "FROM productos p " +
                        "INNER JOIN subcategorias s ON p.idSubcategoriaAsociada = s.idSubcategoria " +
                        "INNER JOIN categorias c ON s.idCategoriaAsociada = c.idCategoria WHERE 1=1 "
        );

        if (busqueda != null && !busqueda.isEmpty()) sql.append("AND p.nombreProducto LIKE ? ");
        if (catId != null && catId > 0) sql.append("AND c.idCategoria = ? ");
        if (subId != null && subId > 0) sql.append("AND s.idSubcategoria = ? ");

        // Filtros >, < , A-Z, Z-A, todos
        sql.append(switch (criterioOrden != null ? criterioOrden : "") {
            case "pmin" -> " ORDER BY p.precioProducto ASC";
            case "pmax" -> " ORDER BY p.precioProducto DESC";
            case "az"   -> " ORDER BY p.nombreProducto ASC";
            case "za"   -> " ORDER BY p.nombreProducto DESC";
            default     -> " ORDER BY p.idProducto DESC";
        });

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int i = 1;
            if (busqueda != null && !busqueda.isEmpty()) ps.setString(i++, "%" + busqueda + "%");
            if (catId != null && catId > 0) ps.setInt(i++, catId);
            if (subId != null && subId > 0) ps.setInt(i++, subId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return lista;
    }

    /**
     * Guardar productos
     * @param p
     */
    public void guardar(ProductoDTO p) {
        String sql = "INSERT INTO productos (idSubcategoriaAsociada, nombreProducto, descripcionProducto, precioProducto, stockProducto, imagenProducto) VALUES (?,?,?,?,?,?)";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getIdSubcategoriaAsociada());
            ps.setString(2, p.getNombreProducto());
            ps.setString(3, p.getDescripcionProducto());
            ps.setDouble(4, p.getPrecioProducto());
            ps.setInt(5, p.getStockProducto());
            ps.setString(6, p.getImagenProducto());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    /**
     * Actualizar productos
     * @param p
     */
    public void actualizar(ProductoDTO p) {
        String sql = "UPDATE productos SET idSubcategoriaAsociada=?, nombreProducto=?, descripcionProducto=?, precioProducto=?, stockProducto=?, imagenProducto=? WHERE idProducto=?";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getIdSubcategoriaAsociada());
            ps.setString(2, p.getNombreProducto());
            ps.setString(3, p.getDescripcionProducto());
            ps.setDouble(4, p.getPrecioProducto());
            ps.setInt(5, p.getStockProducto());
            ps.setString(6, p.getImagenProducto());
            ps.setInt(7, p.getIdProducto());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    /**
     * Eliminar productos
     * @param id
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM productos WHERE idProducto = ?";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    /**
     * Buscar por id productos
     * @param id
     * @return
     */
    public ProductoDTO buscarPorId(int id) {
        // Usamos LEFT JOIN para que traiga el producto aunque la categoría/subcategoría falle
        String sql = "SELECT p.*, s.nombreSubcategoria, c.nombreCategoria FROM productos p " +
                "LEFT JOIN subcategorias s ON p.idSubcategoriaAsociada = s.idSubcategoria " +
                "LEFT JOIN categorias c ON s.idCategoriaAsociada = c.idCategoria " +
                "WHERE p.idProducto = ?";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Mapeo de codigos para reutilizar
     * @param rs
     * @return
     * @throws SQLException
     */
    private ProductoDTO mapear(ResultSet rs) throws SQLException {
        return new ProductoDTO(
                rs.getInt("idProducto"), rs.getInt("idSubcategoriaAsociada"),
                rs.getString("nombreProducto"), rs.getString("descripcionProducto"),
                rs.getDouble("precioProducto"), rs.getInt("stockProducto"),
                rs.getString("imagenProducto"), rs.getString("nombreSubcategoria"),
                rs.getString("nombreCategoria")
        );
    }
}