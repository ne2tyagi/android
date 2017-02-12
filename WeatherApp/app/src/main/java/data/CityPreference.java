package data;

import android.app.Activity;
import android.content.SharedPreferences;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by neetu on 12/02/17.
 */

public class CityPreference {
    SharedPreferences prefs;

    public  CityPreference(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity(){
        String city = prefs.getString("city", "Delhi, IN");
        try {
            city = URLEncoder.encode(city, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        };
        return city;
    }
    public void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }
}
