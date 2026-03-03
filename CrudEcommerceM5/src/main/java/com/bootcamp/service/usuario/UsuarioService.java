package com.bootcamp.service.usuario;

import com.bootcamp.dao.usuario.UsuarioDAO;
import com.bootcamp.modelo.usuario.UsuarioDTO;

import java.util.List;

public class UsuarioService {
    /** Instanciamos el DAO de Usuarios **/
    private final UsuarioDAO dao = new UsuarioDAO();
    public List<UsuarioDTO> listar() {
        return dao.listarTodos();
    }

    public UsuarioDTO buscarPorId(int id) {
        if (id <= 0) return null;
        return dao.buscarPorId(id); // Retorna lo que encuentre el DAO
    }

    /** * Guarda un nuevo usuario con validaciones de negocio */
    public void guardar(UsuarioDTO dto) {
        //  Validar que los campos no estén nulos o vacíos
        if (dto.getNombreCompleto() == null || dto.getNombreCompleto().isBlank()
                || dto.getEmail() == null || dto.getEmail().isBlank()
                || dto.getUsername() == null || dto.getUsername().isBlank()
                || dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Todos los campos obligatorios deben estar completos.");
        }

        // Validar la fortaleza de la contraseña
        validarFortalezaPassword(dto.getPassword());

        // Validar si el email ya existe (Regla de negocio)
        UsuarioDTO usuarioExistente = dao.buscarPorEmail(dto.getEmail().trim());
        if (usuarioExistente != null) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        // Si pasó todas las pruebas, guardamos
        dao.guardar(dto);
    }

    /** * Actualiza la información validando el ID */
    public void actualizar(UsuarioDTO dto) {
        //  Validar ID
        if (dto.getId() <= 0) {
            throw new IllegalArgumentException("ID de usuario no válido.");
        }

        // Validar campos obligatorios
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }

        //  VALIDACIÓN DE EMAIL
        UsuarioDTO usuarioExistente = dao.buscarPorEmail(dto.getEmail().trim());

        // Solo lanzamos error si:
        // El email ya existe en la BD
        // Y ADEMÁS, ese email le pertenece a OTRA persona (id distinto)
        if (usuarioExistente != null && usuarioExistente.getId() != dto.getId()) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado por otro usuario.");
        }

        // Solo validamos si el usuario INTENTÓ escribir una nueva contraseña
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            validarFortalezaPassword(dto.getPassword());
        } else {
            // Aquí hay un truco: Si el DTO viene con password vacío,
            // deberías cargarle el password que ya tenía en la BD para no borrarlo.
            UsuarioDTO actual = dao.buscarPorId(dto.getId());
            dto.setPassword(actual.getPassword());
        }

        // Si pasa la validación, procedemos
        dao.actualizar(dto);
    }

    /**
     * Validacion de password
     * @param password
     */
    private void validarFortalezaPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }

        // Regex: Requiere al menos una mayúscula (?=.*[A-Z])
        // y al menos un carácter especial (?=.*[!@#$%^&*(),.?\":{}|<>])
        String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).+$";

        if (!password.matches(regex)) {
            throw new IllegalArgumentException("La contraseña debe incluir al menos una mayúscula y un carácter especial.");
        }
    }

    /**
     * Eliminar por id
     * @param id
     */
    public void eliminar(int id) {
        if (id <= 0) { throw new IllegalArgumentException("ID no válido para eliminar.");
        } dao.eliminar(id);
    }

    /** * Login de usuario con limpieza de datos */
    public UsuarioDTO login(String username, String password) {
        // Limpiamos espacios accidentales en el username
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return null;
        } return dao.login(username.trim(), password);
    }
}