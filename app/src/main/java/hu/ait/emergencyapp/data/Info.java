package hu.ait.emergencyapp.data;

/**
 * Created by jessicahong on 7/3/17.
 */

public class Info {

    private String cityName;
    private String infoName;
    private String content;

    public Info() {
    }

    public Info(String cityName, String infoName, String content) {
        this.cityName = cityName;
        this.infoName = infoName;
        this.content = content;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
