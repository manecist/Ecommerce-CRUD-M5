package com.bootcamp.controlador.productos;
import com.bootcamp.modelo.productos.ProductoDTO;
import com.bootcamp.modelo.productos.SubcategoriaDTO;
import com.bootcamp.modelo.usuario.UsuarioDTO;
import com.bootcamp.service.productos.CategoriaService;
import com.bootcamp.service.productos.ProductoService;
import com.bootcamp.service.productos.SubcategoriaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Logica de perfil de productos
 * con carga y subida de imagenes
 */
@WebServlet("/productos")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 100    // 100 MB
)
public class ProductoServlet extends HttpServlet {

    private final ProductoService service = new ProductoService();
    private final SubcategoriaService subService = new SubcategoriaService();
    private CategoriaService catService = new CategoriaService();

    /**
     * doGet logica de edicion de productos
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

        // Si es lista o filtro, no necesita ser admin
        if (action.equals("list") || action.equals("filter")) {
            listarYFiltrar(request, response);
            return;
        }

        // Para el resto de acciones (new, edit, delete), validamos admin
        if (!esAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso restringido.");
            return;
        }

        switch (action) {
            case "new"    -> mostrarFormularioNuevo(request, response);
            case "edit"   -> mostrarFormularioEditar(request, response);
            case "delete" -> eliminar(request, response);
            default       -> listarYFiltrar(request, response);
        }
    }

    /**
     * doPost recepcion de formulario
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
     * Listado y filtro de productos
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void listarYFiltrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String busqueda = request.getParameter("txtBuscar");
        String orden = request.getParameter("orden");
        String idSubStr = request.getParameter("idSubcategoria");
        String idCatStr = request.getParameter("idCategoria");

        Integer subId = (idSubStr != null && !idSubStr.isEmpty()) ? Integer.parseInt(idSubStr) : 0;
        Integer catId = (idCatStr != null && !idCatStr.isEmpty()) ? Integer.parseInt(idCatStr) : 0;

        List<ProductoDTO> lista = esAdmin(request)
                ? service.obtenerCatalogoAdmin(busqueda, catId, subId, orden)
                : service.obtenerCatalogoCliente(busqueda, catId, subId, orden);

        request.setAttribute("productos", lista);
        request.setAttribute("listaCategorias", catService.listarTodas());

        if (catId > 0) {
            // Si hay categoría, cargamos sus subcategorías para los botones de filtro
            request.setAttribute("listaSubcategorias", subService.listarPorCategoria(catId));
        } else {
            request.setAttribute("listaSubcategorias", new ArrayList<SubcategoriaDTO>());
        }

        request.setAttribute("idCatActual", catId);
        request.setAttribute("idSubCatActual", subId);

        request.getRequestDispatcher("productos/productos.jsp").forward(request, response);
    }

    /**
     * Guardar actualizar productos
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void guardarOActualizar(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            // LEER LOS DATOS
            String idStr = request.getParameter("idProducto");
            String nombre = request.getParameter("nombreProducto");
            String descripcion = request.getParameter("descripcionProducto");
            String precioStr = request.getParameter("precioProducto");
            String stockStr = request.getParameter("stockProducto");
            String idSubStr = request.getParameter("idSubcategoriaAsociada");
            String imagenExistente = request.getParameter("imagenExistente");
            String imagenActual = request.getParameter("imagenActual");

            // PARSEAR DATOS
            int id = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : 0;
            double precio = (precioStr != null && !precioStr.isEmpty()) ? Double.parseDouble(precioStr) : 0;
            int stock = (stockStr != null && !stockStr.isEmpty()) ? Integer.parseInt(stockStr) : 0;
            int idSub = (idSubStr != null && !idSubStr.isEmpty()) ? Integer.parseInt(idSubStr) : 0;

            // --- BLOQUE DE VALIDACIÓN ÚNICO ---
            String errorValidacion = null;

            if (nombre == null || nombre.trim().isEmpty()) {
                errorValidacion = "✨ El nombre del producto es obligatorio.";
            } else if (precio < 0) {
                errorValidacion = "✨ ¡Error de alquimia! El precio no puede ser menor a 0 CLP.";
            } else if (stock < 0) {
                errorValidacion = "✨ La cantidad en stock no puede ser negativa.";
            }

            if (errorValidacion != null) {
                // Creamos el objeto con lo que el usuario escribió para no perder los datos
                // Usamos la imagen actual para que no se borre la vista previa
                ProductoDTO pErroneo = new ProductoDTO(id, idSub, nombre, descripcion, precio, stock, imagenActual, "", "");

                request.setAttribute("producto", pErroneo);
                request.setAttribute("error", errorValidacion);

                // Volvemos al formulario usando forward
                request.getRequestDispatcher("/productos?action=edit&id=" + id).forward(request, response);
                return; // Detenemos el proceso
            }

            // LÓGICA DE IMAGEN (Si pasó la validación)
            Part filePart = request.getPart("imagenProducto");
            String fileName = (filePart != null) ? filePart.getSubmittedFileName() : "";
            String imagenFinal;

            if (fileName != null && !fileName.isEmpty()) {
                String path = getServletContext().getRealPath("/ASSETS/IMG/productos/");
                File carpeta = new File(path);
                if (!carpeta.exists()) { carpeta.mkdirs(); }
                filePart.write(path + File.separator + fileName);
                imagenFinal = "productos/" + fileName;
            } else if (imagenExistente != null && !imagenExistente.isEmpty()) {
                imagenFinal = imagenExistente;
            } else {
                imagenFinal = (imagenActual != null && !imagenActual.isEmpty()) ? imagenActual : "default.jpg";
            }

            //  GUARDAR Y REDIRIGIR
            ProductoDTO p = new ProductoDTO(id, idSub, nombre, descripcion, precio, stock, imagenFinal, "", "");
            service.guardarOActualizar(p);
            response.sendRedirect(request.getContextPath() + "/productos?action=list&msj=save_ok");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/productos?action=list&msj=error_fatal");
        }
    }
    /**
     * Cargar galeria de imagenes
     * @param request
     */
    private void cargarGaleria(HttpServletRequest request) {
        // Buscamos la carpeta principal de imágenes
        String pathRaiz = getServletContext().getRealPath("/ASSETS/IMG/");
        File directorioRaiz = new File(pathRaiz);
        List<String> todasLasFotos = new ArrayList<>();

        // Llamamos a una función que busca en todas las subcarpetas
        buscarArchivos(directorioRaiz, "", todasLasFotos);

        // Pasamos la lista al JSP
        request.setAttribute("galeriaImagenes", todasLasFotos);
    }

