package kundenverwaltung.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DeletedMemberOfTheFamilyTest {

    @Test
    @Tag("unit")
    void constructorAndGetters_workCorrectly() {
        Date birthday = Date.valueOf(LocalDate.of(1990, 5, 15));
        Date deletedOn = Date.valueOf(LocalDate.of(2023, 1, 10));

        DeletedMemberOfTheFamily member = new DeletedMemberOfTheFamily(
                1,
                42,
                "Max",
                "Mustermann",
                birthday,
                deletedOn,
                "Umzug"
        );

        assertEquals(1, member.getAutoincrementId());
        assertEquals(42, member.getHouseholdId());
        assertEquals("Max", member.getFirstName());
        assertEquals("Mustermann", member.getSurname());
        assertEquals("Max Mustermann", member.getFullName());
        assertNotNull(member.getBirthday());   // Formatierung wird geprüft
        assertNotNull(member.getDeletedOn());
        assertEquals("Umzug", member.getReasonDelete());
    }

    @Test
    @Tag("unit")
    void setters_workCorrectly() {
        DeletedMemberOfTheFamily member = new DeletedMemberOfTheFamily(
                0, 0, "", "", Date.valueOf("2000-01-01"), Date.valueOf("2000-01-02"), ""
        );

        member.setAutoincrementId(5);
        member.setHouseholdId(99);
        member.setFirstName("Erika");
        member.setSurname("Musterfrau");
        member.setFullName("Erika Musterfrau");
        member.setBirthday(Date.valueOf("1985-12-24"));
        member.setDeletedOn(Date.valueOf("2024-02-29"));
        member.setReasonDelete("Testgrund");

        assertEquals(5, member.getAutoincrementId());
        assertEquals(99, member.getHouseholdId());
        assertEquals("Erika", member.getFirstName());
        assertEquals("Musterfrau", member.getSurname());
        assertEquals("Erika Musterfrau", member.getFullName());
        assertNotNull(member.getBirthday());
        assertNotNull(member.getDeletedOn());
        assertEquals("Testgrund", member.getReasonDelete());
    }
}
