package se.lexicon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ContactFileTest {
    @TempDir Path directory;

    @Test
    void saveAndLoadRestoresContactsAndNextId() throws IOException {
        Path file = directory.resolve("contacts.txt");
        ContactRepository first = new ContactRepository();
        first.add("Anna", "Lee", "0701111111", "anna@example.com");
        first.add("Omar", "Smith", "0702222222", "");
        first.saveToFile(file);

        ContactRepository restarted = new ContactRepository();
        restarted.loadFromFile(file);
        assertEquals(2, restarted.findAll().size());
        assertEquals("Anna", restarted.findById(1).orElseThrow().getFirstName());
        assertEquals(3, restarted.add("Zara", "Lee", "0703333333", "").getId());
    }

    @Test
    void missingFileStartsWithEmptyContacts() throws IOException {
        ContactRepository repository = new ContactRepository();
        repository.loadFromFile(directory.resolve("not-found.txt"));
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void invalidFileDoesNotOverwriteExistingContacts() throws IOException {
        ContactRepository repository = new ContactRepository();
        repository.add("Anna", "Lee", "0701111111", "anna@example.com");
        Path file = directory.resolve("contacts.txt");
        Files.writeString(file, "broken line", StandardCharsets.UTF_8);
        assertThrows(IOException.class, () -> repository.loadFromFile(file));
        assertEquals(1, repository.findAll().size());
    }
}
