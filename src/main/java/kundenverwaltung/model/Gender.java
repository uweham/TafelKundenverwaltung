package kundenverwaltung.model;

import kundenverwaltung.service.Constants;

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
            case Constants.GENDER_MALE: return maennlich;
            case Constants.GENDER_FEMALE: return weiblich;
            case Constants.GENDER_OTHER: return sonstiges;
            case Constants.GENDER_NN: return keineangabe;
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
