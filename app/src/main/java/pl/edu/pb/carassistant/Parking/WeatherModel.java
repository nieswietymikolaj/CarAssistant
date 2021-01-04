package pl.edu.pb.carassistant.Parking;

import com.google.gson.annotations.SerializedName;

public class WeatherModel {

    @SerializedName("temp")
    public float temperature;

    @SerializedName("humidity")
    public float humidity;

    @SerializedName("pressure")
    public float pressure;


    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }
}
