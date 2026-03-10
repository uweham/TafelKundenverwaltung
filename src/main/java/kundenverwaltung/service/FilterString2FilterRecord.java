package kundenverwaltung.service;

import java.util.ArrayList;
import java.util.Optional;
import kundenverwaltung.service.Id_FilterRecord;

public class FilterString2FilterRecord {
  
   private Integer tryParseInteger(String string) {
    try {
        return Integer.valueOf(string);
    } catch (NumberFormatException e) {
        return null;
    }
  }
  
  public ArrayList<Id_FilterRecord> convertString2ArrayList(String filterstring)
  {
    
    ArrayList<Id_FilterRecord> IdArrayList =new ArrayList<>() ;
    // only numbers , and -
    filterstring=filterstring.replaceAll("[^\\d,-]", "");
    String[] filterlist=filterstring.split(",");
    for (String filterrec : filterlist)
    {
      
      // check if single
      if (filterrec.indexOf('-') == -1)
      {  
        Integer id_from = tryParseInteger(filterrec);
        if (id_from != null)
        {
          IdArrayList.add(new Id_FilterRecord(id_from.intValue(),0,Id_FilterRecord.ENTRY_SINGLE));
        }
      }
      // check if range
      if (filterrec.indexOf('-') > 0)
      {  
        String[] filtervallist=filterrec.split("-");
        
        Integer id_from = tryParseInteger(filtervallist[0]);
        Integer id_to = tryParseInteger(filtervallist[1]);
        if (id_from != null)
        {
          IdArrayList.add(new Id_FilterRecord(id_from.intValue(),
                                              id_to,Id_FilterRecord.ENTRY_INTERVALL));
        }
      }
    }
   return IdArrayList;
  }

}
