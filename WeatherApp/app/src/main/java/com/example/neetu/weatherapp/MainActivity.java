package com.example.neetu.weatherapp;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import Util.Utils;
import data.CityPreference;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView temp;
    private ImageView iconView;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;

    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (TextView) findViewById(R.id.cityText);
        temp = (TextView) findViewById(R.id.tempText);
        iconView = (ImageView) findViewById(R.id.thumbnailIcon);
        description  = (TextView) findViewById(R.id.cloudText);
        humidity = (TextView) findViewById(R.id.humidText);
        pressure = (TextView) findViewById(R.id.presureText);
        wind = (TextView) findViewById(R.id.windText);
        sunrise = (TextView) findViewById(R.id.riseText);
        sunset = (TextView) findViewById(R.id.setText);
        updated = (TextView) findViewById(R.id.updateText);

        CityPreference cityPreference = new CityPreference(MainActivity.this);

        renderWeatherData(cityPreference.getCity());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.change_cityId){
            showInputDialog();
        }
        return true;
    }

    public void renderWeatherData(String city){
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&units=metric"});

    }

    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change City");
        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Delhi, IN");
        builder.setView(cityInput);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CityPreference cityPreference = new CityPreference(MainActivity.this);
                cityPreference.setCity(cityInput.getText().toString());
                String newCity = cityInput.getText().toString();
                renderWeatherData(newCity);
            }
        });
        builder.show();
    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... params) {
            String address = Utils.ICON_URL + params[0] + ".png";
            Bitmap image = null;
            URL url = null;
            try {
                image = BitmapFactory.decodeStream(new URL(address).openConnection().getInputStream());
            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            iconView.setImageBitmap(bitmap);
        }

    }

    private class WeatherTask extends AsyncTask<String, Void, Weather>{

        @Override
        protected Weather doInBackground(String... params) {
            String data = (new WeatherHttpClient()).getWeatherData(params[0]);
            weather = JSONWeatherParser.getWeather(data);
            Log.v("data: ", weather.place.getCity());

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            DateFormat df = DateFormat.getTimeInstance();
            String sunriseText = df.format(new Date(weather.place.getSunrise()));
            String sunsetText = df.format(new Date(weather.place.getSunset()));
            String updatedText = df.format(new Date(weather.place.getLastUpdate()));

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            String tempText = decimalFormat.format(weather.temprature.getTemp());

            cityName.setText(weather.place.getCity() +", " + weather.place.getCountry());
            temp.setText("" + tempText + "C");
            humidity.setText("Humidity: "+ weather.currentCondition.getHumidity()+" %");
            pressure.setText("Pressure: "+ weather.currentCondition.getPresure() +"hPa");
            wind.setText("Wind: "+ weather.wind.getSpeed() + "mps");
            sunrise.setText("Sunrise: "+ sunriseText);
            sunset.setText("Sunset: " + sunsetText);
            updated.setText("Last Updated: "+ updatedText);
            description.setText("Condition: "+ weather.currentCondition.getCondition()+
            "("+ weather.currentCondition.getDescription() +")");
            Log.v("data",weather.currentCondition.getIcon());


            (new DownloadImageAsyncTask()).execute(new String[]{weather.currentCondition.getIcon()});
        }
    }
}
