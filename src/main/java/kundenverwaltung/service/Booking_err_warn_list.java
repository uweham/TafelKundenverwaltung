package kundenverwaltung.service;

public class Booking_err_warn_list {
  
  public static final int ENTRY_ERROR = 0;
  public static final int ENTRY_WARNING = 1;

  private int list_type ;
  public int getList_type() {
    return list_type;
  }
  public String getList_type_toString() {
    String list_type_string="Unb.";
    switch (list_type) 
    { case ENTRY_ERROR:
        list_type_string="Fehler: ";
        break;
      case ENTRY_WARNING:
        list_type_string="Warnung: ";
        break;
    }
    return list_type_string;
  }
  
  public void setList_type(int list_type) {
    this.list_type = list_type;
  }
  private String list_message;
  
  public String getList_message() {
    return list_message;
  }
  public void setList_message(String list_message) {
    this.list_message = list_message;
  }
  
  public Booking_err_warn_list(int list_type,String list_message) 
 {
    this.list_type=list_type;
    this.list_message=list_message;
    
  } 
  
}
