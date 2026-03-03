package com.bootcamp.controlador.productos;
import com.bootcamp.modelo.productos.SubcategoriaDTO;
import com.bootcamp.modelo.usuario.UsuarioDTO;
import com.bootcamp.service.productos.CategoriaService;
import com.bootcamp.service.productos.SubcategoriaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Logica de subcategorias
 */
@WebServlet("/gestion-subcategorias")
public class SubcategoriaServlet extends HttpServlet {

    private final SubcategoriaService subService = new SubcategoriaService();
    private final CategoriaService catService = new CategoriaService();

    /**
     * doGet para mi pagina
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!esAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado.");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new" -> mostrarFormularioNuevo(request, response);
            case "edit" -> mostrarFormularioEditar(request, response);
            case "delete" -> eliminar(request, response);
            default -> listar(request, response);
        }
    }

    /**
     * doPost Recepcion formularios
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!esAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        guardarOActualizar(request, response);
    }

    /**
     * Listar sub
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("subcategorias", subService.listarTodas());
        request.setAttribute("categorias", catService.listarTodas());
        request.getRequestDispatcher("/productos/subcategorias-list.jsp").forward(request, response);
    }

    /**
     * guardar / actualizar sub
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void guardarOActualizar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        SubcategoriaDTO dto = null;
        try {
            String idStr = request.getParameter("idSubcategoria");
            String idCatStr = request.getParameter("idCategoriaAsociada");
            String nombre = request.getParameter("nombreSubcategoria");

            int idSub = (idStr == null || idStr.isEmpty()) ? 0 : Integer.parseInt(idStr);
            int idCat = (idCatStr == null || idCatStr.isEmpty()) ? 0 : Integer.parseInt(idCatStr);

            dto = new SubcategoriaDTO(idSub, idCat, nombre != null ? nombre.trim() : "");

            if (idSub == 0) {
                subService.guardar(dto);
            } else {
                subService.actualizar(dto);
            }
            response.sendRedirect(request.getContextPath() + "/gestion-subcategorias?msj=save_ok");

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("subcategoriaEnEdicion", dto);
            listar(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/gestion-subcategorias?msj=error");
        }
    }

    /**
     * Editar desde fomrulario de creacion los sub
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("idSubcategoria"));
            SubcategoriaDTO sub = subService.buscarPorId(id);
            request.setAttribute("subcategoriaEnEdicion", sub);
            listar(request, response);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/gestion-subcategorias?msj=error");
        }
    }

    /**
     * Creacion nuevo desde formulario de los sub
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("subcategoriaEnEdicion", new SubcategoriaDTO(0, 0, ""));
        listar(request, response);
    }

    /**
     * Eliminar sub
     * @param request
     * @param response
     * @throws IOException
     */
    private void eliminar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("idSubcategoria"));
            subService.eliminar(id);
            response.sendRedirect(request.getContextPath() + "/gestion-subcategorias?msj=delete_ok");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/gestion-subcategorias?msj=error_ref");
        }
    }

    /**
     * logica es admin
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