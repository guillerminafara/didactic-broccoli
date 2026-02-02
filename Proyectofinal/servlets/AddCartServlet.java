import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

public class AddCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        HttpSession session = request.getSession();
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM producto WHERE id_producto = ?");
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Crear item y agregar al carrito
                ItemCarrito item = new ItemCarrito();
                item.setIdProducto(productId);
                item.setNombre(rs.getString("nombre"));
                item.setPrecio(rs.getDouble("precio"));
                item.setImagen(rs.getString("imagen"));
                item.setCantidad(quantity);

                // Si ya existe, actualizar cantidad
                boolean encontrado = false;
                for (ItemCarrito i : carrito) {
                    if (i.getIdProducto() == productId) {
                        i.setCantidad(i.getCantidad() + quantity);
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    carrito.add(item);
                }
            }
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Guardar carrito en sesión
        session.setAttribute("carrito", carrito);

        // Redirigir de nuevo a la página principal
        response.sendRedirect("IndexPageServlet");
    }
}
