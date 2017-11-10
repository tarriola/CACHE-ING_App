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


    public PlacesData(String data) {
        mLength = 0;

        parseData(data);



    }

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

    private boolean isValid(int index) {
        boolean result = false;
        if (mLength > 0 && index >= 0 && index < mLength)
            result = true;
        return result;
    }

    public String getPlaceName(int index) {
        String result = "";
        if (isValid(index)) {
            result = mPlaceNameList[index];
        }
        return result;
    }

    public double getPlaceLatitude(int index) {
        double result = 0.0;
        if (isValid(index)) {
            result = mPlaceLatList[index];
        }
        return result;
    }

    public double getPlaceLongitude(int index) {
        double result = 0.0;
        if (isValid(index)) {
            result = mPlaceLngList[index];
        }
        return result;
    }

    public int getSize() { return mLength; }

}
