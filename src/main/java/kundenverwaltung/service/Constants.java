package kundenverwaltung.service;

public class Constants {
  
  public static final int SEARCH_MIN_INPUT_LENGTH = 3;
  
  // constants for cbSucheFilter (MainWIndow.fxml)
  public static final int SEARCH_ALL_INDEX = 0;
  public static final int SEARCH_CUSTOMER_ID_INDEX = 1;
  public static final int SEARCH_SURNAME_INDEX = 2;
  public static final int SEARCH_FIRST_NAME_INDEX = 3;
  public static final int SEARCH_STREET_INDEX = 4;
  public static final int SEARCH_POSTCODE_OR_LOCATION_INDEX = 5;
  public static final int SEARCH_DISTRIBUTION_POINT_INDEX = 6;
  public static final int SEARCH_OUTPUT_GROUP_INDEX = 7;
   //constants for cbSpezialfilter (MainWIndow.fxml)
  public static final int SPECIAL_FILTER_NONE_INDEX=0;
  public static final int SPECIAL_FILTER_LAST_HOUSEHOLD_INDEX=1;
  public static final int SPECIAL_FILTER_HOUSEHOLD_WO_NOTIFICATON_INDEX=2;
  public static final int SPECIAL_FILTER_ALL_INDEX = 3;
  
  public static final int SALUTATION_MR = 31;
  public static final int SALUTATION_MRS = 32;
  public static final int SALUTATION_ETC = 33;
  
  public static final int GENDER_MALE = 71;
  public static final int GENDER_FEMALE = 72;
  public static final int GENDER_OTHER =73;
  public static final int GENDER_NN =74;
  
  public static final int ALL_DISTRIBUTION_POINTS = -99;
  
  public static final int NO_ERROR = 0;
  public static final int SQL_ERROR = -1;
  public static final int FILE_ERROR = -2;
  public static final int FILE_NAME_ERROR = -3;
  public static final int FILE_PDF_GEN_ERROR = -4;
  public static final int NUMERIC_ERROR = -98;
  public static final int INTERNAL_ERROR = -99;
  
  
  public static final int ERROR_MSG_ADD = 0;
  public static final int ERROR_MSG_APPEND = 1;
  public static final int ERROR_MSG_CLEAR = 2;
  
  public static final int STATISTIK_OUT_LIST =1;
  public static final int STATISTIK_OUT_CSV =2;
  
  public static final int STATISTIK_AMOUNTS_ALL=0;
  public static final int STATISTIK_AMOUNTS_CREDITS=1;
  public static final int STATISTIK_AMOUNTS_OUTSTANDING = 2;
  
  
}
