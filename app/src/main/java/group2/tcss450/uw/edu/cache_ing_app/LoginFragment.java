package group2.tcss450.uw.edu.cache_ing_app;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    /**
     * Creates view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        Button b = (Button) v.findViewById(R.id.sign_in);
        b.setOnClickListener(this);
        TextView fp = (TextView) v.findViewById(R.id.forgot_pass);
        fp.setOnClickListener(this);
        return v;
    }


    /**
     * handles when clicked.
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            Log.d("DO", "onClick: ");
            switch(v.getId()) {
                case R.id.sign_in:
                    Log.d("DO SOMETHING", "PLEASE");
                    EditText userNameLogin = (EditText) getView().findViewById(R.id.username_editText);
                    EditText passwordLogin = (EditText) getView().findViewById(R.id.password_editText);

                    String userName = userNameLogin.getText().toString();
                    String password = passwordLogin.getText().toString();
                    if(userName.isEmpty()) {
                        userNameLogin.setError("Enter your username/email");
                    } else if (password.isEmpty()) {
                        passwordLogin.setError("Enter your password");
                    } else {
                        if(userName instanceof String && password instanceof String) {
                            mListener.loginFragmentInteraction(userName, password);
                        }
                    }
                    break;
                case R.id.forgot_pass:
                    mListener.loginFragmentInteraction("forgot", "forgot");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Attach
     * @param context
     */
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

    /**
     * Detach
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Tells frontend what to doa fter clicked.
     */
    public interface OnFragmentInteractionListener {

        void loginFragmentInteraction(String c, String c2);
    }
}


