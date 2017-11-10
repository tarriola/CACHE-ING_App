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


/**
 * A simple {@link Fragment} subclass.
 */
public class VerificationFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private static final String TAG = "VerificationFragment";


    /**
     *
     */
    public VerificationFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_verification, container, false);
        Button b = (Button) v.findViewById(R.id.submit_code);
        b.setOnClickListener(this);

        return v;
    }

    /**
     *
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
     *
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(mListener != null) {
            if(v.getId() == R.id.submit_code) {
                EditText code = (EditText) getView().findViewById(R.id.verification_code_editText);

                String email = getArguments().getString("email");
                String veriCode = code.getText().toString();

                if(veriCode.isEmpty()) {
                    code.setError("Enter the code sent to your email");
                } else {
                    mListener.verificationFragmentInteraction(email, veriCode);
                }
            }
        }
    }

    /**
     *
     */
    public interface OnFragmentInteractionListener {
        void verificationFragmentInteraction(String email, String code);
    }
}
