package kundenverwaltung.model;

public final class Gender
{
    private int genderId;
    private String gender;
    private String maennlich = "Männlich";
    private String weiblich = "Weiblich";
    private String sonstiges = "Sonstiges";
    private String keineangabe = "Keine Angabe";

    public Gender(int genderId)
    {
        this.genderId = genderId;
        this.gender = getGender(genderId);
    }

    private String getGender(int genderId)
    {
        switch (genderId)
        {
            case 71: return maennlich;
            case 72: return weiblich;
            case 73: return sonstiges;
            case 74: return keineangabe;
          default:
            break;
        }
        return null;
    }

    public int getGenderId()
    {
        return genderId;
    }

    public String getGender()
    {
        return gender;
    }
}
