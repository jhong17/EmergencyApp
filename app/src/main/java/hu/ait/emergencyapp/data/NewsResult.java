package hu.ait.emergencyapp.data;

/**
 * Created by Sooyoung Kim on 7/4/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsResult {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("source")
    @Expose
    public String source;
    @SerializedName("sortBy")
    @Expose
    public String sortBy;
    @SerializedName("articles")
    @Expose
    public List<Article2> articles = null;

    public String getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public List<Article2> getArticles() {
        return articles;
    }
}