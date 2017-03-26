package com.example.moltox.taxiapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainUiFragment.OnButtonClickedListener} interface
 * to handle interaction events.
 */
public class MainUiFragment extends Fragment
        implements View.OnClickListener {

    private static final String TAG = MainUiFragment.class.getName();
    public static boolean DEBUG_IS_ENABLED = false;
    private GetSettings getSettings;
    View view;

    ImageButton ib_arrowRight_toOrderActivity;
    ImageButton ib_taxi_toOrderActivity;
    ImageButton ImageButton_onTimeOrder;
    RelativeLayout mRelativeLayout_CC;
    RelativeLayout mRlonTimeOrder;
    CardView mCvDebugPage;

    private OnButtonClickedListener mListener;

    public MainUiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: Add Button to visit Homepage
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_ui, container, false);
        ib_arrowRight_toOrderActivity = (ImageButton) view.findViewById(R.id.ib_arrowRight_toOrderActivity);
        ib_arrowRight_toOrderActivity.setOnClickListener(this);
        ib_taxi_toOrderActivity = (ImageButton) view.findViewById(R.id.ib_taxi_toOrderActivity);
        ib_taxi_toOrderActivity.setOnClickListener(this);
        ImageButton_onTimeOrder = (ImageButton) view.findViewById(R.id.ib_arrowRight_toOnTimeOrderActivity);
        ImageButton_onTimeOrder.setOnClickListener(this);

        mRelativeLayout_CC = (RelativeLayout) view.findViewById(R.id.rl_cc_outerrl);
        mRlonTimeOrder = (RelativeLayout) view.findViewById(R.id.rl_cc_outerrl2);
        mCvDebugPage = (CardView) view.findViewById(R.id.cv_debug_page);
        mRelativeLayout_CC.setOnClickListener(this);
        mRlonTimeOrder.setOnClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonClickedListener) {
            mListener = (OnButtonClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSettings = new GetSettings(getActivity());
        DEBUG_IS_ENABLED = getSettings.getDebugIsEnabled();
        if (!DEBUG_IS_ENABLED && mCvDebugPage.getVisibility() == View.VISIBLE) {
            mCvDebugPage.setVisibility(View.GONE);
        }  else  {
            if (DEBUG_IS_ENABLED && mCvDebugPage.getVisibility() == View.GONE)  {
                mCvDebugPage.setVisibility(View.VISIBLE);
            }
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnButtonClickedListener {
        public void onButtonClicked(int button, View v);
    }

    @Override
    public void onClick(View v) {
        Log.v(TAG, "Buttonpressed");
        mListener.onButtonClicked(v.getId(), v);
    }


}
