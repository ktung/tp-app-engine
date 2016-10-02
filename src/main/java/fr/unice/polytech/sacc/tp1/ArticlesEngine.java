package fr.unice.polytech.sacc.tp1;

import com.google.gson.Gson;
import fr.unice.polytech.sacc.tp1.model.Article;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ArticlesEngine extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        Article article = gson.fromJson(reader, Article.class);

        response.setContentType("application/json");
        response.getWriter().println(gson.toJson(article));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("Trésor", 8.00, 1));
        articles.add(new Article("Chocapic", 7.99, 1));
        articles.add(new Article("Trésor", 15.99, 2));
        articles.add(new Article("Lucky Charms", 10.00, 1));

        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().println(gson.toJson(articles));
    }
}
