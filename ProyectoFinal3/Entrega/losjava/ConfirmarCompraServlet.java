import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ConfirmarCompraServlet extends HttpServlet {

    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
        // set response encoding
        respuesta.setContentType("text/html;charset=UTF-8");
        PrintWriter salida = respuesta.getWriter();
        // retrieve curren session without creatind a new one
        HttpSession session = peticion.getSession(false);
        // validate session existance
        if (session == null) {
            salida.println("<h2>Sesión no válida</h2>");
            return;
        }
        // Retrieve shopping cart from session
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        // Validate cart contents
        if (carrito == null || carrito.isEmpty()) {
            salida.println("<h2>No hay productos en el carrito</h2>");
            return;
        }

        // Retrieve user ID from persistent cookie
        int idUsuario = -1;
        Cookie[] cookies = peticion.getCookies();

        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("id_usuario".equals(c.getName())) {
                    try {
                        idUsuario = Integer.parseInt(c.getValue());
                    } catch (NumberFormatException e) {
                        idUsuario = -1;
                    }
                    break;
                }
            }
        }
        // validate user identification
        if (idUsuario == -1) {
            salida.println("<h2>Error: usuario no identificado</h2>");
            return;
        }
        // calculate purchase total price
        double total = 0;
        for (ItemCarrito item : carrito) {
            total += item.getPrecio() * item.getCantidad();
        }

        Connection con = null;
        PreparedStatement psCompra = null;
        PreparedStatement psDetalle = null;

        try {
            con = DBConnection.getConnection();

            // insert purchase to db 
            String sqlCompra = "INSERT INTO compra (id_usuario, fecha_compra, total) VALUES (?,?,?)";

            psCompra = con.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS);
            psCompra.setInt(1, idUsuario);
            psCompra.setDate(2, new Date(System.currentTimeMillis())); 
            psCompra.setDouble(3, total);
            psCompra.executeUpdate();

            ResultSet rs = psCompra.getGeneratedKeys();
            int idCompra = 0;

            if (rs.next()) {
                idCompra = rs.getInt(1);
            }
            rs.close();

            // insert purchase details
            String sqlDetalle = "INSERT INTO compra_producto (id_producto, id_compra, cantidad, precio_unitario, subtotal) VALUES (?,?,?,?,?)";

            psDetalle = con.prepareStatement(sqlDetalle);

            for (ItemCarrito item : carrito) {
                double subtotal = item.getPrecio() * item.getCantidad();

                psDetalle.setInt(1, item.getIdProducto());
                psDetalle.setInt(2, idCompra);
                psDetalle.setInt(3, item.getCantidad());
                psDetalle.setDouble(4, item.getPrecio());
                psDetalle.setDouble(5, subtotal);
                psDetalle.executeUpdate();
            }

            // remove shopping cart from session after purchase
            session.removeAttribute("carrito");

            // Build confirmation HTML page
            salida.println("<!DOCTYPE html>");
            salida.println("<html lang='es'>");
            salida.println("<head>");
            salida.println("<meta charset='UTF-8'>");
            salida.println("<title>Pedido confirmado</title>");
            salida.println("<style>");
            salida.println("body{font-family:Arial;text-align:center;background:#EAEDED;}");
            salida.println(".box{margin:100px auto;width:400px;padding:30px;background:white;border-radius:8px;}");
            salida.println(".ok{color:green;font-size:22px;}");
            salida.println("</style>");
            salida.println("</head>");
            salida.println("<body>");

            salida.println("<div class='box'>");
            salida.println("<h2 class='ok'>¡Gracias por tu compra!</h2>");
            salida.println("<p>Tu pedido ha sido registrado correctamente.</p>");
            salida.println("<p><strong>Número de seguimiento:</strong></p>");
            salida.println("<h1>" + idCompra + "</h1>");
            salida.println("<a href='paginaPrincipal'>Volver a la tienda</a>");
            salida.println("</div>");

            salida.println("</body>");
            salida.println("</html>");

        } catch (Exception e) {
            try {
                if (con != null)
                    con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            salida.println("<h2>Error al registrar la compra</h2>");
            salida.println("<pre>" + e.getMessage() + "</pre>");

        } finally {
            try {
                if (psDetalle != null)
                    psDetalle.close();
                if (psCompra != null)
                    psCompra.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
