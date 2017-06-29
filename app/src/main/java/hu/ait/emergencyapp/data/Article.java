package hu.ait.emergencyapp.data;

/**
 * Created by jessicahong on 6/29/17.
 */

public class Article {

    private String title;
    private String description;

    public Article(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

