import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hallo Welt!");
        JFrame frame = new JFrame("Einfaches Fenster");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font largeFont = new Font("Arial", Font.PLAIN, 18);

        LoginPanel loginPanel = new LoginPanel(largeFont);
        OutputPanel outputPanel = new OutputPanel(largeFont);
        ButtonPanel buttonPanel = new ButtonPanel(largeFont, outputPanel.getSelectedItemsLabel(), outputPanel.getOutputLabel(), loginPanel.getIsLoggedIn(), loginPanel.getIsAdmin());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(loginPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(outputPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}

class LoginPanel extends JPanel {
    private final JTextField userField;
    private final JPasswordField passField;
    private final JLabel loggedInUserLabel;
    private final boolean[] isLoggedIn;
    private final boolean[] isAdmin;

    public LoginPanel(Font largeFont) {
        super(new FlowLayout(FlowLayout.CENTER));
        this.isLoggedIn = new boolean[]{false};
        this.isAdmin = new boolean[]{false};

        JLabel userLabel = new JLabel("Benutzer:");
        userField = new JTextField(10);
        JLabel passLabel = new JLabel("Passwort:");
        passField = new JPasswordField(10);
        JButton loginButton = new JButton("Login");
        loggedInUserLabel = new JLabel("Nicht eingeloggt");

        userLabel.setFont(largeFont);
        userField.setFont(largeFont);
        passLabel.setFont(largeFont);
        passField.setFont(largeFont);
        loginButton.setFont(largeFont);
        loggedInUserLabel.setFont(largeFont);

        add(userLabel);
        add(userField);
        add(passLabel);
        add(passField);
        add(loginButton);
        add(loggedInUserLabel);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            if ("Kassierer".equals(username) && "12345".equals(password)) {
                loggedInUserLabel.setText("Eingeloggt als: " + username);
                isLoggedIn[0] = true;
                isAdmin[0] = false;
            } else if ("Admin".equals(username) && "geheim".equals(password)) {
                loggedInUserLabel.setText("Eingeloggt als: Admin");
                isLoggedIn[0] = true;
                isAdmin[0] = true;
            } else {
                loggedInUserLabel.setText("Login fehlgeschlagen");
                isLoggedIn[0] = false;
                isAdmin[0] = false;
            }
        });
    }

    public boolean[] getIsLoggedIn() {
        return isLoggedIn;
    }

    public boolean[] getIsAdmin() {
        return isAdmin;
    }
}

class OutputPanel extends JPanel {
    private final JLabel selectedItemsLabel;
    private final JLabel outputLabel;

    public OutputPanel(Font largeFont) {
        super(new BorderLayout());
        selectedItemsLabel = new JLabel("Auswahl: ", SwingConstants.RIGHT);
        outputLabel = new JLabel("Gesamt: 0€", SwingConstants.RIGHT);
        selectedItemsLabel.setFont(largeFont);
        outputLabel.setFont(largeFont);

        add(selectedItemsLabel, BorderLayout.NORTH);
        add(outputLabel, BorderLayout.SOUTH);
    }

    public JLabel getSelectedItemsLabel() {
        return selectedItemsLabel;
    }

    public JLabel getOutputLabel() {
        return outputLabel;
    }
}

