package za.co.wyldeweb.budgetapp;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import za.co.wyldeweb.budgetapp.DTO.Income;
import za.co.wyldeweb.budgetapp.Database.IncomeContract;

/**
 * Created by Anton on 2016/03/23.
 */
public class IncomeListLoader  extends AsyncTaskLoader<List<Income>>{
    private static final String LOG_TAG = IncomeListLoader.class.getSimpleName();
    private List<Income> mIncomeSources;
    private ContentResolver mContentResolver;
    private Cursor mCursor;

    public IncomeListLoader(Context context, Uri uri, ContentResolver contentResolver){
        super(context);
        mContentResolver = contentResolver;
    }

    @Override
    public List<Income> loadInBackground() {
        String[] projection = {BaseColumns._ID,
                IncomeContract.IncomeColumns.INCOME_DATE,
                IncomeContract.IncomeColumns.INCOME_DATE_ADDED,
                IncomeContract.IncomeColumns.INCOME_AMOUNT,
                IncomeContract.IncomeColumns.INCOME_DESCRIPTION
        };
        List<Income> incomeEntries = new ArrayList<Income>();
        mCursor = mContentResolver.query(IncomeContract.URI_TABLE, projection, null, null,
                IncomeContract.IncomeColumns.INCOME_DATE + ", " + IncomeContract.IncomeColumns.INCOME_DATE_ADDED  + " ASC");

        if(mCursor != null){
            if(mCursor.moveToFirst()){
                do{
                    int _id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
                    String description = mCursor.getString(mCursor.getColumnIndex(IncomeContract.IncomeColumns.INCOME_DESCRIPTION));
                    double amount = mCursor.getDouble(mCursor.getColumnIndex(IncomeContract.IncomeColumns.INCOME_AMOUNT));
                    long calendarDate = mCursor.getLong(mCursor.getColumnIndex(IncomeContract.IncomeColumns.INCOME_DATE));
                    long calendarAddedDate = mCursor.getLong(mCursor.getColumnIndex(IncomeContract.IncomeColumns.INCOME_DATE_ADDED));
                    Calendar date = new GregorianCalendar();
                    date.setTimeInMillis(calendarDate);
                    Calendar dateAdded = new GregorianCalendar();
                    dateAdded.setTimeInMillis(calendarAddedDate);

                    Income entry = new Income(_id, description, amount, date, dateAdded);
                    incomeEntries.add(entry);


                }
                while(mCursor.moveToNext());
            }
        }
        return incomeEntries;
    }


    @Override
    public void deliverResult(List<Income> incomeSources) {
        if(isReset()){
            if(incomeSources != null){
                mCursor.close();
            }
        }

        List<Income> oldIncomeList = mIncomeSources;

        if(mIncomeSources == null || mIncomeSources.size() == 0){
            Log.d(LOG_TAG, "+++++++++++ NO DATA RETURNED");
        }

        mIncomeSources = incomeSources;

        if(isStarted()){
            super.deliverResult(incomeSources);
        }
        if(oldIncomeList != null && oldIncomeList != incomeSources){
            mCursor.close();
        }

    }

    @Override
    protected void onStartLoading() {

        if(mIncomeSources != null){
            deliverResult(mIncomeSources);
        }

        if(takeContentChanged() || mIncomeSources == null){
            forceLoad();
        }
    }


    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if(mCursor != null){
            mCursor.close();
        }
        mIncomeSources = null;


    }

    @Override
    public void onCanceled(List<Income> incomeList) {
        super.onCanceled(incomeList);
        if(mCursor != null){
            mCursor.close();
        }
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }

    private Calendar convertToCalendar(String calendarString){
        Calendar returnValue;


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        java.util.Date dt = null;
        try {
            returnValue = new GregorianCalendar();

            dt = sdf.parse(calendarString);
            returnValue.setTime(dt);
            return returnValue;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid datetime value: " + calendarString);
        }
    }
}
