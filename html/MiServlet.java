import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class MiServlet extends HttpServlet
{
    public void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException
    {
        respuesta.setContentType("text/html");
        PrintWriter salida = respuesta.getWriter();
        String titulo = "Búsqueda de Empleado por DNI";
        salida.println("<TITLE>"+titulo+"</TITLE>"+
            "<BODY>\n" +
            "<H1 ALIGN=CENTER>" + titulo + "</H1>\n" +
            "<TABLE BORDER=1 ALIGN=CENTER>\n" +
            "<TR BGCOLOR=\"#FFAD00\">\n" +
            "<TH>Nombre del parámetro<TH>Valor del parámetro(s)");
        Enumeration Nombres = peticion.getParameterNames();
        while(Nombres.hasMoreElements())
            {
            String Nombre = (String)Nombres.nextElement();
            salida.print("<TR><TD>" + Nombre + "\n<TD>");
            String[] Valores=peticion.getParameterValues(Nombre);
            if (Valores.length == 1)
            {
                String Valor = Valores[0];
                if (Valor.length() == 0)
                    salida.println("<I>No hay valor</I>");
                else
                    salida.println(Valor);
            }
            else
            {
                salida.println("<UL>");
                for(int i=0; i<Valores.length; i++)
                {
                    salida.println("<LI>" + Valores[i]);
                }
            salida.println("</UL>");
            }
        }
        salida.println("</TABLE>\n</BODY></HTML>");
    }   
}