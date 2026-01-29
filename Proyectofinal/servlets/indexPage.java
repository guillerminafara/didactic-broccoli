import java.io.*;
import java.sql.*;
import util.DBConnection;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class indexPage extends HttpServlet {

    public void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
        // set the response content type to HTML
        respuesta.setContentType("text/html");
        // Writer used to send HTML output to the client
        PrintWriter salida = respuesta.getWriter();
        try {
            Connection miconexion = DBConnection.getConnection();

            // Prepare the SQL statement
            String sqlSelect = "SELECT * FROM productos";
            PreparedStatement psSelect = miconexion.prepareStatement(sqlSelect);
            ResultSet misresultados = psSelect.executeQuery();
            // print all the employees
            while (misresultados.next()) {
                salida.println(
                        "<div class='card'>"
                        + "<img src="+misresultados.getString("imagen") +"alt='Imagen Producto' width='200'>"
                        + "<h3>" + misresultados.getString("nombre") + "</h3>"
                        + "<p>Descripcion: " + misresultados.getString("descripcion") + "</p>"
                        + "<p class='price'>Precio: €" + misresultados.getDouble("precio") + "</p>"
                        + "<form action='addToCart' method='post'>"
                        + "<select name='quantity'>"
                        + "<option value='1'>1</option>"
                        + "<option value='2'>2</option>"
                        + "<option value='3'>3</option>"
                        + "<option value='4'>4</option>"
                        + "</select>"
                        + "<input type='hidden' name='productId' value='" + misresultados.getInt("id_producto") + "'>"
                        + "<input type='submit' value='Add to cart'>" +
                        "</form>" +
                    "</div>"
                
                    );
            }

            // close the databse connection
            miconexion.close();
        } catch (Exception e) {
            salida.println(e);
            salida.println("</BODY></HTML>");
        }
    }
}
