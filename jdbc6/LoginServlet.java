import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {

    // Datos de ejemplo para validar usuario
    private final String USER = "user@example.com";
    private final String PASS = "12345";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Obtener los valores del formulario
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validación simple
        if (USER.equals(email) && PASS.equals(password)) {
            out.println("<h2>Welcome " + email + "!</h2>");
        } else {
            out.println("<h2>Login failed: invalid email or password.</h2>");
            out.println("<a href='login.html'>Try again</a>");
        }
    }
}
