package nodomain.com.i_news.utils.db.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import nodomain.com.i_news.models.Category;
import nodomain.com.i_news.models.News;
import nodomain.com.i_news.utils.db.DatabaseWrapper;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

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
            COLUMN_FULL_TITLE + " " + COLUMN_FULL_TITLE_TYPE +
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
        contentValues.put(COLUMN_TITLE, item.getTitle());
        contentValues.put(COLUMN_FULL_TITLE, item.getFullTitle());
        return contentValues;
    }

    @Override
    public Callable<List<Category>> get(Context context) {

        return new Callable<List<Category>>() {
            @Override
            public List<Category> call() throws Exception {
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
                    Log.i(TAG, "Categories are loaded");
                }

                database.close();

                return categoryList;
            }
        };
    }

    public Observable<List<Category>> getCategories(Context context){
        return makeObservable(get(context)).subscribeOn(Schedulers.computation());
    }


    @Override
    public Category cursorToObject(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        category.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        category.setFullTitle(cursor.getString(cursor.getColumnIndex(COLUMN_FULL_TITLE)));

        return category;
    }

    @Override
    public void delete(Context context) {
        DatabaseWrapper wrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = wrapper.getWritableDatabase();
        database.execSQL("DELETE FROM " + TABLE_NAME);

        database.close();
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(func.call());
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

}
