import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    // handles HTTP Post request
    public void doPost(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {

        // respuesta.setContentType("text/html");
        PrintWriter salida = respuesta.getWriter();
        // get form parameters sent from HTML login
        String emailInput = peticion.getParameter("email");
        String passwordInput = peticion.getParameter("password");
        try {
            // JDBC connection URL and database credentials
            String SourceURL = "jdbc:mysql://localhost/usuario?allowPublicKeyRetrieval=true&useSSL=false";
            String user = "alumno";
            String password = "mipassword";
            Connection miconexion;

            // Establish the connecion to the MYSQL database
            miconexion = DriverManager.getConnection(SourceURL, user, password);
            // sql query to validate email and pass
            String sql = "SELECT * FROM usuarios WHERE email=? AND password=?";
            PreparedStatement ps;
            ps = miconexion.prepareStatement(sql);
            // set the values for the prepared statement
            ps.setString(1, emailInput);
            ps.setString(2, passwordInput);
            ResultSet misresultados = ps.executeQuery();
            // if a record is found, credentials are correct
            if (misresultados.next()) {
                // retrieve values from the database
                String email = misresultados.getString("email");
                int id_usuario = misresultados.getInt("id_usuario");
                String nombre = misresultados.getString("nombre");
                Cookie cookieUser = new Cookie("id_usuario", String.valueOf(id_usuario));
                Cookie cookieUserName = new Cookie("nombre", String.valueOf(nombre));

                cookieUser.setMaxAge(60 * 60 * 24 * 30);
                cookieUser.setPath("/");
                respuesta.addCookie(cookieUser);

                cookieUserName.setPath("/");
                cookieUserName.setMaxAge(60 * 60 * 24 * 30);
                
                respuesta.addCookie(cookieUser);
                respuesta.addCookie(cookieUserName);
                respuesta.sendRedirect(peticion.getContextPath() + "/paginaPrincipal.html");

            } else {
                salida.println("<h2>Login failed: invalid email or password.</h2>");//ver otra forma 

            }
        } catch (Exception e) {
            salida.println(e);
            salida.println("</BODY></HTML>");
        }

    }
}
