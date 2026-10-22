# Testing checklist — to complete during the workshop

Tick only after running each scenario. The checks below are proposed manual tests, not a record of work already done by the group.

- [ ] Add Anna / 0701234567 / anna@example.com -> success.
- [ ] Add Omar / 0709876543 / no email -> success.
- [ ] View all -> both contacts appear and count is 2.
- [ ] Search by name `ann` -> Anna appears (case-insensitive).
- [ ] Search by phone `0701234567` -> Anna appears.
- [ ] Search for an unknown contact -> clear not-found message.
- [ ] Add a second person with `0701234567` -> duplicate rejected.
- [ ] Add an empty name -> rejected.
- [ ] Add an invalid phone -> rejected.
- [ ] Add invalid email -> rejected.
- [ ] Update Anna's name, phone or email -> saved correctly in the current session.
- [ ] Update a phone to another existing contact's phone -> rejected.
- [ ] Delete Omar -> disappears from list.
- [ ] Delete a missing phone -> clear message, no crash.
- [ ] Enter invalid menu option -> error and menu shown again.
- [ ] Exit -> program ends normally.

## Challenge 1: Sort Contacts (optional)

- [ ] Add Zara, anna and Omar **in that order**.
- [ ] Choose menu option 2: output should show **anna, Omar, Zara**, in that order.
- [ ] Search for a contact afterwards: the app still finds the correct record.
- [ ] Add two names with different letter case and verify order ignores case.
- [ ] View an empty address book: friendly message, no crash.

## Automated check (plain Java, NOT JUnit)

Run the commands in `README.md`. Record the result, time and tester after actually executing the test.

| Item | Actual outcome | Tester |
|---|---|---|
| Compile | [fill] | [name] |
| Automated checks | [fill] | [name] |
| Console walkthrough | [fill] | [name] |
| Bugs fixed | [fill] | [name] |
