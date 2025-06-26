import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hallo Ubuntu!");
        JFrame frame = new JFrame("Einfaches Fenster");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1)); // 5 rows, 1 column

        // Add 5 rows with a main button and "+" and "-" buttons
        for (int i = 1; i <= 5; i++) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Row layout
            JButton mainButton = new JButton("Knopf " + i);
            JButton plusButton = new JButton("+");
            JButton minusButton = new JButton("-");
            rowPanel.add(mainButton);
            rowPanel.add(plusButton);
            rowPanel.add(minusButton);
            buttonPanel.add(rowPanel);
        }

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.WEST); // Place buttons in the left third

        // Revert to output label at the bottom-right corner
        JLabel outputLabel = new JLabel("Ausgabe");
        JPanel outputPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        outputPanel.add(outputLabel);
        mainPanel.add(outputPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
    
