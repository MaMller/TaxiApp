package com.example.moltox.taxiapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderConfirmDialog extends DialogFragment {

    private OnDialogConfirmListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_direct_order_confirm_dialog, null);
        builder.setView(inflater.inflate(R.layout.fragment_direct_order_confirm_dialog, null));

        builder
                .setPositiveButton(R.string.button_ordernow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("bla","Bestelleeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                        mListener.OnDialogConfirmed();
                    }
                })
                .setNegativeButton(R.string.button_ordercancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OrderConfirmDialog.this.getDialog().cancel();
                    }
                });
        builder.setTitle("Dialog Titel");

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (OnDialogConfirmListener) this.getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(this.getTag().toString()
                    + " must implement OnDialogConfirmedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDialogConfirmListener  {
        public void OnDialogConfirmed();
    }


}
