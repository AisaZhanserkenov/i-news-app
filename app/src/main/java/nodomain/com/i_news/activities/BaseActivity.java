package nodomain.com.i_news.activities;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import nodomain.com.i_news.Config;
import nodomain.com.i_news.R;
import nodomain.com.i_news.services.INewsService;
import nodomain.com.i_news.services.ServiceFactory;

public abstract class BaseActivity extends AppCompatActivity {

    private INewsService iNewsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iNewsService = ServiceFactory.createRetrofitService(INewsService.class, Config.BASE_URL);
    }

    protected INewsService getiNewsService(){
        return iNewsService;
    }

    protected abstract void initUI();

    protected boolean isFirstRun() {
        return getSharedPreferences("PREFERENCES", MODE_PRIVATE).getBoolean("isFirstRun", true);
    }

    protected boolean isInternetAvailable(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

}
