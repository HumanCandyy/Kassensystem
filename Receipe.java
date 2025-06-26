/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Receipe {
    private static int nextID = 1; // static counter for unique IDs

    private final LocalDateTime dateTime;
    private String cashier;
    private final int ID;
    private List<Article> articleList;
    private double totalPrice;

    private final List<ReceipeObserver> observers = new CopyOnWriteArrayList<>();

    // Constructor increments ID automatically, no need for public setter
    public Receipe() {
        this.ID = nextID++;
        this.dateTime = LocalDateTime.now(); // aktuelles Datum und Uhrzeit setzen
        this.articleList = new ArrayList<>(); // Liste initialisieren
        this.totalPrice = 0.0;
    }

    public Receipe(String cashier) {
        this.ID = nextID++;
        this.dateTime = LocalDateTime.now();
        this.articleList = new ArrayList<>();
        this.totalPrice = 0.0;
        this.cashier = cashier;
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
        notifyObservers();
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
        notifyObservers();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void addObserver(ReceipeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ReceipeObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (ReceipeObserver observer : observers) {
            observer.onReceipeChanged(this);
        }
    }

    // Fügt einen Artikel hinzu und addiert dessen Preis zu totalPrice
    public void addArticle(Article article) {
        if (article != null) {
            articleList.add(article);
            totalPrice += article.getPrice();
            notifyObservers();
        }
    }

    // Entfernt einen Artikel (erstes Vorkommen) und subtrahiert dessen Preis von totalPrice
    public void removeArticle(Article article) {
        if (article != null && articleList.remove(article)) {
            totalPrice -= article.getPrice();
            notifyObservers();
        }
    }
}
