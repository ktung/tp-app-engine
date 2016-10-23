package fr.unice.polytech.sacc.tp1;


import com.google.appengine.api.datastore.*;
import fr.unice.polytech.sacc.tp1.model.Article;
import fr.unice.polytech.sacc.tp1.model.Shop;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static fr.unice.polytech.sacc.tp1.model.Article.ARTICLE_KIND;
import static fr.unice.polytech.sacc.tp1.model.Shop.SHOP_KIND;

public class DataStoreAncestorEngine extends HttpServlet {



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the json object then deserialize
        Article item1 = new Article("Crumble", 2.4, 1);
        Article item2 = new Article("Mille-feuille", 2.5, 1);
        List<Article> catalog = new ArrayList<>();
        catalog.add(item1); catalog.add(item2);
        Shop shop = new Shop("Patisserie", catalog);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        // Create shop entity (ancestor)
        Entity shopEntity = new Entity(SHOP_KIND);
        shopEntity.setProperty("name", shop.getName());
        datastore.put(shopEntity);

        for (Article article : shop.getArticles()) {
            Entity articleEntity = new Entity(ARTICLE_KIND, shopEntity.getKey());
            articleEntity.setProperty("name", article.getName());
            articleEntity.setProperty("price", article.getPrice());
            articleEntity.setProperty("quantity", article.getQuantity());
            datastore.put(articleEntity);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String shopname = request.getParameter("shopname");
        Long limit = Long.parseLong(request.getParameter("limit"));
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        // Ancestor query
        Query q1 = new Query(SHOP_KIND).setFilter(
            new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, shopname));
        List<Entity> shops = datastore.prepare(q1).asList(FetchOptions.Builder.withDefaults());

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        for (Entity shop : shops) {
            Query q = new Query(ARTICLE_KIND).setAncestor(shop.getKey());
            List<Entity> articles = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());

            if (articles.size() <= limit) {
                for (Entity article : articles) {
                    out.format("shopkey: %s; shopname %s; key: %s; name: %s; price: %s; quantity: %s; ",
                        shop.getKey(), shop.getProperty("name"),
                        article.getKey(), article.getProperty("name"),
                        article.getProperty("price"), article.getProperty("quantity"));
                }
            }
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        String shopname = req.getParameter("shopname");

        Transaction txn = datastore.beginTransaction();
        // Ancestor query
        Query q1 = new Query(SHOP_KIND).setFilter(
                new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, shopname));
        List<Entity> shops = datastore.prepare(q1).asList(FetchOptions.Builder.withDefaults());

        try {
            for (Entity shop : shops) {
                Query q = new Query(ARTICLE_KIND).setAncestor(shop.getKey());
                List<Entity> articles = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());

                for (Entity article : articles) {
                    Double oldPrice = (Double) article.getProperty("price");
                    Double newPrice = oldPrice*1.1;

                    article.setProperty("price", newPrice);
                    datastore.put(txn, article);
                }
            }
            txn.commit();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }

    }
}
