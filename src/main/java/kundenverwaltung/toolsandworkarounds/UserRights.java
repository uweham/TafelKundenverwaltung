package kundenverwaltung.toolsandworkarounds;
import java.util.List;
import kundenverwaltung.service.Constants;  

public class UserRights {
  
  private List<String> userrights = List.of(
      Constants.ADMIN_USER_RIGHT,
      Constants.DISTRIBUTION_POINT_LEADER_USER_RIGHT,
      Constants.CASH_PERSONAL_USER_RIGHT);
      /*Remove role statistik (uncomment if you want,
      Constants.STATISTIK_PERSONAL_USER_RIGHT);
      */

  /**
   * @return the userrights
   */
  public List<String> getUserrights() {
    return userrights;
  }
  
}
