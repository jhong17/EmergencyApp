package hu.ait.emergencyapp.data;

/**
 * Created by jessicahong on 7/2/17.
 */

public class City  {

    private String name;
    private String policeNumber;
    private String fireNumber;

    public City() {
    }

    public City(String name, String policeNumber, String fireNumber) {
        this.name = name;
        this.policeNumber = policeNumber;
        this.fireNumber = fireNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoliceNumber() {
        return policeNumber;
    }

    public void setPoliceNumber(String policeNumber) {
        this.policeNumber = policeNumber;
    }

    public String getFireNumber() {
        return fireNumber;
    }

    public void setFireNumber(String fireNumber) {
        this.fireNumber = fireNumber;
    }
}
