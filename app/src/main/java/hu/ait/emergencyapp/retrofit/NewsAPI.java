package hu.ait.emergencyapp.retrofit;

import hu.ait.emergencyapp.data.NewsResult;
import hu.ait.emergencyapp.data.WeatherResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sooyoung Kim on 7/4/2017.
 */

public interface NewsAPI {

    @GET("v1/articles")
    Call<NewsResult> getNews(@Query("source") String source,
                             @Query("sortBy") String sortBy,
                             @Query("apiKey") String apiKey
    );
}
