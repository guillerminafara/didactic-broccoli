import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class CrearUsuarioServlet extends HttpServlet {

    public void doPost(HttpServletRequest peticion, HttpServletResponse respuesta)
            throws ServletException, IOException {
try{
        PrintWriter salida = respuesta.getWriter();
        Connection miconexion = DBConnection.getConnection();

        String emailInput = peticion.getParameter("email");
        String passwordInput = peticion.getParameter("password");
        String checkPasswordInput = peticion.getParameter("password2");
        String nameInput = peticion.getParameter("nombre");
        String surnameInput = peticion.getParameter("apellido");

        if (passwordInput.equals(checkPasswordInput)) {
            String sql = "INSERT INTO usuario(nombre, apellido, email, contrasena) values (?,?,?,?)";
            PreparedStatement ps;
            ps = miconexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nameInput);
            ps.setString(2, surnameInput);
            ps.setString(3, emailInput);
            ps.setString(4, passwordInput);

            int filas= ps.executeUpdate();
         if(filas>0){
         ResultSet rs= ps.getGeneratedKeys();
            if (rs.next()) {
                int id_usuario = rs.getInt(1);
                Cookie cookieUser = new Cookie("id_usuario", String.valueOf(id_usuario));
                Cookie cookieUserName = new Cookie(nameInput, String.valueOf(nameInput));

                cookieUser.setMaxAge(60 * 60 * 24 * 30);
                cookieUser.setPath("/");
                cookieUserName.setMaxAge(60 * 60 * 24 * 30);
                cookieUserName.setPath("/");
                respuesta.addCookie(cookieUser);
                respuesta.addCookie(cookieUserName);

                respuesta.sendRedirect(peticion.getContextPath() + "/paginaPrincipal");
            }}
                    ps.close();
miconexion.close();
       }

}catch(Exception e){
  e.printStackTrace();
System.out.println(e);
}
    }
}
//tareas: corroborar si es correcto agregar cookieName asi 
