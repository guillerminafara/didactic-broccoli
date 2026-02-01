
import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class CrearUsuarioServlet extends HttpServlet {

    public void doPost(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {

        PrintWriter salida = respuesta.getWriter();
        Connection miconexion = DBConnection.getConnection();

        String emailInput = peticion.getParameter("email");
        String passwordInput = peticion.getParameter("password");
        String checkPasswordInput = peticion.getParameter("password2");
        String nameInput = peticion.getParameter("name");
        String surnameInput = peticion.getParameter("surname");

        if (passwordInput.equals(checkPasswordInput)) {
            String sql = "INSERT INTO usuario(nombre, apellido, email, contrasena) values (?,?,?,?)";
            PreparedStatement ps;
            ps = miconexion.prepareStatement(sql);
            ps.setString(1, nameInput);
            ps.setString(2, surnameInput);
            ps.setString(3, emailInput);
            ps.setString(4, passwordInput);

            int rowAffect = ps.executeUpdate();

            if (rowAffect > 0) {
                respuesta.sendRedirect(peticion.getContextPath() + "/paginaPrincipal.html");
            }
        } else {
            salida.println("<p>Las contraseñas deben coincidir</p>");
        }

    }
}
