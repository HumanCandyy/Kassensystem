import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hallo Welt!");
        JFrame frame = new JFrame("Kassensystem");
        frame.setSize(1200, 1000);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // zentriert das Fenster

        Font largeFont = new Font("Arial", Font.BOLD, 20);
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        Font textFont = new Font("Arial", Font.PLAIN, 18);

        LoginPanel loginPanel = new LoginPanel(largeFont, textFont);
        OutputPanel outputPanel = new OutputPanel(largeFont);
        loginPanel.setOutputPanel(outputPanel);
        ButtonPanel buttonPanel = new ButtonPanel(buttonFont, outputPanel, loginPanel, loginPanel.getIsLoggedIn(), loginPanel.getIsAdmin());

        // Container für Beleg und Bedienfeld (vertikal gestapelt, volle Breite)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        outputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        outputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        centerPanel.add(outputPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(buttonPanel);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.add(loginPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

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
    private OutputPanel outputPanel;
    private final String[][] users = {
        {"Kassierer1", "12345"},
        {"Kassierer2", "12345"},
        {"Kassierer3", "12345"},
        {"Admin", "geheim"}
    };

    public LoginPanel(Font labelFont, Font textFont) {
        super(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setBackground(new Color(230, 230, 250));
        setBorder(BorderFactory.createTitledBorder("Login"));
        this.isLoggedIn = new boolean[]{false};
        this.isAdmin = new boolean[]{false};

        JLabel userLabel = new JLabel("Benutzer:");
        userField = new JTextField(10);
        JLabel passLabel = new JLabel("Passwort:");
        passField = new JPasswordField(10);
        JButton loginButton = new JButton("Login");
        loggedInUserLabel = new JLabel("Nicht eingeloggt");

        userLabel.setFont(labelFont);
        userField.setFont(textFont);
        passLabel.setFont(labelFont);
        passField.setFont(textFont);
        loginButton.setFont(labelFont);
        loggedInUserLabel.setFont(labelFont);

        add(userLabel);
        add(userField);
        add(passLabel);
        add(passField);
        add(loginButton);
        add(loggedInUserLabel);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            boolean found = false;
            for (String[] user : users) {
                if (user[0].equals(username) && user[1].equals(password)) {
                    found = true;
                    if ("Admin".equals(username)) {
                        loggedInUserLabel.setText("Eingeloggt als: Admin");
                        isLoggedIn[0] = true;
                        isAdmin[0] = true;
                    } else {
                        loggedInUserLabel.setText("Eingeloggt als: " + username);
                        isLoggedIn[0] = true;
                        isAdmin[0] = false;
                    }
                    if (outputPanel != null) {
                        outputPanel.setCashier(username);
                    }
                    break;
                }
            }
            if (!found) {
                loggedInUserLabel.setText("Login fehlgeschlagen");
                isLoggedIn[0] = false;
                isAdmin[0] = false;
            }
        });
    }

    public void setOutputPanel(OutputPanel outputPanel) {
        this.outputPanel = outputPanel;
    }

    public boolean[] getIsLoggedIn() {
        return isLoggedIn;
    }

    public boolean[] getIsAdmin() {
        return isAdmin;
    }

    // Hilfsmethode, um aktuellen User zu bekommen
    public String getCurrentUser() {
        return userField.getText();
    }
}

class OutputPanel extends JPanel {
    private final JTextArea receiptArea;
    private Receipe currentReceipe;

    public OutputPanel(Font largeFont) {
        super(new BorderLayout());
        setBackground(new Color(255, 255, 240));
        setBorder(BorderFactory.createTitledBorder("Beleg"));
        receiptArea = new JTextArea(12, 28);
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.BOLD, largeFont.getSize()));
        receiptArea.setLineWrap(false);
        receiptArea.setWrapStyleWord(false);
        receiptArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        receiptArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptArea.setAlignmentY(Component.CENTER_ALIGNMENT);
        receiptArea.setBackground(new Color(255, 255, 255));
        receiptArea.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 2));
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1000, 350));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
        add(scrollPane, BorderLayout.CENTER);
        currentReceipe = new Receipe();
        updateReceipt();
    }

    public Receipe getCurrentReceipe() {
        return currentReceipe;
    }

    public void addArticleToReceipt(Article article) {
        currentReceipe.addArticle(article);
        updateReceipt();
    }

    public void resetReceipt() {
        // Kassierer aus LoginPanel holen
        String cashier = null;
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof JFrame) {
            JFrame frame = (JFrame) w;
            for (Component c : frame.getContentPane().getComponents()) {
                if (c instanceof JPanel) {
                    for (Component sub : ((JPanel) c).getComponents()) {
                        if (sub instanceof LoginPanel) {
                            cashier = ((LoginPanel) sub).getCurrentUser();
                        }
                    }
                }
            }
        }
        currentReceipe = new Receipe(cashier);
        updateReceipt();
    }

    public void setCashier(String cashier) {
        if (currentReceipe != null) {
            currentReceipe.setCashier(cashier);
            updateReceipt();
        }
    }

    private void updateReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("Beleg Nr. ").append(currentReceipe.getID()).append("\n");
        sb.append(currentReceipe.getDateTime().toLocalDate()).append(" ").append(currentReceipe.getDateTime().toLocalTime().withNano(0)).append("\n");
        sb.append("Kassierer: ").append(currentReceipe.getCashier() != null ? currentReceipe.getCashier() : "-").append("\n");
        sb.append("-----------------------------\n");
        // Artikel zusammenfassen
        java.util.Map<String, Integer> countMap = new java.util.LinkedHashMap<>();
        java.util.Map<String, Double> priceMap = new java.util.LinkedHashMap<>();
        for (Article a : currentReceipe.getArticleList()) {
            countMap.put(a.getName(), countMap.getOrDefault(a.getName(), 0) + 1);
            priceMap.put(a.getName(), a.getPrice());
        }
        for (String name : countMap.keySet()) {
            int count = countMap.get(name);
            double price = priceMap.get(name);
            sb.append(String.format("%2dx %-18s %8.2f€\n", count, name, count * price));
        }
        sb.append("-----------------------------\n");
        sb.append(String.format("Gesamt:%22.2f€", currentReceipe.getTotalPrice()));
        receiptArea.setText(sb.toString());
    }
}

