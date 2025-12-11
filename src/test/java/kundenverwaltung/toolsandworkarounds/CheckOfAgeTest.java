package kundenverwaltung.toolsandworkarounds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CheckOfAgeTest {

    private CheckOfAge checkOfAge;

    @BeforeEach
    void setUp() {
        checkOfAge = new CheckOfAge();
    }

    @Test
    @Tag("unit")
    void isAdult_WhenPersonIsOlderThan18_ShouldReturnTrue() {
        LocalDate birthDate = LocalDate.now().minusYears(25);
        assertTrue(checkOfAge.isAdult(birthDate), "Eine 25-jährige Person sollte als volljährig gelten.");
    }

    @Test
    @Tag("unit")
    void isAdult_WhenPersonIsExactly18_ShouldReturnTrue() {
        LocalDate birthDate = LocalDate.now().minusYears(18);
        assertTrue(checkOfAge.isAdult(birthDate), "Eine Person, die heute 18 wird, sollte als volljährig gelten.");
    }

    @Test
    @Tag("unit")
    void isAdult_WhenPersonTurned18Yesterday_ShouldReturnTrue() {
        LocalDate birthDate = LocalDate.now().minusYears(18).minusDays(1);
        assertTrue(checkOfAge.isAdult(birthDate), "Eine Person, die gestern 18 wurde, sollte als volljährig gelten.");
    }

    @Test
    @Tag("unit")
    void isAdult_WhenPersonIsYoungerThan18_ShouldReturnFalse() {
        LocalDate birthDate = LocalDate.now().minusYears(17);
        assertFalse(checkOfAge.isAdult(birthDate), "Eine 17-jährige Person sollte nicht als volljährig gelten.");
    }

    @Test
    @Tag("unit")
    void isAdult_WhenPersonTurns18Tomorrow_ShouldReturnFalse() {
        LocalDate birthDate = LocalDate.now().minusYears(18).plusDays(1);
        assertFalse(checkOfAge.isAdult(birthDate), "Eine Person, die morgen 18 wird, sollte noch nicht als volljährig gelten.");
    }

    @Test
    @Tag("unit")
    void isAdult_WhenDateIsNull_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> {
            checkOfAge.isAdult(null);
        }, "Die Übergabe eines null-Datums sollte eine NullPointerException auslösen.");
    }
}