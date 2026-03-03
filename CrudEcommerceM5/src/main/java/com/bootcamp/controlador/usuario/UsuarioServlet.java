package com.bootcamp.controlador.usuario;

import com.bootcamp.modelo.usuario.UsuarioDTO;
import com.bootcamp.service.usuario.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    /**
     * Usuario service
     */
    private final UsuarioService service = new UsuarioService();

    /**
     * captacion de links
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        // Obtenemos al usuario de la sesión para comparaciones de ID
        UsuarioDTO logueado = (UsuarioDTO) request.getSession().getAttribute("usuarioLogueado");

        String idStr = request.getParameter("id");
        int idParam = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : 0;

        // Cualquiera puede entrar a "new"
        if ("new".equals(action)) {
            mostrarFormularioNuevo(request, response);
            return;
        }

        // Si no estás logueado y no es "new", vas al login
        if (logueado == null) {
            response.sendRedirect(request.getContextPath() + "/usuario/login.jsp");
            return;
        }

        // admin / dueño
        boolean admin = esAdmin(request);
        boolean esDuenio = (logueado.getId() == idParam);

        // Bloqueo para Listar o Eliminar (Solo Admin o Dueño para eliminar)
        if ("list".equals(action) && !admin) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso de Admin.");
            return;
        }

        if ("delete".equals(action) && !admin && !esDuenio) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No puedes borrar a otros.");
            return;
        }

        // Bloqueo para Editar (Solo Admin o el propio Dueño)
        if ("edit".equals(action) && !admin && !esDuenio) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado al perfil.");
            return;
        }

        switch (action) {
            case "list" -> listarUsuarios(request, response);
            case "edit" -> mostrarFormularioEditar(request, response);
            case "delete" -> eliminarUsuario(request, response);
            default -> {
                if (admin) listarUsuarios(request, response);
                else response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        }
    }

    /**
     * Captaciion formularios
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        guardarOActualizarUsuario(request, response);
    }

    /**
     * Guardar / actualizar usuario
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void guardarOActualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        UsuarioDTO dto = null;
        try {
            String idStr = request.getParameter("id");
            int id = (idStr == null || idStr.isEmpty()) ? 0 : Integer.parseInt(idStr);

            String nuevaPass = request.getParameter("password");
            String passFinal;

            // Lógica de recuperación de pass si viene vacía en edición
            if (id > 0 && (nuevaPass == null || nuevaPass.isBlank())) {
                UsuarioDTO existente = service.buscarPorId(id);
                passFinal = existente.getPassword();
            } else {
                passFinal = nuevaPass;
            }

            // Mapeo del DTO desde el formulario
            dto = new UsuarioDTO();
            dto.setId(id);
            dto.setNombreCompleto(request.getParameter("nombreCompleto"));
            String fechaStr = request.getParameter("fechaNacimiento");
            if(fechaStr != null && !fechaStr.isBlank()) dto.setFechaNacimiento(LocalDate.parse(fechaStr));
            dto.setTelefono(request.getParameter("telefono"));
            dto.setEmail(request.getParameter("email"));
            dto.setUsername(request.getParameter("username"));
            dto.setPassword(passFinal);

            // Seguridad en el ROL: Si no es admin, siempre será 'usuario'
            String rolForm = request.getParameter("rol");
            dto.setRol(esAdmin(request) && rolForm != null ? rolForm : "usuario");

            if (id == 0) {
                service.guardar(dto);
                response.sendRedirect(request.getContextPath() + "/usuario/login.jsp?msj=ok");
            } else {
                service.actualizar(dto);
                // Si el usuario se editó a sí mismo, actualizamos su sesión
                UsuarioDTO logueado = (UsuarioDTO) request.getSession().getAttribute("usuarioLogueado");
                if (logueado.getId() == dto.getId()) {
                    request.getSession().setAttribute("usuarioLogueado", dto);
                }

                String destino = esAdmin(request) ? "/usuarios" : "/index.jsp";
                response.sendRedirect(request.getContextPath() + destino + "?msj=edit_ok");
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("usuario", dto);
            request.getRequestDispatcher("/usuario/user-form.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/usuario/user-form.jsp?msj=err");
        }
    }

    /**
     * Eliminar usuario por id
     * @param request
     * @param response
     * @throws IOException
     */
    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        UsuarioDTO logueado = (UsuarioDTO) request.getSession().getAttribute("usuarioLogueado");

        service.eliminar(id);

        if (logueado != null && logueado.getId() == id) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/usuario/login.jsp?msj=delete_ok");
        } else {
            response.sendRedirect(request.getContextPath() + "/usuarios?msj=delete_ok");
        }
    }

    /**
     * Listar usuarios
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("usuarios", service.listar());
        request.getRequestDispatcher("/usuario/user-list.jsp").forward(request, response);
    }

    /**
     * Crear nuevo en formulario
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("usuario", new UsuarioDTO());
        request.getRequestDispatcher("/usuario/user-form.jsp").forward(request, response);
    }

    /**
     * Editar en formulario de creacion usuario
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("usuario", service.buscarPorId(id));
        request.getRequestDispatcher("/usuario/user-form.jsp").forward(request, response);
    }

    /**
     * Logica de admin
     * @param request
     * @return
     */
    private boolean esAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            UsuarioDTO u = (UsuarioDTO) session.getAttribute("usuarioLogueado");
            return "admin".equalsIgnoreCase(u.getRol());
        }
        return false;
    }
}
