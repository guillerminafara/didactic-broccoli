import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class CrearUsuarioServlet extends HttpServlet {

    public void doPost(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
        try {
            //Obtain database Connection
            Connection miconexion = DBConnection.getConnection();
            //retrieve formparameters sent by user 
            String emailInput = peticion.getParameter("email");
            String passwordInput = peticion.getParameter("password");
            String checkPasswordInput = peticion.getParameter("password2");
            String nameInput = peticion.getParameter("nombre");
            String surnameInput = peticion.getParameter("apellido");

            if (passwordInput.equals(checkPasswordInput)) {
                String sql = "INSERT INTO usuario(nombre, apellido, email, contrasena) values (?,?,?,?)";
                PreparedStatement ps;
                // prepared statement and id_usuario key 
                ps = miconexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                //inser user data into prepared statement 
                ps.setString(1, nameInput);
                ps.setString(2, surnameInput);
                ps.setString(3, emailInput);
                ps.setString(4, passwordInput);

                int filas = ps.executeUpdate();
                if (filas > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        int id_usuario = rs.getInt(1);
                        //create cookies 
                        Cookie cookieUser = new Cookie("id_usuario", String.valueOf(id_usuario));
                        Cookie cookieUserName = new Cookie("nombre", String.valueOf(nameInput));

                        // se cookies expiration time 
                        cookieUser.setMaxAge(60 * 60 * 24 * 30);
                        cookieUser.setPath("/");
                        cookieUserName.setMaxAge(60 * 60 * 24 * 30);
                        cookieUserName.setPath("/");
                        //add cookies to response
                        respuesta.addCookie(cookieUser);
                        respuesta.addCookie(cookieUserName);
                        // Redirect user to main shop page 
                        respuesta.sendRedirect(peticion.getContextPath() + "/paginaPrincipal");
                    }
                    //close resources
                    ps.close();
                    miconexion.close();
                }
            }
        } catch (Exception e) {
            //print error stacktrace for debugging
            System.out.println(e);
            e.printStackTrace();

        }
    }
}
// tareas: corroborar si es correcto agregar cookieName asi
