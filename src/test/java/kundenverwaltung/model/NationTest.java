package kundenverwaltung.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class NationTest
{

	private static final int DEFAULT_NATION_ID = 42;
	private static final String DEFAULT_NAME = "Deutschland";
	private static final String DEFAULT_NATIONALITAET = "Deutsch";
	private static final boolean DEFAULT_AKTIV = true;

	private Nation nation;
	/**
     */
	@BeforeEach
	public void setUp()
	{
		nation = new Nation(DEFAULT_NATION_ID, DEFAULT_NAME, DEFAULT_NATIONALITAET, DEFAULT_AKTIV);
	}

	@Test
	@Tag("unit")
	public void testNationCreationWithAllParameters()
	{
		assertEquals(DEFAULT_NATION_ID, nation.getNationId());
		assertEquals(DEFAULT_NAME, nation.getName());
		assertEquals(DEFAULT_NATIONALITAET, nation.getNationalitaet());
		assertTrue(nation.getAktiv());
	}

	@Test
	@Tag("unit")
	public void testNationCreationWithoutAktiv()
	{
		nation = new Nation(DEFAULT_NAME, DEFAULT_NATIONALITAET);
		assertEquals(DEFAULT_NAME, nation.getName());
		assertEquals(DEFAULT_NATIONALITAET, nation.getNationalitaet());
		assertFalse(nation.getAktiv() == null ? false : nation.getAktiv());
	}

	@Test
	@Tag("unit")
	public void testNationCreationWithPartialParameters()
	{
		nation = new Nation(DEFAULT_NATION_ID, DEFAULT_NAME, DEFAULT_NATIONALITAET);
		assertEquals(DEFAULT_NATION_ID, nation.getNationId());
		assertEquals(DEFAULT_NAME, nation.getName());
		assertEquals(DEFAULT_NATIONALITAET, nation.getNationalitaet());
		assertFalse(nation.getAktiv() == null ? false : nation.getAktiv());
	}

	@Test
	@Tag("unit")
	public void testSetAndGetAktiv()
	{
		nation.setAktiv(false);
		assertFalse(nation.getAktiv());
	}

	@Test
	@Tag("unit")
	public void testSetAndGetNationId()
	{
		final int newNationId = 100;
		nation.setNationId(newNationId);
		assertEquals(newNationId, nation.getNationId());
	}

	@Test
	@Tag("unit")
	public void testSetAndGetName()
	{
		final String newName = "Frankreich";
		nation.setName(newName);
		assertEquals(newName, nation.getName());
	}

	@Test
	@Tag("unit")
	public void testSetAndGetNationalitaet()
	{
		final String newNationalitaet = "Französisch";
		nation.setNationalitaet(newNationalitaet);
		assertEquals(newNationalitaet, nation.getNationalitaet());
	}

	@Test
	@Tag("unit")
	public void testToString()
	{
		assertEquals(DEFAULT_NAME, nation.toString());
	}
}
