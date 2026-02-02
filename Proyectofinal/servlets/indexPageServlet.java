import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import util.DBConnection;

public class IndexPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>AmazingStore</title>");
        out.println("<link rel='stylesheet' href='styles.css'>"); // si tienes CSS
        out.println("</head>");
        out.println("<body>");

        out.println("<header class='header'>");
        out.println("    <div class='logo'>AmazingStore</div>");
        out.println("    <div class='user-icon'>👤</div>");
        out.println("</header>");

        out.println("<main class='container'>");

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM producto");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                out.println("<div class='card'>");
                out.println("    <img src='" + rs.getString("imagen") + "' width='200' alt='Imagen Producto'>");
                out.println("    <h3>" + rs.getString("nombre") + "</h3>");
                out.println("    <p>Descripcion: " + rs.getString("descripcion") + "</p>");
                out.println("    <p class='price'>€" + rs.getDouble("precio") + "</p>");

                // Formulario para añadir al carrito
                out.println("    <form action='AddCartServlet' method='post'>");
                out.println("        <select name='quantity'>");
                out.println("            <option>1</option>");
                out.println("            <option>2</option>");
                out.println("            <option>3</option>");
                out.println("            <option>4</option>");
                out.println("        </select>");
                out.println("        <input type='hidden' name='productId' value='" + rs.getInt("id_producto") + "'>");
                out.println("        <input type='submit' value='Add to cart'>");
                out.println("    </form>");

                out.println("</div>"); // end card
            }

            con.close();

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }

        // Botón para ir al carrito (finalizar compra)
        out.println("<form action='CartServlet' method='get'>");
        out.println("    <input type='submit' value='Finalizar compra'>");
        out.println("</form>");

        out.println("</main>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir POST al GET para evitar duplicar código
        doGet(request, response);
    }
}