class ButtonPanel extends JPanel {
    public ButtonPanel(Font largeFont, JLabel selectedItemsLabel, JLabel outputLabel, boolean[] isLoggedIn, boolean[] isAdmin) {
        super(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Neue Artikel-Objekte
        ArrayList<Article> articles = new ArrayList<>();
        articles.add(new Bier());
        articles.add(new AsbachCola());
        articles.add(new RothausWeizen());
        articles.add(new WaldhausBier());
        articles.add(new Wein());

        HashMap<Article, Integer> itemCounts = new HashMap<>();
        HashMap<Article, Integer> stats = new HashMap<>(); // Statistik für Auswertung
        for (Article article : articles) {
            itemCounts.put(article, 0);
        }

        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            String label = article.getName() + " - " + article.getPrice() + "€";
            JButton mainButton = new JButton(label);
            JButton plusButton = new JButton("+");
            JButton minusButton = new JButton("-");
            JLabel counterLabel = new JLabel("0");

            mainButton.setFont(largeFont);
            plusButton.setFont(largeFont);
            minusButton.setFont(largeFont);
            counterLabel.setFont(largeFont);

            plusButton.addActionListener(e -> {
                if (isLoggedIn[0]) {
                    int currentCount = Integer.parseInt(counterLabel.getText());
                    counterLabel.setText(String.valueOf(currentCount + 1));
                    itemCounts.put(article, currentCount + 1);
                    updateSelectedItemsLabel(selectedItemsLabel, itemCounts);
                    updateTotalSum(outputLabel, itemCounts);
                }
            });
            minusButton.addActionListener(e -> {
                if (isLoggedIn[0]) {
                    int currentCount = Integer.parseInt(counterLabel.getText());
                    if (currentCount > 0) {
                        counterLabel.setText(String.valueOf(currentCount - 1));
                        itemCounts.put(article, currentCount - 1);
                        updateSelectedItemsLabel(selectedItemsLabel, itemCounts);
                        updateTotalSum(outputLabel, itemCounts);
                    }
                }
            });

            rowPanel.add(mainButton);
            rowPanel.add(plusButton);
            rowPanel.add(minusButton);
            rowPanel.add(counterLabel);

            gbc.gridy = i;
            add(rowPanel, gbc);
        }

        JButton resetButton = new JButton("Bezahlen");
        resetButton.setFont(largeFont);
        resetButton.addActionListener(e -> {
            if (isLoggedIn[0]) {
                // Statistik aktualisieren
                for (Article article : itemCounts.keySet()) {
                    int count = itemCounts.get(article);
                    if (count > 0) {
                        stats.put(article, stats.getOrDefault(article, 0) + count);
                    }
                }
                itemCounts.keySet().forEach(a -> itemCounts.put(a, 0));
                for (Component component : getComponents()) {
                    if (component instanceof JPanel) {
                        JPanel rowPanel = (JPanel) component;
                        for (Component subComponent : rowPanel.getComponents()) {
                            if (subComponent instanceof JLabel) {
                                ((JLabel) subComponent).setText("0");
                            }
                        }
                    }
                }
                updateSelectedItemsLabel(selectedItemsLabel, itemCounts);
                updateTotalSum(outputLabel, itemCounts);
            }
        });

        gbc.gridy = articles.size();
        add(resetButton, gbc);

        // Auswertung-Button hinzufügen
        JButton auswertungButton = new JButton("Auswertung");
        auswertungButton.setFont(largeFont);
        gbc.gridy = articles.size() + 1;
        add(auswertungButton, gbc);
        auswertungButton.setVisible(isAdmin[0]);

        auswertungButton.addActionListener(e -> {
            if (!isAdmin[0]) return;
            try {
                java.io.PrintWriter writer = new java.io.PrintWriter("auswertung.txt", "UTF-8");
                double gesamt = 0;
                writer.println("Auswertung der bezahlten Artikel:");
                for (Article article : stats.keySet()) {
                    int count = stats.get(article);
                    double preis = article.getPrice();
                    writer.println(count + "x " + article.getName() + " = " + (count * preis) + "€");
                    gesamt += count * preis;
                }
                writer.println("Gesamtsumme: " + gesamt + "€");
                writer.close();
                JOptionPane.showMessageDialog(this, "Auswertung wurde in auswertung.txt gespeichert.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fehler beim Schreiben der Auswertung: " + ex.getMessage());
            }
        });

        // Sichtbarkeit bei Login-Status-Änderung aktualisieren
        Timer adminCheckTimer = new Timer(300, e -> auswertungButton.setVisible(isAdmin[0]));
        adminCheckTimer.start();
    }

    private void updateSelectedItemsLabel(JLabel label, HashMap<Article, Integer> itemCounts) {
        StringBuilder text = new StringBuilder("Auswahl: ");
        itemCounts.forEach((article, count) -> {
            if (count > 0) {
                text.append(count).append("x ").append(article.getName()).append(", ");
            }
        });
        label.setText(text.toString().replaceAll(", $", ""));
    }

    private void updateTotalSum(JLabel label, HashMap<Article, Integer> itemCounts) {
        double totalSum = 0;
        for (Article article : itemCounts.keySet()) {
            totalSum += itemCounts.get(article) * article.getPrice();
        }
        label.setText("Gesamt: " + totalSum + "€");
    }
}

abstract class Article {
    public abstract String getName();
    public abstract double getPrice();
}

class Bier extends Article {
    @Override
    public String getName() {
        return "Bier";
    }

    @Override
    public double getPrice() {
        return 2.5;
    }
}

class AsbachCola extends Article {
    @Override
    public String getName() {
        return "AsbachCola";
    }

    @Override
    public double getPrice() {
        return 3.0;
    }
}

class RothausWeizen extends Article {
    @Override
    public String getName() {
        return "RothausWeizen";
    }

    @Override
    public double getPrice() {
        return 3.5;
    }
}

class WaldhausBier extends Article {
    @Override
    public String getName() {
        return "WaldhausBier";
    }

    @Override
    public double getPrice() {
        return 4.0;
    }
}

class Wein extends Article {
    @Override
    public String getName() {
        return "Wein";
    }

    @Override
    public double getPrice() {
        return 5.0;
    }
}