import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class MiServlet extends HttpServlet {
    public void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        respuesta.setContentType("text/html");
        PrintWriter salida = respuesta.getWriter();
        String titulo = "Búsqueda de Empleado por DNI";
        String dniParametro = peticion.getParameter("DNIinput");

        salida.println("<TITLE>" + titulo + "</TITLE>" +
                "<BODY>\n" +
                "<H1 ALIGN=CENTER>" + titulo + "</H1>\n" +
                "<TABLE BORDER=1 ALIGN=CENTER>\n" +
                "<TR BGCOLOR=\"#FFAD00\">\n" +
                "<TH>DNI<TH>Nombre<TH>Sueldo");

        try {
            String SourceURL = "jdbc:mysql://localhost/bdprueba?allowPublicKeyRetrieval=true&useSSL=false";
            String user = "alumno";
            String password = "mipassword";
            Connection miconexion;
            miconexion = DriverManager.getConnection(SourceURL, user, password);
            PreparedStatement ps;
            String sql = "SELECT * FROM empleados where DNI= ?";
            ps = miconexion.prepareStatement(sql);
            ps.setString(1, Integer.parseInt(dniParametro));

            ResultSet misresultados = ps.executeQuery();

            while (misresultados.next()) {
                salida.println("<TR><TD>" + misresultados.getInt("DNI") + "\n<TD>" +
                        misresultados.getString("nombre") + "\n<TD>" +
                        misresultados.getFloat("sueldo"));

            }
            salida.println("</TABLE>\n</BODY></HTML>");
            miconexion.close();
        } catch (Exception e) {
            salida.println(e);
            salida.println("</BODY></HTML>");
        }

    }
}