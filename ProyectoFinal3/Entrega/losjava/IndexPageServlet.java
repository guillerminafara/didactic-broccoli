import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class IndexPageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {

        respuesta.setContentType("text/html;charset=UTF-8");

        // wwriter to send response to cliente
        PrintWriter salida = respuesta.getWriter();
        // print page header and css style
        salida.println("<html>");
        salida.println("<head>");
        salida.println("<title>AmazingStore</title>");
        salida.println("<link rel='stylesheet' href='styles.css'>");
        salida.println("</head>");
        salida.println("<body>");
        // page header section
        salida.println("<header class='header'>");
        salida.println("<div class='logo'>AmazingStore</div>");
        salida.println("<div class='user-icon'>👤</div>");
        salida.println("</header>");
        // main where products will be displayed
        salida.println("<main class='container'>");

        try (Connection con = DBConnection.getConnection()) {
            //SQL query to retrieve all prodcuts 
            PreparedStatement ps = con.prepareStatement("SELECT * FROM producto");
            ResultSet rs = ps.executeQuery();
            // loop throught database results and print each product
            while (rs.next()) {
                salida.println("<div class='card'>");
                //image
                salida.println("<img src='" + rs.getString("imagen") + "' width='200'>");
                salida.println("<h3>" + rs.getString("nombre") + "</h3>");
                salida.println("<p>" + rs.getString("descripcion") + "</p>");
                salida.println("<p class='price'>€" + rs.getDouble("precio") + "</p>");//price
                
                salida.println("<form action='addToCart' method='post'>");
                //Combobox selector to choose quantity 
                salida.println("<select name='cantidad'>");
                for (int i = 1; i <= 5; i++) {
                    salida.println("<option value='" + i + "'>" + i + "</option>");
                }
                salida.println("</select>");
                //hidden field storing product ID
                salida.println("<input type='hidden' name='idProducto' value='" +
                        rs.getInt("id_producto") + "'>");
                //submit button 
                salida.println("<input type='submit' value='Add to cart'>");
                salida.println("</form>");

                salida.println("</div>");
            }

        } catch (Exception e) {
            salida.println("<p>Error: " + e.getMessage() + "</p>");
        }
        // button to go to cart page 
        salida.println("<form action='cart' method='get'>");
        salida.println("<input type='submit' value='Finalizar compra'>");
        salida.println("</form>");

        salida.println("</main>");
        salida.println("</body>");
        salida.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir POST al GET para evitar duplicar código
        doGet(request, response);
    }
}
