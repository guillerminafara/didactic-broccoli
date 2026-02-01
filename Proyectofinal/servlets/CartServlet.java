import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import util.DBConnection;

public class CartServlet extends HttpServlet {

    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)throws ServletException, IOException {

        respuesta.setContentType("text/html;charset=UTF-8");
        PrintWriter out = respuesta.getWriter();

        HttpSession session = peticion.getSession();
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        double subtotal = 0;
        int totalArticulos = 0;

        out.println("<!DOCTYPE html>");
        out.println("<html lang='es'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Shopping Cart - AmazingStore</title>");
        out.println("<link rel='stylesheet' href='styleCarrito.css'>");
        out.println("</head>");

        out.println("<body>");

        // HEADER
        out.println("<header class='header'>");
        out.println("<div class='logo'>AmazingStore</div>");
        out.println("<div class='user-icon'>👤</div>");
        out.println("</header>");

        out.println("<main class='cart-container'>");
        out.println("<h2>Shopping Cart</h2>");

        // MOSTRAR ITEMS
        for (ItemCarrito item : carrito) {

            double itemSubtotal = item.getPrecio() * item.getCantidad();
            subtotal += itemSubtotal;
            totalArticulos += item.getCantidad();

            out.println("<div class='cart-item'>");

            out.println("<img src='"+ item.getImagen()+"' alt='Product'>");

            out.println("<div class='item-details'>");
            out.println("<h3>" + item.getNombre() + "</h3>");
            out.println("<p>Precio unitario: €" + item.getPrecio() + "</p>");
            out.println("<p>Cantidad: " + item.getCantidad() + "</p>");
            out.println("</div>");

            out.println("<div class='item-price'>");
            out.println("€" + itemSubtotal);
            out.println("</div>");

            out.println("</div>");
        }

        // calculate coat
        double envio = 2 + (totalArticulos * 1);
        double total = subtotal + envio;

        // RESUME
        out.println("<div class='cart-summary'>");
        out.println("<p><strong>Subtotal:</strong> €" + subtotal + "</p>");
        out.println("<p><strong>Shipping:</strong> €" + envio + "</p>");
        out.println("<p class='total'><strong>Total:</strong> €" + total + "</p>");

        out.println("<form action='ConfirmarCompraServlet' method='post'>");
        out.println("<button class='checkout-btn'>Proceed to checkout</button>");
        out.println("</form>");

        out.println("</div>");

        out.println("</main>");
        out.println("</body>");
        out.println("</html>");
    }
}
