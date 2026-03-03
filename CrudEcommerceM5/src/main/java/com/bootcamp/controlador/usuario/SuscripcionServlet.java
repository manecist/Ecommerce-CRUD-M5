package com.bootcamp.controlador.usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/subscribir")
public class SuscripcionServlet extends HttpServlet {

    /**
     * Solo metodo de captacion
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email != null && !email.trim().isEmpty()) {
            System.out.println("🧙‍♂️ ¡Nueva Suscripción Mágica!: " + email);

            String paginaAnterior = request.getHeader("referer");

            // Redirigir con parámetro de éxito
            if (paginaAnterior != null && !paginaAnterior.isEmpty()) {
                String urlLimpia = paginaAnterior.split("\\?")[0];
                response.sendRedirect(urlLimpia + "?suscripcion=ok");
            } else {
                response.sendRedirect(request.getContextPath() + "/home?suscripcion=ok");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}