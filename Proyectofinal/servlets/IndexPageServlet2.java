import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class IndexPageServlet2 extends HttpServlet {

    public void doPost(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
        // set the response content type to HTML
        respuesta.setContentType("text/html");
        respuesta.setContentType("<header class='header'>\r\n" + //
                        "        <div class='logo'>AmazingStore</div>\r\n" + //
                        "        <div class='user-icon'>\r\n" + //
                        "            👤\r\n" + //
                        "        </div>\r\n" + //
                        "    </header><body>"+
                        "<form action='' method='post'>");
        PrintWriter salida = respuesta.getWriter(); // Writer used to send HTML output to the client
        try {
            Connection miconexion = DBConnection.getConnection();
            String sqlSelect = "SELECT * FROM producto";// Prepare the SQL statement
            PreparedStatement psSelect = miconexion.prepareStatement(sqlSelect);
            ResultSet misresultados = psSelect.executeQuery();
            while (misresultados.next()) {// print all the cards product
                salida.println(
                        "<div class='card'>"
                        
                                + "<img src=" + misresultados.getString("imagen") + "alt='Imagen Producto' width='200'>"
                                + "<h3>" + misresultados.getString("nombre") + "</h3>"
                                + "<p>Descripcion: " + misresultados.getString("descripcion") + "</p>"
                                + "<p class='price'>Precio: €" + misresultados.getDouble("precio") + "</p>"
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
            salida.println("<main class='container'>\r\n" + //
                                "        <input type='submit' value='Finaliza compra' formaction='addToCart'>\r\n" + //
                                "    </main>");
            salida.println("</body></HTML>");
        } catch (Exception e) {
            salida.println(e);
        }
    }
}