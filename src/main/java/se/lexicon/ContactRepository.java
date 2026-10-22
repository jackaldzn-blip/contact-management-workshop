package se.lexicon;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class ContactRepository {
    public static final int NO_EXCLUDED_ID = -1;

    private final Map<Integer, Contact> contacts = new LinkedHashMap<>();
    private int nextId = 1;

    public Contact add(String firstName, String lastName, String phone, String email) {
        if (phoneExists(phone, NO_EXCLUDED_ID) || emailExists(email, NO_EXCLUDED_ID)) {
            return null;
        }
        Contact contact = new Contact(nextId++, firstName, lastName, phone, email);
        contacts.put(contact.getId(), contact);
        return contact;
    }

    public List<Contact> findAll() {
        return new ArrayList<>(contacts.values());
    }

    public Optional<Contact> findById(int id) {
        return Optional.ofNullable(contacts.get(id));
    }

    public List<Contact> search(String query) {
        String term = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        List<Contact> matches = new ArrayList<>();
        for (Contact c : contacts.values()) {
            if (c.getFirstName().toLowerCase(Locale.ROOT).contains(term)
                    || c.getLastName().toLowerCase(Locale.ROOT).contains(term)
                    || c.getPhone().contains(term)) {
                matches.add(c);
            }
        }
        return matches;
    }

    public Optional<Contact> update(int id, String firstName, String lastName, String phone, String email) {
        if (!contacts.containsKey(id) || phoneExists(phone, id) || emailExists(email, id)) {
            return Optional.empty();
        }
        Contact updated = new Contact(id, firstName, lastName, phone, email);
        contacts.put(id, updated);
        return Optional.of(updated);
    }

    public boolean delete(int id) {
        return contacts.remove(id) != null;
    }

    public boolean phoneExists(String phone, int excludeId) {
        return contacts.values().stream().anyMatch(c ->
                c.getId() != excludeId && c.getPhone().equals(phone));
    }

    public boolean emailExists(String email, int excludeId) {
        return email != null && !email.isBlank() && contacts.values().stream().anyMatch(c ->
                c.getId() != excludeId && c.getEmail().equalsIgnoreCase(email));
    }
}
