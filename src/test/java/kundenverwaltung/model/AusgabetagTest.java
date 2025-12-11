package kundenverwaltung.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class AusgabetagTest
{

	@Test
	@Tag("unit")
	void ausgabetagWithId1()
	{
		Ausgabetag ausgabetag = new Ausgabetag(1);
		assertEquals("Sonntag", ausgabetag.getName());
		assertEquals("So.", ausgabetag.getAbkuerzung());
		assertEquals(1, ausgabetag.getTagId());
		assertEquals("Sonntag", ausgabetag.toString());

	}

	@Test
	@Tag("unit")
	void ausgabetagWithId2()
	{
		Ausgabetag ausgabetag = new Ausgabetag(2);
		assertEquals("Montag", ausgabetag.getName());
		assertEquals("Mo.", ausgabetag.getAbkuerzung());
		assertEquals(2, ausgabetag.getTagId());
		assertEquals("Montag", ausgabetag.toString());

	}

	@Test
	@Tag("unit")
	void ausgabetagWithId3()
	{
		Ausgabetag ausgabetag = new Ausgabetag(3);
		assertEquals("Dienstag", ausgabetag.getName());
		assertEquals("Di.", ausgabetag.getAbkuerzung());
		assertEquals(3, ausgabetag.getTagId());
		assertEquals("Dienstag", ausgabetag.toString());

	}

	@Test
	@Tag("unit")
	void ausgabetagWithId4()
	{
		Ausgabetag ausgabetag = new Ausgabetag(4);
		assertEquals("Mittwoch", ausgabetag.getName());
		assertEquals("Mi.", ausgabetag.getAbkuerzung());
		assertEquals(4, ausgabetag.getTagId());
		assertEquals("Mittwoch", ausgabetag.toString());

	}

	@Test
	@Tag("unit")
	void ausgabetagWithId5()
	{
		Ausgabetag ausgabetag = new Ausgabetag(5);
		assertEquals("Donnerstag", ausgabetag.getName());
		assertEquals("Do.", ausgabetag.getAbkuerzung());
		assertEquals(5, ausgabetag.getTagId());
		assertEquals("Donnerstag", ausgabetag.toString());

	}

	@Test
	@Tag("unit")
	void ausgabetagWithId6()
	{
		Ausgabetag ausgabetag = new Ausgabetag(6);
		assertEquals("Freitag", ausgabetag.getName());
		assertEquals("Fr.", ausgabetag.getAbkuerzung());
		assertEquals(6, ausgabetag.getTagId());
		assertEquals("Freitag", ausgabetag.toString());

	}

	@Test
	@Tag("unit")
	void ausgabetagWithId7()
	{
		Ausgabetag ausgabetag = new Ausgabetag(7);
		assertEquals("Samstag", ausgabetag.getName());
		assertEquals("Sa.", ausgabetag.getAbkuerzung());
		assertEquals(7, ausgabetag.getTagId());
		assertEquals("Samstag", ausgabetag.toString());

	}

}


