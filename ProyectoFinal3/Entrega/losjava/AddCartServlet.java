import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AddCartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // retrieve current session
        HttpSession session = request.getSession();
        // retrieve shopping cart from session
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        // if cart does not exist, create new cart
        if (carrito == null) {
            carrito = new ArrayList<ItemCarrito>();
            session.setAttribute("carrito", carrito);
        }
        // Retrieve form parameters
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM producto WHERE id_producto=?");

            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
  // If product exists, create cart item and add it to session cart
            if (rs.next()) {
                ItemCarrito item = new ItemCarrito(
                        idProducto,
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        cantidad,
                        rs.getString("imagen"));

                carrito.add(item);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            throw new ServletException(e);
        }

        response.sendRedirect("paginaPrincipal");
    }
}
