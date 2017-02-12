package data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Util.Utils;
import model.Place;
import model.Weather;

/**
 * Created by neetu on 12/02/17.
 */

public class JSONWeatherParser {
    public static Weather getWeather(String data){
        Weather weather = new Weather();

        //Create json object from data
        try {
            JSONObject jsonObject = new JSONObject(data);
            Place place = new Place();

            JSONObject coordObj = Utils.getObject("coord", jsonObject);
            place.setLat(Utils.getFloat("lat", coordObj));
            place.setLon(Utils.getFloat("lon", coordObj));

            //Get the sysObj
            JSONObject sysObj = Utils.getObject("sys", jsonObject);
            place.setCountry(Utils.getString("country", sysObj));
            place.setLastUpdate(Utils.getInt("dt", jsonObject));
            place.setSunrise(Utils.getInt("sunrise", sysObj));
            place.setSunset(Utils.getInt("sunset", sysObj));
            place.setCity(Utils.getString("name", jsonObject));

            weather.place = place;

            //get the weather Info
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject jsonWeather = jsonArray.getJSONObject(0);
            weather.currentCondition.setWeatherId(Utils.getInt("id", jsonWeather));
            weather.currentCondition.setDescription(Utils.getString("description", jsonWeather));
            weather.currentCondition.setCondition(Utils.getString("main", jsonWeather));
            weather.currentCondition.setIcon(Utils.getString("icon", jsonWeather));
            //Log.v("data", jsonWeather.toString());
            //Log.v("data", weather.currentCondition.getIcon());

            // wind
            JSONObject windObj = Utils.getObject("wind", jsonObject);
            weather.wind.setSpeed(Utils.getFloat("speed", windObj));
            weather.wind.setDeg(Utils.getFloat("deg", windObj));

            JSONObject cloudObj = Utils.getObject("clouds", jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all", cloudObj));

            JSONObject tempObj = Utils.getObject("main", jsonObject);
            weather.temprature.setTemp(Utils.getDouble("temp", tempObj));
            //weather.temprature.setMinTemp(Utils.getFloat("min_temp", tempObj));
            //weather.temprature.setMaxTemp(Utils.getFloat("max_temp", tempObj));
            weather.currentCondition.setHumidity(Utils.getInt("humidity", tempObj));
            weather.currentCondition.setPresure(Utils.getInt("pressure", tempObj));

            Log.v("data","Data proccessed success in JSON parser");
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
