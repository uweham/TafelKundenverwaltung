package kundenverwaltung.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class GenderTest
{

	@Test
	@Tag("unit")
	public void testGenderMale()
	{
		Gender male = new Gender(71);
		assertEquals(71, male.getGenderId());
		assertEquals("Männlich", male.getGender());
	}

	@Test
	@Tag("unit")
	public void testGenderFemale()
	{
		Gender female = new Gender(72);
		assertEquals(72, female.getGenderId());
		assertEquals("Weiblich", female.getGender());
	}

	@Test
	@Tag("unit")
	public void testGenderOther()
	{
		Gender other = new Gender(73);
		assertEquals(73, other.getGenderId());
		assertEquals("Sonstiges", other.getGender());
	}

	@Test
	@Tag("unit")
	public void testGenderNoSpecification()
	{
		Gender noSpecification = new Gender(74);
		assertEquals(74, noSpecification.getGenderId());
		assertEquals("Keine Angabe", noSpecification.getGender());
	}

	@Test
	@Tag("unit")
	public void testGenderInvalid()
	{
		Gender invalid = new Gender(75);
		assertEquals(75, invalid.getGenderId());
		assertNull(invalid.getGender());
	}
}
