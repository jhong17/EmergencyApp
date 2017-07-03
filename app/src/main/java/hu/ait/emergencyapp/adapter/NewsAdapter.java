package hu.ait.emergencyapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import hu.ait.emergencyapp.R;
import hu.ait.emergencyapp.data.Article;

/**
 * Created by jessicahong on 6/29/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<Article> articleList;

    public NewsAdapter(){
        articleList = new ArrayList<Article>();

        for (int i = 0; i < 10; i++) {
            articleList.add(new Article("Article " + (i + 1), "description filler"));
        }

    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View newsRow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.news_row, parent, false);

        return new ViewHolder(newsRow);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        holder.articleTitle.setText(articleList.get(position).getTitle());
        holder.articleDescription.setText(articleList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView articleTitle;
        private TextView articleDescription;

        public ViewHolder(View itemView) {
            super(itemView);

            articleTitle = (TextView) itemView.findViewById(R.id.articleTitle);
            articleDescription = (TextView) itemView.findViewById(R.id.articleDescription);

        }
    }
}
