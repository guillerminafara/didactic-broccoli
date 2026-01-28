
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;

public class MiServlet extends HttpServlet {

    // handles HTTP Post request
    public void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {

        respuesta.setContentType("text/html");
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
            PreparedStatement ps;
            //sql query to validate email and pass
            String sql = "SELECT * FROM usuarios WHERE email=? AND password=?"; 
            ps = miconexion.prepareStatement(sql);
            //set the values for the prepared statement
            ps.setString(1, emailInput);
            ps.setString(2, passwordInput);
            ResultSet misresultados = ps.executeQuery();
	// if a record is found, credentials are correct
            if (misresultados.next()) {
            //retrieve values from the database
            String email= misresultados.getString("email");
            String passw= misresultados.getString("password");
            //unnecesary comparatioon :l
            if(email.equals(emailInput) && passw.equals(passwordInput)){
            
                salida.println("<h2>Welcome! to amazing store"+email+"</h2>");
                salida.println("<a href='parametros.html'>Volver atrás</a>");
            } }else {
                salida.println("<h2>Login failed: invalid email or password.</h2>");
                salida.println("<a href='parametros.html'>Try again</a>");
            }
        } catch (Exception e) {
            salida.println(e);
            salida.println("</BODY></HTML>");
        }

    }
}
