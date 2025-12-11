package kundenverwaltung.dao;

import java.util.ArrayList;
import kundenverwaltung.model.DeletedMemberOfTheFamily;

public interface DeletedMemberOfTheFamilyDAO
{
  ArrayList<DeletedMemberOfTheFamily> getAllDeletedMemberOfTheFamily(int householdId);
}
