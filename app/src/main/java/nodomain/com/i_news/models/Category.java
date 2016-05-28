package nodomain.com.i_news.models;

/**
 * Created by mukhamed.issa on 5/27/16.
 */
public class Category extends AbstractModel{

    private String fullTitle;

    public Category() {}

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }
}
