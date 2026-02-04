import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
public class CartServlet extends HttpServlet {

    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)throws ServletException, IOException {

        respuesta.setContentType("text/html;charset=UTF-8");
        PrintWriter salida = respuesta.getWriter();

        HttpSession session = peticion.getSession();
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        double subtotal = 0;
        int totalArticulos = 0;

        salida.println("<!DOCTYPE html>");
        salida.println("<html lang='es'>");
        salida.println("<head>");
        salida.println("<meta charset='UTF-8'>");
        salida.println("<title>Shopping Cart - AmazingStore</title>");
        salida.println("<link rel='stylesheet' href='styleCarrito.css'>");
        salida.println("</head>");

        salida.println("<body>");

        // HEADER section
        salida.println("<header class='header'>");
        salida.println("<div class='logo'>AmazingStore</div>");
        salida.println("<div class='user-icon'>👤</div>");
        salida.println("</header>");
        //shopping cart container
        salida.println("<main class='cart-container'>");
        salida.println("<h2>Shopping Cart</h2>");

        //display each product stored in cart 
        for (ItemCarrito item : carrito) {

            double itemSubtotal = item.getPrecio() * item.getCantidad();
            subtotal += itemSubtotal;
            totalArticulos += item.getCantidad();

            salida.println("<div class='cart-item'>");

            salida.println("<img src='"+ item.getImagen()+"'width=100px alt='Product'>");
            //prodcut detail
            salida.println("<div class='item-details'>");
            salida.println("<h3>" + item.getNombre() + "</h3>");
            salida.println("<p>Precio unitario: €" + item.getPrecio() + "</p>");
            salida.println("<p>Cantidad: " + item.getCantidad() + "</p>");
            salida.println("</div>");
            //item subtotal price
            salida.println("<div class='item-price'>");
            salida.println("€" + itemSubtotal);
            salida.println("</div>");

            salida.println("</div>");
        }

        // calculate coat
        double envio = 2 + (totalArticulos * 1);
        double total = subtotal + envio;

        // RESUME
        salida.println("<div class='cart-summary'>");
        salida.println("<p><strong>Subtotal:</strong> €" + subtotal + "</p>");
        salida.println("<p><strong>Shipping:</strong> €" + envio + "</p>");
        salida.println("<p class='total'><strong>Total:</strong> €" + total + "</p>");

        salida.println("<form action='confirmarCompraServlet' method='post'>");
        salida.println("<button class='checkout-btn'>Proceed to checkout</button>");
        salida.println("</form>");

        salida.println("</div>");

        salida.println("</main>");
        salida.println("</body>");
        salida.println("</html>");
    }
}
