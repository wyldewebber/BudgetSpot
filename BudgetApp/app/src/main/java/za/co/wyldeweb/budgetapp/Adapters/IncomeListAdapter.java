package za.co.wyldeweb.budgetapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import za.co.wyldeweb.budgetapp.DTO.Income;
import za.co.wyldeweb.budgetapp.Database.IncomeContract;
import za.co.wyldeweb.budgetapp.EditIncomeActivity;
import za.co.wyldeweb.budgetapp.IncomeDialog;
import za.co.wyldeweb.budgetapp.R;

/**
 * Created by Anton on 2016/03/23.
 */
public class IncomeListAdapter extends ArrayAdapter<Income>{
    private LayoutInflater mLayoutInflater;
    private static FragmentManager sFragmentManager;

    private SimpleDateFormat mDateFormatter;

    public IncomeListAdapter(Context context, FragmentManager fragmentManager){
        super(context, android.R.layout.simple_list_item_2);

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sFragmentManager = fragmentManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        mDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());//Locale.US);

        final View view;

        if(convertView == null){
            view = mLayoutInflater.inflate(R.layout.income_view, parent, false);
        }
        else{
            view = convertView;
        }

        final Income income = getItem(position);
        final int _id = income.getId();
        final String description = income.getDescription();
        final double amount = income.getAmount();
        final Calendar date = income.getDate();
        final Calendar dateAdded = income.getDateAdded();

        ((TextView) view.findViewById(R.id.txtIncomeDescription)).setText(description);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        ((TextView) view.findViewById(R.id.txtIncomeAmount)).setText(currencyFormat.format(amount));

        long dateMillis = date.getTimeInMillis();

        ((TextView) view.findViewById(R.id.txtDate))
                .setText(mDateFormatter.format(date.getTimeInMillis()));

        ImageButton editButton = (ImageButton) view.findViewById(R.id.btnEditIncome);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditIncomeActivity.class);
                intent.putExtra(IncomeContract.IncomeColumns.INCOME_ID, String.valueOf(_id));
                intent.putExtra(IncomeContract.IncomeColumns.INCOME_DESCRIPTION, String.valueOf(description));
                intent.putExtra(IncomeContract.IncomeColumns.INCOME_AMOUNT, String.valueOf(amount));
                intent.putExtra(IncomeContract.IncomeColumns.INCOME_DATE, String.valueOf(date));

                getContext().startActivity(intent);
            }
        });

        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.btnDeleteIncome);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FriendsDialog dialog = new FriendsDialog();
//                Bundle args = new Bundle();
//                args.putString(FriendsDialog.DIALOG_TYPE, FriendsDialog.DELETE_RECORD);
//                args.putInt(FriendsContract.FriendsColumns.FRIENDS_ID, _id);
//                args.putString(FriendsContract.FriendsColumns.FRIENDS_NAME, name);
//                dialog.setArguments(args);
//                dialog.show(sFragmentManager, "delete-record");

                IncomeDialog dialog = new IncomeDialog();
                Bundle args = new Bundle();
                args.putString(IncomeDialog.DIALOG_TYPE, IncomeDialog.DELETE_RECORD);
                args.putInt(IncomeContract.IncomeSources.INCOME_ID, _id);
                args.putString(IncomeContract.IncomeSources.INCOME_DESCRIPTION, description);
                dialog.setArguments(args);
                dialog.show(sFragmentManager, "delete-record");
            }
        });

        return view;
    }

    public void setData(List<Income> incomeSources){
        clear();
        if(incomeSources != null){
            for(Income income : incomeSources){
                add(income);
            }
        }
    }

}
