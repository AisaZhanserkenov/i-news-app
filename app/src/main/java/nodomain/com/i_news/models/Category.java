package nodomain.com.i_news.models;

/**
 * Created by mukhamed.issa on 5/27/16.
 */
public class Category {

    private int id;
    private String title;
    private String fullTitle;

    public Category() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }
}
