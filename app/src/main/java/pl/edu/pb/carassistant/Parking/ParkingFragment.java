package pl.edu.pb.carassistant.Parking;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import pl.edu.pb.carassistant.R;

public class ParkingFragment extends Fragment {

    TextView weatherCity, weatherTemperature, weatherHumidity, weatherPressure;

    Activity activity;
    Context context;

    WeatherProvider.WeatherDataLoaded weatherDataLoaded;

    WeatherApiModel weatherApiModel;

    FusedLocationProviderClient fusedLocationProviderClient;

    int PERMISSION_ID = 44;

    Button youtubeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking, container, false);

        weatherDataLoaded = this::GetWeatherData;

        weatherCity = view.findViewById(R.id.weather_city);
        weatherTemperature = view.findViewById(R.id.weather_temperature);
        weatherHumidity = view.findViewById(R.id.weather_humidity);
        weatherPressure = view.findViewById(R.id.weather_pressure);

        youtubeButton = view.findViewById(R.id.youtube_button);

        youtubeButton.setOnClickListener(v -> {
            activity.startActivity(new Intent(context, YouTubeActivity.class));
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        GetLastLocation();

        return view;
    }

    private void GetWeatherData(WeatherApiModel weatherApiModel, boolean dataLoaded) {
        if (dataLoaded) {
            this.weatherApiModel = weatherApiModel;

            if (weatherApiModel.getCityName() != null) {
                weatherCity.setText(weatherApiModel.getCityName());
            }

            if (weatherApiModel.getWeatherList() != null) {
                weatherTemperature.setText(weatherApiModel.getWeatherList().getTemperature() + "°C");
                weatherHumidity.setText(weatherApiModel.getWeatherList().getHumidity() + " %");
                weatherPressure.setText(weatherApiModel.getWeatherList().getPressure() + " hPa");
            }
        } else {
            Toast.makeText(context, getResources().getString(R.string.weather_error), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void GetLastLocation() {
        if (CheckPermissions()) {
            if (IsLocationEnabled()) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                RequestNewLocationData();
                            }
                        }
                );
            } else {
                Toast.makeText(context, getResources().getString(R.string.weather_location_error), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            RequestPermissions();
        }
    }

    private boolean CheckPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean IsLocationEnabled() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    @SuppressLint("MissingPermission")
    private void RequestNewLocationData() {

        LocationRequest locationRequest = new LocationRequest();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();
            WeatherProvider.GetWeatherData(weatherDataLoaded, lastLocation.getLongitude(), lastLocation.getLatitude());
        }
    };
}