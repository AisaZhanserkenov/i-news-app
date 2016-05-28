package nodomain.com.i_news.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nodomain.com.i_news.OnItemClickListener;
import nodomain.com.i_news.R;
import nodomain.com.i_news.models.Category;

/**
 * Created by mukhamed.issa on 5/27/16.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {


    private List<Category> categories;
    private OnItemClickListener listener;

    public CategoriesAdapter() {
        categories = new ArrayList<Category>();
    }

    public void addCategory(Category category){
        categories.add(category);
        notifyDataSetChanged();
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_list_item, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public Category getCategory(int position){
        return categories.get(position);
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;

        public CategoriesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView)itemView.findViewById(R.id.title);
        }

        public void bind(final Category category){
            title.setText(category.getTitle());
        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
