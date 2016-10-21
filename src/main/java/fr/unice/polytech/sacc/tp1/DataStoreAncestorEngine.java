package fr.unice.polytech.sacc.tp1;

import com.google.cloud.datastore.*;
import com.google.gson.Gson;
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

public class DataStoreAncestorEngine extends HttpServlet {

    public static String SHOP_KIND = "shop";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Article item1 = new Article("Crumble", 2.4, 1);
        Article item2 = new Article("Mille-feuille", 2.5, 1);

        List<Article> catalog = new ArrayList<>();
        catalog.add(item1); catalog.add(item2);
        Shop shop = new Shop("Patisserie", catalog);

        Datastore datastore = DatastoreOptions.defaultInstance().service();
        KeyFactory keyFactory = datastore.newKeyFactory().kind(SHOP_KIND);
        IncompleteKey key = keyFactory.kind(SHOP_KIND).newKey();
        Gson gson = new Gson();

        FullEntity<IncompleteKey> shopEntry = FullEntity.builder(key)
            .set("shopname", shop.getName())
            .set("nb_article", shop.getArticles().size())
            .set("catalog_json", gson.toJson(shop.getArticles()))
            .build();
        datastore.add(shopEntry);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String shopname = request.getParameter("shopname");
        Long limit = Long.parseLong(request.getParameter("limit"));


        Datastore datastore = DatastoreOptions.defaultInstance().service();
        Query<Entity> query = Query.entityQueryBuilder().kind(SHOP_KIND).build();
        QueryResults<Entity> results = datastore.run(query);

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        while (results.hasNext()) {
            Entity entity = results.next();
            Long catalogLimit = entity.getLong("nb_article");
            if (shopname.equals(entity.getString("shopname")) && catalogLimit <= limit) {
                out.format("shopname: %s, nb_article: %s, json: %s; key: %s",
                    entity.getString("shopname"),
                    entity.getLong("nb_article"),
                    entity.getString("catalog_json"),
                    entity.key()
                );
            }
        }
    }
}
