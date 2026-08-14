package controllers;

import enums.AppConstants;
import models.AccountModel;
import models.ChatMessageModel;
import models.ChatModel;
import utils.SessionHandler;
import utils.services.ChatService;
import utils.services.UserService;
import views.MainFrameView;
import views.chat.ChatPanelView;
import views.chat.SidePanelView;
import views.login.LoginPanelView;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatPanelController {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final MainFrameView mainFrameView;
    private final SidePanelView sidePanel;
    private final ChatPanelView chatPanel;

    /**
     * Prueft zyklisch, ob der Gespraechspartner neue Nachrichten geschrieben hat.
     * Ohne diesen Timer wuerde ein Benutzer fremde Nachrichten erst sehen,
     * wenn er selbst etwas sendet.
     */
    private final Timer refreshTimer;

    private String currentPartner;

    public ChatPanelController(MainFrameView mainFrameView) {
        this.mainFrameView = mainFrameView;
        this.sidePanel = mainFrameView.getMainChatView().getSidePanel();
        this.chatPanel = mainFrameView.getMainChatView().getChatPanel();
        this.refreshTimer = new Timer(AppConstants.CHAT_REFRESH_INTERVAL_MS, e -> onRefreshTick());

        initAddChatButton();
        initContactSelection();
        initSendMessage();
        initLogoutButton();
    }

    private void initAddChatButton() {
        sidePanel.getAddChatButton().addActionListener(e -> handleAddChat());
    }

    private void initLogoutButton() {
        sidePanel.getLogoutButton().addActionListener(e -> handleLogout());
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

    /**
     * Meldet den aktuellen Benutzer ab, setzt die Oberflaeche vollstaendig zurueck
     * und kehrt zum Login zurueck. So kann in derselben Instanz die Gegenseite
     * des Chats angemeldet werden.
     */
    private void handleLogout() {
        refreshTimer.stop();
        currentPartner = null;

        sidePanel.getContactListModel().clear();
        sidePanel.getSearchField().setText("");
        updateStatusLabel();

        chatPanel.getChatArea().setText("");
        chatPanel.getContactLabel().setText(ChatPanelView.DEFAULT_CONTACT_LABEL);
        chatPanel.getMessageField().setText("");
        chatPanel.getMessageField().setEnabled(false);
        chatPanel.getSendButton().setEnabled(false);

        SessionHandler.getInstance().setCurrentAccount(null);

        // Die Zugangsdaten des Vorgaengers duerfen im Loginfenster nicht stehen bleiben.
        LoginPanelView loginPanel = mainFrameView.getLoginPanelView();
        loginPanel.getUsernameField().setText("");
        loginPanel.getPasswordField().setText("");

        mainFrameView.showLoginPanelView();
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

    /**
     * Wird vom Timer aufgerufen und holt neue Chats sowie neue Nachrichten nach.
     */
    private void onRefreshTick() {
        if (SessionHandler.getInstance().getCurrentAccount() == null) {
            refreshTimer.stop();
            return;
        }

        refreshContacts();

        if (currentPartner != null) {
            loadMessages(currentPartner);
        }
    }

    private void loadMessages(String partnerUsername) {
        String conversation = renderConversation(partnerUsername);

        // Der Text wird nur ersetzt, wenn er sich geaendert hat. Andernfalls wuerde
        // der Timer im Sekundentakt die Scrollposition zuruecksetzen.
        if (conversation.equals(chatPanel.getChatArea().getText())) {
            return;
        }

        chatPanel.getChatArea().setText(conversation);
        chatPanel.getChatArea().setCaretPosition(chatPanel.getChatArea().getDocument().getLength());
    }

    /**
     * Baut den kompletten Gespraechsverlauf als Text auf.
     *
     * @return der formatierte Verlauf, oder ein leerer Text wenn kein Chat existiert
     */
    private String renderConversation(String partnerUsername) {
        AccountModel currentAccount = SessionHandler.getInstance().getCurrentAccount();

        if (currentAccount == null) {
            return "";
        }

        ChatModel chat = ChatService.findChat(
                currentAccount,
                UserService.getAccountByUserName(partnerUsername));

        if (chat == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        List<ChatMessageModel> messages = chat.getMessages();

        for (ChatMessageModel message : messages) {
            if (message.getTimestamp() != null) {
                builder.append("[").append(message.getTimestamp().format(TIME_FORMATTER)).append("] ");
            }
            builder.append(message.getSender().getUsername())
                    .append(": ")
                    .append(message.getContent())
                    .append("\n");
        }

        return builder.toString();
    }

    private void initSendMessage() {
        Runnable send = () -> {
            // Nachrichten werden direkt in den aktuellen Chat geschrieben und anschließend neu geladen.
            if (currentPartner == null) return;
            String text = chatPanel.getMessageField().getText().trim();
            if (text.isEmpty()) return;

            AccountModel sender = SessionHandler.getInstance().getCurrentAccount();
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

    /**
     * Baut die Kontaktliste nach dem Login neu auf und startet die Aktualisierung.
     */
    public void loadContacts() {
        sidePanel.getContactListModel().clear();
        refreshContacts();
        refreshTimer.start();
    }

    /**
     * Ergaenzt fehlende Gespraechspartner, ohne die Liste zu leeren.
     * Dadurch bleibt die aktuelle Auswahl des Benutzers erhalten, auch wenn
     * waehrenddessen ein neuer Chat von der Gegenseite angelegt wurde.
     */
    private void refreshContacts() {
        AccountModel currentAccount = SessionHandler.getInstance().getCurrentAccount();

        if (currentAccount == null) {
            return;
        }

        List<ChatModel> chats = ChatService.getChatsForUser(currentAccount);

        for (ChatModel chat : chats) {
            String partner = resolvePartner(chat, currentAccount.getUsername());

            if (!isContactInList(partner)) {
                sidePanel.getContactListModel().addElement(partner);
            }
        }

        updateStatusLabel();
    }

    private String resolvePartner(ChatModel chat, String ownUsername) {
        return chat.getChatter1().getUsername().equalsIgnoreCase(ownUsername)
                ? chat.getChatter2().getUsername()
                : chat.getChatter1().getUsername();
    }
}