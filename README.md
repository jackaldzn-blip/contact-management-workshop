# Contact Management App — Lexicon Workshop

A beginner-friendly Java console application using `ArrayList<Contact>`. It implements add, list (alphabetically sorted), name/phone search, update and delete. It uses only standard Java (no external dependencies).

## Java files

- `ContactApp.java`: main entry point; wires the classes together.
- `Contact.java`: fields, getters, setters, display text.
- `ContactRepository.java`: `ArrayList` storage and data-access operations.
- `ContactService.java`: validation, duplicate rules and application operations.
- `ContactConsole.java`: menu, input and output.
- `ContactServiceChecks.java`: a small self-contained test harness (not JUnit).

## Open and run

Open this folder as a project in IntelliJ IDEA and run `se.lexicon.ContactApp.main()`.

Alternatively, open PowerShell or a terminal **in the folder containing `sources.txt`**:

```powershell
javac -d out '@sources.txt'
java -cp out se.lexicon.ContactApp
```

Run the core automated checks:

```powershell
javac -cp out -d out src/test/java/se/lexicon/ContactServiceChecks.java
java -cp out se.lexicon.ContactServiceChecks
```

If using an existing Maven/Gradle course project, copy `src/main/java/se/lexicon/*.java` into its corresponding source directory. Avoid overwriting unrelated project files.

## Suggested design choices

- **Data:** name, phone, optional email; all strings.
- **Storage:** `ArrayList<Contact>`; ideal for practising loops and collections on a small data set. Lookup is O(n), which is fine for a workshop. It preserves insertion order.
- **Unique identifier:** phone number; must be unique and may be changed through the update function, provided the new value is unused.
- **Validation:** name required; phone is 7–15 digits with optional leading `+`; optional email needs a simple `name@domain.ext` format.
- **No permanent storage:** contacts exist only while the app runs. File import/export is an optional extension, not part of this baseline.
- **Challenge 1 completed:** `View all contacts (A-Z)` uses `ContactService.getContactsSortedByName()`. It sorts a copy of the list, without modifying the original `ArrayList` order. Names are sorted case-insensitively.
- **Not implemented:** the other optional challenges (JUnit 5 and file I/O). The included self-contained automated checks use plain Java, not JUnit.

## Optional Challenge 1: Sort Contacts

The exercise requires this challenge to be developed on a **separate feature branch** and merged after it works. Read `CHALLENGE_1_SORTING.md` before applying the feature to your group's Git repository. This ZIP includes the integrated end-state for reference; unpacking it alone does not create Git branches or prove a collaboration history.

## How to collaborate

Read `DISCUSSION_AND_TASKS.md` and `TESTING.md` together. Complete `FINAL_REPORT_TEMPLATE.md` with actual member names, meetings, commits, reviews and results before submitting. Do not claim work that has not happened.
