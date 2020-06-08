package com.feedmycat.aii3_app1;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.feedmycat.aii3_app1.BottomSheetDialog.BottomSheetListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
    OnMarkerClickListener, BottomSheetListener {
  private List<Marker> markers = new ArrayList<>();
  private Marker selectedMarker;

  GoogleMap map;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    map = googleMap;

    map.setOnMarkerClickListener(this);

    LatLng Ampelokipoi = new LatLng(40.6454036, 22.9289857);
    map.addMarker(new MarkerOptions().position(Ampelokipoi).title("Ampleokipoi"));
    map.animateCamera(CameraUpdateFactory.newLatLng(Ampelokipoi));

    // The minimum time (in milliseconds) the system will wait until checking if the location changed
    int minTime = 1000;
    // The minimum distance (in meters) traveled until you will be notified
    float minDistance = 5;

    MyLocationListener myLocListener = new MyLocationListener();

    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    // Get the criteria you would like to use
    Criteria criteria = new Criteria();
    criteria.setPowerRequirement(Criteria.POWER_LOW);
    criteria.setAccuracy(Criteria.ACCURACY_FINE);
    criteria.setAltitudeRequired(false);
    criteria.setBearingRequired(false);
    criteria.setCostAllowed(true);
    criteria.setSpeedRequired(false);
    // Get the best provider from the criteria specified, and false to say it can turn the provider on if it isn't already
    String bestProvider = locationManager.getBestProvider(criteria, false);

    // Request location updates
    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    locationManager.requestLocationUpdates(bestProvider, minTime, minDistance, myLocListener);
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
    bottomSheetDialog.show(getSupportFragmentManager(), "bottom sheet dialog");
    selectedMarker = marker;
    return true;
  }

  @Override
  public void onSpinnerItemSelected(String color) {
    switch (color) {
      case "Red": selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        break;
      case "Blue": selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        break;
      case "Green": selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        break;
      case "Yellow": selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        break;
      case "Orange": selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        break;
    }
  }

  private class MyLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location loc) {
      if (loc != null) {
        // Create the marker for the new location and save it in a list
        LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
        Marker newMarker = map.addMarker(new MarkerOptions().position(latLng).title("title"));
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        markers.add(newMarker);

        // When there are more than 5 markers remove the oldest one
        if (markers.size() > 5) {
          markers.get(0).remove();
          markers.remove(0);
        }
      }
    }

    @Override
    public void onProviderDisabled(String arg0) {
      // Do something here if you would like to know when the provider is disabled by the user
    }

    @Override
    public void onProviderEnabled(String arg0) {
      // Do something here if you would like to know when the provider is enabled by the user
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
      // Do something here if you would like to know when the provider status changes
    }
  }
}
