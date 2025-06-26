import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ButtonPanel extends JPanel {
    private final HashMap<String, Integer> itemCounts;
    private final HashMap<String, Integer> itemPrices;

    public ButtonPanel(Font largeFont, JLabel selectedItemsLabel, JLabel outputLabel, boolean[] isLoggedIn) {
        super(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        itemCounts = new HashMap<>();
        itemPrices = new HashMap<>();

        String[] buttonLabels = {"Cola - 1€", "Wasser - 1€", "Weizen - 1€", "Pils - 1€", "Fanta - 1€"};
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
                    updateSelectedItemsLabel(selectedItemsLabel);
                    updateTotalSum(outputLabel);
                }
            });

            minusButton.addActionListener(e -> {
                if (isLoggedIn[0]) {
                    int currentCount = Integer.parseInt(counterLabel.getText());
                    if (currentCount > 0) {
                        counterLabel.setText(String.valueOf(currentCount - 1));
                        itemCounts.put(label, currentCount - 1);
                        updateSelectedItemsLabel(selectedItemsLabel);
                        updateTotalSum(outputLabel);
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
                updateSelectedItemsLabel(selectedItemsLabel);
                updateTotalSum(outputLabel);
            }
        });

        gbc.gridy = buttonLabels.length;
        add(resetButton, gbc);
    }

    private void updateSelectedItemsLabel(JLabel label) {
        StringBuilder text = new StringBuilder("Auswahl: ");
        itemCounts.forEach((item, count) -> {
            if (count > 0) {
                text.append(count).append("x ").append(item.split(" - ")[0]).append(", ");
            }
        });
        label.setText(text.toString().replaceAll(", $", ""));
    }

    private void updateTotalSum(JLabel label) {
        int totalSum = 0;
        for (String item : itemCounts.keySet()) {
            totalSum += itemCounts.get(item) * itemPrices.get(item);
        }
        label.setText("Gesamt: " + totalSum + "€");
    }
}
