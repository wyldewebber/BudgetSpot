package za.co.wyldeweb.budgetapp;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import za.co.wyldeweb.budgetapp.DTO.Income;
import za.co.wyldeweb.budgetapp.Database.IncomeContract;

/**
 * Created by Anton on 2016/03/24.
 */
public class EditIncomeActivity extends FragmentActivity implements View.OnClickListener {
    private final String LOG_TAG = EditIncomeActivity.class.getSimpleName();
    private TextView mDescriptionTextView, mAmountTextView;
    private TextView mDate;
    private Button mEditIncome;
    private ContentResolver mContentResolver;
    private Income mIncome;
    private DatePickerDialog mDatePicker;
    private SimpleDateFormat mDateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_income_expense);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());//Locale.US);

        SetupActivity();

        setDateTimeField();

        setEditFields();
    }

    private void setEditFields() {
        Intent intent = getIntent();
        final String id = intent.getStringExtra(IncomeContract.IncomeColumns.INCOME_ID);

        Uri incomeItemQuery = IncomeContract.IncomeSources.buildIncomeUri(id);
        Cursor incomeCursor = mContentResolver.query(incomeItemQuery, null, null, null, null);


        if (incomeCursor.moveToFirst()) {
            int _id = incomeCursor.getInt(incomeCursor.getColumnIndex(BaseColumns._ID));
            String description = incomeCursor.getString(incomeCursor.getColumnIndex(IncomeContract.IncomeColumns.INCOME_DESCRIPTION));
            double amount = incomeCursor.getDouble(incomeCursor.getColumnIndex(IncomeContract.IncomeColumns.INCOME_AMOUNT));
            String calendarDate = incomeCursor.getString(incomeCursor.getColumnIndex(IncomeContract.IncomeColumns.INCOME_DATE));
            String calendarAddedDate = incomeCursor.getString(incomeCursor.getColumnIndex(IncomeContract.IncomeColumns.INCOME_DATE_ADDED));
            Calendar date = Calendar.getInstance();//new GregorianCalendar();

            date.setTimeInMillis(Long.parseLong(calendarDate));
            date.getTime();
            Calendar dateAdded = Calendar.getInstance();
            dateAdded.setTimeInMillis(Long.parseLong(calendarAddedDate));

            Log.v(LOG_TAG, "Date value = " + String.valueOf(date.getTimeInMillis()) +
                    ", retrieved value is: " + String.valueOf(calendarDate));

            mIncome = new Income(_id, description, amount, date, dateAdded);

            mDescriptionTextView.setText(mIncome.getDescription());

            mAmountTextView.setText(String.format("%.2f", mIncome.getAmount()));
            mDate.setText(mDateFormatter.format(Long.parseLong(calendarDate)));

        }


        mEditIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                try {

                    String dateString = mDate.getText().toString();
                    Calendar date = Calendar.getInstance();
                    date.setTime(mDateFormatter.parse(dateString));
                    values.put(IncomeContract.IncomeColumns.INCOME_DESCRIPTION, mDescriptionTextView.getText().toString());
                    values.put(IncomeContract.IncomeColumns.INCOME_AMOUNT, mAmountTextView.getText().toString());
                    values.put(IncomeContract.IncomeColumns.INCOME_DATE, date.getTimeInMillis());

                    Uri uri = IncomeContract.IncomeSources.buildIncomeUri(id);
                    int recordsUpdated = mContentResolver.update(uri, values, null, null);

                    Log.d(LOG_TAG, "number of records updated = " + recordsUpdated);

                    Intent intent = new Intent(EditIncomeActivity.this, IncomeListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    finish();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void SetupActivity() {
        mDescriptionTextView = (TextView) findViewById(R.id.txtIncomeType);
        mAmountTextView = (TextView) findViewById(R.id.txtIncomeAmount);
        mDate = (TextView) findViewById(R.id.txtDate);
        mEditIncome = (Button) findViewById(R.id.btnAddIncome);
        mEditIncome.setText(R.string.save_changes_button_text);

        mContentResolver = EditIncomeActivity.this.getContentResolver();
    }

    private void setDateTimeField() {
        mDate.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDate.setText(mDateFormatter.format(newDate.getTime()));
            }
        },
                newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public void onClick(View view) {
        if (view == mDate) {
            mDatePicker.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }
}
