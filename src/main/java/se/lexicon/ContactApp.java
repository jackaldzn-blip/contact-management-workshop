package se.lexicon;

import java.util.List;
import java.util.Scanner;

public class ContactApp {
    private static final Scanner SCANNER = new Scanner(System.in);
    private final ContactService service = new ContactService(new ContactRepository());

    public static void main(String[] args) {
        new ContactApp().runMenu();
    }

    private void runMenu() {
        System.out.println("=== CONTACT MANAGEMENT ===");
        while (true) {
            printMenu();
            String choice = readLine("Choose: ");
            if (choice == null || choice.equals("0")) {
                System.out.println("Goodbye!");
                return;
            }
            switch (choice) {
                case "1" -> viewContacts(service.list());
                case "2" -> addContact();
                case "3" -> searchContacts();
                case "4" -> updateContact();
                case "5" -> deleteContact();
                default -> System.out.println("Please choose a valid option.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n1. View contacts");
        System.out.println("2. Add contact");
        System.out.println("3. Search contacts");
        System.out.println("4. Update contact");
        System.out.println("5. Delete contact");
        System.out.println("0. Exit");
    }

    private void viewContacts(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        System.out.printf("%-5s %-20s %-18s %s%n", "ID", "NAME", "PHONE", "EMAIL");
        System.out.println("-".repeat(65));
        contacts.forEach(System.out::println);
    }

    private void addContact() {
        String first = readLine("First name: ");
        String last = readLine("Last name: ");
        String phone = readLine("Phone: ");
        String email = readLine("Email (optional): ");
        if (first == null || last == null || phone == null || email == null) return;
        printResult(service.add(first, last, phone, email));
    }

    private void searchContacts() {
        String query = readLine("Search name or phone: ");
        if (query != null) viewContacts(service.search(query));
    }

    private void updateContact() {
        Integer id = promptId("Contact id to update: ");
        if (id == null) return;
        var match = service.find(id);
        if (match.isEmpty()) {
            System.out.println("No contact with id " + id);
            return;
        }
        Contact current = match.get();
        System.out.println("Leave a field empty to keep its current value.");
        String first = keepIfBlank("First name [" + current.getFirstName() + "]: ", current.getFirstName());
        String last = keepIfBlank("Last name [" + current.getLastName() + "]: ", current.getLastName());
        String phone = keepIfBlank("Phone [" + current.getPhone() + "]: ", current.getPhone());
        String email = keepIfBlank("Email [" + current.getEmail() + "]: ", current.getEmail());
        printResult(service.update(id, first, last, phone, email));
    }

    private void deleteContact() {
        Integer id = promptId("Contact id to delete: ");
        if (id == null) return;
        if (service.find(id).isEmpty()) {
            System.out.println("No contact with id " + id);
            return;
        }
        String answer = readLine("Delete contact #" + id + "? Type yes to confirm: ");
        if (!"yes".equalsIgnoreCase(answer)) {
            System.out.println("Delete cancelled.");
            return;
        }
        printResult(service.delete(id));
    }

    private Integer promptId(String prompt) {
        while (true) {
            String input = readLine(prompt);
            if (input == null) return null;
            try {
                int id = Integer.parseInt(input);
                if (id > 0) return id;
            } catch (NumberFormatException ignored) {
                // Ask again instead of crashing on letters or a huge number.
            }
            System.out.println("Please enter a valid positive ID number.");
        }
    }

    private String keepIfBlank(String prompt, String current) {
        String value = readLine(prompt);
        return value == null || value.isEmpty() ? current : value;
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.hasNextLine() ? SCANNER.nextLine().trim() : null;
    }

    private void printResult(ContactService.ValidationResult result) {
        System.out.println((result.ok() ? "OK: " : "Error: ") + result.message());
    }
}
