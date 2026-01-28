import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class InsertSErvlet extends HttpServlet {
    // handles HTTP Post request
    public void doPost(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
        // set the response content type to HTML
        respuesta.setContentType("text/html");

        // Writer used to send HTML output to the client
        PrintWriter salida = respuesta.getWriter();
        // Page title
        String titulo = "Agregar Empleado";
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
            PreparedStatement ps;
            String sql = "INSERT INTO empleados (DNI, nombre, sueldo) values(?,?,?)";
            // Prepare the SQL statement
            ps = miconexion.prepareStatement(sql);
            // set the value for the first paramater(dni)
            ps.setInt(1, Integer.parseInt(dniParametro));
            ps.setString(2, nombre);
            ps.setFloat(3, Float.parseFloat(sueldo));
            // execute the prepared queryc
            int rowAffect = ps.executeUpdate();

            if (rowAffect > 0) {
                salida.println("<p>Usuario agreagado con éxito</p>");
                String sqlSelect = "SELECT * FROM empleados";
                ps.executeQuery(sqlSelect);
                PreparedStatement psSelect = miconexion.prepareStatement(sqlSelect);
                // psSelect.setInt(1, Integer.parseInt(dniParametro));
                ResultSet misresultados = psSelect.executeQuery();
                // print all the employees
                while (misresultados.next()) {
                    salida.println(
                            "<TR><TD>" + misresultados.getInt("DNI") + "\n <TD>" +
                                    misresultados.getString("nombre") + "\n<TD>" +
                                    misresultados.getFloat("sueldo"));
                }
            }
        } catch (Exception e) {
            salida.println(e);
            salida.println("</BODY></HTML>");
        }
    }
}