package za.co.wyldeweb.budgetapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import za.co.wyldeweb.budgetapp.Database.IncomeContract;

/**
 * Created by Anton on 2016/06/04.
 */
public class IncomeDialog extends DialogFragment {
    private LayoutInflater mLayoutInflater;
    public static final String DIALOG_TYPE = "command";
    public static final String DELETE_RECORD = "deleteRecord";
    public static final String DELETE_DATABASE = "deleteDatabase";
    public static final String CONFIRM_EXIT = "confirmExit";
    private static final String LOG_TAG = IncomeDialog.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mLayoutInflater = getActivity().getLayoutInflater();
        final View view  = mLayoutInflater.inflate(R.layout.income_dialog_layout, null);

        String command = getArguments().getString(DIALOG_TYPE);

        if(command.equals(DELETE_RECORD)){
            final int _id = getArguments().getInt(IncomeContract.IncomeColumns.INCOME_ID);
            final String name = getArguments().getString(IncomeContract.IncomeColumns.INCOME_DESCRIPTION);

            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("Are you sure you want to delete " + name + "?");

            builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Deleted " + name, Toast.LENGTH_LONG).show();
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Uri uri = IncomeContract.IncomeSources.buildIncomeUri(String.valueOf(String.valueOf(_id)));

                    contentResolver.delete(uri, null, null);
                    Intent intent = new Intent(getActivity().getApplicationContext(), IncomeListActivity.class);
                    startActivity(intent);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

        }

        return builder.create();
    }
}
