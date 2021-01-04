package pl.edu.pb.carassistant.Parking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("weather?units=metric&APPID=df85c77d05a4eb4dde5f05fc6697618d")
    Call<WeatherApiModel> getWeather(@Query("lon") double longitude, @Query("lat") double latitude, @Query("lang") String language);
}
