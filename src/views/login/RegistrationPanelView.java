package views.login;

import javax.swing.*;
import java.awt.*;

public class RegistrationPanelView extends JPanel{
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton cancelButton;
    private final JButton registerButton;

    public RegistrationPanelView() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel usernameLabel = new JLabel("Benutzername:");
        usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Passwort:");
        passwordField = new JPasswordField(20);

        cancelButton = new JButton("Abbrechen");
        registerButton = new JButton("Registrieren");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(cancelButton);
        buttonPanel.add(registerButton);
        add(buttonPanel, gbc);
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }
}


