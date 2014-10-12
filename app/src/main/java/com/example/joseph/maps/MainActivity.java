package com.example.joseph.maps;

import android.app.Activity;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

//222test commit
public class MainActivity extends Activity {

    private GoogleMap mMap;
    private GroundOverlayOptions overlay;
    private LocationManager locationManager;
    private Geocoder gc;
    private LocationListener locationListener;
    private Location lastLocation;
    private double lastLatitude;
    private double lastLongitude;
    private PolylineOptions polylineOptions;
    private List<LatLng> points;
    private TextView locationTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationTextView = (TextView) findViewById(R.id.location);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        gc = new Geocoder(this, Locale.getDefault());

        /*Location Listener*/
        /*================================================================*/
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER))	{
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
                    location.setProvider(locationManager.GPS_PROVIDER);
                    displayLocation(location);
                }
                else if(!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER))	{
                    locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    location.setProvider(locationManager.NETWORK_PROVIDER);
                    displayLocation(location);
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if(!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER))	{
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            lastLocation = new Location(locationManager.NETWORK_PROVIDER);
            displayLocation(lastLocation);
        }
        else	{
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
            lastLocation = new Location(locationManager.GPS_PROVIDER);
            displayLocation(lastLocation);
        }




        // Flat markers will rotate when the map is rotated,
        // and change perspective when the map is tilted.
       /* mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(-18.142, 178.431), 2)); */

        // Polylines are useful for marking paths and routes on the map.
        /*
        mMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(-33.866, 151.195))  // Sydney
                .add(new LatLng(-18.142, 178.431))  // Fiji
                .add(new LatLng(21.291, -157.821))  // Hawaii
                .add(new LatLng(37.423, -122.091))  // Mountain View
        );
        */

    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.normal_map:
                if (checked)    {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                    break;
            case R.id.satellite_map:
                if (checked)    {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                    break;
            case R.id.hybrid_map:
                if (checked)    {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
                    break;
        }
    }
    public void displayLocation(Location location)	{

        locationTextView.setText("Latitude: " + location.getLatitude()
                        + " Longitude: " + location.getLongitude());
        if(location.getLongitude() != 0 && location.getLatitude() != 0 && lastLatitude !=0 && lastLongitude != 0) {
            LatLng mapCenter = new LatLng(location.getLatitude(), location.getLongitude());
            polylineOptions = new PolylineOptions().geodesic(true)
            .add(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(lastLatitude, lastLongitude))
                    .width(25)
                    .color(Color.RED);
            mMap.addPolyline(polylineOptions);
        }   else    {
            LatLng mapCenter = new LatLng(location.getLatitude(), location.getLongitude());
            polylineOptions = new PolylineOptions().geodesic(true)
                    .add(new LatLng(location.getLatitude(), location.getLongitude()))
                    .width(25)
                    .color(Color.RED);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 50));
            mMap.addPolyline(polylineOptions);
        }
        lastLatitude = location.getLatitude();
        lastLongitude = location.getLongitude();

		/*
		if(latitude != 0 && longitude != 0)	{
		
			try {
				List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
				if( addresses.get(0) != null)	{
					address = addresses.get(0).getAddressLine(0)+
												"\n"+addresses.get(0).getAddressLine(1)+
												"\n"+addresses.get(0).getAddressLine(2);
				}
				else
					address = "Address not found!";
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	} 

        calculations.setText("Altitude: "+altitude
                +"\nLatitude: "+latitude
                +"\nLongitude: "+longitude
                +/*"\nAddress:\n"+address+
                "\nProvider: "+location.getProvider());
        
		Geocoder gc = new Geocoder(this, Locale.getDefault() );
		List<Address> addresses = null;
		try {
			addresses = gc.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(addresses.size() > 0)	{
			Toast.makeText(this, (CharSequence) addresses.get(0), 10).show();
		}
        */

    }

}
