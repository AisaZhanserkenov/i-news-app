package nodomain.com.i_news.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import nodomain.com.i_news.OnItemClickListener;
import nodomain.com.i_news.OnLoadMoreListener;
import nodomain.com.i_news.R;
import nodomain.com.i_news.adapters.NewsAdapter;
import nodomain.com.i_news.models.News;
import nodomain.com.i_news.utils.db.orm.ORMFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsActivity extends BaseActivity implements OnItemClickListener{

    private static final String TAG = NewsActivity.class.getCanonicalName();

    private TextView title;
    private TextView description;
    private TextView toolbarTitle;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView.LayoutManager layoutManager;

    private NewsAdapter newsAdapter;

    private int categoryId;

    private Typeface typeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        categoryId = getIntent().getIntExtra("categoryId", 1);
        initUI();
        toolbarTitle.setText(getIntent().getStringExtra("category"));
//        if(isFirstRun()){
//            if(isInternetAvailable()) {
//                ORMFactory.getNewsORM().delete(this);
//                loadNewsFromServer();
//                Toast.makeText(this, "First run", Toast.LENGTH_LONG).show();
//                getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit()
//                        .putBoolean("isFirstRun", false).commit();
//            }else {
//                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
//            }
//        }else{
//            loadNewsFromLocalDb();
//        }

        loadNewsFromServer();
    }

    @Override
    protected void initUI(){
        typeface = Typeface.createFromAsset(getAssets(), "roboto.ttf");
        title = (TextView) findViewById(R.id.news_title);
        description = (TextView) findViewById(R.id.news_description);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(typeface);
        recyclerView = (RecyclerView) findViewById(R.id.news_list);
        setupRecyclerView();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.content_news);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNewsFromServer();
            }
        });

    }

    private void setupRecyclerView(){
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        newsAdapter = new NewsAdapter(this, recyclerView);
        recyclerView.setAdapter(newsAdapter);
        newsAdapter.setClickListener(this);
        newsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                newsAdapter.addNews(null);
                loadMore();
            }
        });
    }

    private void loadNewsFromServer(){
        newsAdapter.clear();
        compositeSubscription.add(getiNewsService().getNewsByCategory(categoryId)
                .flatMap(news -> Observable.from(news.getNews()))
                .toSortedList(News::compareTo)
                .flatMap(sorted -> Observable.from(sorted))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(news -> ORMFactory.getNewsORM().insert(this.getBaseContext(), news))
                .subscribe(news -> {
                            newsAdapter.removeProgressBar();
                            newsAdapter.addNews(news);
                            swipeRefreshLayout.setRefreshing(false);
                        }, error -> Log.e(TAG, error.getMessage())));
    }

    private void loadNewsFromLocalDb(){
        compositeSubscription.add(ORMFactory.getNewsORM().get(this)
                .flatMap(Observable::from)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsAdapter::addNews,
                        error -> Log.e(TAG, error.getMessage())));
    }

    private void loadMore(){
        getiNewsService().getMoreNews(categoryId, newsAdapter.getItemCount())
                .delay(1000, TimeUnit.MILLISECONDS)
                .flatMap(news -> Observable.from(news.getNews()))
                .toSortedList(News::compareTo)
                .flatMap(sorted -> Observable.from(sorted))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> {
                    newsAdapter.addNews(news);
                    newsAdapter.removeProgressBar();
                    Log.d(TAG, "loading more");
                    Log.d(TAG, newsAdapter.getItemCount() + "items");
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

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_refresh:
                loadNewsFromServer();
                break;
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
