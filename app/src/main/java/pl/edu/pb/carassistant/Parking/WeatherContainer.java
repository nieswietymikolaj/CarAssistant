package pl.edu.pb.carassistant.Parking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherContainer {

    @SerializedName("main")
    List<WeatherModel> weatherList;

    public List<WeatherModel> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<WeatherModel> weatherList) {
        this.weatherList = weatherList;
    }
}
