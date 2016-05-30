package nodomain.com.i_news.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import nodomain.com.i_news.R;
import nodomain.com.i_news.adapters.NewsAdapter;
import nodomain.com.i_news.adapters.SimilarNewsAdapter;
import nodomain.com.i_news.listeners.OnItemClickListener;
import nodomain.com.i_news.models.News;
import nodomain.com.i_news.utils.AppUtils;
import nodomain.com.i_news.utils.DateParser;
import nodomain.com.i_news.utils.DividerItemDecoration;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailedNewsActivity extends BaseActivity implements OnItemClickListener {

    private static final String TAG = DetailedNewsActivity.class.getCanonicalName();

    private TextView title;
    private TextView text;
    private TextView date;
    private TextView source;
    private TextView alsoRead;

    private ImageView backdrop;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private SimilarNewsAdapter similarNewsAdapter;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private int id;

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
        if(AppUtils.isInternetAvailable(this)) {
            loadFromServer();
        }else{
            AppUtils.showErrorDialog(this);
        }
    }

    @Override
    protected void initUI() {
        title = (TextView) findViewById(R.id.detailed_news_title);
        text = (TextView) findViewById(R.id.news_text);
        date = (TextView) findViewById(R.id.date);
        source = (TextView) findViewById(R.id.source);
        alsoRead = (TextView) findViewById(R.id.also_read);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        title.setTypeface(AppUtils.getTypeface(this));
        text.setTypeface(AppUtils.getTypeface(this));
        date.setTypeface(AppUtils.getTypeface(this));
        source.setTypeface(AppUtils.getTypeface(this));
        alsoRead.setTypeface(AppUtils.getTypeface(this));
        collapsingToolbarLayout.setCollapsedTitleTypeface(AppUtils.getTypeface(this));
        collapsingToolbarLayout.setExpandedTitleTypeface(AppUtils.getTypeface(this));

        backdrop = (ImageView) findViewById(R.id.backdrop);

        recyclerView = (RecyclerView) findViewById(R.id.similar_news_list);
        setupRecyclerView();


    }

    private void setupRecyclerView(){
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        similarNewsAdapter = new SimilarNewsAdapter(this);
        recyclerView.setAdapter(similarNewsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        similarNewsAdapter.setClickListener(this);
    }

    @Override
    protected void loadFromServer(){
        compositeSubscription.add(getiNewsService().getNewsById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> bindData(news),
                        error -> Log.e(TAG, error.getMessage())));

        loadSimilarNews();

    }

    private void loadSimilarNews(){
        compositeSubscription.add(getiNewsService().getSimilarNews(id)
                .flatMap(newsResponse -> Observable.from(newsResponse.getNews()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(similarNewsAdapter::addNews,
                        error -> Log.e(TAG, error.getMessage())));
    }

    @Override
    protected void loadFromLocalDb(){

    }

    private void loadPage(String url){
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void bindData(News news){
        title.setText(news.getTitle());
        text.setText(Html.fromHtml(news.getText_plain()));
        date.setText(String.format("%s %s", getResources().getString(R.string.date), DateParser.toString(news.getConvertedDate())));
        source.setText(String.format("%s %s", getResources().getString(R.string.url) , news.getUrl()));
        source.setOnClickListener(view -> loadPage(news.getUrl()));
        if(!news.isIllustrationNull()){
            Picasso.with(this)
                    .load(news.getIllustration().getLarge())
                    .error(R.drawable.placeholder)
                    .into(backdrop);
        }

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


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DetailedNewsActivity.class);
        intent.putExtra("id", similarNewsAdapter.getNews(position).getId());
        intent.putExtra("category", getIntent().getStringExtra("category"));
        startActivity(intent);
    }

}
