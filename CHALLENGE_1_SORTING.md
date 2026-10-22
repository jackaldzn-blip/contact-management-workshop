# Challenge 1 — Sort Contacts Alphabetically

## What the trainer requires

Display **all contacts sorted alphabetically by name**. Work on a **separate branch**, commit regularly, and merge the branch when the feature works.

## Design discussion

We already store contacts in an `ArrayList<Contact>`. We could sort that existing list, but that would change the collection's original order. A simpler, safer choice is to request a **copy** and sort the copy for display.

We decided to put sorting in `ContactService`, not directly in the console. The console only asks for a sorted list and prints it. This keeps user interaction and collection logic separate.

## Changes

1. `ContactService.java`: add `getContactsSortedByName()`:

```java
public List<Contact> getContactsSortedByName() {
    List<Contact> sortedContacts = repository.findAll();
    sortedContacts.sort((first, second) ->
            first.getName().compareToIgnoreCase(second.getName()));
    return sortedContacts;
}
```

2. `ContactConsole.java`: change `showAllContacts()` to call `service.getContactsSortedByName()`. Label option 2 "View all contacts (A-Z)".
3. `ContactServiceChecks.java`: verify an empty list, A-Z ordering, case-insensitive comparison, and that the original collection remains unchanged.

### Why this works

- `repository.findAll()` already returns `new ArrayList<>(contacts)`, i.e. a new list.
- `List.sort(...)` sorts that list in place.
- `(first, second) -> ...` is a Java lambda expression that compares two contacts.
- `compareToIgnoreCase` sorts `anna` before `Omar` before `Zara` regardless of upper/lower case.
- Sorting affects **display only**, not the stored contact objects or insertion order.

## Example to demo

Add these contacts in this order:

| Name | Phone |
|---|---|
| Zara | 0701111111 |
| anna | 0702222222 |
| Omar | 0703333333 |

Option 2 must display `anna`, `Omar`, `Zara`.

## GitHub workflow: separate feature branch

**If starting with the original core ZIP**, create your group repository with the core version committed to `main`. Then implement the changes above on a fresh branch (or use the separately supplied patch):

```powershell
git switch main
git pull --ff-only origin main
git switch -c feature/sort-contacts
# Edit the source and tests, or apply the supplied patch at repository root.
# If using a patch file outside your project:
git apply "C:\path\to\challenge-1-sorting.patch"
# Run compilation and tests, then:
git add .
git commit -m "Add alphabetical contact sorting challenge"
git push -u origin feature/sort-contacts
```

Open a pull request into `main`, ask another member to review, then merge. Afterwards, update local `main` with `git pull --ff-only origin main`.

**If using the updated sorted ZIP**, it already contains the integrated result. To preserve genuine workshop evidence, do the exercise with your teammates on a feature branch in your actual GitHub repository rather than claiming the ZIP alone demonstrates a branch or PR.

## Documentation for final report

Record who proposed the sorting idea, who wrote the code, who tested it, which commit/PR was merged, and what the team learned about sorting a copy rather than mutating stored data.
