package kundenverwaltung.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


class HaushaltsinformationenTest
{

  private Familienmitglied mockFamilienmitglied;
  private Haushaltsinformationen haushaltsinformationen;
  private Informationstypen mockInformationstypen;


	@BeforeEach
	void setUp()
	{

		mockFamilienmitglied = mock(Familienmitglied.class);

		mockInformationstypen = mock(Informationstypen.class);

		haushaltsinformationen = new Haushaltsinformationen(mockFamilienmitglied, "Beschreibung", mockInformationstypen);
	}

	@Test
	@Tag("unit")
	void testToStringReturnsBeschreibung()
	{
		assertEquals("Beschreibung", haushaltsinformationen.toString());
	}

	@Test
	@Tag("unit")
	void testGetTyp()
	{
		assertEquals(mockInformationstypen, haushaltsinformationen.getTyp());
	}
}
