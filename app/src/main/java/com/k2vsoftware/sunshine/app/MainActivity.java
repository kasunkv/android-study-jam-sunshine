package com.k2vsoftware.sunshine.app;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // for the OpenWeatherAPI
            HttpURLConnection urlConnection= null;
            BufferedReader reader = null;
            String forecastString = null;

            ArrayList<String> weekForecast = new ArrayList<String>();
            weekForecast.add("Today - Sunny - 88/63");
            weekForecast.add("Tomorrow - Foggy - 70/46");
            weekForecast.add("Weds - Cloudy - 88/63");
            weekForecast.add("Thurs - Sunny - 88/63");
            weekForecast.add("Fri - Cloudy - 88/63");
            weekForecast.add("Sat - Sunny - 88/63");

            /* Alternet Way */

            /*
            String[] forecastArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/46",
                "Weds - Cloudy - 88/63",
                "Thurs - Sunny - 88/63",
                "Fri - Cloudy - 88/63",
                "Sat - Sunny - 88/63",
                "Sat - Sunny - 88/63"
            };

            List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));
            */

            ArrayAdapter<String> mForecastAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    R.layout.list_item_forecast,
                    R.id.list_item_forecast_textview,
                    weekForecast
            );

            ListView listView = (ListView) rootView.findViewById(R.id.list_view_forecast);
            listView.setAdapter(mForecastAdapter);



            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

                // Connect to the API
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input Stream
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(inputStream == null) {
                    forecastString = null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n"); // for readability of JSON String
                }

                if(buffer.length() == 0) {
                    forecastString = null;
                } else {
                    forecastString = buffer.toString();
                }


            } catch(IOException e) {
                Log.e("PlaceholderFragment", "Error", e);
                forecastString = null;

            } finally {

                // Close the connection
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }

                if(reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }

            }






            return rootView;
        }
    }
}
