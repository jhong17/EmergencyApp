package hu.ait.emergencyapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import hu.ait.emergencyapp.MainActivity;
import hu.ait.emergencyapp.R;
import hu.ait.emergencyapp.data.Article;
import hu.ait.emergencyapp.data.Article2;
import hu.ait.emergencyapp.data.NewsResult;
import hu.ait.emergencyapp.retrofit.NewsAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jessicahong on 6/29/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<Article> articleList;
    private String currentTitle = "";
    private String currentDescription = "";
    private List<Article> listOfArticlesOnline = new ArrayList<>();

    public NewsAdapter(){
        articleList = new ArrayList<Article>();

        for (int i = 0; i < 10; i++) {

            articleList.add(new Article("HI", "YO"));
        }
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View newsRow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.news_row, parent, false);

        return new ViewHolder(newsRow);
    }

    @Override
    public void onBindViewHolder(final NewsAdapter.ViewHolder holder, final int position) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final NewsAPI newsAPI = retrofit.create(NewsAPI.class);

        final Call<NewsResult> currentNews = newsAPI.getNews("the-new-york-times", "top","f99da080518b4f39b6954bba91bea2bc");

        currentNews.enqueue(new Callback<NewsResult>() {
            @Override
            public void onResponse(Call<NewsResult> call, Response<NewsResult> response) {

                NewsResult newsResult = response.body();

                if (newsResult != null) {

                    for (int i = 0; i < 10; i++) {


                        String currentTitle = newsResult.getArticles().get(i).getTitle();
                        String currentDescription = newsResult.getArticles().get(i).getDescription();

                        listOfArticlesOnline.add(new Article(currentTitle, currentDescription));
                    }

                }


                holder.articleTitle.setText(listOfArticlesOnline.get(position).getTitle());
                holder.articleDescription.setText(listOfArticlesOnline.get(position).getDescription());
            }

            @Override
            public void onFailure(Call<NewsResult> call, Throwable t) {

            }
        });




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
