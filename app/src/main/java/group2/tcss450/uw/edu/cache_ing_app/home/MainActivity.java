package group2.tcss450.uw.edu.cache_ing_app.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import group2.tcss450.uw.edu.cache_ing_app.R;
import group2.tcss450.uw.edu.cache_ing_app.map.MapActivity;

/**
 * Main activity that holds the activity and handles the asynctasks.
 */
public class MainActivity extends AppCompatActivity implements
        StartupFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        VerificationFragment.OnFragmentInteractionListener {

    private static final String PARTIAL_URL = "http://cssgate.insttech.washington.edu/" +
            "~tarriola/cachewebservice/";

    private static final String TAG = "MainActivity";


    /**
     * Creating the first view and displaying it.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                        new StartupFragment())
                        .commit();

            }
        }
    }

    /**
     * Handles the interaction on the startUp fragment when the two buttons are clicked.
     * @param c takes in a string and does something depending on what string gets passed.
     */
    @Override
    public void startUpFragmentInteraction(String c) {
        if(c == "Login") {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.left, R.anim.right);
            transaction.replace(R.id.fragment_container, new LoginFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.left, R.anim.right);
            transaction.replace(R.id.fragment_container, new RegisterFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * Handles the login fragment interactions.
     * @param c
     * @param c2
     */
    @Override
    public void loginFragmentInteraction(String c, String c2) {
        if(c.equals("forgot") && c.equals("forgot")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new StartupFragment());
        } else {
            AsyncTask<String, Void, String> task = new LoginWebServiceTask();
            task.execute(PARTIAL_URL, c, c2);
            Log.d("Login", c);
        }
    }

    /**
     * A private class that handles backend calls from the data base.
     */
    private class LoginWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "login.php";
        private String email;
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String args = "?my_email=" + strings[1] + "&my_pwd=" + strings[2];
            email = strings[1];
            try {
                URL urlObject = new URL(url + SERVICE + args);
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
         * What happens after conditions are met after executing.
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), "Unable to connect to the server", Toast.LENGTH_LONG)
                        .show();
                return;
            } else if (result.startsWith("{\"code\":200")) {
                Toast.makeText(getApplicationContext(), "Account not verified.", Toast.LENGTH_LONG)
                        .show();
                VerificationFragment verifyFragment = new VerificationFragment();
                Bundle args = new Bundle();
                args.putString("email", email);
                verifyFragment.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, verifyFragment)
                        .commit();
            } else if (result.startsWith("{\"code\":300")) {
                Toast.makeText(getApplicationContext(), "Email and password doesn't match", Toast.LENGTH_LONG)
                        .show();
            } else if (result.startsWith("{\"code\":400")) {
                Toast.makeText(getApplicationContext(), "Error connecting to database", Toast.LENGTH_LONG)
                        .show();
            } else {
                try {
                    JSONObject json = new JSONObject(result);
                    int id = json.getInt("id");
//                    Log.d(TAG, "onPostExecute: id = " + id);
                    Intent i = new Intent(MainActivity.this, MapActivity.class);
                    i.putExtra("email", email);
                    i.putExtra("id", id);
                    startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("Login Result", result);
            //mTextView.setText(result);
        }
    }

    /**
     * Handles the interactions in the register fragment.
     * @param email
     * @param password
     * @param firstN
     * @param lastN
     */
    @Override
    public void registerFragmentInteraction(String email, String password, String firstN, String lastN) {
        AsyncTask<String, Void, String> task = new RegisterWebServiceTask();
        task.execute(PARTIAL_URL, email, password, firstN, lastN);
        Log.d("Register", email);
    }

    /**
     * A private class that handles backend calls from the data base.
     */
    private class RegisterWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "register.php";
        private String mEmail;
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String args = "?my_email=" + strings[1] + "&my_pwd=" + strings[2]
                    + "&my_firstN=" + strings[3]
                    + "&my_lastN=" + strings[4];
            mEmail = strings[1];

            try {
                URL urlObject = new URL(url + SERVICE + args);
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
         * Handles actions after execution.
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), "Unable to connect to the server", Toast.LENGTH_LONG)
                        .show();
                return;
            } else if (result.startsWith("{\"code\":200")) {
                Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_LONG)
                        .show();
            } else if (result.startsWith("{\"code\":300")) {
                Toast.makeText(getApplicationContext(), "Error connecting to the databse", Toast.LENGTH_LONG)
                        .show();
            }else {
                //String[] string = result.split("\"");

                VerificationFragment verifyFragment = new VerificationFragment();
                Bundle args = new Bundle();
                args.putString("email", mEmail);
                verifyFragment.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, verifyFragment)
                        .commit();
            }
            Log.d("Register Result", result);
        }
    }

    /**
     * Handles the interactions made on th everification fragment.
     * @param email
     * @param code
     */
    @Override
    public void verificationFragmentInteraction(String email, String code) {
        AsyncTask<String, Void, String> task = new VerificationWebServiceTask();
        Log.d(TAG, "verificationFragmentInteraction: executing");
        task.execute(PARTIAL_URL, email, code);
    }

    /**
     * A private class that handles backend calls from the data base.
     */
    private class VerificationWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "verification.php";
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String args = "?my_email=" + strings[1] + "&my_code=" + strings[2];
            try {
                URL urlObject = new URL(url + SERVICE + args);
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
         * Handles stuff after execution.
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), "Unable to connect to the server", Toast.LENGTH_LONG)
                        .show();
                return;
            } else if (result.startsWith("{\"code\":200")) {
                Toast.makeText(getApplicationContext(), "Verification code doesn't match", Toast.LENGTH_LONG)
                        .show();
            } else if (result.startsWith("{\"code\":300")) {
                Toast.makeText(getApplicationContext(), "Error connecting to the database", Toast.LENGTH_LONG)
                        .show();
            } else {
                //String[] string = result.split("\"");

                LoginFragment loginFragment = new LoginFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, loginFragment)
                        .commit();

            }
            Log.d("Verification Result", result);
        }
    }

}