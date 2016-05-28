package nodomain.com.i_news.models;

/**
 * Created by mukhamed.issa on 5/28/16.
 */

public abstract class AbstractModel {

    protected int id;
    protected String title;

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
}
