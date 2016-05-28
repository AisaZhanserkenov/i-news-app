package nodomain.com.i_news.models;

import java.text.ParseException;
import java.util.Date;

import nodomain.com.i_news.utils.DateParser;

/**
 * Created by mukhamed.issa on 5/27/16.
 */
public class News extends AbstractModel {

    private String description_plain;
    private String text_plain;
    private int category;
    private Illustration illustration;
    private String date;
    private String url;

    public News() {}

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getConvertedDate() throws ParseException {
        return DateParser.parse(date);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
