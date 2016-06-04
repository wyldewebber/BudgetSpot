package za.co.wyldeweb.budgetapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;

import za.co.wyldeweb.budgetapp.Database.IncomeContract;

public class MainActivity extends Activity {

    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentResolver = getContentResolver();
        setContentView(R.layout.content_main);

        SetupActivity();



//        FragmentManager fragmentManager = getSupportFragmentManager();
//        if(fragmentManager.findFragmentById(android.R.id.content) == null){
//            IncomeListFragment incomeListFragment = new IncomeListFragment();
//            fragmentManager.beginTransaction().add(android.R.id.content, incomeListFragment).commit();
//        }


    }

    private void SetupActivity() {

        Button btnAddIncome = (Button) findViewById(R.id.btnAddNewIncome);
        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddIncomeActivity.class);
                startActivity(intent);
            }
        });


        Button btnViewAll = (Button) findViewById(R.id.btnShowAllIncome);
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IncomeListActivity.class);
                startActivity(intent);
            }
        });

        double income = getTotalIncome();

        TextView totalIncome = (TextView) findViewById(R.id.txtTotalIncome);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        totalIncome.setText(currencyFormat.format(income));

    }

    private double getTotalIncome() {
        double totalIncome = 0;

        Cursor cursor;

        String[] projection = {BaseColumns._ID,
                IncomeContract.IncomeColumns.INCOME_AMOUNT,
        };

        cursor = mContentResolver.query(IncomeContract.URI_TABLE, projection, null, null, null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    double amount = cursor.getDouble(cursor.getColumnIndex(IncomeContract.IncomeColumns.INCOME_AMOUNT));

                    totalIncome += amount;
                }
                while(cursor.moveToNext());
            }
        }
        return totalIncome;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
