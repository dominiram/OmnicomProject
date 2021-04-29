package app.appworks.myapplication.views;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay;

import java.util.List;

import app.appworks.myapplication.BuildConfig;
import app.appworks.myapplication.R;
import app.appworks.myapplication.model.LocationModel;
import app.appworks.myapplication.networking.LocationApi;
import app.appworks.myapplication.networking.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment {

    private static final String TAG = "MapsFragment";
    private MapView mapView;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        try {
            assert getArguments() != null;
            String title = MapsFragmentArgs.fromBundle(getArguments()).getTitle();
            requireActivity().setTitle(title);
        } catch (NullPointerException e) {
            Toast.makeText(
                    requireContext(),
                    "Sorry, we were unable to set the application title",
                    Toast.LENGTH_SHORT
            ).show();
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = rootView.findViewById(R.id.map_view);
        MapController mapController = (MapController) mapView.getController();
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapController.setZoom(15);

        LocationApi locationApi = RetrofitClient
                .getRetrofitInstance()
                .create(LocationApi.class);

        Call<List<LocationModel>> call = locationApi.getLocations();

        call.enqueue(new Callback<List<LocationModel>>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<List<LocationModel>> call, Response<List<LocationModel>> response) {
                Log.d(TAG, "onResponse: ");
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            requireContext(),
                            "Response wasn't successful!",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                List<LocationModel> locations = response.body();
                for (LocationModel location : locations) {
                    GeoPoint geoPoint = new GeoPoint(location.latitude, location.longitude);
                    mapController.setCenter(geoPoint);
                    SimpleLocationOverlay overlay = new SimpleLocationOverlay(
                            BitmapFactory.decodeResource(
                                    getResources(),
                                    R.drawable.marker_default)
                    );
                    overlay.setLocation(geoPoint);
                    mapView.getOverlays().add(overlay);
                }
            }

            @Override
            public void onFailure(Call<List<LocationModel>> call, Throwable t) {
                Toast.makeText(
                        requireContext(),
                        "Ops, something went wrong with the API request!",
                        Toast.LENGTH_SHORT
                ).show();
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }
}


