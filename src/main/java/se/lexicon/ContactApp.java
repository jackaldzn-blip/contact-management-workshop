package se.lexicon;

import java.util.Scanner;

/** Workshop entry point. Run this class to start the application. */
public class ContactApp {
    public static void main(String[] args) {
        ContactRepository repository = new ContactRepository();
        ContactService service = new ContactService(repository);

        try (Scanner scanner = new Scanner(System.in)) {
            ContactConsole console = new ContactConsole(service, scanner);
            console.run();
        }
    }
}
