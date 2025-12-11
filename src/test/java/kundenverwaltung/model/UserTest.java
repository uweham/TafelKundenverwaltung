package kundenverwaltung.model;

import kundenverwaltung.dao.UserDAO;
import kundenverwaltung.dao.UserDAOimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {

    private User user;
    private final LocalDate testBirthday = LocalDate.of(1995, 10, 20);

    @BeforeEach
    void setUp() {
        user = new User(
                1,
                "testuser",
                "Max",
                "Mustermann",
                testBirthday,
                "secret",
                "Admin"
        );
    }

    @Test
    @Tag("unit")
    void constructor_withId_setsAllFieldsCorrectly() {
        assertAll("Prüfen der durch den Konstruktor mit ID gesetzten Werte",
                () -> assertEquals(1, user.getUserId()),
                () -> assertEquals("testuser", user.getUserName()),
                () -> assertEquals("Max", user.getFirstName()),
                () -> assertEquals("Mustermann", user.getSurname()),
                () -> assertEquals(testBirthday, user.getBirthday()),
                () -> assertEquals("secret", user.getPassword()),
                () -> assertEquals("Admin", user.getUserRights())
        );
    }

    @Test
    @Tag("unit")
    void constructor_withoutId_callsDAOCreate() {
        try (MockedConstruction<UserDAOimpl> daoMock = mockConstruction(UserDAOimpl.class)) {
            User newUser = new User(
                    "newuser",
                    "Anna",
                    "Anders",
                    testBirthday,
                    "newpass",
                    "User"
            );

            assertEquals(1, daoMock.constructed().size());
            UserDAO mockDao = daoMock.constructed().get(0);

            verify(mockDao, times(1)).insert(newUser);

            assertEquals("newuser", newUser.getUserName());
            assertEquals("Anna", newUser.getFirstName());
        }
    }

    @Test
    @Tag("unit")
    void gettersAndSetters_workAsExpected() {
        user.setUserId(99);
        assertEquals(99, user.getUserId());

        user.setUserName("changedUser");
        assertEquals("changedUser", user.getUserName());

        user.setFirstName("Erika");
        assertEquals("Erika", user.getFirstName());

        user.setSurname("Musterfrau");
        assertEquals("Musterfrau", user.getSurname());

        LocalDate newBirthday = LocalDate.of(2000, 1, 1);
        user.setBirthday(newBirthday);
        assertEquals(newBirthday, user.getBirthday());

        user.setPassword("newSecret");
        assertEquals("newSecret", user.getPassword());

        user.setUserRights("Guest");
        assertEquals("Guest", user.getUserRights());
    }

    @Test
    @Tag("unit")
    void getFullName_returnsCorrectName() {
        assertEquals("Mustermann Max", user.getFullName());

        user.setFirstName("Anna");
        user.setSurname("Nym");
        assertEquals("Nym Anna", user.getFullName());
    }

    @Test
    @Tag("unit")
    void getBirthdayString_returnsFormattedDate() {
        String expectedDateString = "20.10.1995";
        assertEquals(expectedDateString, user.getBirthdayString());
    }

    @Test
    @Tag("unit")
    void isDefaultPassword_checksCorrectness() {
        String defaultPassword = "20.10.1995";
        String wrongPassword = "not-the-password";

        assertTrue(user.isDefaultPassword(defaultPassword), "Sollte true sein, wenn das Passwort dem Geburtsdatum entspricht.");
        assertFalse(user.isDefaultPassword(wrongPassword), "Sollte false sein, wenn das Passwort nicht dem Geburtsdatum entspricht.");
    }
}