package pl.edu.pb.carassistant.Home;

import com.google.gson.annotations.SerializedName;

public class WeatherApiModel {

    @SerializedName("name")
    public String cityName;

    @SerializedName("main")
    public WeatherDataModel weatherList;


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String name) {
        this.cityName = cityName;
    }

    public WeatherDataModel getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(WeatherDataModel weatherList) {
        this.weatherList = weatherList;
    }

}
