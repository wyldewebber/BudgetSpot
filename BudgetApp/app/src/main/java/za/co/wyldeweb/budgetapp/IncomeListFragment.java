package za.co.wyldeweb.budgetapp;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.List;

import za.co.wyldeweb.budgetapp.Adapters.IncomeListAdapter;
import za.co.wyldeweb.budgetapp.DTO.Income;
import za.co.wyldeweb.budgetapp.Database.IncomeContract;

/**
 * Created by Anton on 2016/03/23.
 */
public class IncomeListFragment  extends ListFragment
    implements LoaderManager.LoaderCallbacks<List<Income>>{

    private static final String LOG_TAG = IncomeListFragment.class.getSimpleName();
    private IncomeListAdapter mAdapter;
    private static int LOADER_ID = 1;
    private ContentResolver mContentResolver;
    private List<Income> mIncomeList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(false);
        mContentResolver = getActivity().getContentResolver();
        mAdapter = new IncomeListAdapter(getActivity(), getChildFragmentManager());
        setEmptyText("No income captured");
        setListAdapter(mAdapter);
        setListShown(false);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Income>> onCreateLoader(int id, Bundle args) {
        mContentResolver = getActivity().getContentResolver();
        return new IncomeListLoader(getActivity(), IncomeContract.URI_TABLE, mContentResolver);
    }

    @Override
    public void onLoadFinished(Loader<List<Income>> loader, List<Income> incomeSources) {
        mAdapter.setData(incomeSources);

        if(isResumed()){
            setListShown(true);
        }
        else{
            setListShownNoAnimation(true);
        }

    }


    @Override
    public void onLoaderReset(Loader<List<Income>> loader) {
        mAdapter.setData(null);
    }
}
