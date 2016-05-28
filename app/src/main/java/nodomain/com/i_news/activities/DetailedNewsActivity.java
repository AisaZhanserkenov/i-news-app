package nodomain.com.i_news.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import nodomain.com.i_news.R;
import nodomain.com.i_news.models.News;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailedNewsActivity extends BaseActivity {

    private static final String TAG = DetailedNewsActivity.class.getCanonicalName();

    private TextView title;
    private TextView text;

    private int id;

    private Typeface typeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("category"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getIntExtra("id", 1);
        initUI();
        loadNewsData();
    }

    @Override
    protected void initUI() {
        title = (TextView) findViewById(R.id.detailed_news_title);
        text = (TextView) findViewById(R.id.news_text);
        typeface = Typeface.createFromAsset(getAssets(), "roboto.ttf");
        title.setTypeface(typeface);
        text.setTypeface(typeface);
    }

    private void loadNewsData(){
        getiNewsService().getNewsById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> bindData(news),
                        error -> Log.e(TAG, error.getMessage()));
    }

    private void bindData(News news){
        title.setText(news.getTitle());
        text.setText(news.getText_plain());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detailed_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
