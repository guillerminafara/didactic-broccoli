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
    // handles HTTP Post request
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
        // print the beggining of the HTML page an table header
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
            PreparedStatement ps;
            String sql = "INSERT INTO empleados (DNI, nombre, sueldo) values(?,?,?)";

            // Prepare the SQL statement
            ps = miconexion.prepareStatement(sql);
            // set the value for the first paramater(dni)
            ps.setInt(1, Integer.parseInt(dniParametro));
            ps.setString(2, nombre);
            ps.setFloat(3, Float.parseFloat(sueldo));

            // execute the prepared query
            int rowAffect = ps.executeUpdate();
            // Iterate through the result set and print each row
            if (rowAffect > 0) {
                String sqlSelect = "SELECT * FROM empleados where DNI= ?";
                ps.executeQuery(sqlSelect);
                PreparedStatement psSelect=miconexion.prepareStatement(sqlSelect);
                psSelect.setInt(1, Integer.parseInt(dniParametro));
                ResultSet misresultados = psSelect.executeQuery();

                while (misresultados.next()) {
                    salida.println("<TR><TD>" + misresultados.getInt("DNI") + "\n <TD>" +
                            misresultados.getString("nombre") + "\n<TD>" +
                            misresultados.getFloat("sueldo"));
                }
            }

            salida.println("</TABLE>\n</BODY></HTML>");
            
               salida.println("<br><hr><br>");
            salida.println("<h2>Insert New Employee</h2>");
            salida.println("<form action='servlet' method='POST'>");
            salida.println("Name: <input type='text' name='nombre'><br><br>");
            salida.println("Salary: <input type='text' name='sueldo'><br><br>");
            salida.println("DNI: <input type='text' name='dni'><br><br>");
            salida.println("<button type='submit'>Insert</button>");
            salida.println("</form>");
            
            // close the databse connection
            miconexion.close();
        } catch (Exception e) {
            salida.println(e);
            salida.println("</BODY></HTML>");
        }

    }
}
