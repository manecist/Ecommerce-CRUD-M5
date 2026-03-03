package com.bootcamp.controlador.usuario;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // El false evita que se cree una sesión nueva si no existía
        HttpSession session = request.getSession(false);

        if (session != null) {
            // borramos todos los datos de la sesión
            session.invalidate();
        }

        // Redirigimos al index con el parámetro de logout.
        // Usamos request.getContextPath() para asegurar que la ruta sea siempre correcta
        response.sendRedirect(request.getContextPath() + "/usuario/login.jsp?logout=true");
    }
}
