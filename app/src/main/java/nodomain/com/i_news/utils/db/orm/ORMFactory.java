package nodomain.com.i_news.utils.db.orm;

/**
 * Created by mukhamed.issa on 5/28/16.
 */

public class ORMFactory {

    private static CategoryORM categoryORM = null;
    private static NewsORM newsORM = null;

    public static CategoryORM getCategoryORM() {
        if(categoryORM == null){
            categoryORM = new CategoryORM();
        }

        return categoryORM;
    }

    public static NewsORM getNewsORM() {
        if(newsORM == null) {
            newsORM = new NewsORM();
        }

        return  newsORM;
    }

}
