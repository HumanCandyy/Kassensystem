import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hallo Ubuntu!");
        JFrame frame = new JFrame("Einfaches Fenster");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1)); // 5 rows, 1 column

        // Create labels for selected items and total sum
        JLabel selectedItemsLabel = new JLabel("Auswahl: ");
        JLabel outputLabel = new JLabel("Gesamt: 0€");
        HashMap<String, Integer> itemCounts = new HashMap<>();
        HashMap<String, Integer> itemPrices = new HashMap<>();

        // Update button labels with names and prices
        String[] buttonLabels = {"Cola - 1€", "Wasser - 1€", "Weizen - 1€", "Pils - 1€", "Fanta - 1€"};
        for (String label : buttonLabels) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Row layout
            JButton mainButton = new JButton(label);
            JButton plusButton = new JButton("+");
            JButton minusButton = new JButton("-");
            JLabel counterLabel = new JLabel("0"); // Counter label

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
            buttonPanel.add(rowPanel);
        }

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.WEST); // Place buttons in the left third

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

