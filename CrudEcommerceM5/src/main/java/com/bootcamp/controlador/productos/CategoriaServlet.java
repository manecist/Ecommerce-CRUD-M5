package com.bootcamp.controlador.productos;

import com.bootcamp.dao.productos.CategoriaDAO;
import com.bootcamp.modelo.productos.CategoriaDTO;
import com.bootcamp.modelo.usuario.UsuarioDTO;
import com.bootcamp.service.productos.CategoriaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mi logica de categorias
 * con subida de imagen o procesar imagenes guardadas en carpeta
 * no funciona por siempre subir imagen se elimina al hacer un clean
 */
@WebServlet("/gestion-categorias")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 100
)
public class CategoriaServlet extends HttpServlet {

    private final CategoriaService service = new CategoriaService();

    /**
     * DoGet logica de mandos
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!esAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        cargarGaleria(request);
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new"    -> mostrarFormularioNuevo(request, response);
            case "edit"   -> mostrarFormularioEditar(request, response);
            case "delete" -> eliminar(request, response);
            default       -> listar(request, response);
        }
    }

    /**
     * Recepcion formularios
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
     * Crear nuevo en formulario categorias
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categoriaEnEdicion", new CategoriaDTO(0, "", "categorias/default-banner.jpg"));
        listar(request, response);
    }

    /**
     * Guardar / actualizar categorias
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void guardarOActualizar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        CategoriaDTO dto = null;
        try {
            String idStr = request.getParameter("idCategoria");
            int id = (idStr == null || idStr.isEmpty() || idStr.equals("0")) ? 0 : Integer.parseInt(idStr);
            String nombre = request.getParameter("nombreCategoria");

            String imagenExistente = request.getParameter("imagenExistente");

            String uploadPath = getServletContext().getRealPath("/ASSETS/IMG/categorias/");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            Part filePart = request.getPart("imagenBanner");
            String fileName = (filePart != null) ? filePart.getSubmittedFileName() : null;
            String imagenFinal;

            if (fileName != null && !fileName.isEmpty()) {
                //  Subir imagen nueva
                String extension = fileName.substring(fileName.lastIndexOf("."));
                String nombreUnico = "cat_" + System.currentTimeMillis() + extension;
                filePart.write(uploadPath + File.separator + nombreUnico);
                // Guardamos con la carpeta 'categorias/' para que el JSP sepa donde buscar
                imagenFinal = "categorias/" + nombreUnico;
            } else if (imagenExistente != null && !imagenExistente.isEmpty()) {
                //  Eligió una de la galería (ya trae la subcarpeta)
                imagenFinal = imagenExistente;
            } else {
                // Mantener actual
                String imagenActual = request.getParameter("imagenActual");
                imagenFinal = (imagenActual != null && !imagenActual.isEmpty()) ? imagenActual : "categorias/default-banner.jpg";
            }

            dto = new CategoriaDTO(id, nombre, imagenFinal);

            if (id == 0) {
                service.guardar(dto);
            } else {
                service.actualizar(dto);
            }

            response.sendRedirect(request.getContextPath() + "/gestion-categorias?msj=save_ok");

        } catch (IllegalArgumentException e) {
            cargarGaleria(request); // Recargamos galería en caso de error
            request.setAttribute("error", e.getMessage());
            request.setAttribute("categoriaEnEdicion", dto);
            listar(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/gestion-categorias?msj=error");
        }
    }

    /**
     * Métodos para buscar imágenes en tus carpetas reales
     * @param request
     */
    private void cargarGaleria(HttpServletRequest request) {
        String pathRaiz = getServletContext().getRealPath("/ASSETS/IMG/");
        File directorioRaiz = new File(pathRaiz);
        List<String> todasLasFotos = new ArrayList<>();
        buscarArchivos(directorioRaiz, "", todasLasFotos);
        request.setAttribute("galeriaImagenes", todasLasFotos);
    }

    /**
     * Buscar archivos en carpetas
     * @param directorio
     * @param rutaRelativa
     * @param lista
     */
    private void buscarArchivos(File directorio, String rutaRelativa, List<String> lista) {
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isDirectory()) {
                    buscarArchivos(archivo, rutaRelativa + archivo.getName() + "/", lista);
                } else {
                    String nombre = archivo.getName().toLowerCase();
                    if (nombre.endsWith(".jpg") || nombre.endsWith(".png") || nombre.endsWith(".jpeg") || nombre.endsWith(".webp")) {
                        lista.add(rutaRelativa + archivo.getName());
                    }
                }
            }
        }
    }

    /**
     * Listar categorias
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categorias", service.listarTodas());
        request.getRequestDispatcher("/productos/categorias-list.jsp").forward(request, response);
    }

    /**
     * Editar mediante formulario de creacion de categorias
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("idCategoria"));
            CategoriaDTO cat = service.buscarPorId(id);
            request.setAttribute("categoriaEnEdicion", cat);
            listar(request, response);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/gestion-categorias?msj=error");
        }
    }

    /**
     * Eliminar por id categoria
     * @param request
     * @param response
     * @throws IOException
     */
    private void eliminar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("idCategoria"));
            service.eliminar(id);
            response.sendRedirect(request.getContextPath() + "/gestion-categorias?msj=delete_ok");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/gestion-categorias?msj=error_ref");
        }
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