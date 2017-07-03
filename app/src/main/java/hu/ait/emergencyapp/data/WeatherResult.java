package hu.ait.emergencyapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sooyoung Kim on 6/28/2017.
 */

public class WeatherResult {

    @SerializedName("main")
    @Expose
    public Main main;
    @SerializedName("weather")
    @Expose
    public List<Weather> weather = null;


    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }
}