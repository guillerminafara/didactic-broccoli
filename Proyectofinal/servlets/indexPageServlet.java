import java.io.*;
import java.sql.*;
import util.DBConnection;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class IndexPageServlet extends HttpServlet {
    public void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        // set the response content type to HTML
        respuesta.setContentType("text/html");
        PrintWriter salida = respuesta.getWriter(); // Writer used to send HTML output to the client
        peticion.getDouble();
        try {
            Connection miconexion = DBConnection.getConnection();
            String sqlSelect = "SELECT * FROM productos";// Prepare the SQL statement
            PreparedStatement psSelect = miconexion.prepareStatement(sqlSelect);
            ResultSet misresultados = psSelect.executeQuery();
            while (misresultados.next()) {// print all the cards product
                salida.println(
                        "<div class='card'>"
                                + "<img src=" + misresultados.getString("imagen") + "alt='Imagen Producto' width='200'>"
                                + "<h3>" + misresultados.getString("nombre") + "</h3>"
                                + "<p>Descripcion: " + misresultados.getString("descripcion") + "</p>"
                                + "<p class='price'>Precio: €" + misresultados.getDouble("precio") + "</p>"
                                // + "<form action='addToCart' method='post'>"
                                + "<select name='quantity'>"
                                + "<option value='1'>1</option>"
                                + "<option value='2'>2</option>"
                                + "<option value='3'>3</option>"
                                + "<option value='4'>4</option>"
                                + "</select>"
                                + "<input type='hidden' name='productId' value='" + misresultados.getInt("id_producto")
                                + "'>"
                                + "<input type='submit' value='Add to cart'>"
                                + "</form>"
                                + "</div>");
            }

            // close the database connection
            miconexion.close();
            salida.println("</BODY></HTML>");
        } catch (Exception e) {
            salida.println(e);

        }
    }
}