class ButtonPanel extends JPanel {
    private final LoginPanel loginPanel;
    public ButtonPanel(Font buttonFont, OutputPanel outputPanel, LoginPanel loginPanel, boolean[] isLoggedIn, boolean[] isAdmin) {
        super(new GridBagLayout());
        this.loginPanel = loginPanel;
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createTitledBorder("Kasse"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        ArrayList<Article> articles = new ArrayList<>();
        articles.add(new Bier());
        articles.add(new AsbachCola());
        articles.add(new RothausWeizen());
        articles.add(new WaldhausBier());
        articles.add(new Wein());

        // Counter-Labels für alle Artikel speichern
        java.util.Map<String, JLabel> counterLabels = new java.util.HashMap<>();
        // Methode zum Aktualisieren aller Counter
        Runnable updateCounters = () -> {
            java.util.Map<String, Integer> countMap = new java.util.HashMap<>();
            for (Article a : outputPanel.getCurrentReceipe().getArticleList()) {
                countMap.put(a.getName(), countMap.getOrDefault(a.getName(), 0) + 1);
            }
            for (String name : counterLabels.keySet()) {
                int count = countMap.getOrDefault(name, 0);
                counterLabels.get(name).setText(String.valueOf(count));
            }
        };

        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            JPanel rowPanel = new JPanel(new BorderLayout(10, 0)); // BorderLayout für volle Breite
            rowPanel.setOpaque(false);
            String label = article.getName() + " - " + article.getPrice() + "€";
            JButton mainButton = new JButton(label);
            JButton plusButton = new JButton("+");
            JButton minusButton = new JButton("-");
            JLabel counterLabel = new JLabel("0");
            counterLabels.put(article.getName(), counterLabel);

            mainButton.setFont(buttonFont);
            plusButton.setFont(buttonFont);
            minusButton.setFont(buttonFont);
            counterLabel.setFont(buttonFont);
            mainButton.setPreferredSize(null); // Automatische Breite
            mainButton.setMinimumSize(new Dimension(100, 40));
            mainButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            plusButton.setPreferredSize(new Dimension(50, 40));
            minusButton.setPreferredSize(new Dimension(50, 40));
            counterLabel.setPreferredSize(new Dimension(40, 40));

            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            rightPanel.setOpaque(false);
            rightPanel.add(plusButton);
            rightPanel.add(minusButton);
            rightPanel.add(counterLabel);

            rowPanel.add(mainButton, BorderLayout.CENTER);
            rowPanel.add(rightPanel, BorderLayout.EAST);

            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.CENTER; // Zeile zentrieren
            add(rowPanel, gbc);

            plusButton.addActionListener(e -> {
                if (isLoggedIn[0]) {
                    outputPanel.addArticleToReceipt(article);
                    updateCounters.run();
                }
            });
            minusButton.addActionListener(e -> {
                if (isLoggedIn[0]) {
                    outputPanel.getCurrentReceipe().removeArticle(article);
                    outputPanel.addArticleToReceipt(null); // update
                    updateCounters.run();
                }
            });
        }

        // Initial einmal aufrufen
        updateCounters.run();

        JButton resetButton = new JButton("Bezahlen");
        resetButton.setFont(buttonFont);
        resetButton.setPreferredSize(new Dimension(180, 45));
        resetButton.setBackground(new Color(200, 255, 200));
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> {
            if (isLoggedIn[0]) {
                // Kassierer direkt aus LoginPanel holen
                String cashier = loginPanel.getCurrentUser();
                outputPanel.getCurrentReceipe().setCashier(cashier);
                ReceipeManager.getInstance().addReceipe(outputPanel.getCurrentReceipe());
                outputPanel.resetReceipt();
                updateCounters.run();
            }
        });
        add(resetButton, gbc);

        JButton auswertungButton = new JButton("Auswertung");
        auswertungButton.setFont(buttonFont);
        auswertungButton.setPreferredSize(new Dimension(180, 45));
        auswertungButton.setBackground(new Color(255, 255, 200));
        auswertungButton.setFocusPainted(false);
        gbc.gridy = articles.size();
        add(auswertungButton, gbc);
        gbc.gridy = articles.size() + 1;
        add(resetButton, gbc);
        auswertungButton.setVisible(isAdmin[0]);

        // Sichtbarkeit immer aktualisieren, wenn sich der Login-Status ändert
        Timer adminCheckTimer = new Timer(300, e -> auswertungButton.setVisible(isAdmin[0]));
        adminCheckTimer.start();

        auswertungButton.addActionListener(e -> {
            if (!isAdmin[0]) return;
            ReceipeManager.getInstance().print();
        });
    }
}


