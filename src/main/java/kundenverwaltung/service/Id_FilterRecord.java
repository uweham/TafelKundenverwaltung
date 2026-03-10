package kundenverwaltung.service;



public class Id_FilterRecord {

  public static final int ENTRY_SINGLE = 0;
  public static final int ENTRY_INTERVALL = 1;
  
  private int id_from;
  private int id_to;
  private int filt_type;
  
  public Id_FilterRecord(int id_from,int id_to, int filt_type)
  {
    this.id_from=id_from;
    this.id_to=id_to;
    this.filt_type=filt_type;
  }
  public int getId_from() {
    return id_from;
  }
  public void setId_from(int id_from) {
    this.id_from = id_from;
  }
  public int getId_to() {
    return id_to;
  }
  public void setId_to(int id_to) {
    this.id_to = id_to;
  }
  public int getFilt_type() {
    return filt_type;
  }
  public void setFilt_type(int filt_type) {
    this.filt_type = filt_type;
  }
  
  

}
