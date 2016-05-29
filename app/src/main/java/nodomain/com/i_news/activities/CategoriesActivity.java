package nodomain.com.i_news.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import nodomain.com.i_news.listeners.OnItemClickListener;
import nodomain.com.i_news.R;
import nodomain.com.i_news.adapters.CategoriesAdapter;
import nodomain.com.i_news.utils.AppUtils;
import nodomain.com.i_news.utils.DividerItemDecoration;
import nodomain.com.i_news.utils.db.orm.ORMFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CategoriesActivity extends BaseActivity implements OnItemClickListener{

    private static final String TAG = CategoriesActivity.class.getCanonicalName();

    private RecyclerView recyclerView;
    private CategoriesAdapter categoriesAdapter;
    private TextView toolbarTitle;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initUI();
        if(AppUtils.isInternetAvailable(this)) {
            ORMFactory.getCategoryORM().delete(this);
            loadFromServer();
        }else {
            AppUtils.showSnackbar(findViewById(R.id.base_layout), this);
            loadFromLocalDb();
        }

    }

    @Override
    protected void initUI(){
        recyclerView = (RecyclerView) findViewById(R.id.categories);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(AppUtils.getTypeface(this));
        toolbarTitle.setText(getResources().getString(R.string.i_news));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        categoriesAdapter = new CategoriesAdapter(this);
        categoriesAdapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(categoriesAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    protected void loadFromServer(){
        changeStateOfProgressBar(true);
        compositeSubscription.add(getiNewsService().getCategories()
                .flatMap(Observable::from)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(category -> ORMFactory.getCategoryORM().insert(this.getBaseContext(), category))
                .subscribe(category -> {
                    categoriesAdapter.addCategory(category);
                    changeStateOfProgressBar(false);
                }, error -> Log.e(TAG, error.getMessage())));
    }

    @Override
    protected void loadFromLocalDb(){
        changeStateOfProgressBar(false);
        compositeSubscription.add(ORMFactory.getCategoryORM().get(this)
                .flatMap(Observable::from)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categoriesAdapter::addCategory,
                        error -> Log.e(TAG, error.getMessage())));
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, NewsActivity.class);
        intent.putExtra("category", categoriesAdapter.getCategory(position).getFullTitle());
        intent.putExtra("categoryId", categoriesAdapter.getCategory(position).getId());
        startActivity(intent);
    }

    private void changeStateOfProgressBar(boolean isVisible){
        progressBar.setVisibility(isVisible ? View.VISIBLE: View.GONE);
        progressBar.setIndeterminate(isVisible);
    }


}
