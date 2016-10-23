package fr.unice.polytech.sacc.tp1;

import com.google.appengine.api.datastore.*;
import com.google.gson.Gson;
import fr.unice.polytech.sacc.tp1.model.Article;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class DataStoreEngine extends HttpServlet {

    public static String ARTICLE_KIND = "article";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the json object then deserialize
        Article article = new Article("Crumble", 2.4, 1);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity articleEntity = new Entity(ARTICLE_KIND);
        articleEntity.setProperty("name", article.getName());
        articleEntity.setProperty("price", article.getPrice());
        articleEntity.setProperty("quantity", article.getQuantity());

        datastore.put(articleEntity);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query q = new Query(ARTICLE_KIND);
        List<Entity> results = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        for (Entity result : results) {
            out.format("key: %s; name: %s; price: %s; quantity: %s; ",
                    result.getKey(), result.getProperty("name"),
                    result.getProperty("price"), result.getProperty("quantity"));
        }
    }

    /**
     * http://localhost:8080/datastore?key={key}
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query(ARTICLE_KIND).setFilter(
            new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, name));
        List<Entity> results = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());

        for (Entity result : results) {
            datastore.delete(result.getKey());
        }
    }
}