    /**
     * Revisa por todas las carpetas internas los archivos
     * @param directorio
     * @param rutaRelativa
     * @param lista
     */
    private void buscarArchivos(File directorio, String rutaRelativa, List<String> lista) {
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isDirectory()) {
                    // Si es carpeta, entramos en ella (recursividad)
                    buscarArchivos(archivo, rutaRelativa + archivo.getName() + "/", lista);
                } else {
                    // Si es imagen, la guardamos
                    String nombre = archivo.getName().toLowerCase();
                    if (nombre.endsWith(".jpg") || nombre.endsWith(".png") || nombre.endsWith(".jpeg") || nombre.endsWith(".webp")) {
                        lista.add(rutaRelativa + archivo.getName());
                    }
                }
            }
        }
    }

    /**
     * Crear nuevo producto en formulario
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cargarGaleria(request);
        request.setAttribute("producto", new ProductoDTO(0, 0, "", "", 0.0, 0, "default-prod.jpg", "", ""));
        request.setAttribute("subcategorias", subService.listarTodas());
        request.getRequestDispatcher("productos/producto-form.jsp").forward(request, response);
    }

    /**
     * Editar productos desde formulario de creacion
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            cargarGaleria(request);

            String idStr = request.getParameter("idProducto");
            int id = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : 0;

            ProductoDTO p = service.obtenerPorId(id);

            if (p == null) {
                response.sendRedirect(request.getContextPath() + "/productos?msj=not_found");
                return;
            }

            request.setAttribute("producto", p);
            request.setAttribute("subcategorias", subService.listarTodas());
            request.getRequestDispatcher("/productos/producto-form.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/productos?msj=error");
        }
    }

    /**
     * Eliminar por id productos
     * @param request
     * @param response
     * @throws IOException
     */
    private void eliminar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("idProducto"));
            service.eliminarProducto(id);
            response.sendRedirect(request.getContextPath() + "/productos?action=list&msj=delete_ok");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/productos?action=list&msj=error");
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