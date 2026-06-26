package kundenverwaltung.service;

public class UtilErrorLog {
 
  private String errormessage="";
  private int cnterror=0;  

  public void addError(int logmode,String errormessage)
  {
    if (logmode == Constants.ERROR_MSG_CLEAR)
    {
      this.setErrormessage("");
      this.setCnterror(0);
    }
    if (logmode == Constants.ERROR_MSG_ADD)
    {
      this.setErrormessage(errormessage);
      this.setCnterror(1);
    }
    if (logmode == Constants.ERROR_MSG_APPEND)
    {
      this.setErrormessage(this.getErrormessage() + '\n'+ errormessage);
      this.setCnterror(this.getCnterror() + 1);
    }
  }

  /**
   * @return the errormessage
   */
  public String getErrormessage() {
    return errormessage;
  }

  /**
   * @param errormessage the errormessage to set
   */
  public void setErrormessage(String errormessage) {
    this.errormessage = errormessage;
  }

  /**
   * @return the cnterror
   */
  public int getCnterror() {
    return cnterror;
  }

  /**
   * @param cnterror the cnterror to set
   */
  public void setCnterror(int cnterror) {
    this.cnterror = cnterror;
  }
  
}
