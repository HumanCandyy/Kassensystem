import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final JLabel loggedInUserLabel;
    private final boolean[] isLoggedIn;
    private final boolean[] isAdmin;

    public LoginPanel(Font largeFont) {
        super(new FlowLayout(FlowLayout.CENTER));

        JLabel userLabel = new JLabel("Benutzer:");
        JTextField userField = new JTextField(10);
        JLabel passLabel = new JLabel("Passwort:");
        JPasswordField passField = new JPasswordField(10);
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

        isLoggedIn = new boolean[]{false};
        isAdmin = new boolean[]{false};

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
