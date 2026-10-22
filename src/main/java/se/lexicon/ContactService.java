package se.lexicon;

import java.util.List;
import java.util.Optional;

// Keeps simple validation out of the console menu.
public class ContactService {
    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public ValidationResult add(String first, String last, String phone, String email) {
        first = trim(first);
        last = trim(last);
        phone = trim(phone);
        email = trim(email);
        String problem = validate(first, last, phone, email, ContactRepository.NO_EXCLUDED_ID);
        if (problem != null) return new ValidationResult(false, problem);
        Contact saved = repository.add(first, last, phone, email);
        return new ValidationResult(true, "Contact added with id " + saved.getId());
    }

    public ValidationResult update(int id, String first, String last, String phone, String email) {
        if (repository.findById(id).isEmpty()) {
            return new ValidationResult(false, "No contact with id " + id);
        }
        first = trim(first);
        last = trim(last);
        phone = trim(phone);
        email = trim(email);
        String problem = validate(first, last, phone, email, id);
        if (problem != null) return new ValidationResult(false, problem);
        repository.update(id, first, last, phone, email);
        return new ValidationResult(true, "Contact " + id + " updated");
    }

    public ValidationResult delete(int id) {
        if (!repository.delete(id)) return new ValidationResult(false, "No contact with id " + id);
        return new ValidationResult(true, "Contact " + id + " deleted");
    }

    public Optional<Contact> find(int id) { return repository.findById(id); }
    public List<Contact> list() { return repository.findAll(); }
    public List<Contact> search(String query) { return repository.search(query); }
    public List<Contact> sorted() { return repository.findAllSorted(); }

    private String validate(String first, String last, String phone, String email, int excludeId) {
        if (first.isBlank() || last.isBlank()) return "First and last name are required.";
        if (phone.isBlank() || !phone.matches("\\+?[0-9 ()-]{5,20}")) {
            return "Enter a phone number (digits, optional +, spaces or hyphens).";
        }
        if (!email.isEmpty() && !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            return "Enter a valid email address or leave it blank.";
        }
        if ((first + last + phone + email).contains("|")) {
            return "Do not use | in contact fields.";
        }
        if (repository.phoneExists(phone, excludeId)) return "Phone number already in use.";
        if (repository.emailExists(email, excludeId)) return "Email already in use.";
        return null;
    }

    private static String trim(String value) { return value == null ? "" : value.trim(); }

    public record ValidationResult(boolean ok, String message) { }
}
