package kundenverwaltung.toolsandworkarounds;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

class ChangeDateFormatTest {

    private final ChangeDateFormat changeDateFormat = new ChangeDateFormat();

    /** Test LocalDate conversion */
    @Test
    @Tag("unit")
    void shouldConvertLocalDateToDefaultString() {
        LocalDate date = LocalDate.of(2023, 10, 26);
        String expected = "26.10.2023";
        assertEquals(expected, changeDateFormat.changeDateToDefaultString(date));
    }

    /** Test null LocalDate handling */
    @Test
    @Tag("unit")
    void shouldThrowExceptionForNullLocalDate() {
        assertThrows(NullPointerException.class, () -> {
            changeDateFormat.changeDateToDefaultString((LocalDate) null);
        });
    }

    /** Test SQL Date conversion */
    @Test
    @Tag("unit")
    void shouldConvertSqlDateToDefaultString() {
        java.util.Date utilDate = new GregorianCalendar(2023, Calendar.OCTOBER, 26).getTime();
        Date sqlDate = new Date(utilDate.getTime());
        String expected = "26.10.2023";
        assertEquals(expected, changeDateFormat.changeDateToDefaultString(sqlDate));
    }

    /** Test null SQL Date handling */
    @Test
    @Tag("unit")
    void shouldThrowExceptionForNullSqlDate() {
        assertThrows(NullPointerException.class, () -> {
            changeDateFormat.changeDateToDefaultString((Date) null);
        });
    }

    /** Test LocalDateTime conversion */
    @Test
    @Tag("unit")
    void shouldConvertLocalDateTimeToDefaultString() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 10, 26, 14, 30, 55);
        String expected = "26.10.2023 14:30:55";
        assertEquals(expected, changeDateFormat.changeDateTimeToDefaultString(dateTime));
    }

    /** Test null LocalDateTime handling */
    @Test
    @Tag("unit")
    void shouldThrowExceptionForNullLocalDateTime() {
        assertThrows(NullPointerException.class, () -> {
            changeDateFormat.changeDateTimeToDefaultString(null);
        });
    }
}