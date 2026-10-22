package se.lexicon;

import java.util.ArrayList;
import java.util.List;

/** Stores contacts in memory. No menu or validation code belongs here. */
public class ContactRepository {
    private final List<Contact> contacts = new ArrayList<>();

    public void add(Contact contact) {
        contacts.add(contact);
    }

    public List<Contact> findAll() {
        return new ArrayList<>(contacts);
    }

    public Contact findByPhone(String phone) {
        for (Contact contact : contacts) {
            if (contact.getPhone().equals(phone)) {
                return contact;
            }
        }
        return null;
    }

    public List<Contact> searchByName(String namePart) {
        List<Contact> matches = new ArrayList<>();
        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().contains(namePart.toLowerCase())) {
                matches.add(contact);
            }
        }
        return matches;
    }

    public boolean deleteByPhone(String phone) {
        Contact found = findByPhone(phone);
        if (found == null) {
            return false;
        }
        contacts.remove(found);
        return true;
    }
}
