package nodomain.com.i_news.utils.db.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import nodomain.com.i_news.models.AbstractModel;
import rx.Observable;

/**
 * Created by mukhamed.issa on 5/28/16.
 */

public interface IOrm<T extends AbstractModel> {

    public void insert(Context context, T item);
    public Observable<List<T>> get(Context context);
    public ContentValues objectToContentValues(T item);
    public T cursorToObject(Cursor cursor);
    public void delete(Context context);
}
