import model.Cart;
import model.CartItem;
import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddToCartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {

        String productIdStr = peticion.getParameter("productId");
        String quantityStr = peticion.getParameter("quantity");

        if (productIdStr == null || quantityStr == null) {
            respuesta.sendRedirect(peticion.getContextPath() + "/index.jsp");
            return;
        }

        int productId;
        int quantity;
        try {
            productId = Integer.parseInt(productIdStr);
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) quantity = 1;
        } catch (NumberFormatException e) {
            respuesta.sendRedirect(peticion.getContextPath() + "/index.jsp");
            return;
        }

        // Obtener datos del producto de la BD (especialmente precio actual y nombre)
        String sql = "SELECT nombre, precio FROM producto WHERE id_producto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    // producto no existe
                    respuesta.sendRedirect(peticion.getContextPath() + "/index.jsp?error=notfound");
                    return;
                }
                String name = rs.getString("nombre");
                BigDecimal price = rs.getBigDecimal("precio");

                // Crear o recuperar carrito de sesión
               
                // Redirigir de vuelta (o a la página del carrito)
                respuesta.sendRedirect(peticion.getContextPath() + "/cart.jsp");
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}