package za.co.wyldeweb.budgetapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by Anton on 2016/03/24.
 */
public class IncomeListActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.findFragmentById(android.R.id.content) == null){
            IncomeListFragment incomeListFragment = new IncomeListFragment();
            fragmentManager.beginTransaction().add(android.R.id.content, incomeListFragment).commit();
        }
    }
}


