package za.co.wyldeweb.budgetapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Anton on 2016/03/23.
 */
public class BudgetDatabase extends SQLiteOpenHelper {
    private static final String LOG_TAG = BudgetDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "budgetapp.db";
    private static final int DATABASE_VERSION = 3;
    private final Context mContext;

    interface Tables{
        String INCOME = "income";
    }

    public BudgetDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.INCOME + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + IncomeContract.IncomeColumns.INCOME_DESCRIPTION + " TEXT NOT NULL,"
                + IncomeContract.IncomeColumns.INCOME_AMOUNT + " DECIMAL(10,2) NOT NULL,"
                + IncomeContract.IncomeColumns.INCOME_DATE + " INT NULL,"
                + IncomeContract.IncomeColumns.INCOME_DATE_ADDED + " INT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;

        if(version == 1){
            version = 2;
        }
        else if(version == 2){
            version = 3;
        }

        if(version != DATABASE_VERSION){
            db.execSQL("DROP TABLE IF EXISTS " + Tables.INCOME);
            onCreate(db);
        }
    }

    public static  void deleteDatabase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }
}
