package kundenverwaltung.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class AnredeTest {

	@Test
	@Tag("unit")
	void anredeID31_shouldReturnHerr() {
		Anrede anrede31 = new Anrede(31);
		assertEquals("Herr", anrede31.getAnrede());
		assertEquals(31, anrede31.getAnredeId());
	}

	@Test
	@Tag("unit")
	void anredeID32_shouldReturnFrau() {
		Anrede anrede32 = new Anrede(32);
		assertEquals("Frau", anrede32.getAnrede());
		assertEquals(32, anrede32.getAnredeId());
	}

	@Test
	@Tag("unit")
	void anredeID33_shouldReturnDivers() {
		Anrede anrede33 = new Anrede(33);
		assertEquals("Divers", anrede33.getAnrede());
		assertEquals(33, anrede33.getAnredeId());
	}

	@Test
	@Tag("unit")
	void anredeInvalidId_shouldReturnNull() {
		Anrede anrede99 = new Anrede(99);
		assertNull(anrede99.getAnrede());
		assertEquals(99, anrede99.getAnredeId());
	}
}
