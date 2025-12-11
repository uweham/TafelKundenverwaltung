package kundenverwaltung.toolsandworkarounds;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import kundenverwaltung.dao.UserDAOimpl;
import kundenverwaltung.model.User;

public class PasswordEncodingTest {

    private static final String PEPPER = "pepper123";

    @BeforeEach
    void setup() {
        UserDAOimpl daoMock = mock(UserDAOimpl.class);
        when(daoMock.getPepper()).thenReturn(PEPPER);
    }

    @Test
    @Tag("unit")
    void testHashPasswordAndCheckPassword() {
        User plainUser = new User(1, "TestUser", "Max", "Mustermann", null, "geheim", "user");
        User hashedUser = PasswordEncoding.hashPassword(plainUser);

        assertNotNull(hashedUser);
        assertNotEquals("geheim", hashedUser.getPassword(), "Passwort sollte gehasht sein");
        assertTrue(PasswordEncoding.checkPassword(hashedUser, "geheim"), "CheckPassword sollte true liefern");
        assertFalse(PasswordEncoding.checkPassword(hashedUser, "falsch"), "CheckPassword sollte false liefern");
    }

    @Test
    @Tag("unit")
    void testHashText() {
        String hashed = PasswordEncoding.hashText("irgendwas");
        assertNotNull(hashed);
        assertNotEquals("irgendwas", hashed);
    }

    @Test
    @Tag("unit")
    void generateSalt_shouldCreateSaltFromIdAndSwappedCaseUsername() throws Exception {
        User user = new User(42, "AbCd", "Max", "Mustermann", null, "", "user");
        String expectedSalt = "42aBcD";

        java.lang.reflect.Method method = PasswordEncoding.class.getDeclaredMethod("generateSalt", User.class);
        method.setAccessible(true);
        String actualSalt = (String) method.invoke(null, user);

        assertEquals(expectedSalt, actualSalt, "Der generierte Salt sollte aus der ID und dem case-geswappten Usernamen bestehen.");
    }
}
