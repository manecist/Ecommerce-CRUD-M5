package com.bootcamp.controlador;

import com.bootcamp.dao.productos.CategoriaDAO;
import com.bootcamp.modelo.productos.CategoriaDTO;
import com.bootcamp.modelo.productos.ProductoDTO;
import com.bootcamp.service.productos.CategoriaService;
import com.bootcamp.service.productos.ProductoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet(urlPatterns = {"/home", ""})
public class HomeServlet extends HttpServlet {

    private final CategoriaService catService = new CategoriaService();
    private final ProductoService prodService = new ProductoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Categorías para el menú y banners
            List<CategoriaDTO> listaCat = catService.listarTodas();

            // 2. Usamos tu método de "Cliente" (trae solo productos con stock)
            // Pasamos null a los parámetros de búsqueda para que no filtre por nombre o categoría
            List<ProductoDTO> listaProd = prodService.obtenerCatalogoCliente(null, null, null, null);

            // 3. Enviamos al REQUEST para que el index.jsp los use
            request.setAttribute("listaCategorias", listaCat);
            request.setAttribute("listaProductos", listaProd);

            request.getRequestDispatcher("index.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
