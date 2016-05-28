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
import java.util.List;

import nodomain.com.i_news.OnItemClickListener;
import nodomain.com.i_news.R;
import nodomain.com.i_news.models.AbstractModel;
import nodomain.com.i_news.models.Category;
import nodomain.com.i_news.models.News;
import nodomain.com.i_news.utils.DateParser;

/**
 * Created by mukhamed.issa on 5/27/16.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    private static final int CONTENT_VIEW = R.layout.news_list_item;
    private static final int AD_VIEW = R.layout.news_ad;

    private List<News> news;
    private OnItemClickListener listener;
    private Context context;

    public NewsAdapter(Context context) {
        this.context = context;
        news = new ArrayList<News>();
    }

    public void addNews(News _news){
        news.add(_news);
        notifyDataSetChanged();
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = (viewType == CONTENT_VIEW) ? CONTENT_VIEW : AD_VIEW;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
//        if (position == 0 || position % 4 != 0) {
//            holder.bind(news.get(position));
//        }
        holder.bind(news.get(position));
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        return (position != 0 && position % 4 == 0) ? AD_VIEW : CONTENT_VIEW;
//    }

    public void setClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public News getNews(int position){
        return news.get(position);
    }

    public void clear(){
        news.clear();
        notifyDataSetChanged();
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView description;
        private TextView date;
        private TextView sourceUrl;
        private ImageView thumbnail;

        private Typeface typeface;

        public NewsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView)itemView.findViewById(R.id.news_title);
            description = (TextView) itemView.findViewById(R.id.news_description);
            date = (TextView) itemView.findViewById(R.id.date);
            sourceUrl = (TextView) itemView.findViewById(R.id.source_url);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

            typeface = Typeface.createFromAsset(context.getAssets(), "roboto.ttf");
            sourceUrl.setTypeface(typeface);
            title.setTypeface(typeface);
            date.setTypeface(typeface);
            description.setTypeface(typeface);
        }

        public void bind(final News news){
            title.setText(news.getTitle());
            description.setText(Html.fromHtml(news.getDescription_plain()));
            try {
                date.setText(DateParser.toString(news.getConvertedDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sourceUrl.setText(news.getUrl());
            if(!news.isIllustrationNull()){
                Picasso.with(context)
                        .load(news.getIllustration().getSmall())
                        .error(R.drawable.placeholder)
                        .into(thumbnail);
            }
        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }

}
