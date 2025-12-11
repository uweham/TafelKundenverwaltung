package kundenverwaltung.toolsandworkarounds;

import kundenverwaltung.toolsandworkarounds.IndividualExceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CheckUserInputTest {

    private CheckUserInput checkUserInput;

    @BeforeEach
    void setUp() {
        checkUserInput = new CheckUserInput();
    }

    @Test
    @Tag("unit")
    void addChangeUserIsNotEmpty_success() throws UserInputIsEmpty {
        assertTrue(checkUserInput.addChangeUserIsNotEmpty(
                "user", "Max", "Mustermann", LocalDate.of(2000, 1, 1)
        ));
    }

    @Test
    @Tag("unit")
    void addChangeUserIsNotEmpty_throws() {
        assertThrows(UserInputIsEmpty.class, () ->
                checkUserInput.addChangeUserIsNotEmpty("", "Max", "Mustermann", LocalDate.of(2000, 1, 1))
        );
        assertThrows(UserInputIsEmpty.class, () ->
                checkUserInput.addChangeUserIsNotEmpty("user", "", "Mustermann", LocalDate.now())
        );
        assertThrows(UserInputIsEmpty.class, () ->
                checkUserInput.addChangeUserIsNotEmpty("user", "Max", "", null)
        );
    }

    @Test
    @Tag("unit")
    void addChangeUserInputTooLong_success() throws UserInputTooLong {
        assertTrue(checkUserInput.addChangeUserInputTooLong("user", "Max", "Mustermann"));
    }

    @Test
    @Tag("unit")
    void addChangeUserInputTooLong_throws() {
        String longName = "a".repeat(45);
        assertThrows(UserInputTooLong.class, () ->
                checkUserInput.addChangeUserInputTooLong(longName, "Max", "Mustermann")
        );
    }

    @Test
    @Tag("unit")
    void addChangeUserInputValidPasswordLength_success() throws InvalidPasswordLength {
        assertTrue(checkUserInput.addChangeUserInputValidPasswordLenght("123456"));
    }

    @Test
    @Tag("unit")
    void addChangeUserInputValidPasswordLength_throws() {
        assertThrows(InvalidPasswordLength.class, () ->
                checkUserInput.addChangeUserInputValidPasswordLenght("123")
        );
        assertThrows(InvalidPasswordLength.class, () ->
                checkUserInput.addChangeUserInputValidPasswordLenght("a".repeat(25))
        );
    }

    @Test
    @Tag("unit")
    void addChangeUserPasswordsEquals_success() throws ConfirmPassword {
        assertTrue(checkUserInput.addChangeUserPasswordsEquels("password", "password"));
    }

    @Test
    @Tag("unit")
    void addChangeUserPasswordsEquals_throws() {
        assertThrows(ConfirmPassword.class, () ->
                checkUserInput.addChangeUserPasswordsEquels("password", "different")
        );
    }
}
