package se.lexicon;

import java.util.List;
import java.util.Scanner;

/** Reads user input and displays the menu. Rules stay in ContactService. */
public class ContactConsole {
    private final ContactService service;
    private final Scanner scanner;

    public ContactConsole(ContactService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void run() {
        boolean running = true;
        while (running) {
            showMenu();
            String choice = read("Choose an option: ");
            System.out.println();

            try {
                switch (choice) {
                    case "1": addContact(); break;
                    case "2": showAllContacts(); break;
                    case "3": searchByName(); break;
                    case "4": searchByPhone(); break;
                    case "5": updateContact(); break;
                    case "6": deleteContact(); break;
                    case "0":
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Choose 0-6.");
                }
            } catch (IllegalArgumentException error) {
                System.out.println("Error: " + error.getMessage());
            }
            System.out.println();
        }
    }

    private void showMenu() {
        System.out.println("===== CONTACT MANAGEMENT APP =====");
        System.out.println("1. Add a contact");
        System.out.println("2. View all contacts (A-Z)");
        System.out.println("3. Search by name");
        System.out.println("4. Search by phone");
        System.out.println("5. Update a contact");
        System.out.println("6. Delete a contact");
        System.out.println("0. Exit");
    }

    private void addContact() {
        String name = read("Name: ");
        String phone = read("Phone: ");
        String email = read("Email (optional): ");
        service.addContact(name, phone, email);
        System.out.println("Contact added successfully.");
    }

    private void showAllContacts() {
        printContacts(service.getContactsSortedByName());
    }

    private void searchByName() {
        String query = read("Enter a name or part of a name: ");
        printContacts(service.searchByName(query));
    }

    private void searchByPhone() {
        String phone = read("Enter the exact phone number: ");
        Contact contact = service.findByPhone(phone);
        System.out.println(contact == null ? "Contact not found." : contact);
    }

    private void updateContact() {
        String oldPhone = read("Phone of contact to update: ");
        Contact contact = service.findByPhone(oldPhone);
        if (contact == null) {
            System.out.println("Contact not found.");
            return;
        }

        System.out.println("Current contact: " + contact);
        System.out.println("Press Enter to keep an existing value.");
        String name = read("New name: ");
        String phone = read("New phone: ");
        String email = read("New email (type - to clear): ");

        if (name.isEmpty()) name = contact.getName();
        if (phone.isEmpty()) phone = contact.getPhone();
        if (email.isEmpty()) email = contact.getEmail();
        if (email.equals("-")) email = "";

        service.updateContact(oldPhone, name, phone, email);
        System.out.println("Contact updated successfully.");
    }

    private void deleteContact() {
        String phone = read("Phone of contact to delete: ");
        boolean deleted = service.deleteContact(phone);
        System.out.println(deleted ? "Contact deleted successfully." : "Contact not found.");
    }

    private void printContacts(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        for (Contact contact : contacts) {
            System.out.println(contact);
        }
        System.out.println("Total: " + contacts.size());
    }

    private String read(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
