package com.bootcamp.controlador.usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/contacto")
public class ContactoServlet extends HttpServlet {

    /**
     * Metodo de captacion de Link
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/usuario/contacto.jsp").forward(request, response);
    }

    /**
     * Metodo de procesar formularios
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("txtNombre");
        String email = request.getParameter("txtEmail");
        String asunto = request.getParameter("txtAsunto");
        String mensaje = request.getParameter("txtMensaje");

        System.out.println("📩 Mensaje recibido de: " + nombre + " (" + email + ")");

        response.sendRedirect(request.getContextPath() + "/contacto?msj=contacto_ok");
    }
}
