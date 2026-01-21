import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class MiServlet extends HttpServlet {
    // handles HTTP POST request
    public void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        // set the response content type to HTML
        respuesta.setContentType("text/html");
        // Writer used to send HTML output to the client
        PrintWriter salida = respuesta.getWriter();
        // Page title
        String titulo = "Búsqueda de Empleado por DNI";
        // retrieve DNI parameter from the request
        String dniParametro = peticion.getParameter("dni");
        String sueldo = peticion.getParameter("sueldo");
        String nombre = peticion.getParameter("nombre");
 
 
        try {
            // JDBC connection URL and database credentials
            String SourceURL = "jdbc:mysql://localhost/bdprueba?allowPublicKeyRetrieval=true&useSSL=false";
            String user = "alumno";
            String password = "mipassword";
            Connection miconexion;
            // Establish the connecion to the MYSQL database
            miconexion = DriverManager.getConnection(SourceURL, user, password);
            PreparedStatement ps;
            
            String sql = "INSERT INTO empleados (DNI, nombre, sueldo) values(?,?,?)";

            // Prepare the SQL statement
            ps = miconexion.prepareStatement(sql);
            // set the values for the paramaters
            ps.setInt(1, Integer.parseInt(dniParametro));
            ps.setString(2, nombre);
            ps.setFloat(3, Float.parseFloat(sueldo));

            // execute the prepared query
            int rowAffect = ps.executeUpdate();
            // Iterate through the result set and print each row
            if (rowAffect > 0) {
               salida.println("<h3> Usuario agregado con éxito</h3>");
            }

         
            // close the databse connection
            miconexion.close();
        } catch (Exception e) {
            salida.println(e);
            salida.println("</BODY></HTML>");
        }

    }
}
