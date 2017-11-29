package group2.tcss450.uw.edu.cache_ing_app.home;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import group2.tcss450.uw.edu.cache_ing_app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartupFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    /**
     * Starts
     */
    public StartupFragment() {
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
        View v = inflater.inflate(R.layout.fragment_startup, container, false);
        Button b= (Button) v.findViewById(R.id.login_button);
        b.setOnClickListener(this);
        TextView signUp = (TextView) v.findViewById(R.id.sign_up_here);
        signUp.setOnClickListener(this);

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
     * Handles when clicked.
     * @param view
     */
    @Override
    public void onClick(View view) {
        if(mListener != null) {
            switch (view.getId()) {
                case R.id.login_button:
                    mListener.startUpFragmentInteraction("Login");
                    break;
                case R.id.sign_up_here:
                    mListener.startUpFragmentInteraction("SignUp");
            }
        }
    }

    /**
     * Tells what to do after clicked.
     */
    public interface OnFragmentInteractionListener {
        void startUpFragmentInteraction(String c);
    }

}
