import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import util.DBConnection;

public class ConfirmarCompraServlet extends HttpServlet {

    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {

        respuesta.setContentType("text/html;charset=UTF-8");
        PrintWriter out = respuesta.getWriter();

        HttpSession session = peticion.getSession();
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

        if (carrito == null || carrito.isEmpty()) {
            out.println("<h2>No hay productos en el carrito</h2>");
            return;
        }

        // We obtained the user from cookies
        int idUsuario = -1;
        Cookie[] cookies = peticion.getCookies();

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("id_usuario")) {
                    idUsuario = Integer.parseInt(c.getValue());
                    break;
                }
            }
        }

        if (idUsuario == -1) {
            out.println("<h2>Error: usuario no identificado</h2>");
            return;
        }

        double total = 0;

        for (ItemCarrito item : carrito) {
            total += item.getPrecio() * item.getCantidad();
        }

        Connection con = null;
        PreparedStatement psCompra = null;
        PreparedStatement psDetalle = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false); // transacción

            // We created the purchase in the database
            String sqlCompra = "INSERT INTO compra (id_usuario, total) VALUES (?,?)";
            psCompra = con.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS);
            psCompra.setInt(1, idUsuario);
            psCompra.setDouble(2, total);
            psCompra.executeUpdate();

            // obtener id_compra (número seguimiento)
            ResultSet rs = psCompra.getGeneratedKeys();
            int idCompra = 0;

            if (rs.next()) {
                idCompra = rs.getInt(1);
            }

            // 2️⃣ Insertar en COMPRA_PRODUCTO
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

            con.commit();

            // limpiar carrito
            session.removeAttribute("carrito");

            // display the HTML
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Pedido confirmado</title>");
            out.println("<style>");
            out.println("body{font-family:Arial;text-align:center;background:#EAEDED;}");
            out.println(".box{margin:100px auto;width:400px;padding:30px;background:white;border-radius:8px;}");
            out.println(".ok{color:green;font-size:22px;}");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            out.println("<div class='box'>");
            out.println("<h2 class='ok'>¡Gracias por tu compra!</h2>");
            out.println("<p>Tu pedido ha sido registrado correctamente.</p>");
            out.println("<p><strong>Número de seguimiento:</strong></p>");
            out.println("<h1>" + idCompra + "</h1>");
            out.println("<a href='IndexPageServlet'>Volver a la tienda</a>");
            out.println("</div>");

            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            try {
                if (con != null)
                    con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            out.println("<h2>Error al registrar la compra</h2>");
            out.println(e.getMessage());
        } finally {
            try {
                if (psCompra != null)
                    psCompra.close();
                if (psDetalle != null)
                    psDetalle.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
