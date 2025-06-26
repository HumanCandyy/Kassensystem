import java.awt.*;
import java.util.HashMap;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hallo Ubuntu!");
        JFrame frame = new JFrame("Einfaches Fenster");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font largeFont = new Font("Arial", Font.PLAIN, 18);

        LoginPanel loginPanel = new LoginPanel(largeFont);
        OutputPanel outputPanel = new OutputPanel(largeFont);
        ButtonPanel buttonPanel = new ButtonPanel(largeFont, outputPanel.getSelectedItemsLabel(), outputPanel.getOutputLabel(), loginPanel.getIsLoggedIn());

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
    public ButtonPanel(Font largeFont, JLabel selectedItemsLabel, JLabel outputLabel, boolean[] isLoggedIn) {
        super(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        String[] buttonLabels = {"Cola - 1€", "Wasser - 1€", "Weizen - 1€", "Pils - 1€", "Fanta - 1€"};
        HashMap<String, Integer> itemCounts = new HashMap<>();
        HashMap<String, Integer> itemPrices = new HashMap<>();

        for (int i = 0; i < buttonLabels.length; i++) {
            String label = buttonLabels[i];
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton mainButton = new JButton(label);
            JButton plusButton = new JButton("+");
            JButton minusButton = new JButton("-");
            JLabel counterLabel = new JLabel("0");

            mainButton.setFont(largeFont);
            plusButton.setFont(largeFont);
            minusButton.setFont(largeFont);
            counterLabel.setFont(largeFont);

            itemCounts.put(label, 0);
            itemPrices.put(label, 1);

            plusButton.addActionListener(e -> {
                if (isLoggedIn[0]) {
                    int currentCount = Integer.parseInt(counterLabel.getText());
                    counterLabel.setText(String.valueOf(currentCount + 1));
                    itemCounts.put(label, currentCount + 1);
                    updateSelectedItemsLabel(selectedItemsLabel, itemCounts);
                    updateTotalSum(outputLabel, itemCounts, itemPrices);
                }
            });
            minusButton.addActionListener(e -> {
                if (isLoggedIn[0]) {
                    int currentCount = Integer.parseInt(counterLabel.getText());
                    if (currentCount > 0) {
                        counterLabel.setText(String.valueOf(currentCount - 1));
                        itemCounts.put(label, currentCount - 1);
                        updateSelectedItemsLabel(selectedItemsLabel, itemCounts);
                        updateTotalSum(outputLabel, itemCounts, itemPrices);
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
                itemCounts.keySet().forEach(key -> itemCounts.put(key, 0));
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
                updateTotalSum(outputLabel, itemCounts, itemPrices);
            }
        });

        gbc.gridy = buttonLabels.length;
        add(resetButton, gbc);
    }

    private void updateSelectedItemsLabel(JLabel label, HashMap<String, Integer> itemCounts) {
        StringBuilder text = new StringBuilder("Auswahl: ");
        itemCounts.forEach((item, count) -> {
            if (count > 0) {
                text.append(count).append("x ").append(item.split(" - ")[0]).append(", ");
            }
        });
        label.setText(text.toString().replaceAll(", $", ""));
    }

    private void updateTotalSum(JLabel label, HashMap<String, Integer> itemCounts, HashMap<String, Integer> itemPrices) {
        int totalSum = 0;
        for (String item : itemCounts.keySet()) {
            totalSum += itemCounts.get(item) * itemPrices.get(item);
        }
        label.setText("Gesamt: " + totalSum + "€");
    }
}