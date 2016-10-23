package fr.unice.polytech.sacc.tp1.model;

public class Article {
    public static String ARTICLE_KIND = "article";

    private String name;
    private Double price;
    private Integer quantity;

    public Article() {
    }

    public Article(String name, Double price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
