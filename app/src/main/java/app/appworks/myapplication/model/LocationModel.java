package app.appworks.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LocationModel  {
    @SerializedName("name")
    public String name;
    @SerializedName("latitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;
}
