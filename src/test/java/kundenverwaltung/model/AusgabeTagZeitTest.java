package kundenverwaltung.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import kundenverwaltung.dao.AusgabeTagZeitDAO;


import java.time.LocalTime;

class AusgabeTagZeitTest
{

	@Mock
	private AusgabeTagZeitDAO ausgabeTagZeitDAOMock;

	@Mock
	private Ausgabetag ausgabetagMock;

	private AusgabeTagZeit ausgabeTagZeitmitID;
	private AusgabeTagZeit ausgabeTagZeitohneID;

	@SuppressWarnings("deprecation")
  @BeforeEach
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		LocalTime startzeit = LocalTime.of(12, 0); // 12:00 Uhr Mittag
		LocalTime endzeit = LocalTime.of(13, 0); // 13:00 Uhr
		int tagID = 1;
		int ausgabeZeitID = 1;

		ausgabeTagZeitmitID = new AusgabeTagZeit(tagID, ausgabeZeitID, startzeit, endzeit);
		ausgabeTagZeitohneID = new AusgabeTagZeit(ausgabetagMock, startzeit, endzeit);

		when(ausgabeTagZeitDAOMock.create(ausgabeTagZeitohneID)).thenReturn(true);
		when(ausgabetagMock.getName()).thenReturn("Sonntag");
	}

	@Test
	@Tag("unit")
	void testGetAusgabetagID()
	{
		assertEquals(1, ausgabeTagZeitmitID.getAusgabezeitId());
	}

	@Test
	@Tag("unit")
	void testGetAusgabetagName()
	{
		assertEquals("Sonntag", ausgabetagMock.getName());
	}

	@Test
	@Tag("unit")
	void testsetAusgabezeitID()
	{
		ausgabeTagZeitmitID.setAusgabezeitId(2);
		assertEquals(2, ausgabeTagZeitmitID.getAusgabezeitId());
	}

	@Test
	@Tag("unit")
	void testToString()
	{
		LocalTime startzeit = LocalTime.of(12, 0); // 12:00 Uhr Mittag
		LocalTime endzeit = LocalTime.of(13, 0); // 13:00 Uhr
		String expected = ausgabetagMock + " (" + startzeit + " - " + endzeit + "}";
		assertEquals(expected, ausgabeTagZeitohneID.toString());
	}
}
