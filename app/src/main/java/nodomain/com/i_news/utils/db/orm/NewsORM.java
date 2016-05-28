package nodomain.com.i_news.utils.db.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import nodomain.com.i_news.models.News;

/**
 * Created by mukhamed.issa on 5/28/16.
 */

public class NewsORM implements IOrm<News> {

    @Override
    public void insert(Context context, News item) {

    }

    @Override
    public ContentValues objectToContentValues(News item) {
        return null;
    }

    @Override
    public List<News> get(Context context) {
        return null;
    }

    @Override
    public News cursorToObject(Cursor cursor) {
        return null;
    }
}
