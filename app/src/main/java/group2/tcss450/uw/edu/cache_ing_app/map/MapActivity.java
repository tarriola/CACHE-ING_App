package group2.tcss450.uw.edu.cache_ing_app.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.net.HttpURLConnection;

import group2.tcss450.uw.edu.cache_ing_app.R;

/**
 * An activity that will be used for our maps.
 */
public class MapActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        MapFragment.OnFragmentInteractionListener,
        CongratsFragment.OnFragmentInteractionListener,
        ArrowFragment.OnFragmentInteractionListener {

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    /**
     * The desired interval for location updates. Inexact. Updates may be
     * more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will
     * never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final int MY_PERMISSIONS_LOCATIONS = 814;

    // vars
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private GoogleApiClient mGoogleApiClient;
    private MapFragment mMapFragment;
    private ArrowFragment mArrowFragment;
    private String mEmail;
    private int mAccountID, mLocationID;

    /**
     * onCreate function.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mEmail = getIntent().getStringExtra("email");
        mAccountID = getIntent().getIntExtra("id", 0);

        mLocationRequest = new LocationRequest();
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, COURSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{FINE_LOCATION, FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);
        }

        mMapFragment = new MapFragment();


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, mMapFragment)
                    .commit();

    }

    /**
     * Requests current location from FusedAPI, then calls initializeMap().
     *
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, COURSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//                initializeMap();
                if (mCurrentLocation != null)
                    Log.i(TAG, "onConnected " + mCurrentLocation.toString());
                startLocationUpdates();
            }
        }
    }

    /**
     * Called if connection is lost.
     *
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();

    }

    /**
     * Called if connection failed.
     *
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " +
                connectionResult.getErrorCode());

    }

    /**
     * Called when location changes to update location and update camera for map.
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        Log.d(TAG, " onLocationChanged: " + mCurrentLocation.toString());

        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, COURSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mMapFragment != null && mMapFragment.getMap() != null) {
            mMapFragment.getMap().setMyLocationEnabled(true);
            mMapFragment.updateLocation(location);
        }

        if (mArrowFragment != null)
            mArrowFragment.updateLocation(location);

    }

    /**
     * Checks if Locations permissions are enabled or not.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
//                    // locations-related task you need to do.
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    startLocationUpdates();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Locations need to be working for this portion, please provide permission"
                            , Toast.LENGTH_SHORT)
                            .show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates: initiating");
        // The final argument to {@code requestLocationUpdates()} is aLocationListener
        //(http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, COURSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /**
     * Disconnects connection and destroys.
     **/
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    /**
     * Connects connection and starts.
     **/
    protected void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    /**
     * Disconnects connection and stops.
     **/
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onMapFragmentInteraction(String msg, int locationID, double lat, double lng) {
        mLocationID = locationID;
        Bundle args = new Bundle();
        switch (msg) {
            // calls the congrats fragment
            case "congrats":
                CongratsFragment congratsFragment = new CongratsFragment();
                args.putInt("locationID", mLocationID);
                args.putInt("accountID", mAccountID);
                congratsFragment.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, congratsFragment)
                        .commit();
                break;
            // calls arrow fragment.
            case "arrow":
                mArrowFragment = new ArrowFragment();
                args.putDouble("mlat", mCurrentLocation.getLatitude());
                args.putDouble("mlng", mCurrentLocation.getLongitude());
                args.putDouble("lat", lat);
                args.putDouble("lng", lng);
                mArrowFragment.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, mArrowFragment)
                        .commit();
                break;

        }


    }

    @Override
    public void onCongratsFragmentInteraction(String message) {
        // open arrow fragment
        if (mMapFragment == null) {
            mMapFragment = new MapFragment();
            mMapFragment.setLocation(mCurrentLocation);
        }
        mArrowFragment = null;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, mMapFragment)
                .commit();

    }

    @Override
    public void arrowFragmentInteraction(String message) {
        switch (message) {
            // call the mapfragment
            case "map":
                if (mMapFragment == null) {
                    mMapFragment = new MapFragment();
                    mMapFragment.setLocation(mCurrentLocation);
                }
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, mMapFragment)
                        .commit();
                break;

            // call the congrats fragment
            case "congrats":
                CongratsFragment congratsFragment = new CongratsFragment();
                Bundle args = new Bundle();
                Log.d(TAG, "arrowFragmentInteraction: " + mAccountID + ", " + mLocationID);
                args.putString("email", mEmail);
                args.putInt("accountID", mAccountID);
                args.putInt("locationID", mLocationID);
                congratsFragment.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, congratsFragment)
                        .commit();
                break;
        }

    }
}