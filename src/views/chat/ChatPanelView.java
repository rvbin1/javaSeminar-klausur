package views.chat;

import javax.swing.*;
import java.awt.*;

public class ChatPanelView extends JPanel {

    private final JLabel contactLabel;
    private final JTextArea chatArea;
    private final JTextField messageField;
    private final JButton sendButton;

    public ChatPanelView() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 255));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(33, 150, 243));
        contactLabel = new JLabel("Wählen Sie einen Kontakt aus");
        contactLabel.setName("contactLabel");
        contactLabel.setForeground(Color.WHITE);
        contactLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        topPanel.add(contactLabel);
        add(topPanel, BorderLayout.NORTH);

        chatArea = new JTextArea();
        chatArea.setName("chatArea");
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        chatArea.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(255, 255, 255));
        bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        messageField = new JTextField();
        messageField.setName("messageField");
        messageField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        messageField.setEnabled(false);
        bottomPanel.add(messageField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        sendButton = new JButton("Senden");
        sendButton.setName("sendButton");
        sendButton.setEnabled(false);
        buttonPanel.add(sendButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public JTextArea getChatArea() {
        return chatArea;
    }

    public JTextField getMessageField() {
        return messageField;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public JLabel getContactLabel() {
        return contactLabel;
    }
}