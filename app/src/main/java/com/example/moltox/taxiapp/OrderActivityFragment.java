package com.example.moltox.taxiapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderActivityFragment extends Fragment implements OrderConfirmDialog.OnDialogConfirmListener, View.OnClickListener, HttpParser.OnRequestExecutedListener, NumberPicker.OnValueChangeListener {
    private static final String TAG = "vOut:OrderActivityFrag";
    private static final String TAG_DIRECTORDERCONFIRMDIALOG = "DirectOrderConfirmDialog_01";
    private static final String SERVER_ROOT_URL = "http://5.9.67.156/TaxiApp/";
    private static final String SERVER_DIRECT_ORDER_EXTENSION = "taxirequest.php";
    private static final String API_KEY_DIRECTORDER = "u23923u5r823894n23z34fz8hhdsbvahuishwe8278";
    private static final int MIN_STR_LEN = 3;
    private FragmentActivity mContext;
    View view;
    Button button_orderNow;
    TextView textView_person_count;
    TextView textView_car_count;

    EditText et_prename;
    EditText et_name;
    EditText et_email;
    EditText et_startStreet;
    EditText et_startHouseNr;
    EditText et_startZip;
    EditText et_startCity;
    EditText et_destinationStreet;
    EditText et_destinatinHouseNr;
    EditText et_destinationZip;
    EditText et_destinationCity;
    TextView tv_countPerson;
    TextView tv_countCars;
    CheckBox cb_limitedPerson;
    TextWatcher mTextWatcher;


    // TODO: move DEBUG_IS_ENABLED to Activity class

    public OrderActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order, container, false);
        button_orderNow = (Button) view.findViewById(R.id.button_orderNow);
        button_orderNow.setOnClickListener(this);
        textView_person_count = (TextView) view.findViewById(R.id.textView_count_person);
        textView_person_count.setOnClickListener(this);
        textView_car_count = (TextView) view.findViewById(R.id.textView_count_cars);
        textView_car_count.setOnClickListener(this);

        et_prename = (EditText) view.findViewById(R.id.editText_prename);
        et_name = (EditText) view.findViewById(R.id.editText_name);
        et_name.addTextChangedListener(new mTextWatcher(MIN_STR_LEN));
        et_email = (EditText) view.findViewById(R.id.editText_email);
        et_email.addTextChangedListener(new mTextWatcher(MIN_STR_LEN));
        et_startStreet = (EditText) view.findViewById(R.id.editText_address_street);
        et_startStreet.addTextChangedListener(new mTextWatcher(MIN_STR_LEN));
        et_startHouseNr = (EditText) view.findViewById(R.id.editText_address_housenr);
        et_startHouseNr.addTextChangedListener(new mTextWatcher(1));
        et_startZip = (EditText) view.findViewById(R.id.editText_address_zip);
        et_startZip.addTextChangedListener(new mTextWatcher(5));
        et_startCity = (EditText) view.findViewById(R.id.editText_address_city);
        et_startCity.addTextChangedListener(new mTextWatcher(MIN_STR_LEN));
        et_destinationStreet = (EditText) view.findViewById(R.id.editText_dest_address_street);
        et_destinatinHouseNr = (EditText) view.findViewById(R.id.editText_dest_address_housenr);
        et_destinationZip = (EditText) view.findViewById(R.id.editText_dest_address_zip);
        et_destinationCity = (EditText) view.findViewById(R.id.editText_dest_address_city);
        tv_countPerson = (TextView) view.findViewById(R.id.textView_count_person);
        tv_countCars = (TextView) view.findViewById(R.id.textView_count_cars);
        cb_limitedPerson = (CheckBox) view.findViewById(R.id.cb_limitedPerson);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        mContext = (FragmentActivity) context;
        super.onAttach(context);
        /*
        if (context instanceof OrderActivityFragmentOnButtonClickedListener) {
            mListener = (OrderActivityFragmentOnButtonClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OrderActivityFragmentOnButtonClickedListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_orderNow:
                Log.v(TAG, "abschicken gedrückt");
                // mListener.onButtonClicked(view.getId(),view);
                showConfirmDialog();
                break;
            case R.id.textView_count_person:
                Log.v(TAG, "TextView Person Count Clicked");
                showNumberPickerTextViewChangeDialog(R.string.numberPickerDialog_title_count_person, R.id.textView_count_person);
                break;
            case R.id.textView_count_cars:
                Log.v(TAG, "TextView Car Count clicked");
                showNumberPickerTextViewChangeDialog(R.string.numberPickerDialog_title_count_cars, R.id.textView_count_cars);
                break;
        }
    }

    public void showConfirmDialog() {
        OrderConfirmDialog dialog = new OrderConfirmDialog();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), TAG_DIRECTORDERCONFIRMDIALOG);
    }

    @Override
    public void OnDialogConfirmed() {
        Log.v(TAG, "OnDialog OrderConfirmDialog Confirmed");
        JSONObject jDATA = null;
        if (allRequiredFieldsFilled()) {
            Log.v(TAG, "Alle Felder korrekt ausgefüllt -> make JSON now");
            jDATA = putFieldDataToJSON();
            try {
                Log.v(TAG, "OnDialog Confirmed Timestamp: " + jDATA.getString("requestStarted"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int jsonlength = jDATA.length();
            Log.v(TAG, "JSONObject requestContent length: " + jsonlength);
            HttpParser httpParser = null;
            try {
                httpParser = new HttpParser(getActivity(), "POST", SERVER_ROOT_URL + SERVER_DIRECT_ORDER_EXTENSION, API_KEY_DIRECTORDER, this);
                httpParser.execute(jDATA);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast mToast = Toast.makeText(mContext, R.string.toast_not_all_required_fields_filled, Toast.LENGTH_LONG);
            mToast.show();
        }
    }

    private boolean allRequiredFieldsFilled() {
        boolean allOK = true;
        allOK = ((et_name.getText().toString().length() <= MIN_STR_LEN || allOK == false) ? false : true);
        allOK = ((et_startStreet.getText().toString().length() <= MIN_STR_LEN || allOK == false) ? false : true);
        allOK = ((et_startHouseNr.getText().toString() == "" || allOK == false) ? false : true);
        allOK = ((et_startZip.getText().toString().length() != 5 || allOK == false) ? false : true);
        allOK = ((et_startCity.getText().toString().length() != MIN_STR_LEN || allOK == false) ? false : true);
        // TODO: check für Personen/Wagen Verhältnis einbauen ( allRequiredFieldsFilled method)
        return allOK;
    }


    public JSONObject putFieldDataToJSON() {
        Log.v(TAG, "Prename Text: " + et_prename.getText().toString());

        JSONObject result = new JSONObject();
        Long currentTimestampLong = System.currentTimeMillis() / 1000;
        String currentTimestamp = currentTimestampLong.toString();
        try {
            result.put("prename", et_prename.getText().toString());
            result.put("name", et_name.getText().toString());
            result.put("start_Street", et_startStreet.getText().toString());
            result.put("start_Housenr", et_startHouseNr.getText().toString());
            result.put("start_Zip", et_startZip.getText().toString());
            result.put("start_City", et_startCity.getText().toString());
            result.put("dest_Street", et_destinationStreet.getText().toString());
            result.put("dest_Housenr", et_destinatinHouseNr.getText().toString());
            result.put("dest_Zip", et_destinationZip.getText().toString());
            result.put("dest_City", et_destinationCity.getText().toString());
            result.put("email", et_email.getText().toString());
            result.put("requestStarted", currentTimestamp);
            result.put("countPerson", tv_countPerson.getText().toString());
            result.put("countCars", tv_countCars.getText().toString());
            result.put("limitedPerson", (cb_limitedPerson.isChecked()) ? "true" : "false");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public void OnRequestExecuted(String result) {
        boolean isSuccess = false;
// TODO: Show order success or fail dialog
        try {
            JSONObject jObject = new JSONObject(result);
            isSuccess = jObject.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (isSuccess) {
            Log.v(TAG, "isSuccess!!!");
            showPostOrderAlertDialog(result, R.string.aDialog_order_success);
        } else {
            Log.v(TAG, "Request not successfull");
            showPostOrderAlertDialog(result, R.string.aDialog_order_fail);
        }
    }


    private void showPostOrderAlertDialog(final String result, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setTitle(R.string.aDialog_title_order);
        builder.setNeutralButton(R.string.aDialog_buttonn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User cancelled the dialog
                if (MainUiFragment.DEBUG_IS_ENABLED) {

                    changeToDebugFrag(result);
                } else {
                    backToMainActivity();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void backToMainActivity() {
        // reset backstack and replace Fragment with MainuiFragment
        MainUiFragment mainUiFragment = new MainUiFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.MainFragmentContainer, mainUiFragment, MainUiActivity.MAINUIFRAGMENT_TAG);
        transaction.commit();
    }


    private void changeToDebugFrag(String resultString) {
        Bundle resultBundle = new Bundle();
        resultString = resultString.replace(",", "\n");
        resultBundle.putString("orderResponse", resultString);
        // TODO: Try to use SupportFragmentManager
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        DebugFragment mDebugFrag = new DebugFragment();
        mDebugFrag.setArguments(resultBundle);
        transaction.replace(R.id.MainFragmentContainer, mDebugFrag, "DEBUGFRAG_TAG");
        transaction.addToBackStack(null);
        transaction.commit();
    }
//blabla
    private void showNumberPickerTextViewChangeDialog(int title, int matchedTextView) {
        final TextView mTextView = (TextView) view.findViewById(matchedTextView);
        final Dialog d = new Dialog(getActivity());
        d.setTitle(title);
        d.setContentView(R.layout.numberpickerdialog);
        Button b1 = (Button) d.findViewById(R.id.button1_nrpickerdialog);
        Button b2 = (Button) d.findViewById(R.id.button2_nrpickerdialog);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(50);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText(String.valueOf(np.getValue()));
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.v(TAG, "Neuer Number pick: " + newVal + " alte Number: " + oldVal);
    }

    private class mTextWatcher implements TextWatcher {
        int mMinStrLen;
        public mTextWatcher(int minStrLen)  {
            mMinStrLen = minStrLen;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().trim().length() >= mMinStrLen)  {
                et_name.setTextColor(ContextCompat.getColor(mContext, R.color.colorTextDefault));
            }  else  {
                et_name.setTextColor(ContextCompat.getColor(mContext, R.color.colorTextFail));
                String textFailMsg =  getString(R.string.atLeastXChars);
                textFailMsg = textFailMsg.replace("%%CharCount%%",Integer.toString(mMinStrLen));
                Snackbar.make(view, textFailMsg, Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}

/*    new TextWatcher()
             {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.v(TAG, "beforeTextChanged:\nCharSequence: " + s.toString() + "\nStart: " + start + "\nCount: " + count + "\nAfter: " + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.v(TAG, "afterTextChanged: " + s.toString());
            }});
            */

