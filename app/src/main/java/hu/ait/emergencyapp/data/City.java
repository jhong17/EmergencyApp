package hu.ait.emergencyapp.data;

/**
 * Created by jessicahong on 7/2/17.
 */

public class City  {

    //primary information
    private String name;
    private String policeNumber;
    private String fireNumber;
    private String ambulanceNumber;
    private String generalEmergency;

    //secondary information

    public City() {
    }

    public City(String name){
        this.name = name;
    }

    public City(String name, String policeNumber, String fireNumber, String ambulanceNumber) {
        this.name = name;
        this.policeNumber = policeNumber;
        this.fireNumber = fireNumber;
        this.ambulanceNumber = ambulanceNumber;
        this.generalEmergency = null;
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

    public String getAmbulanceNumber() {
        return ambulanceNumber;
    }

    public void setAmbulanceNumber(String ambulanceNumber) {
        this.ambulanceNumber = ambulanceNumber;
    }

    public String getGeneralEmergency() {
        return generalEmergency;
    }

    public void setGeneralEmergency(String generalEmergency) {
        this.generalEmergency = generalEmergency;
    }
}
