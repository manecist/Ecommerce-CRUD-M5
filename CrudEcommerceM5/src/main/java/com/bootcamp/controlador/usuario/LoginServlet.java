package com.bootcamp.controlador.usuario;

import com.bootcamp.modelo.usuario.UsuarioDTO;
import com.bootcamp.service.productos.CategoriaService;
import com.bootcamp.service.usuario.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UsuarioService service = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("usuario/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        if (user == null || pass == null || user.isBlank() || pass.isBlank()) {
            request.setAttribute("mensajeError", "Por favor, completa todos los campos.");
            request.getRequestDispatcher("usuario/login.jsp").forward(request, response);
            return;
        }

        UsuarioDTO usuario = service.login(user.trim(), pass);

        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(15 * 60); // 15 minutos
            session.setAttribute("usuarioLogueado", usuario);

            CategoriaService catService = new CategoriaService();
            session.setAttribute("listaCategorias", catService.listarTodas());

            if ("admin".equalsIgnoreCase(usuario.getRol())) {
                response.sendRedirect(request.getContextPath() + "/usuarios?action=list");
            } else {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        } else {
            request.setAttribute("mensajeError", "Usuario o contraseña incorrectos.");
            request.getRequestDispatcher("usuario/login.jsp").forward(request, response);
        }
    }
}