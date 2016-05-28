package nodomain.com.i_news.models;

/**
 * Created by mukhamed.issa on 5/28/16.
 */

public class Illustration {

    private String small;
    private String large;

    public Illustration() {}

    public Illustration(String small, String large) {
        this.small = small;
        this.large = large;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}
