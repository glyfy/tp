package expresslibrary.model.person;

import static expresslibrary.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static expresslibrary.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static expresslibrary.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static expresslibrary.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static expresslibrary.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static expresslibrary.testutil.Assert.assertThrows;
import static expresslibrary.testutil.TypicalBooks.A_GAME_OF_THRONES;
import static expresslibrary.testutil.TypicalBooks.BELOVED;
import static expresslibrary.testutil.TypicalPersons.ALICE;
import static expresslibrary.testutil.TypicalPersons.BOB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import expresslibrary.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertEquals(ALICE, aliceCopy);

        // same object -> returns true
        assertEquals(ALICE, ALICE);

        // null -> returns false
        assertNotEquals(ALICE, null);

        // different type -> returns false
        assertNotEquals(ALICE, 5);

        // different person -> returns false
        assertNotEquals(ALICE, BOB);

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertNotEquals(ALICE, editedAlice);

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertNotEquals(ALICE, editedAlice);

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertNotEquals(ALICE, editedAlice);

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertNotEquals(ALICE, editedAlice);

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertNotEquals(ALICE, editedAlice);
    }

    @Test
    public void borrowBook() {
        Person editedAlice = new PersonBuilder(ALICE).build();
        editedAlice.borrowBook(A_GAME_OF_THRONES);
        assertTrue(editedAlice.hasBorrowedBook(A_GAME_OF_THRONES));
    }

    @Test
    public void returnBook() {
        Person editedAlice = new PersonBuilder(ALICE).build();
        editedAlice.borrowBook(A_GAME_OF_THRONES);
        editedAlice.returnBook(A_GAME_OF_THRONES);
        assertFalse(editedAlice.hasBorrowedBook(A_GAME_OF_THRONES));
    }


    @Test
    public void hasBorrowedBook() {
        Person editedAlice = new PersonBuilder(ALICE).build();
        editedAlice.borrowBook(A_GAME_OF_THRONES);
        assertTrue(editedAlice.hasBorrowedBook(A_GAME_OF_THRONES));
        assertFalse(editedAlice.hasBorrowedBook(BELOVED));
    }

    @Test
    public void isValidPerson() {
        // null person
        assertThrows(NullPointerException.class, () -> Person.isValidPerson(null));

        // invalid person
        assertFalse(Person.isValidPerson("")); // empty string
        assertFalse(Person.isValidPerson(" ")); // spaces only
        assertFalse(Person.isValidPerson("^")); // only non-alphanumeric characters
        assertFalse(Person.isValidPerson("peter*")); // contains non-alphanumeric characters

        // valid person
        assertTrue(Person.isValidPerson("peter jack")); // alphabets only
        assertTrue(Person.isValidPerson("12345")); // numbers only
        assertTrue(Person.isValidPerson("peter the 2nd")); // alphanumeric characters
        assertTrue(Person.isValidPerson("Capital Tan")); // with capital letters
        assertTrue(Person.isValidPerson("David Roger Jackson Ray Jr 2nd")); // long names
    }

    @Test
    public void testHashcode() {
        assertEquals(ALICE.hashCode(), ALICE.hashCode());
        assertNotEquals(ALICE.hashCode(), BOB.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(ALICE.toString(), ALICE.toString());
        assertNotEquals(ALICE.toString(), BOB.toString());

        Person editedAlice = new PersonBuilder(ALICE).build();
        editedAlice.borrowBook(A_GAME_OF_THRONES);
        editedAlice.borrowBook(BELOVED);
        assertEquals(editedAlice.toString(), "Alice Pauline; Phone: 94351253; Email: alice@example.com;"
                + " Address: 123, Jurong West Ave 6, #08-111; Books: A Game of Thrones | George RR MartinBeloved |"
                + " Toni Morrison; Tags: [friends]");
    }
}
