package nodomain.com.i_news.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import nodomain.com.i_news.Config;
import nodomain.com.i_news.OnItemClickListener;
import nodomain.com.i_news.R;
import nodomain.com.i_news.adapters.CategoriesAdapter;
import nodomain.com.i_news.services.INewsService;
import nodomain.com.i_news.services.ServiceFactory;
import nodomain.com.i_news.utils.DividerItemDecoration;
import nodomain.com.i_news.utils.db.orm.ORMFactory;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CategoriesActivity extends BaseActivity implements OnItemClickListener{

    private static final String TAG = CategoriesActivity.class.getCanonicalName();

    private RecyclerView recyclerView;
    private CategoriesAdapter categoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUI();
        if(isFirstRun()){
            if(isInternetAvailable()) {
                ORMFactory.getCategoryORM().delete(this);
                loadCategoriesFromServer();
                Toast.makeText(this, "First run", Toast.LENGTH_LONG).show();
                getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit()
                        .putBoolean("isFirstRun", false).commit();
            }else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        }else{
            loadCategoriesFromLocalDb();
        }

    }

    @Override
    protected void initUI(){
        recyclerView = (RecyclerView) findViewById(R.id.categories);
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        categoriesAdapter = new CategoriesAdapter();
        categoriesAdapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(categoriesAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
    }

    private void loadCategoriesFromServer(){
        getiNewsService().getCategories()
                .flatMap(Observable::from)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(category -> ORMFactory.getCategoryORM().insert(this.getBaseContext(), category))
                .subscribe(categoriesAdapter::addCategory,
                        error -> Log.e(TAG, error.getMessage()));
    }

    private void loadCategoriesFromLocalDb(){
        ORMFactory.getCategoryORM().getCategories(this)
                .flatMap(Observable::from)
                .observeOn(AndroidSchedulers.mainThread())
                .limit(14)
                .subscribe(categoriesAdapter::addCategory,
                        error -> Log.e(TAG, error.getMessage()));
        Log.d(TAG, "loaded");
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, NewsActivity.class);
        intent.putExtra("category", categoriesAdapter.getCategory(position).getFullTitle());
        intent.putExtra("categoryId", categoriesAdapter.getCategory(position).getId());
        startActivity(intent);
    }
}
