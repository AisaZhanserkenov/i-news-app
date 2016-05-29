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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUI();
        if(AppUtils.isFirstRun(this)){
            if(AppUtils.isInternetAvailable(this)) {
                ORMFactory.getCategoryORM().delete(this);
                loadFromServer();
                Toast.makeText(this, "First run", Toast.LENGTH_LONG).show();
//                getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit()
//                        .putBoolean("isFirstRun", false).commit();
            }else {
                AppUtils.showErrorDialog(this);
            }
        }else{
            loadFromLocalDb();
        }

    }

    @Override
    protected void initUI(){
        recyclerView = (RecyclerView) findViewById(R.id.categories);
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
        compositeSubscription.add(getiNewsService().getCategories()
                .flatMap(Observable::from)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(category -> ORMFactory.getCategoryORM().insert(this.getBaseContext(), category))
                .subscribe(categoriesAdapter::addCategory,
                        error -> Log.e(TAG, error.getMessage())));
    }

    @Override
    protected void loadFromLocalDb(){
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


}
