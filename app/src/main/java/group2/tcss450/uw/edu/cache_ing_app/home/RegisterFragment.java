package group2.tcss450.uw.edu.cache_ing_app.home;


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

import group2.tcss450.uw.edu.cache_ing_app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
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
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        Button b = (Button) v.findViewById(R.id.register_button_in_registration);
        b.setOnClickListener(this);
//        b = (Button) v.findViewById(R.id.register_button_in_registration);
//        b.setOnClickListener(this);
        return v;
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
     * Handles the buttons click listener.
     * @param v
     */
    @Override
    public void onClick(View v) {

        if (mListener != null) {


            if(v.getId() == R.id.register_button_in_registration) {

                EditText userEmail = (EditText) getView().findViewById(R.id.email_editText);
                EditText firstName = (EditText) getView().findViewById(R.id.enter_first_name);
                EditText lastName = (EditText) getView().findViewById(R.id.enter_last_name);
                EditText userPassword = (EditText) getView().findViewById(R.id.reg_password);
                EditText confirmPass = (EditText) getView().findViewById(R.id.confirm_pass);

                String userName = userEmail.getText().toString();
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String password = userPassword.getText().toString();
                String confirmPassword = confirmPass.getText().toString();

                if(userName.isEmpty() || (!(userName.contains("@") && userName.contains(".")))) {
                    userEmail.setError("Enter a valid email");
                } if (fName.isEmpty()) {
                    firstName.setError("Enter your first name");
                } if (lName.isEmpty()) {
                    lastName.setError("Enter your last name");
                } if (password.isEmpty() || (!(password.length() >= 6 && password.length() <= 12))
                        || (!(password.matches(".*\\d+.*")))) {
                    userPassword.setError("Password must be between 6-12 characters long and " +
                            "have at least one number.");
                } if (!password.equals(confirmPassword)) {
                    confirmPass.setError("Passwords do not match");
                } else {

                    mListener.registerFragmentInteraction(userName, password, fName, lName);
                }
            }
        }
    }

    /**
     *Handles interaction when clicked.
     */
    public interface OnFragmentInteractionListener {
        void registerFragmentInteraction(String email, String firstN, String lastN, String password);
    }


}
