package se.lexicon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Challenge 2: at least one positive and one negative test for each operation.
class ContactWorkshopTest {
    private ContactRepository repository;
    private ContactService service;

    @BeforeEach
    void setUp() {
        repository = new ContactRepository();
        service = new ContactService(repository);
    }

    @Test
    void add_validContactWorks() {
        assertTrue(service.add("Anna", "Lee", "0701111111", "anna@example.com").ok());
        assertEquals(1, repository.findAll().size());
        assertEquals("Anna", repository.findById(1).orElseThrow().getFirstName());
    }

    @Test
    void add_rejectsDuplicatePhone() {
        service.add("Anna", "Lee", "0701111111", "anna@example.com");
        assertFalse(service.add("Omar", "Ali", "0701111111", "omar@example.com").ok());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void add_rejectsMissingNamesOrBadFormats() {
        assertFalse(service.add("", "Lee", "0701111111", "").ok());
        assertFalse(service.add("Anna", "Lee", "abcd", "").ok());
        assertFalse(service.add("Anna", "Lee", "0701111111", "not-email").ok());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void add_rejectsDuplicateNonEmptyEmail() {
        service.add("Anna", "Lee", "0701111111", "anna@example.com");
        assertFalse(service.add("Omar", "Ali", "0702222222", "ANNA@example.com").ok());
    }

    @Test
    void add_allowsBlankOptionalEmailForDifferentContacts() {
        assertTrue(service.add("Anna", "Lee", "0701111111", "").ok());
        assertTrue(service.add("Omar", "Ali", "0702222222", "").ok());
    }

    @Test
    void search_findsContactByNameOrPhone() {
        service.add("Anna", "Lee", "0701111111", "");
        assertEquals(1, service.search("ann").size());
        assertEquals(1, service.search("1111").size());
    }

    @Test
    void search_unknownTermReturnsEmpty() {
        service.add("Anna", "Lee", "0701111111", "");
        assertTrue(service.search("no-match").isEmpty());
    }

    @Test
    void update_changesFieldsAndKeepsId() {
        service.add("Anna", "Lee", "0701111111", "");
        assertTrue(service.update(1, "Annie", "Lee", "0701111111", "").ok());
        assertEquals("Annie", service.find(1).orElseThrow().getFirstName());
    }

    @Test
    void update_unknownIdIsRejected() {
        assertFalse(service.update(99, "Anna", "Lee", "0701111111", "").ok());
    }

    @Test
    void update_duplicateEmailOrPhoneIsRejected() {
        service.add("Anna", "Lee", "0701111111", "anna@example.com");
        service.add("Omar", "Ali", "0702222222", "omar@example.com");
        assertFalse(service.update(2, "Omar", "Ali", "0701111111", "omar@example.com").ok());
        assertFalse(service.update(2, "Omar", "Ali", "0702222222", "anna@example.com").ok());
    }

    @Test
    void delete_existingContactWorks() {
        service.add("Anna", "Lee", "0701111111", "");
        assertTrue(service.delete(1).ok());
        assertTrue(service.list().isEmpty());
    }

    @Test
    void delete_unknownIdIsRejected() {
        assertFalse(service.delete(99).ok());
        assertTrue(service.list().isEmpty());
    }

    @Test
    void sorting_showsAlphabeticalOrderWithoutChangingStoredOrder() {
        service.add("Zara", "Jones", "0703333333", "");
        service.add("Anna", "Lee", "0701111111", "");
        assertEquals("Anna", service.sorted().get(0).getFirstName());
        assertEquals("Zara", service.list().get(0).getFirstName());
    }

    @Test
    void sorting_emptyCollectionIsSafe() {
        assertTrue(service.sorted().isEmpty());
    }
}
