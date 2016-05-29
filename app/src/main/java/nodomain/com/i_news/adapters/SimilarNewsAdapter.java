package nodomain.com.i_news.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nodomain.com.i_news.R;
import nodomain.com.i_news.listeners.OnItemClickListener;
import nodomain.com.i_news.models.Category;
import nodomain.com.i_news.models.News;
import nodomain.com.i_news.utils.AppUtils;

/**
 * Created by mukhamed.issa on 5/29/16.
 */

public class SimilarNewsAdapter extends RecyclerView.Adapter<SimilarNewsAdapter.SimilarNewsViewHolder> {


    private List<News> similarNews;
    private OnItemClickListener listener;

    private Context context;

    public SimilarNewsAdapter(Context context) {
        this.context = context;
        similarNews = new ArrayList<News>();
    }

    public void addNews(News news){
        similarNews.add(news);
        notifyDataSetChanged();
    }

    @Override
    public SimilarNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.similar_list_item, parent, false);
        return new SimilarNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimilarNewsViewHolder holder, int position) {
        holder.bind(similarNews.get(position));
    }

    @Override
    public int getItemCount() {
        return similarNews.size();
    }

    public void setClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public News getNews(int position){
        return similarNews.get(position);
    }

    public class SimilarNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;

        public SimilarNewsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView)itemView.findViewById(R.id.title);
            title.setTypeface(AppUtils.getTypeface(context));
        }

        public void bind(final News news){
            title.setText(news.getTitle());
        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}

