package se.lexicon;

import java.util.List;

/** Applies the rules for adding, searching, editing and removing contacts. */
public class ContactService {
    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public void addContact(String name, String phone, String email) {
        String cleanName = requireName(name);
        String cleanPhone = requirePhone(phone);
        String cleanEmail = requireEmail(email);

        if (repository.findByPhone(cleanPhone) != null) {
            throw new IllegalArgumentException("A contact with this phone number already exists.");
        }
        repository.add(new Contact(cleanName, cleanPhone, cleanEmail));
    }

    public List<Contact> getAllContacts() {
        return repository.findAll();
    }

    /** Challenge 1: return an A-Z sorted COPY; keep original storage order unchanged. */
    public List<Contact> getContactsSortedByName() {
        List<Contact> sortedContacts = repository.findAll();
        sortedContacts.sort((first, second) ->
                first.getName().compareToIgnoreCase(second.getName()));
        return sortedContacts;
    }

    public List<Contact> searchByName(String namePart) {
        if (namePart == null || namePart.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter a name to search for.");
        }
        return repository.searchByName(namePart.trim());
    }

    public Contact findByPhone(String phone) {
        return repository.findByPhone(phone == null ? "" : phone.trim());
    }

    public boolean updateContact(String currentPhone, String name, String phone, String email) {
        Contact contact = findByPhone(currentPhone);
        if (contact == null) {
            return false;
        }

        String cleanName = requireName(name);
        String cleanPhone = requirePhone(phone);
        String cleanEmail = requireEmail(email);

        Contact phoneOwner = repository.findByPhone(cleanPhone);
        if (phoneOwner != null && phoneOwner != contact) {
            throw new IllegalArgumentException("Another contact already uses this phone number.");
        }

        contact.setName(cleanName);
        contact.setPhone(cleanPhone);
        contact.setEmail(cleanEmail);
        return true;
    }

    public boolean deleteContact(String phone) {
        return repository.deleteByPhone(phone == null ? "" : phone.trim());
    }

    private String requireName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        return name.trim();
    }

    private String requirePhone(String phone) {
        String cleanPhone = phone == null ? "" : phone.trim();
        // Optional leading + followed by 7 to 15 digits. Leading zeroes are preserved.
        if (!cleanPhone.matches("\\+?[0-9]{7,15}")) {
            throw new IllegalArgumentException("Phone must contain 7-15 digits (optional leading +).");
        }
        return cleanPhone;
    }

    private String requireEmail(String email) {
        String cleanEmail = email == null ? "" : email.trim();
        // Email is optional; this is a deliberately simple format check.
        if (!cleanEmail.isEmpty() && !cleanEmail.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            throw new IllegalArgumentException("Enter a valid email address or leave it empty.");
        }
        return cleanEmail;
    }
}
