import javax.swing.*;
import java.awt.*;

public class OutputPanel extends JPanel {
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
