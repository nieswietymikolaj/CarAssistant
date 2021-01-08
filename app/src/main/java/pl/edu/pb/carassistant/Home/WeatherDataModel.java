package pl.edu.pb.carassistant.Home;

import com.google.gson.annotations.SerializedName;

public class WeatherDataModel {

    @SerializedName("temp")
    public String temperature;

    @SerializedName("pressure")
    public String pressure;

    @SerializedName("humidity")
    public String humidity;


    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
