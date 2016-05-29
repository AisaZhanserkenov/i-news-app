package nodomain.com.i_news.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nodomain.com.i_news.listeners.OnItemClickListener;
import nodomain.com.i_news.listeners.OnLoadMoreListener;
import nodomain.com.i_news.R;
import nodomain.com.i_news.models.News;
import nodomain.com.i_news.utils.AppUtils;
import nodomain.com.i_news.utils.DateParser;

/**
 * Created by mukhamed.issa on 5/27/16.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final String TAG = NewsAdapter.class.getCanonicalName();

    private static final int CONTENT_VIEW = 0;
    public static final int PROGRESS_VIEW = 1;

    private List<News> news;
    private OnItemClickListener listener;
    private Context context;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public NewsAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        news = new ArrayList<News>();

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();

                            if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                if (AppUtils.isInternetAvailable(context)) {
                                    if (onLoadMoreListener != null) {
                                        onLoadMoreListener.onLoadMore();
                                    }
                                    loading = true;
                                }
                            }
                        }
                    });
        }
    }

    public void addNews(News _news){
        news.add(_news);
        notifyDataSetChanged();
    }

    public void remove(int position){
        news.remove(position);
        notifyItemRemoved(getItemCount());
        Log.d(TAG, "removed");
    }

    public void removeProgressBar(){
        news.removeAll(Collections.singleton(null));
        setLoaded();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == CONTENT_VIEW) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.news_list_item, parent, false);

            vh = new NewsViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_footer, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NewsViewHolder) {
            ((NewsViewHolder)holder).bind(news.get(position));
        }else{
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    @Override
    public int getItemViewType(int position) {
        return news.get(position) != null ? CONTENT_VIEW : PROGRESS_VIEW;
    }

    public void setClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public News getNews(int position){
        return news.get(position);
    }

    public void clear(){
        removeProgressBar();
        news.clear();
        notifyDataSetChanged();
    }

    public void setLoaded(){
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView date;
        private TextView sourceUrl;
        private ImageView thumbnail;

        private Typeface typeface;

        public NewsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> listener.onItemClick(view, getAdapterPosition()));
            title = (TextView)itemView.findViewById(R.id.news_title);
            description = (TextView) itemView.findViewById(R.id.news_description);
            date = (TextView) itemView.findViewById(R.id.date);
            sourceUrl = (TextView) itemView.findViewById(R.id.source_url);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

            sourceUrl.setTypeface(AppUtils.getTypeface(context));
            title.setTypeface(AppUtils.getTypeface(context));
            date.setTypeface(AppUtils.getTypeface(context));
            description.setTypeface(AppUtils.getTypeface(context));
        }

        public void bind(final News news){
            if(news.getTitle().length() > 75){
                title.setText(news.getTitle().substring(0, 75).concat("..."));
            }else {
                title.setText(news.getTitle());
            }
            description.setText(Html.fromHtml(news.getDescription_plain()));
            date.setText(DateParser.toString(news.getConvertedDate()));
            sourceUrl.setText(news.getUrl());
            if(!news.isIllustrationNull()){
                Picasso.with(context)
                        .load(news.getIllustration().getSmall())
                        .error(R.drawable.placeholder)
                        .into(thumbnail);
            }
        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.footer);
        }
    }

}
