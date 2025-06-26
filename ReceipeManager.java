import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ReceipeManager {
    private static ReceipeManager instance;
    private final List<Receipe> receipes;

    private ReceipeManager() {
        receipes = new ArrayList<>();
    }

    public static ReceipeManager getInstance() {
        if (instance == null) {
            instance = new ReceipeManager();
        }
        return instance;
    }

    public void addReceipe(Receipe receipe) {
        if (receipe != null) {
            receipes.add(receipe);
        }
    }

    public void print() {
        String filePath;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportiere Abrechnung als TXT");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Textdateien (*.txt)", "txt"));

        // Automatisch vorgeschlagener Dateiname mit aktuellem Datum und Uhrzeit
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String defaultFileName = "Abrechnung_" + dateStr + ".txt";
        fileChooser.setSelectedFile(new java.io.File(defaultFileName));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".txt")) {
                filePath += ".txt";
            }
        } else {
            return; // Abbrechen
        }

        double totalSum = 0.0;
        int count = receipes.size();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Receipe receipe : receipes) {
                writer.write("Beleg-ID: " + receipe.getID());
                writer.newLine();
                writer.write("Datum/Zeit: " + receipe.getDateTime());
                writer.newLine();
                writer.write("Kassierer: " + (receipe.getCashier() != null ? receipe.getCashier() : "Unbekannt"));
                writer.newLine();
                writer.write("Artikel:");
                writer.newLine();
                for (Object obj : receipe.getArticleList()) {
                    Article article = (Article) obj;
                    writer.write(" - " + article.getName() + ": " + String.format("%.2f", article.getPrice()) + " EUR");
                    writer.newLine();
                }
                writer.write("Gesamtpreis: " + String.format("%.2f", receipe.getTotalPrice()) + " EUR");
                writer.newLine();
                writer.write("--------------------------------------------------");
                writer.newLine();
                totalSum += receipe.getTotalPrice();
            }
            writer.write("Anzahl Belege: " + count);
            writer.newLine();
            writer.write("Gesamtsumme: " + String.format("%.2f", totalSum) + " EUR");
            writer.newLine();
        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Fehler beim Exportieren der Abrechnung:\n" + e.getMessage(), "Fehler", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
