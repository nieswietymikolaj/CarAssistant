package pl.edu.pb.carassistant.Parking;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.edu.pb.carassistant.R;

public class ParkingFragment extends Fragment {

    TextView weatherTemperature, weatherHumidity, weatherPressure;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking, container, false);

        weatherTemperature = view.findViewById(R.id.weather_temperature);
        weatherHumidity = view.findViewById(R.id.weather_humidity);
        weatherPressure = view.findViewById(R.id.weather_pressure);

        return view;
    }
}