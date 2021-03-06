package nodomain.com.i_news.services;

import java.util.List;

import nodomain.com.i_news.models.News;
import nodomain.com.i_news.models.NewsResponse;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

import nodomain.com.i_news.Config;
import nodomain.com.i_news.models.Category;
import retrofit.http.GET;


/**
 * Created by mukhamed.issa on 5/27/16.
 */
public interface INewsService {

    String CATEGORIES_GET_ALL = "/category/get-all?" + Config.AUTH;
    String NEWS_GET_BY_CATEGORY = "/news/search?" + Config.AUTH + "&order[tstamp]=desc&limit=20";
    String NEWS_LOAD_MORE = "/news/search?" + Config.AUTH + "&order[tstamp]=desc&limit=20";
    String NEWS_GET_BY_ID = "/news/get-one?" + Config.AUTH;
    String NEWS_GET_SIMILAR = "/news/more-like-this?" + Config.AUTH + "&limit=5";

    @GET(CATEGORIES_GET_ALL)
    Observable<List<Category>> getCategories();

    @GET(NEWS_GET_BY_CATEGORY)
    Observable<NewsResponse> getNewsByCategory(@Query("query[cat_id]") int categoryId);

    @GET(NEWS_GET_BY_ID)
    Observable<News> getNewsById(@Query("id") int newsId);

    @GET(NEWS_GET_SIMILAR)
    Observable<NewsResponse> getSimilarNews(@Query("id") int newsId);

    @GET(NEWS_LOAD_MORE)
    Observable<NewsResponse> getMoreNews(@Query("query[cat_id]") int categoryId,
                                         @Query("offset") int offset);

}
