package za.co.wyldeweb.budgetapp.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Anton on 2016/03/23.
 */
public class IncomeContract {
    public interface IncomeColumns{
        String INCOME_ID = "_id";
        String INCOME_DESCRIPTION = "description";
        String INCOME_AMOUNT = "amount";
        String INCOME_DATE = "date";
        String INCOME_DATE_ADDED = "dateAdded";
    }

    public static final String CONTENT_AUTHORITY = "za.co.wyldeweb.budgetapp.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INCOME = "income";
    public static  final Uri URI_TABLE = Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATH_INCOME);

    public static final String[] TOP_LEVEL_PATHS = {
            PATH_INCOME
    };

    public static class IncomeSources implements IncomeColumns, BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_INCOME).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "budgetapp";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "budgetapp";

        public static Uri buildIncomeUri(String incomeId){
            return CONTENT_URI.buildUpon().appendEncodedPath(incomeId).build();
        }

        public static String getIncomeId(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }

}
