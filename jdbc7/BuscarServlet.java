import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class BuscarServlet extends HttpServlet {
    // handles HTTP GET request
    public void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
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
        salida.println("<TITLE>" + titulo + "</TITLE>" +
                "<BODY>\n" +
                "<H1 ALIGN=CENTER>" + titulo + "</H1>\n" +
                "<TABLE BORDER=1 ALIGN=CENTER>\n" +
                "<TR BGCOLOR=\"#FFAD00\">\n" +
                "<TH>DNI<TH>Nombre <TH>Sueldo");
        try {
            // JDBC connection URL and database credentials
            String SourceURL = "jdbc:mysql://localhost/bdprueba?allowPublicKeyRetrieval=true&useSSL=false";
            String user = "alumno";
            String password = "mipassword";
            Connection miconexion;
            // Establish the connecion to the MYSQL database
            miconexion = DriverManager.getConnection(SourceURL, user, password);

            // Prepare the SQL statement
            String sqlSelect = "SELECT * FROM empleados where DNI=?";
            PreparedStatement psSelect = miconexion.prepareStatement(sqlSelect);
            // set the value for the first paramater(dni)
            psSelect.setInt(1, Integer.parseInt(dniParametro));

            ResultSet misresultados = psSelect.executeQuery();
            // print all the employees
            while (misresultados.next()) {
                salida.println(
                        "<TR><TD>" + misresultados.getInt("DNI") + "\n <TD>" +
                                misresultados.getString("nombre") + "\n<TD>" +
                                misresultados.getFloat("sueldo"));
            }

            // close the databse connection
            miconexion.close();
        } catch (Exception e) {
            salida.println(e);
            salida.println("</BODY></HTML>");
        }

    }
}
