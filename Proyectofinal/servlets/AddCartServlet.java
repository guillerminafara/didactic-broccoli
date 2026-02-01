
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import util.DBConnection;

public class AddCartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {

        int productId = Integer.parseInt(peticion.getParameter("productId"));
        int quantity = Integer.parseInt(peticion.getParameter("quantity"));
        HtppSession session = peticion.getSession();

        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        //
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        boolean encontrado = false;

        for (ItemCarrito item : carrito) {
            if (item.getIdProducto() == productId) {
                item.setCantidad(item.getCantidad() + quantity);
                encontrado = true;
                break;
            }
        }

        // retrieve the products from db
        if (encontrado) {
            try {
                Connection conn = DBConnection.getConnection();
                String sql = "SELECT nombre, precio FROM producto WHERE id_producto = ?";

                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, productId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    ItemCarrito item = new ItemCarrito(
                            productId,
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            quantity, rs.getString("imagen"));
                    carrito.add(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        session.setAttribute("carrito", carrito);
        respuesta.sendRedirect("CartServlet");
    }
}
