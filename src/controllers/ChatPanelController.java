package controllers;

import models.AccountModel;
import models.ChatMessageModel;
import models.ChatModel;
import utils.services.ChatService;
import utils.SessionHandler;
import utils.services.UserService;
import views.MainFrameView;
import views.chat.ChatPanelView;
import views.chat.SidePanelView;

import javax.swing.JOptionPane;
import java.util.List;

public class ChatPanelController {
    private final MainFrameView mainFrameView;
    private final SidePanelView sidePanel;
    private final ChatPanelView chatPanel;
    private String currentPartner;

    public ChatPanelController(MainFrameView mainFrameView) {
        this.mainFrameView = mainFrameView;
        this.sidePanel = mainFrameView.getMainChatView().getSidePanel();
        this.chatPanel = mainFrameView.getMainChatView().getChatPanel();

        initAddChatButton();
        initContactSelection();
        initSendMessage();
    }

    private void initAddChatButton() {
        sidePanel.getAddChatButton().addActionListener(e -> handleAddChat());
    }

    /**
     * Legt einen Chat mit dem im Suchfeld eingetragenen Benutzer an.
     * Jede Eingabe wird vorher geprueft, damit kein unbekannter Benutzer
     * an den ChatService weitergereicht wird.
     */
    private void handleAddChat() {
        String partnerName = sidePanel.getSearchField().getText().trim();

        if (partnerName.isEmpty()) {
            showError("Bitte gib einen Benutzernamen ein.");
            return;
        }

        AccountModel currentAccount = SessionHandler.getInstance().getCurrentAccount();

        if (partnerName.equalsIgnoreCase(currentAccount.getUsername())) {
            showError("Du kannst keinen Chat mit dir selbst starten.");
            return;
        }

        AccountModel partner = UserService.getAccountByUserName(partnerName);

        if (partner == null) {
            showError("Der Benutzer \"" + partnerName + "\" existiert nicht.");
            return;
        }

        if (isContactInList(partner.getUsername())) {
            showError("Mit \"" + partner.getUsername() + "\" besteht bereits ein Chat.");
            sidePanel.getSearchField().setText("");
            return;
        }

        // createChat liefert false, wenn bereits eine Chatdatei existiert.
        // Der bestehende Verlauf bleibt dann erhalten und wird nur nachgeladen.
        ChatService.createChat(currentAccount, partner);

        if (ChatService.findChat(currentAccount, partner) == null) {
            showError("Der Chat konnte nicht gespeichert werden.");
            return;
        }

        sidePanel.getContactListModel().addElement(partner.getUsername());
        updateStatusLabel();
        sidePanel.getSearchField().setText("");
    }

    private boolean isContactInList(String username) {
        for (int i = 0; i < sidePanel.getContactListModel().getSize(); i++) {
            if (sidePanel.getContactListModel().get(i).equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    private void updateStatusLabel() {
        sidePanel.getStatusLabel().setText(sidePanel.getContactListModel().getSize() + " Chats");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(mainFrameView, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    private void initContactSelection() {
        sidePanel.getChatList().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            String selected = sidePanel.getChatList().getSelectedValue();
            if (selected == null) return;

            openChat(selected);
        });
    }

    private void openChat(String partnerUsername) {
        currentPartner = partnerUsername;

        chatPanel.getContactLabel().setText(partnerUsername);
        chatPanel.getMessageField().setEnabled(true);
        chatPanel.getSendButton().setEnabled(true);

        loadMessages(partnerUsername);
    }

    private void loadMessages(String partnerUsername) {
        String me = SessionHandler.getInstance().getCurrentAccount().getUsername();
        ChatModel chat = ChatService.findChat(
                UserService.getAccountByUserName(me),
                UserService.getAccountByUserName(partnerUsername));

        chatPanel.getChatArea().setText("");
        if (chat == null) return;

        List<ChatMessageModel> messages = chat.getMessages();
        for (ChatMessageModel m : messages) {
            chatPanel.getChatArea().append(m.getSender().getUsername() + ": " + m.getContent() + "\n");
        }
    }

    private void initSendMessage() {
        Runnable send = () -> {
            // Nachrichten werden direkt in den aktuellen Chat geschrieben und anschließend neu geladen.
            if (currentPartner == null) return;
            String text = chatPanel.getMessageField().getText().trim();
            if (text.isEmpty()) return;

            String me = SessionHandler.getInstance().getCurrentAccount().getUsername();
            AccountModel sender = UserService.getAccountByUserName(me);
            AccountModel receiver = UserService.getAccountByUserName(currentPartner);

            if (!ChatService.sendMessage(sender, receiver, text)) {
                showError("Die Nachricht konnte nicht gesendet werden.");
                return;
            }

            chatPanel.getMessageField().setText("");
            loadMessages(currentPartner);
        };

        chatPanel.getSendButton().addActionListener(e -> send.run());
        chatPanel.getMessageField().addActionListener(e -> send.run());
    }

    public void loadContacts() {
        // Die Kontaktliste wird aus den vorhandenen Chats des eingeloggten Nutzers aufgebaut.
        sidePanel.getContactListModel().clear();

        String me = SessionHandler.getInstance().getCurrentAccount().getUsername();
        AccountModel myAccount = UserService.getAccountByUserName(me);

        List<ChatModel> chats = ChatService.getChatsForUser(myAccount);

        for (ChatModel chat : chats) {
            String partner = chat.getChatter1().getUsername().equalsIgnoreCase(me)
                    ? chat.getChatter2().getUsername()
                    : chat.getChatter1().getUsername();

            sidePanel.getContactListModel().addElement(partner);
        }

        updateStatusLabel();
    }
}