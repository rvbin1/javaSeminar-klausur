package utils.services;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileHandlerService {

    public static void writeFile(String fileName, String json) {
        // Die App speichert Accounts und Chats als JSON-Dateien im aktuellen Arbeitsverzeichnis.
        try {
            Files.writeString(
                    Path.of(fileName),
                    json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben von " + fileName + ": " + e.getMessage());
        }
    }

    public static String readFile(String fileName) {
        Path path = Path.of(fileName);
        if (!Files.exists(path)) {
            return null;
        }
        try {
            return Files.readString(path);
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen von " + fileName + ": " + e.getMessage());
            return null;
        }
    }

    public static List<String> listFiles(String suffix) {
        List<String> result = new ArrayList<>();
        Path dir = Path.of(".");

        // "*" + suffix ist ein Glob-Pattern (kein regulaerer Ausdruck): es liefert nur Dateien,
        // deren Name auf den uebergebenen Suffix endet, z. B. alle "*-chat.json"-Dateien.
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*" + suffix)) {
            for (Path path : stream) {
                result.add(path.getFileName().toString());
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Auflisten der Dateien: " + e.getMessage());
        }

        return result;
    }
}