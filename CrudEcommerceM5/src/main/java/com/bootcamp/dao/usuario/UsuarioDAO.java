package com.bootcamp.dao.usuario;

import com.bootcamp.configuracion.DataBaseConnection;
import com.bootcamp.modelo.usuario.UsuarioDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private final DataBaseConnection db = DataBaseConnection.getInstance();

    /**
     * Mapa de codigos para no repetir
     */
    private UsuarioDTO mapearUsuario(ResultSet rs) throws SQLException {
        Date sqlDate = rs.getDate("fecha_nacimiento");
        LocalDate fecha = (sqlDate != null) ? sqlDate.toLocalDate() : null;

        return new UsuarioDTO(
                rs.getInt("id"),
                rs.getString("nombre_completo"),
                fecha,
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("rol")
        );
    }

    /**
     * Listar usuarios
     * @return
     */
    public List<UsuarioDTO> listarTodos() {
        List<UsuarioDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            // Lanzamos el error para que el Servlet sepa que falló la base de datos
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage());
        }
        return list;
    }

    /**
     * Buscar por id
     * @param id
     * @return
     */
    public UsuarioDTO buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Buscar por email
     * @param email
     * @return
     */
    public UsuarioDTO buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar email: " + e.getMessage());
        }
        return null;
    }

    /**
     * Guardar
     * @param dto
     */
    public void guardar(UsuarioDTO dto) {
        String sql = "INSERT INTO usuarios (nombre_completo, fecha_nacimiento, telefono, email, username, password, rol) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getNombreCompleto());
            if (dto.getFechaNacimiento() != null) {
                ps.setDate(2, java.sql.Date.valueOf(dto.getFechaNacimiento()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }
            ps.setString(3, dto.getTelefono());
            ps.setString(4, dto.getEmail());
            ps.setString(5, dto.getUsername());
            ps.setString(6, dto.getPassword());
            ps.setString(7, dto.getRol());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el usuario: " + e.getMessage());
        }
    }

    /**
     * Actualizar / editar
     * @param dto
     */
    public void actualizar(UsuarioDTO dto) {
        String sql = "UPDATE usuarios SET nombre_completo=?, fecha_nacimiento=?, telefono=?, email=?, username=?, password=?, rol=? WHERE id=?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getNombreCompleto());
            if (dto.getFechaNacimiento() != null) {
                ps.setDate(2, java.sql.Date.valueOf(dto.getFechaNacimiento()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }
            ps.setString(3, dto.getTelefono());
            ps.setString(4, dto.getEmail());
            ps.setString(5, dto.getUsername());
            ps.setString(6, dto.getPassword());
            ps.setString(7, dto.getRol());
            ps.setInt(8, dto.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    /**
     * Eliminar por id
     * @param id
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    /**
     * Login
     * @param username
     * @param password
     * @return
     */
    public UsuarioDTO login(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en el login: " + e.getMessage());
        }
        return null;
    }
}