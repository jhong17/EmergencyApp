package hu.ait.emergencyapp.data;

/**
 * Created by Sooyoung Kim on 7/4/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article2 {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;

    public Article2() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}