package nodomain.com.i_news.utils.db.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telecom.Call;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import nodomain.com.i_news.models.Illustration;
import nodomain.com.i_news.models.News;
import nodomain.com.i_news.utils.db.DatabaseWrapper;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by mukhamed.issa on 5/28/16.
 */

public class NewsORM implements IOrm<News> {

    public static final String TAG = NewsORM.class.getCanonicalName();
    public static final String TABLE_NAME = "news";
    public static final String COMMA_SEPARATOR = ", ";

    private static final String COLUMN_ID_TYPE = "INTEGER PRIMARY KEY";
    private static final String COLUMN_ID = "id";

    private static final String COLUMN_NEWS_ID_TYPE = "INTEGER";
    private static final String COLUMN_NEWS_ID = "news_id";

    private static final String COLUMN_CATEGORY_ID_TYPE = "INTEGER";
    private static final String COLUMN_CATEGORY_ID = "category_id";

    private static final String COLUMN_TITLE_TYPE = "TEXT";
    private static final String COLUMN_TITLE = "title";

    private static final String COLUMN_DESCRIPTION_TYPE = "TEXT";
    private static final String COLUMN_DESCRIPTION = "description";

    private static final String COLUMN_TEXT_TYPE = "TEXT";
    private static final String COLUMN_TEXT = "text";

    private static final String COLUMN_ILLUSTRATION_SMALL_TYPE = "TEXT";
    private static final String COLUMN_ILLUSTRATION_SMALL = "illustration_small";

    private static final String COLUMN_ILLUSTRATION_LARGE_TYPE = "TEXT";
    private static final String COLUMN_ILLUSTRATION_LARGE = "illustration_large";

    private static final String COLUMN_URL_TYPE = "TEXT";
    private static final String COLUMN_URL = "url";

    private static final String COLUMN_DATE_TYPE = "TEXT";
    private static final String COLUMN_DATE = "date";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " " + COLUMN_ID_TYPE + COMMA_SEPARATOR +
            COLUMN_NEWS_ID + " " + COLUMN_NEWS_ID_TYPE + COMMA_SEPARATOR +
            COLUMN_CATEGORY_ID + " " + COLUMN_CATEGORY_ID_TYPE + COMMA_SEPARATOR +
            COLUMN_TITLE  + " " + COLUMN_TITLE_TYPE + COMMA_SEPARATOR +
            COLUMN_DESCRIPTION + " " + COLUMN_DESCRIPTION_TYPE + COMMA_SEPARATOR +
            COLUMN_TEXT + " " + COLUMN_TEXT_TYPE + COMMA_SEPARATOR +
            COLUMN_ILLUSTRATION_SMALL + " " + COLUMN_ILLUSTRATION_SMALL_TYPE + COMMA_SEPARATOR +
            COLUMN_ILLUSTRATION_LARGE + " " + COLUMN_ILLUSTRATION_LARGE_TYPE + COMMA_SEPARATOR +
            COLUMN_URL + " " + COLUMN_URL_TYPE + COMMA_SEPARATOR +
            COLUMN_DATE + COLUMN_DATE_TYPE +
            ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private int categoryId;

    @Override
    public void insert(Context context, News item) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        ContentValues contentValues = objectToContentValues(item);
        database.insert(TABLE_NAME, "null", contentValues);

        Log.i(TAG, "Inserted news with id: " + item.getId());
        database.close();
    }

    @Override
    public ContentValues objectToContentValues(News item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NEWS_ID, item.getId());
        contentValues.put(COLUMN_CATEGORY_ID, item.getCategory());
        contentValues.put(COLUMN_TITLE, item.getTitle());
        contentValues.put(COLUMN_DESCRIPTION, item.getDescription_plain());
        contentValues.put(COLUMN_TEXT, item.getText_plain());
        contentValues.put(COLUMN_ILLUSTRATION_SMALL, item.getIllustration().getSmall());
        contentValues.put(COLUMN_ILLUSTRATION_LARGE, item.getIllustration().getLarge());
        contentValues.put(COLUMN_URL, item.getUrl());
        contentValues.put(COLUMN_DATE, item.getDate());

        return contentValues;
    }

    @Override
    public Observable<List<News>> get(Context context) {
        return makeObservable(getNews(context)).subscribeOn(Schedulers.computation());
    }

    private Callable<List<News>> getNews(Context context){
        return new Callable<List<News>>() {
            @Override
            public List<News> call() throws Exception {
                DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
                SQLiteDatabase database = databaseWrapper.getReadableDatabase();
                Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CATEGORY_ID + "=" + categoryId, null);

                Log.i(TAG, "Loaded " + cursor.getCount() + " news with category id: " + categoryId);
                List<News> newsList = new ArrayList<News>();

                if(cursor.getCount() > 0){
                    cursor.moveToFirst();
                    while(!cursor.isAfterLast()){
                        News news = cursorToObject(cursor);
                        newsList.add(news);
                        cursor.moveToNext();
                    }
                    Log.i(TAG, "News are loaded");
                }
                database.close();

                return newsList;
            }
        };
    }

    public Observable<List<News>> getNewsByCategory(Context context, int categoryId) {
        this.categoryId = categoryId;
        return get(context);
    }

    @Override
    public News cursorToObject(Cursor cursor) {
        News news = new News();
        news.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_NEWS_ID)));
        news.setCategory(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID)));
        news.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        news.setDescription_plain(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
        news.setText_plain(cursor.getString(cursor.getColumnIndex(COLUMN_TEXT)));
        news.setIllustration(new Illustration(cursor.getString(cursor.getColumnIndex(COLUMN_ILLUSTRATION_SMALL)),
                cursor.getString(cursor.getColumnIndex(COLUMN_ILLUSTRATION_LARGE))));
        news.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
        news.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));

        return news;
    }

    @Override
    public void delete(Context context) {
        DatabaseWrapper wrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = wrapper.getWritableDatabase();
        database.execSQL("DELETE FROM " + TABLE_NAME);

        database.close();
    }

    private <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(func.call());
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
