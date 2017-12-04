package group2.tcss450.uw.edu.cache_ing_app.map;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import group2.tcss450.uw.edu.cache_ing_app.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CongratsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CongratsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private int mLocationID, mAccountID;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~tarriola/cachewebservice/";
    private static final String TAG = "CongratsFragment";

    public CongratsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_congrats, container, false);
        LogsWebServiceTask task = new LogsWebServiceTask();
        task.execute(PARTIAL_URL);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setupRecyclerWidget(String jList) {
        ArrayList<JSONObject> log_data = new ArrayList<JSONObject>();
        try {
            JSONObject reader = new JSONObject(jList);
            JSONArray jsonArray = reader.getJSONArray("logs");
            for (int i = 0; i < jsonArray.length(); i++) {
                log_data.add(jsonArray.getJSONObject(i));
            }
            mRecyclerView = (RecyclerView) getView().findViewById(R.id.my_recycler_view);
            mLayoutManager = new LinearLayoutManager(this.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new LogAdapter(log_data);
            mRecyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Async web service task for GooglePlaces.
     **/
    private class LogsWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "locationlogs.php";

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String parameters = strings[0];
            String token = "?my_id=1";
            try {
                URL urlObject = new URL(parameters + SERVICE + token);
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
            setupRecyclerWidget(result);

        }
    }
}
