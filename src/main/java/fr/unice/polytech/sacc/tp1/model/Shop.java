package fr.unice.polytech.sacc.tp1.model;

import java.util.List;

public class Shop {
    private String name;
    private List<Article> articles;

    public Shop() {
    }

    public Shop(String name, List<Article> articles) {
        this.name = name;
        this.articles = articles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
