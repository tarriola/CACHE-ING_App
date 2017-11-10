package group2.tcss450.uw.edu.cache_ing_app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by travis on 11/9/17.
 */

public class PlacesData {

    private static final String TAG = "PlacesData";

    // var
    private String[] mPlaceNameList;
    private double[] mPlaceLatList;
    private double[] mPlaceLngList;
    private int mLength;

    /**
    * Handles the GooglePlaces data.
    * @param: data - the data for a places location.
    **/
    public PlacesData(String data) {
        mLength = 0;

        parseData(data);

        if (mLength == 0) {
            mPlaceNameList = new String[0];
            mPlaceLatList = new double[0];
            mPlaceLngList = new double[0];

        }



    }


    /**
    * Parses the google places data.
    * @param: data - the data for a places location.
    **/
    private void parseData(String data)  {
        Log.d(TAG, "parseData: data");
        try {
            JSONObject json = new JSONObject(data);
            if (json.getString("status").equals("INVALID_REQUEST")) return;
            JSONArray jsonArray = json.getJSONArray("results");

            // initialize fields
            mLength = jsonArray.length();
            mPlaceNameList = new String[mLength];
            mPlaceLatList = new double[mLength];
            mPlaceLngList = new double[mLength];
//            Log.d(TAG, "parseData: length = " + mLength);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject result = jsonArray.getJSONObject(i);

                // get the place name
                String name = result.getString("name");

                // get the latitude and longitude
                JSONObject geometry = result.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");

                // place into lists
                mPlaceNameList[i] = name;
                mPlaceLatList[i] = lat;
                mPlaceLngList[i] = lng;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
    * Gets a list of places names.
    * @return: returns a list of places names.
    **/
    public String[] getNameList() { return mPlaceNameList; }
    
    /**
    * Checks that the location is a valid index.
    * @param: index - the index you wish to check.
    * @return: returns true if the index exists, false otherwise.
    **/
    private boolean isValid(int index) {
        boolean result = false;
        if (mLength > 0 && index >= 0 && index < mLength)
            result = true;
        return result;
    }
    
    /**
    * Gets the name of a place based on index.
    * @param: index - the index you wish to check.
    * @return: returns a string with the name of the place from the array.
    **/
    public String getPlaceName(int index) {
        String result = "";
        if (isValid(index)) {
            result = mPlaceNameList[index];
        }
        return result;
    }

    /**
    * Gets the latitude of a place based on index.
    * @param: index - the index you wish to check.
    * @return: returns double with latitude of place.
    **/
    public double getPlaceLatitude(int index) {
        double result = 0.0;
        if (isValid(index)) {
            result = mPlaceLatList[index];
        }
        return result;
    }
    
    /**
    * Gets the longitude of a place based on index.
    * @param: index - the index you wish to check.
    * @return: returns double with longitude of place.
    **/
    public double getPlaceLongitude(int index) {
        double result = 0.0;
        if (isValid(index)) {
            result = mPlaceLngList[index];
        }
        return result;
    }
    /**
    * Check the length of all places in array.
    * @return: returns an int of array size.
    **/
    public int getSize() { return mLength; }

}
