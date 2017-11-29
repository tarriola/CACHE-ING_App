package group2.tcss450.uw.edu.cache_ing_app.map;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import group2.tcss450.uw.edu.cache_ing_app.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArrowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ArrowFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private ImageView mArrow;
    private TextView mDistanceTxt;
    private Button mToButton;
    private Location mCurrentLocation;
    private Location mTargetLocation;
    private int mDistance;

    public ArrowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_arrow, container, false);

        mArrow = (ImageView) view.findViewById(R.id.arrowImg);
        mDistanceTxt = (TextView) view.findViewById(R.id.distanceTxt);
        mToButton = (Button) view.findViewById(R.id.toMap);

        mCurrentLocation = new Location("My Location");
        mCurrentLocation.setLatitude(getArguments().getDouble("mlat"));
        mCurrentLocation.setLongitude(getArguments().getDouble("mlng"));

        mTargetLocation = new Location("Target Location");
        mTargetLocation.setLatitude(getArguments().getDouble("lat"));
        mTargetLocation.setLongitude(getArguments().getDouble("lng"));

        mArrow.setRotation(mCurrentLocation.bearingTo(mTargetLocation));
        mDistance = (int) mCurrentLocation.distanceTo(mTargetLocation);
        mDistanceTxt.setText(mDistance + "m");
        mToButton.setOnClickListener(this);

        return view;
    }

    public void updateLocation(Location location) {
        mCurrentLocation = location;
        mArrow.setRotation(mCurrentLocation.bearingTo(mTargetLocation));
        mDistance = (int) mCurrentLocation.distanceTo(mTargetLocation);
        mDistanceTxt.setText(mDistance + "m");
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
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.toMap:
                    mListener.arrowFragmentInteraction("map");
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
        // TODO: Update argument type and name
        void arrowFragmentInteraction(String message);
    }
}
