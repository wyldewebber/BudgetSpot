package za.co.wyldeweb.budgetapp;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import za.co.wyldeweb.budgetapp.Database.IncomeContract;

/**
 * Created by Anton on 2016/03/23.
 */
public class AddIncomeActivity extends FragmentActivity implements View.OnClickListener {
    private final String LOG_TAG = AddIncomeActivity.class.getSimpleName();

    private TextView mDescriptionTextView, mAmountTextView, mDate;
    private Calendar mCalDate;
    private Button mAddIncome;
    private ContentResolver mContentResolver;
    private DatePickerDialog mDatePicker;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_income_expense);

        SetupActivity();

        //dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());//Locale.US);

        setDateTimeField();


    }

    private void setDateTimeField() {

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());//Locale.US);

        mDate.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDate.setText(dateFormatter.format(newDate.getTime()));
            }
        },
        newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void SetupActivity() {
        mDescriptionTextView = (TextView) findViewById(R.id.txtIncomeType);
        mAmountTextView = (TextView) findViewById(R.id.txtIncomeAmount);
        mDate = (TextView) findViewById(R.id.txtDate);
        mAddIncome = (Button) findViewById(R.id.btnAddIncome);
        mAddIncome.setText(R.string.add_income_button_text);



        mContentResolver = AddIncomeActivity.this.getContentResolver();

        mAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    ContentValues values = new ContentValues();


                    mCalDate = Calendar.getInstance();
                    try {
                        mCalDate.setTime(dateFormatter.parse(mDate.getText().toString()));

                        String description = (!TextUtils.isEmpty(mDescriptionTextView.getText()) ? mDescriptionTextView.getText().toString() : "Income");

                        double val = mCalDate.getTimeInMillis();

                        values.put(IncomeContract.IncomeColumns.INCOME_AMOUNT, mAmountTextView.getText().toString());
                        values.put(IncomeContract.IncomeColumns.INCOME_DESCRIPTION, description);
                        values.put(IncomeContract.IncomeColumns.INCOME_DATE, mCalDate.getTimeInMillis());
                        values.put(IncomeContract.IncomeColumns.INCOME_DATE_ADDED, Calendar.getInstance().getTimeInMillis());

                        Uri returned = mContentResolver.insert(IncomeContract.URI_TABLE, values);
                        Log.d(LOG_TAG, "record id returned is " + returned.toString());

                        Intent intent = new Intent(AddIncomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please ensure you have entered valid data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isValid(){
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == mDate){
            mDatePicker.show();
        }
    }
}
