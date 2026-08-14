# Java Klausur – Chat-Anwendung

Eine kleine Java-Swing-Anwendung für lokale Benutzerregistrierung, Login und Direktnachrichten. Die Daten werden als JSON-Dateien im Projektordner gespeichert, damit die App ohne Datenbank direkt genutzt werden kann.

## Funktionen

- Registrierung neuer Benutzer (Benutzername + Passwort, Passwort wird gehasht gespeichert)
- Login mit Benutzername und Passwort
- Chat-Erstellung zwischen zwei registrierten Nutzern
- Nachrichtenfluss im Hauptfenster, inkl. automatischer Aktualisierung neuer Nachrichten (Polling alle 2 Sekunden)
- Persistenz über JSON-Dateien (`accounts.json`, `*-chat.json`)

## Projektstruktur

- `src/Main.java` – Einstiegspunkt der Swing-App
- `src/controllers/` – Login-, Registrierungs- und Chat-Controller
- `src/views/` – GUI-Komponenten (Login/Registrierung, Chat-Hauptansicht, Seitenleiste)
- `src/models/` – Datenmodelle wie `AccountModel`, `ChatModel` und `ChatMessageModel`
- `src/utils/` – `SessionHandler` (angemeldeter Benutzer) und `LocalDateTimeAdapter` (JSON-Zeitformat)
- `src/utils/services/` – Services für Authentifizierung, Datei-Handling und Chats
- `src/enums/` – zentrale Konstanten und Statuswerte

## Wichtige Mechanismen

- `AccountModel` speichert Benutzer und hasht Passwörter beim Setzen (`PasswordHashService`, SHA-256).
- `LoginAccountService` prüft den Login gegen die gespeicherten Accounts und liefert einen typisierten `LoginStatus` zurück.
- `ChatService` erzeugt und lädt Chats über einen deterministischen Schlüssel (sortierter, gehashter Benutzernamen-Verbund), damit dasselbe Nutzerpaar immer dieselbe Datei teilt – unabhängig davon, wer den Chat zuerst angelegt hat.
- `FileHandlerService` liest und schreibt die JSON-Daten auf der Festplatte und kapselt alle Datei-I/O-Fehler.
- `SessionHandler` hält den aktuell angemeldeten Benutzer im Speicher (Singleton, gültig für die laufende Anwendung).
- `ChatPanelController` pollt per `javax.swing.Timer` alle 2 Sekunden neue Kontakte/Nachrichten, damit ein zweiter, lokal angemeldeter Benutzer neue Nachrichten sieht, ohne selbst aktiv etwas zu senden.

## Voraussetzung

Java JDK 17 oder höher sowie die mitgelieferte Gson-Bibliothek (`deps/gson-2.11.0.jar`).

Beim ersten Start werden die benötigten JSON-Dateien automatisch erzeugt bzw. beim Registrieren/Chatten ergänzt.

## Beispiel-Interaktion

Da es sich um einen **lokalen** Chat handelt, lässt sich das Zwei-Personen-Szenario testen, indem die Anwendung zweimal gestartet wird (zwei separate `java`-Prozesse, siehe oben).

1. **Registrierung:** Im ersten Fenster auf "Registrieren" klicken, Benutzername `alice` und Passwort `geheim1` eingeben, "Registrieren" klicken. Alice landet direkt im Chat-Hauptfenster; `accounts.json` enthält jetzt ihren Account mit gehashtem Passwort.
2. **Zweiter Benutzer:** Im zweiten Fenster ebenso einen Benutzer `bob` mit Passwort `geheim2` registrieren.
3. **Chat starten:** Alice trägt im Suchfeld der Seitenleiste `bob` ein und klickt "Chat hinzufügen". Bob erscheint in Alices Kontaktliste; im Hintergrund wird die Datei `<hash>-chat.json` angelegt.
4. **Nachricht senden:** Alice wählt `bob` aus der Kontaktliste, tippt `Hallo Bob!` in das Nachrichtenfeld und klickt "Senden" (oder drückt Enter). Die Nachricht erscheint sofort als

   ```
   [14:32] alice: Hallo Bob!
   ```

   in Alices Chatverlauf.
5. **Empfangen:** Innerhalb von 2 Sekunden erscheint `bob` automatisch in Bobs Kontaktliste (falls er dort noch nicht stand). Öffnet Bob den Chat mit `alice`, sieht er dieselbe Nachricht und kann direkt antworten, z. B. `Hallo Alice, alles klar?`.
6. **Persistenz prüfen:** Nach Beenden beider Fenster bleibt der Verlauf in der `*-chat.json`-Datei erhalten und wird beim nächsten Login wieder geladen.

