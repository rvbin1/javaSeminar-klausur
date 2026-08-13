package utils.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.AccountModel;
import models.ChatMessageModel;
import models.ChatModel;
import utils.LocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatService {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
                                                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                                                        .create();
    private static final String FILE_SUFFIX = "-chat.json";

    public static void createChat(AccountModel chatter1, AccountModel chatter2){
        // Ein Chat wird immer in derselben JSON-Datei gespeichert, damit zwischen denselben
        // Nutzern keine Duplikate entstehen.
        String fileName = getSortedUserHash(chatter1, chatter2) + FILE_SUFFIX;
        ChatModel chatModel = new ChatModel(chatter1, chatter2);

        String json = GSON.toJson(chatModel);
        FileHandlerService.writeFile(fileName, json);
    }

    private static String getSortedUserHash(AccountModel chatter1, AccountModel chatter2)
    {
        String c1 = chatter1.getUsername().trim().toLowerCase();
        String c2 = chatter2.getUsername().trim().toLowerCase();

        // Der Name wird sortiert, damit die Dateinamen für A-B und B-A identisch bleiben.
        String combined = c1.compareTo(c2) <= 0 ? c1 + "|" + c2 : c2 + "|" + c1;

        return HashService.sha256(combined);
    }

    public static ChatModel findChat(AccountModel chatter1, AccountModel chatter2)
    {
        String json = FileHandlerService.readFile(ChatService.getSortedUserHash(chatter1, chatter2) + FILE_SUFFIX);

        if (json == null || json.isBlank()) {
            return null;
        }

        return GSON.fromJson(json, ChatModel.class);
    }

    public static void sendMessage(AccountModel sender, AccountModel receiver, String content) {
        ChatModel chat = findChat(sender, receiver);
        if (chat == null) {
            return;
        }

        // Neue Nachrichten werden an den vorhandenen Chat angehängt und anschließend persistiert.
        ChatMessageModel message = new ChatMessageModel(sender, receiver, content);
        chat.addMessage(message);

        String fileName = getSortedUserHash(sender, receiver) + FILE_SUFFIX;
        String json = GSON.toJson(chat);
        FileHandlerService.writeFile(fileName, json);
    }

    public static List<ChatModel> getChatsForUser(AccountModel account) {
        List<ChatModel> result = new ArrayList<>();
        String username = account.getUsername().trim().toLowerCase();

        List<String> allFiles = FileHandlerService.listFiles(FILE_SUFFIX);

        for (String fileName : allFiles) {
            if (!fileName.endsWith(FILE_SUFFIX)) {
                continue;
            }

            String json = FileHandlerService.readFile(fileName);
            if (json == null || json.isBlank()) {
                continue;
            }

            ChatModel chat = GSON.fromJson(json, ChatModel.class);
            if (chat == null) continue;

            String c1 = chat.getChatter1().getUsername().trim().toLowerCase();
            String c2 = chat.getChatter2().getUsername().trim().toLowerCase();

            if (c1.equals(username) || c2.equals(username)) {
                result.add(chat);
            }
        }

        return result;
    }
}
