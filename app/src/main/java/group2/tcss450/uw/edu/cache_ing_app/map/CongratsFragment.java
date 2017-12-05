package group2.tcss450.uw.edu.cache_ing_app.map;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
public class CongratsFragment extends Fragment implements View.OnClickListener {

    private static final String PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~tarriola/cachewebservice/";
    private static final String TAG = "CongratsFragment";

    private OnFragmentInteractionListener mListener;
    private int mLocationID, mAccountID;
    private String mComment;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mSigButton, mDoneButton;

    public CongratsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_congrats, container, false);
        mLocationID = getArguments().getInt("locationID");
        mAccountID = getArguments().getInt("accountID");
        Log.d(TAG, "onCreateView: locationID = " + mLocationID);
        Log.d(TAG, "onCreateView: accountID = " + mAccountID);
        mComment = " ";

        LogsWebServiceTask task = new LogsWebServiceTask();
        task.execute(PARTIAL_URL);

        mSigButton = v.findViewById(R.id.add_sig);
        mSigButton.setOnClickListener(this);
        mDoneButton = v.findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(this);

        return v;
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

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch(v.getId()) {
                case R.id.add_sig:
                    showInputDialog();
                    Log.d(TAG, "onClick: starting sign webservice");
                    mSigButton.setEnabled(false);
//                    SignWebServiceTask task = new SignWebServiceTask();
//                    task.execute(PARTIAL_URL);
                    break;

                case R.id.done_button:
                    mListener.onCongratsFragmentInteraction("map");

                    break;
            }
        }
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
        void onCongratsFragmentInteraction(String message);
    }

    private void setupRecyclerWidget(String jList) {
        ArrayList<JSONObject> log_data = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(jList);
            JSONArray jsonArray = reader.getJSONArray("logs");
            int[] ids = new int[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                ids[i] = json.getInt("accountID");
                log_data.add(json);
            }

            for (int id : ids ) {
                if (id == mAccountID) {
                    mSigButton.setEnabled(false);
                    break;
                }
            }
            mRecyclerView = getView().findViewById(R.id.my_recycler_view);
            mLayoutManager = new LinearLayoutManager(this.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new LogAdapter(log_data);
            mRecyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        if (!mSigButton.isEnabled()) mSigButton.setEnabled(true);
    }

    /**
     * Async web service task for LocationLogs.
     **/
    private class LogsWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "locationlogs.php";

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String parameters = strings[0];
            String token = "?my_id=" + mLocationID;
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

    /**
     * Async web service task for Sign Log.
     **/
    private class SignWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "signlog.php";

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String parameters = strings[0];
            String token = "?my_locID=" + mLocationID + "&my_accID=" + mAccountID + "&my_msg=" + mComment;
            Log.d(TAG, "doInBackground: " + parameters + SERVICE + token);
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
            if (result.startsWith("Unable to")) {
                Toast.makeText(getContext(), "Unable to connect to the server", Toast.LENGTH_LONG)
                        .show();
                return;
            } else if (result.startsWith("{\"code\":200")) {
                Toast.makeText(getContext(), "Log has already been signed", Toast.LENGTH_LONG)
                        .show();
            } else if (result.startsWith("{\"code\":300")) {
                Toast.makeText(getContext(), "Error connecting to the databse", Toast.LENGTH_LONG)
                        .show();
            }else {
                Log.d(TAG, "onPostExecute: " + result);
                Log.d(TAG, "onPostExecute: starting log service task");
                LogsWebServiceTask task = new LogsWebServiceTask();
                task.execute(PARTIAL_URL);
            }

        }
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Leave a Comment:");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.log_message, (ViewGroup) getView(), false);
        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mComment = input.getText().toString();
                Log.d(TAG, "showInputDialog: message = " + mComment);
                SignWebServiceTask task = new SignWebServiceTask();
                task.execute(PARTIAL_URL);

            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                SignWebServiceTask task = new SignWebServiceTask();
                task.execute(PARTIAL_URL);
            }
        });

        builder.show();

    }
}
