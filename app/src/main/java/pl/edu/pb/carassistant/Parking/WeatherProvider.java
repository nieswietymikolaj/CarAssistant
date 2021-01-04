package pl.edu.pb.carassistant.Parking;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherProvider {

    public interface WeatherDataLoaded {
        void WeatherDataLoaded(WeatherApiModel weatherApiModel, boolean dataLoaded);
    }

    public static void GetWeatherData(final WeatherDataLoaded weatherDataLoaded, double longitude, double latitude) {

        String language;

        if (Locale.getDefault().getDisplayLanguage().equals("English")) {
            language = "en";
        } else if (Locale.getDefault().getDisplayLanguage().equals("Russian")) {
            language = "ru";
        } else {
            language = "pl";
        }

        double roundLongitude = Math.round(longitude * 1000.0) / 1000.0;
        double roundLatitude = Math.round(latitude * 1000.0) / 1000.0;

        WeatherService weatherService = WeatherApi.getRetrofitInstance().create(WeatherService.class);

        Call<WeatherApiModel> weatherApiModelCall = weatherService.getWeather(roundLongitude, roundLatitude, language);

        weatherApiModelCall.enqueue(new Callback<WeatherApiModel>() {
            @Override
            public void onResponse(Call<WeatherApiModel> call, Response<WeatherApiModel> response) {
                weatherDataLoaded.WeatherDataLoaded(response.body(), true);
            }

            @Override
            public void onFailure(Call<WeatherApiModel> call, Throwable t) {
                weatherDataLoaded.WeatherDataLoaded(null, false);
            }
        });
    }
}
