package views.chat;

import javax.swing.*;
import java.awt.*;

public class SidePanelView extends JPanel {

    private final JList<String> chatList;
    private final DefaultListModel<String> contactListModel = new DefaultListModel<>();
    private final JButton addChatButton;
    private final JButton logoutButton;
    private final JLabel statusLabel;
    private final JTextField searchField;

    public SidePanelView() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        setPreferredSize(new Dimension(200, 0));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(33, 150, 243));
        JLabel titleLabel = new JLabel("Kontakte");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        chatList = new JList<>(contactListModel);
        chatList.setName("chatList");
        chatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chatList.setBackground(new Color(255, 255, 255));
        JScrollPane scrollPane = new JScrollPane(chatList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        statusLabel = new JLabel("0 Chats");
        statusLabel.setName("statusLabel");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        bottomPanel.add(statusLabel, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));

        searchField = new JTextField();
        searchField.setName("searchField");
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        searchField.setToolTipText("Benutzername suchen");
        searchField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel searchWrapper = new JPanel(new BorderLayout());
        searchWrapper.setOpaque(false);
        searchWrapper.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        searchWrapper.add(searchField, BorderLayout.CENTER);
        actionPanel.add(searchWrapper);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        addChatButton = new JButton("Chat hinzufügen");
        addChatButton.setName("addChatButton");
        buttonPanel.add(addChatButton);
        actionPanel.add(buttonPanel);

        // Eigene Zeile, damit der Button im schmalen Seitenpanel nicht umbricht.
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.setOpaque(false);
        logoutButton = new JButton("Abmelden");
        logoutButton.setName("logoutButton");
        logoutPanel.add(logoutButton);
        actionPanel.add(logoutPanel);

        bottomPanel.add(actionPanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public JList<String> getChatList() {
        return chatList;
    }

    public DefaultListModel<String> getContactListModel() {
        return contactListModel;
    }

    public JButton getAddChatButton() {
        return addChatButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JTextField getSearchField() {
        return searchField;
    }


}