/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Receipe {
    private static int nextID = 1; // static counter for unique IDs

    private final LocalDateTime dateTime;
    private String cashier;
    private final int ID;
    private List<Article> articleList;
    private double totalPrice;

    // Constructor increments ID automatically, no need for public setter
    public Receipe() {
        this.ID = nextID++;
        this.dateTime = LocalDateTime.now(); // aktuelles Datum und Uhrzeit setzen
        this.articleList = new ArrayList<>(); // Liste initialisieren
        this.totalPrice = 0.0;
    }

    // Getter only, no public setter for ID to keep it unique
    public int getID() {
        return ID;
    }

    // Getter für dateTime, kein Setter mehr notwendig
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Fügt einen Artikel hinzu und addiert dessen Preis zu totalPrice
    public void addArticle(Article article) {
        if (article != null) {
            articleList.add(article);
            totalPrice += article.getPrice();
        }
    }

    // Entfernt einen Artikel (erstes Vorkommen) und subtrahiert dessen Preis von totalPrice
    public void removeArticle(Article article) {
        if (article != null && articleList.remove(article)) {
            totalPrice -= article.getPrice();
        }
    }
}
