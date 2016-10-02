package fr.unice.polytech.sacc.tp1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ScalingEngine extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Thread.sleep(50);
            response.setContentType("text/plain");
            response.getWriter().println("Scaling sleep50");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
