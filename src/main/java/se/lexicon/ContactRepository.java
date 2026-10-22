package se.lexicon;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
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

    // Challenge 1: sort a copy, keeping the stored order unchanged.
    public List<Contact> findAllSorted() {
        List<Contact> sorted = findAll();
        sorted.sort(Comparator.comparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Contact::getLastName, String.CASE_INSENSITIVE_ORDER));
        return sorted;
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

    // Challenge 3: one contact per line: id|firstName|lastName|phone|email.
    public void saveToFile(Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Contact c : contacts.values()) {
            lines.add(c.getId() + "|" + c.getFirstName() + "|" + c.getLastName()
                    + "|" + c.getPhone() + "|" + c.getEmail());
        }
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    public void loadFromFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            return; // First run: start with an empty collection.
        }
        Map<Integer, Contact> loaded = new LinkedHashMap<>();
        int largestId = 0;
        int lineNumber = 0;
        for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            lineNumber++;
            String[] parts = line.split("\\|", -1);
            if (parts.length != 5) {
                throw new IOException("Invalid contact at line " + lineNumber);
            }
            try {
                int id = Integer.parseInt(parts[0]);
                if (id <= 0 || id == Integer.MAX_VALUE || loaded.containsKey(id)
                        || parts[1].isBlank() || parts[2].isBlank() || parts[3].isBlank()) {
                    throw new IllegalArgumentException("Invalid contact");
                }
                for (Contact previous : loaded.values()) {
                    if (previous.getPhone().equals(parts[3])
                            || (!parts[4].isBlank() && previous.getEmail().equalsIgnoreCase(parts[4]))) {
                        throw new IllegalArgumentException("Duplicate contact");
                    }
                }
                loaded.put(id, new Contact(id, parts[1], parts[2], parts[3], parts[4]));
                largestId = Math.max(largestId, id);
            } catch (IllegalArgumentException ex) {
                throw new IOException("Invalid contact at line " + lineNumber, ex);
            }
        }
        contacts.clear();
        contacts.putAll(loaded);
        nextId = largestId + 1;
    }
}
