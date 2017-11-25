package group2.tcss450.uw.edu.cache_ing_app;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;

    private static final String TAG = "MapFragment";

    private static final String PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    private static final String API_KEY = "AIzaSyA2FO0ykhMK2VaSlx2JVVpAcWfjVRFWyu4";
    private static final int NEARBY_RADIUS = 200;
    private static final float ZOOM = 15f;


    // vars
    private GoogleMap mGoogleMap;
    private double mLat, mLng;
    private Location mMyLocation;
    private Location mTargetLocation;
    private boolean mIsAvailable;
    private Marker mCurrentMarker;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

//        mLat = 47.2529;
//        mLng = -122.4443;
        mIsAvailable = false;

        mTargetLocation = new Location("Tacoma");
        mTargetLocation.setLatitude(47.2529);
        mTargetLocation.setLongitude(-122.4443);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(mLat,
                                mLng), ZOOM));
            }
        });

        FloatingActionButton placesFab = view.findViewById(R.id.placesFab);
        placesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: executing task;");
                AsyncTask<String, Void, String> task = new PlacesWebServiceTask();
                task.execute(PLACES_URL);
//                Log.d(TAG, "onClick: task done executing");
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateLocation(Location location) {
        mLat = location.getLatitude();
        mLng = location.getLongitude();
        mMyLocation = location;
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLat, mLng), ZOOM));

        if (mIsAvailable) {
            Log.d(TAG, "updateLocation: " + mMyLocation.distanceTo(mTargetLocation));
            if (mMyLocation.distanceTo(mTargetLocation) <= 20) {
                mIsAvailable = false;
                mListener.onMapFragmentInteraction("congrats");
            }
        }



    }

    public GoogleMap getMap() {
        return mGoogleMap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Toast.makeText(getContext(), marker.getId(), Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        mGoogleMap.addMarker(new MarkerOptions().title("Tacoma")
                    .position(new LatLng(mTargetLocation.getLatitude(), mTargetLocation.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mIsAvailable = true;


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onMapFragmentInteraction(String message);
    }

    /**
     * Async web service task for GooglePlaces.
     **/
    private class PlacesWebServiceTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String parameters = strings[0];
            parameters += "?location=" + mLat
                    + "," + mLng;
            parameters += "&radius=" + NEARBY_RADIUS;
            parameters += "&key=" + API_KEY;
            Log.d(TAG, "doInBackground: " + parameters);
            try {
                URL urlObject = new URL(parameters);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }

        /**
         * onPostExecute for AsyncTask.
         **/
        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "onPostExecute: begin parsing json");
            PlacesData placesData = new PlacesData(result);

            showPlacesList(placesData);

        }
    }

    /**
     * shows nearby places
     *
     * @param places
     */
    private void showPlacesList(final PlacesData places) {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            private LatLng placeLatLng;
            private String placeName;

            @Override
            public void onClick(DialogInterface dialog, int index) {
                placeLatLng = new LatLng(places.getPlaceLatitude(index), places.getPlaceLongitude(index));
                placeName = places.getPlaceName(index);

                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .title(placeName)
                        .position(placeLatLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, ZOOM));
                mCurrentMarker = marker;
            }
        };

        if (mCurrentMarker != null) mCurrentMarker.remove();


        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Nearby Places")
                .setItems(places.getNameList(), listener)
                .show();
    }


}
