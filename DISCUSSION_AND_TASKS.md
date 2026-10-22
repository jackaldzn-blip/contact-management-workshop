# Group Discussion and Task Plan (6 members)

## Suggested opening discussion — 10 minutes

1. What must users do? Add, list, search by name/phone, update, delete, exit.
2. What data fields? Name, phone (unique ID), email (optional).
3. Which collection? `ArrayList<Contact>` for simplicity, loops, and insertion order; explain why `HashMap` is an alternative.
4. What rules? Required name; unique valid phone; optional but well-formed email; graceful handling of missing contacts.
5. How will we organise code? Model, repository, service rules, console interface, entry point; one responsibility per class.
6. What is done first? Agree on method signatures, create the skeleton, code small parts, merge/review them, run a shared demo.

Record actual decisions here after group discussion. These are suggestions, not pre-existing group decisions.

## Provisional roles

| Person | Assigned file(s) | Pair/review contribution | Evidence after workshop |
|---|---|---|---|
| Leader (member 1) | `ContactApp.java`, integration, final report | Explain design, review branches, lead demo | [PR/commit and notes] |
| Member 2 | `Contact.java` | Explain fields, constructor and `toString()` | [PR/commit and notes] |
| Member 3 | `ContactRepository.java` | Explain `ArrayList`, `for` search, removal | [PR/commit and notes] |
| Member 4 | `ContactService.java` | Explain checks, uniqueness and updating | [PR/commit and notes] |
| Member 5 | `ContactConsole.java` | Explain menu loop, `switch`, `Scanner` | [PR/commit and notes] |
| Member 6 | `ContactServiceChecks.java`, `TESTING.md` | Run good/bad cases, report bugs | [PR/commit and notes] |

Rotate keyboard/reviewer roles so everyone contributes to code or code review. If you do not have time for parallel branches, pair on each small method and rotate who types.

## One-line architecture

`ContactApp` -> `ContactConsole` -> `ContactService` -> `ContactRepository` -> `ArrayList<Contact>`

`Contact` represents one contact. Only the console reads input; only the service applies validation; only the repository stores contacts.

## GitHub workflow

1. Leader creates a private or public repository according to trainer instructions, invites group collaborators, commits the agreed skeleton, and pushes `main`.
2. Each person works on a distinct file or feature branch. Always sync local `main` first:

```powershell
git switch main
git fetch origin
git pull --ff-only origin main
git switch -c feature/contact-model
```

3. Make a small change, compile, commit and push:

```powershell
git add src/main/java/se/lexicon/Contact.java
git commit -m "Add contact model"
git push -u origin feature/contact-model
```

4. Create a pull request into `main`; another member reviews it. Resolve feedback and merge.
5. For a **new task**, create a fresh branch from updated `main`, not a branch containing the already-merged work.
6. `git fetch` downloads remote updates without changing your local working files. `git pull` fetches and integrates those updates into the current branch.
7. Tell the group who edits which file, and avoid committing `out/` or `.class` files.

## Optional challenge: sorting (separate branch)

After the core app works, agree who will implement **Challenge 1: Sort Contacts**. Suggested pairing: Member 3 (collections) and Member 6 (testing); the leader reviews/integrates. They create `feature/sort-contacts` from current `main`, implement and test sorting, and submit a PR. Record the actual contributors and commit in the final report. See `CHALLENGE_1_SORTING.md`.

## Merge/order suggestion

Merge model first, then repository, service, console, entry point and tests (or agree on interfaces so parallel work compiles later). Run the program together after integration. Branches for **optional** sort, JUnit and file I/O must be separate.

## Suggested commit milestones

- `Initial project structure and plan`
- `Add contact model`
- `Add array list repository`
- `Implement contact validation and operations`
- `Add interactive console menu`
- `Add contact operation checks`
- `Sort contact display alphabetically (challenge 1)`
- `Document tests and workshop results`

## Presentation — 3 minutes

- Person 1: project goal and shared design choice.
- Person 2: what a `Contact` object holds.
- Person 3: why and how `ArrayList` works.
- Person 4: validation, duplicates, service methods.
- Person 5: demonstrate the console menu.
- Person 6: run tests and explain a bug or edge case checked.

Finish by showing the GitHub repository and each member's contributions.
