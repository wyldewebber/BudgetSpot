package za.co.wyldeweb.budgetapp.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Anton on 2016/03/23.
 */
public class IncomeProvider extends ContentProvider{
    private BudgetDatabase mOpenHelper;
    private static String LOG_TAG = IncomeProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int INCOME_SOURCES = 100;
    private static final int INCOME_SOURCES_ID = 101;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = IncomeContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,"income", INCOME_SOURCES);
        matcher.addURI(authority,"income/*", INCOME_SOURCES_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BudgetDatabase(getContext());
        return true;
    }

    private void deleteDatabase(){
        mOpenHelper.close();
        BudgetDatabase.deleteDatabase(getContext());
        mOpenHelper = new BudgetDatabase(getContext());
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(BudgetDatabase.Tables.INCOME);

        switch(match){
            case INCOME_SOURCES:
                break;
            case INCOME_SOURCES_ID:
                String id = IncomeContract.IncomeSources.getIncomeId(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch(match){
            case INCOME_SOURCES:
                return IncomeContract.IncomeSources.CONTENT_TYPE;
            case INCOME_SOURCES_ID:
                return IncomeContract.IncomeSources.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(LOG_TAG, "insert(uri=" + uri + ",values=" + values.toString());

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch(match){
            case INCOME_SOURCES:
                long recordId = db.insertOrThrow(BudgetDatabase.Tables.INCOME, null, values);
                return  IncomeContract.IncomeSources.buildIncomeUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(LOG_TAG, "delete(uri=" + uri);

        if(uri.equals(IncomeContract.URI_TABLE)){
            deleteDatabase();
            return 0;
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match){
            case INCOME_SOURCES_ID:
                String id = IncomeContract.IncomeSources.getIncomeId(uri);
                String selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");

                return db.delete(BudgetDatabase.Tables.INCOME, selectionCriteria, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(LOG_TAG, "update(uri=" + uri + ", values=" + values.toString());

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String id;
        String selectionCriteria = selection;

        switch (match){
            case INCOME_SOURCES:
                id = IncomeContract.IncomeSources.getIncomeId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            case INCOME_SOURCES_ID:
                id = IncomeContract.IncomeSources.getIncomeId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        return db.update(BudgetDatabase.Tables.INCOME, values, selectionCriteria, selectionArgs);
    }
}
