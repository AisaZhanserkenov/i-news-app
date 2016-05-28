package nodomain.com.i_news.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import nodomain.com.i_news.Config;
import nodomain.com.i_news.OnItemClickListener;
import nodomain.com.i_news.R;
import nodomain.com.i_news.adapters.CategoriesAdapter;
import nodomain.com.i_news.adapters.NewsAdapter;
import nodomain.com.i_news.models.News;
import nodomain.com.i_news.services.INewsService;
import nodomain.com.i_news.services.ServiceFactory;
import nodomain.com.i_news.utils.DividerItemDecoration;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsActivity extends BaseActivity implements OnItemClickListener{

    private static final String TAG = NewsActivity.class.getCanonicalName();

    private TextView title;
    private TextView description;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private NewsAdapter newsAdapter;

    private int categoryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("category"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryId = getIntent().getIntExtra("categoryId", 1);
        initUI();
        loadNews();
    }

    @Override
    protected void initUI(){
        title = (TextView) findViewById(R.id.news_title);
        description = (TextView) findViewById(R.id.news_description);

        recyclerView = (RecyclerView) findViewById(R.id.news_list);
        setupRecyclerView();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.content_news);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews();
            }
        });

    }

    private void setupRecyclerView(){
        newsAdapter = new NewsAdapter(this);
        newsAdapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newsAdapter);
    }

    public void loadNews(){
        newsAdapter.clear();
        getiNewsService().getNewsByCategory(categoryId)
                .flatMap(news -> Observable.from(news.getNews()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> {
                            newsAdapter.addNews(news);
                            swipeRefreshLayout.setRefreshing(false);
                        }, error -> Log.e(TAG, error.getMessage()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
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

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DetailedNewsActivity.class);
        intent.putExtra("id", newsAdapter.getNews(position).getId());
        intent.putExtra("category", getIntent().getStringExtra("category"));
        startActivity(intent);
    }
}
