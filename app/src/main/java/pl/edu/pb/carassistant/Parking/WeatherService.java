package pl.edu.pb.carassistant.Parking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("weather?units=metric&APPID=db405aa0337ff19e0fe02650f1300947")
    Call<WeatherContainer> getWeather(@Query("lon") double longitude, @Query("lat") double latitude, @Query("lang") String language);

    @GET("weather?units=metric&APPID=db405aa0337ff19e0fe02650f1300947")
    Call<WeatherContainer> getWeatherByCityName(@Query("q") String cityName, @Query("lang") String language);

}
