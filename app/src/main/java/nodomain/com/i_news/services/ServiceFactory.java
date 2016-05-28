package nodomain.com.i_news.services;

import retrofit.RestAdapter;

/**
 * Created by mukhamed.issa on 5/27/16.
 */
public class ServiceFactory {

    public static <T> T createRetrofitService(final Class<T> tClass,
                                              final String endPoint){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(endPoint)
                .build();

        return adapter.create(tClass);

    }
}
