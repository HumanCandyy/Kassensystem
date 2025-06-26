import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hallo Ubuntu!");
        JFrame frame = new JFrame("Einfaches Fenster");
        frame.setSize(600, 400); // Increase initial window size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel for the buttons using GridBagLayout for better scaling
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Allow components to grow in both directions
        gbc.weightx = 1.0; // Horizontal scaling
        gbc.weighty = 1.0; // Vertical scaling
        gbc.insets = new Insets(10, 10, 10, 10); // Increase padding

        // Set a larger font for all components
        Font largeFont = new Font("Arial", Font.PLAIN, 18);

        // Create labels for selected items and total sum
        JLabel selectedItemsLabel = new JLabel("Auswahl: ", SwingConstants.RIGHT);
        JLabel outputLabel = new JLabel("Gesamt: 0€", SwingConstants.RIGHT);
        selectedItemsLabel.setFont(largeFont);
        outputLabel.setFont(largeFont);

        HashMap<String, Integer> itemCounts = new HashMap<>();
        HashMap<String, Integer> itemPrices = new HashMap<>();

        // Update button labels with names and prices
        String[] buttonLabels = {"Cola - 1€", "Wasser - 1€", "Weizen - 1€", "Pils - 1€", "Fanta - 1€"};
        for (int i = 0; i < buttonLabels.length; i++) {
            String label = buttonLabels[i];
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Row layout
            JButton mainButton = new JButton(label);
            JButton plusButton = new JButton("+");
            JButton minusButton = new JButton("-");
            JLabel counterLabel = new JLabel("0"); // Counter label

            // Apply larger font to buttons and labels
            mainButton.setFont(largeFont);
            plusButton.setFont(largeFont);
            minusButton.setFont(largeFont);
            counterLabel.setFont(largeFont);

            itemCounts.put(label, 0);
            itemPrices.put(label, 1); // All items are 1€

            // Add action listeners for "+" and "-" buttons
            plusButton.addActionListener(e -> {
                int currentCount = Integer.parseInt(counterLabel.getText());
                counterLabel.setText(String.valueOf(currentCount + 1));
                itemCounts.put(label, currentCount + 1);
                updateSelectedItemsLabel(selectedItemsLabel, itemCounts);
                updateTotalSum(outputLabel, itemCounts, itemPrices);
            });
            minusButton.addActionListener(e -> {
                int currentCount = Integer.parseInt(counterLabel.getText());
                if (currentCount > 0) {
                    counterLabel.setText(String.valueOf(currentCount - 1));
                    itemCounts.put(label, currentCount - 1);
                    updateSelectedItemsLabel(selectedItemsLabel, itemCounts);
                    updateTotalSum(outputLabel, itemCounts, itemPrices);
                }
            });

            rowPanel.add(mainButton);
            rowPanel.add(plusButton);
            rowPanel.add(minusButton);
            rowPanel.add(counterLabel); // Add counter label to the row

            // Add row panel to button panel with GridBagConstraints
            gbc.gridy = i;
            buttonPanel.add(rowPanel, gbc);
        }

        // Add a Reset button below the 5 buttons
        JButton resetButton = new JButton("Bezahlen");
        resetButton.setFont(largeFont);
        resetButton.addActionListener(e -> {
            itemCounts.keySet().forEach(key -> itemCounts.put(key, 0));
            buttonPanel.getComponents();
            for (Component component : buttonPanel.getComponents()) {
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
        });

        gbc.gridy = buttonLabels.length; // Place Reset button below the last row
        buttonPanel.add(resetButton, gbc);

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.CENTER); // Center the button panel for resizing

        // Add the selected items label and output label
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(selectedItemsLabel, BorderLayout.NORTH);
        outputPanel.add(outputLabel, BorderLayout.SOUTH);
        mainPanel.add(outputPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void updateSelectedItemsLabel(JLabel label, HashMap<String, Integer> itemCounts) {
        StringBuilder text = new StringBuilder("Auswahl: ");
        itemCounts.forEach((item, count) -> {
            if (count > 0) {
                text.append(count).append("x ").append(item.split(" - ")[0]).append(", ");
            }
        });
        label.setText(text.toString().replaceAll(", $", ""));
    }

    private static void updateTotalSum(JLabel label, HashMap<String, Integer> itemCounts, HashMap<String, Integer> itemPrices) {
        int totalSum = 0;
        for (String item : itemCounts.keySet()) {
            totalSum += itemCounts.get(item) * itemPrices.get(item);
        }
        label.setText("Gesamt: " + totalSum + "€");
    }
}

