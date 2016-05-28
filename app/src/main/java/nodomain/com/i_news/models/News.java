package nodomain.com.i_news.models;

/**
 * Created by mukhamed.issa on 5/27/16.
 */
public class News {

    private int id;
    private String title;
    private String description_plain;
    private String text_plain;
    private Illustration illustration;

    public News() {}

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

    public String getDescription_plain() {
        return description_plain;
    }

    public void setDescription_plain(String description_plain) {
        this.description_plain = description_plain;
    }

    public Illustration getIllustration() {
        return illustration;
    }

    public void setIllustration(Illustration illustration) {
        this.illustration = illustration;
    }

    public boolean isIllustrationNull() {
        return illustration == null;
    }

    public String getText_plain() {
        return text_plain;
    }

    public void setText_plain(String text_plain) {
        this.text_plain = text_plain;
    }
}
