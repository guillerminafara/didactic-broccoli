import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import util.DBConnection;

public class AdminPedidosServlet extends HttpServlet {

    protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
        Cookie[] cookies = peticion.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getAttribute("nombre").equals("admin") && c.getAttribute("id_usuario") == 1) {
          
                } else {
                    respuesta.sendRedirect("PaginaPrincipal.html");
                    return;
                }
            }
        }
        respuesta.setContentType("text/html;charset=UTF-8");
        PrintWriter out = respuesta.getWriter();

        out.println("<html><head><title>Pedidos Admin</title></head><body>");
        out.println("<h2>Listado de pedidos</h2>");

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT c.id_compra, c.fecha_compra, c.total, u.nombre, u.email " +
                    "FROM compra c JOIN usuario u ON c.id_usuario = u.id_usuario";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            out.println("<table border='1'>");
            out.println("<tr><th>ID Pedido</th><th>Fecha</th><th>Cliente</th><th>Email</th><th>Total</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id_compra") + "</td>");
                out.println("<td>" + rs.getDate("fecha_compra") + "</td>");
                out.println("<td>" + rs.getString("nombre") + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("<td>€" + rs.getDouble("total") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");

            con.close();

        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }

        out.println("</body></html>");
    }
}
