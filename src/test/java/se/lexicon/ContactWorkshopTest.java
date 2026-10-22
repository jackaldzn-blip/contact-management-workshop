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

    // Direct repository tests meet Challenge 2's data-access requirement.

    @Test
    void repositoryAdd_positive() {
        assertNotNull(repository.add("Anna", "Lee", "0701111111", "anna@example.com"));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void repositoryAdd_negativeDuplicatePhone() {
        repository.add("Anna", "Lee", "0701111111", "anna@example.com");
        assertNull(repository.add("Omar", "Ali", "0701111111", "omar@example.com"));
    }

    @Test
    void repositorySearch_positive() {
        repository.add("Anna", "Lee", "0701111111", "");
        assertEquals(1, repository.search("ANN").size());
    }

    @Test
    void repositorySearch_negative() {
        repository.add("Anna", "Lee", "0701111111", "");
        assertTrue(repository.search("unknown").isEmpty());
    }

    @Test
    void repositoryUpdate_positive() {
        repository.add("Anna", "Lee", "0701111111", "");
        assertTrue(repository.update(1, "Annie", "Lee", "0701111111", "").isPresent());
        assertEquals("Annie", repository.findById(1).orElseThrow().getFirstName());
    }

    @Test
    void repositoryUpdate_negativeUnknownId() {
        assertTrue(repository.update(99, "Anna", "Lee", "0701111111", "").isEmpty());
    }

    @Test
    void repositoryDelete_positive() {
        repository.add("Anna", "Lee", "0701111111", "");
        assertTrue(repository.delete(1));
        assertTrue(repository.findById(1).isEmpty());
    }

    @Test
    void repositoryDelete_negativeUnknownId() {
        assertFalse(repository.delete(99));
    }

    @Test
    void add_rejectsNumbersInsteadOfNamesAndPhoneWithoutDigits() {
        assertFalse(service.add("123", "Lee", "0701111111", "").ok());
        assertFalse(service.add("Anna", "456", "0701111111", "").ok());
        assertFalse(service.add("Anna", "Lee", "-----", "").ok());
        assertFalse(service.add("Anna", "Lee", "12---", "").ok());
        assertTrue(service.list().isEmpty());
    }

    @Test
    void add_acceptsAccentedNamesAndInternationalPhone() {
        assertTrue(service.add("Åsa", "Öberg", "+46 70 123 45 67", "").ok());
        assertEquals("Åsa", service.list().get(0).getFirstName());
    }
}
