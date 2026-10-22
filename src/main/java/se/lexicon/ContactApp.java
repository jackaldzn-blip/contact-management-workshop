package se.lexicon;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContactApp {

    // One contact holds three pieces of information.
    static class Contact {
        String name;
        String phone;
        String email;

        Contact(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }

        @Override
        public String toString() {
            return name + " | " + phone + " | " + email;
        }
    }

    // Contacts are stored in memory while the program is running.
    static List<Contact> contacts = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            showMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": addContact(); break;
                case "2": viewContacts(); break;
                case "3": searchContacts(); break;
                case "4": updateContact(); break;
                case "5": deleteContact(); break;
                case "6": sortContacts(); break; // Optional challenge
                case "0": running = false; break;
                default: System.out.println("Please choose a valid option.");
            }
        }

        System.out.println("Goodbye!");
    }

    static void showMenu() {
        System.out.println("\n=== CONTACT MANAGEMENT ===");
        System.out.println("1. Add contact");
        System.out.println("2. View contacts");
        System.out.println("3. Search contacts");
        System.out.println("4. Update contact");
        System.out.println("5. Delete contact");
        System.out.println("6. Sort A-Z (challenge)");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    // Add a contact.
    static void addContact() {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Email (optional): ");
        String email = scanner.nextLine().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            System.out.println("Name and phone are required.");
            return;
        }
        if (findByPhone(phone) != null) {
            System.out.println("That phone number already exists.");
            return;
        }
        contacts.add(new Contact(name, phone, email));
        System.out.println("Contact added!");
    }

    // View and search contacts.
    static void viewContacts() {
        printContacts(contacts);
    }

    static void searchContacts() {
        System.out.print("Search by name or phone: ");
        String query = scanner.nextLine().trim().toLowerCase();
        List<Contact> matches = new ArrayList<>();

        for (Contact contact : contacts) {
            if (contact.name.toLowerCase().contains(query) || contact.phone.contains(query)) {
                matches.add(contact);
            }
        }
        printContacts(matches);
    }

    // Update and delete contacts.
    static void updateContact() {
        System.out.print("Phone number of contact to update: ");
        Contact contact = findByPhone(scanner.nextLine().trim());
        if (contact == null) {
            System.out.println("Contact not found.");
            return;
        }

        System.out.print("New name (Enter to keep): ");
        String name = scanner.nextLine().trim();
        System.out.print("New phone (Enter to keep): ");
        String phone = scanner.nextLine().trim();
        System.out.print("New email (Enter to keep): ");
        String email = scanner.nextLine().trim();

        if (!phone.isEmpty() && !phone.equals(contact.phone) && findByPhone(phone) != null) {
            System.out.println("That phone number already exists.");
            return;
        }
        if (!name.isEmpty()) contact.name = name;
        if (!phone.isEmpty()) contact.phone = phone;
        if (!email.isEmpty()) contact.email = email;
        System.out.println("Contact updated!");
    }

    static void deleteContact() {
        System.out.print("Phone number to delete: ");
        Contact contact = findByPhone(scanner.nextLine().trim());
        if (contact == null) {
            System.out.println("Contact not found.");
            return;
        }
        contacts.remove(contact);
        System.out.println("Contact deleted!");
    }

    // Optional challenge: sort without changing the original list.
    static void sortContacts() {
        List<Contact> sorted = new ArrayList<>(contacts);
        sorted.sort((first, second) -> first.name.compareToIgnoreCase(second.name));
        printContacts(sorted);
    }

    // Helpers shared by different menu options.
    static Contact findByPhone(String phone) {
        for (Contact contact : contacts) {
            if (contact.phone.equals(phone)) return contact;
        }
        return null;
    }

    static void printContacts(List<Contact> list) {
        if (list.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        for (Contact contact : list) {
            System.out.println(contact);
        }
    }
}
