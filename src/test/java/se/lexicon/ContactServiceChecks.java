package se.lexicon;

import java.util.List;

/** Basic automated checks using standard Java; this is NOT the optional JUnit 5 challenge. */
public class ContactServiceChecks {
    public static void main(String[] args) {
        ContactService service = new ContactService(new ContactRepository());

        service.addContact("Anna Andersson", "0701234567", "anna@example.com");
        service.addContact("Omar Ali", "0709876543", "");
        check(service.getAllContacts().size() == 2, "add/list");

        List<Contact> annaMatches = service.searchByName("aNnA");
        check(annaMatches.size() == 1, "case-insensitive search");
        check(service.findByPhone("0701234567") != null, "find by phone");
        check(service.findByPhone("0000000") == null, "missing phone");

        expectInvalid(() -> service.addContact("Duplicate", "0701234567", ""), "duplicate phone");
        expectInvalid(() -> service.addContact("   ", "0705555555", ""), "empty name");
        expectInvalid(() -> service.addContact("Bad phone", "abc", ""), "invalid phone");
        expectInvalid(() -> service.addContact("Bad email", "0705555555", "not-an-email"), "invalid email");
        expectInvalid(() -> service.searchByName("  "), "blank search");

        check(service.updateContact("0701234567", "Anna A.", "0701111111", "new@example.com"), "update");
        check(service.findByPhone("0701234567") == null, "old phone removed after update");
        check(service.findByPhone("0701111111").getName().equals("Anna A."), "new phone/name");
        expectInvalid(() -> service.updateContact("0701111111", "Anna", "0709876543", ""), "duplicate on update");
        check(!service.updateContact("0000000", "Missing", "0702222222", ""), "update missing");

        check(service.deleteContact("0701111111"), "delete present");
        check(!service.deleteContact("0701111111"), "delete missing");
        check(service.getAllContacts().size() == 1, "final count");
        // Challenge 1: alphabetical ordering, ignoring letter case.
        ContactService sortService = new ContactService(new ContactRepository());
        check(sortService.getContactsSortedByName().isEmpty(), "sort empty contacts");
        sortService.addContact("Zara", "0701111111", "");
        sortService.addContact("anna", "0702222222", "");
        sortService.addContact("Omar", "0703333333", "");
        List<Contact> sorted = sortService.getContactsSortedByName();
        check(sorted.get(0).getName().equals("anna"), "sort places anna first");
        check(sorted.get(1).getName().equals("Omar")
                && sorted.get(2).getName().equals("Zara"), "sort names A to Z");
        check(sortService.getAllContacts().get(0).getName().equals("Zara"),
                "sort does not change original storage order");
        System.out.println("All 17 core checks and 4 sorting checks passed.");
    }

    private static void check(boolean condition, String description) {
        if (!condition) {
            throw new AssertionError("FAILED: " + description);
        }
    }

    private static void expectInvalid(Runnable task, String description) {
        try {
            task.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("FAILED (expected an error): " + description);
    }
}
