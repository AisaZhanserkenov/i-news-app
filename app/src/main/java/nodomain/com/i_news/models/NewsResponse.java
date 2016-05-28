package nodomain.com.i_news.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mukhamed.issa on 5/28/16.
 */

public class NewsResponse {

    private List<News> news;

    public NewsResponse() {
        news = new ArrayList<News>();
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
}
