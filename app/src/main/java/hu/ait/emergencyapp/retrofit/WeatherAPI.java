package hu.ait.emergencyapp.retrofit;

import hu.ait.emergencyapp.data.WeatherResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sooyoung Kim on 6/28/2017.
 */

public interface WeatherAPI {

    @GET("data/2.5/weather")
    Call<WeatherResult> getWeather(@Query("q") String cityName,
                                   @Query("units") String units,
                                   @Query("appid") String appid
    );
}
