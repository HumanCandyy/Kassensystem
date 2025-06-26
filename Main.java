import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hallo Ubuntu!");
        JFrame frame = new JFrame("Einfaches Fenster");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use a GridLayout to make buttons scale with the window
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1)); // 5 rows, 1 column

        // Add 5 buttons to the panel
        for (int i = 1; i <= 5; i++) {
            JButton button = new JButton("Knopf " + i);
            panel.add(button);
        }

        // Add the panel to the frame
        frame.add(panel);
        frame.setVisible(true);
    }
}
