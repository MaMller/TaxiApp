package com.example.moltox.taxiapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DebugFragment extends Fragment {

    private static final String TAG = "vOut: DebugFragment";

    TextView debugTextView;

    public DebugFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_debug, container, false);
        debugTextView = (TextView) view.findViewById(R.id.textView_debugger);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)  {
            Log.v(TAG,"savedInstanceState == null");
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle contentBundle = getArguments();
        if (contentBundle != null) {
            String orderResponse = contentBundle.get("orderResponse").toString();
            debugTextView.setText(orderResponse);
        }
        // HttpParser httpParser = new HttpParser(getActivity());

    }
}
