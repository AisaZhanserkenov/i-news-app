package nodomain.com.i_news.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mukhamed.issa on 5/28/16.
 */

public class DatabaseWrapper extends SQLiteOpenHelper{

    private static final String TAG = DatabaseWrapper.class.getCanonicalName();

    public static final String DATABASE_NAME = "INewsCache.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseWrapper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Creating database [" + DATABASE_NAME + "]");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading database [" + DATABASE_NAME + "v." + oldVersion + "to v." + newVersion + "]");
    }
}
