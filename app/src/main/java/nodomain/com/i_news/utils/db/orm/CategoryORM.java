package nodomain.com.i_news.utils.db.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nodomain.com.i_news.models.Category;
import nodomain.com.i_news.utils.db.DatabaseWrapper;

/**
 * Created by mukhamed.issa on 5/28/16.
 */

public class CategoryORM implements IOrm<Category> {

    public static final String TAG = CategoryORM.class.getCanonicalName();
    public static final String TABLE_NAME = "categories";
    public static final String COMMA_SEPARATOR = ", ";

    private static final String COLUMN_ID_TYPE = "INTEGER PRIMARY KEY";
    private static final String COLUMN_ID = "id";

    private static final String COLUMN_TITLE_TYPE = "TEXT";
    private static final String COLUMN_TITLE = "title";

    private static final String COLUMN_FULL_TITLE_TYPE = "TEXT";
    private static final String COLUMN_FULL_TITLE = "full_title";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " " + COLUMN_ID_TYPE + COMMA_SEPARATOR +
            COLUMN_TITLE  + " " + COLUMN_TITLE_TYPE + COMMA_SEPARATOR +
            COLUMN_FULL_TITLE + " " + COLUMN_FULL_TITLE_TYPE + COMMA_SEPARATOR +
            ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    @Override
    public void insert(Context context, Category item) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();
        ContentValues contentValues = objectToContentValues(item);
        database.insert(TABLE_NAME, "null", contentValues);

        Log.i(TAG, "Inserted category with id: " + item.getId());
        database.close();
    }

    @Override
    public ContentValues objectToContentValues(Category item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, item.getId());
        contentValues.put(COLUMN_TITLE, item.getTitle());
        contentValues.put(COLUMN_FULL_TITLE, item.getFullTitle());
        return contentValues;
    }

    @Override
    public List<Category> get(Context context) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        Log.i(TAG, "Loaded " + cursor.getCount() + " categories");
        List<Category> categoryList = new ArrayList<Category>();

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Category category = cursorToObject(cursor);
                categoryList.add(category);
                cursor.moveToNext();
            }
            Log.i(TAG, "Posts are loaded");
        }

        database.close();

        return categoryList;
    }

    @Override
    public Category cursorToObject(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        category.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        category.setFullTitle(cursor.getString(cursor.getColumnIndex(COLUMN_FULL_TITLE)));

        return category;
    }
}
