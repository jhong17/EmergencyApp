package hu.ait.emergencyapp.data;

import java.util.List;
import java.util.Set;

/**
 * Created by jessicahong on 7/4/17.
 */

public class User {

    private List<String> cities;

    public User(List<String> faveCities) {
        this.cities = faveCities;
    }

    public User() {
    }

    public List<String> getFaveCities() {
        return cities;
    }

    public void setFaveCities(List<String> faveCities) {
        this.cities = faveCities;
    }
}
