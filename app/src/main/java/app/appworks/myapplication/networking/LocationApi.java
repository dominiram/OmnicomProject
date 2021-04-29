package app.appworks.myapplication.networking;

import android.location.Location;

import java.util.List;

import app.appworks.myapplication.model.LocationModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface LocationApi {

    @GET("locations")
    Call<List<LocationModel>> getLocations();
}
