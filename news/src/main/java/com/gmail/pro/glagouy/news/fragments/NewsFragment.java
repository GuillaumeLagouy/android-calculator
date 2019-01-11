package com.gmail.pro.glagouy.news.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.pro.glagouy.news.R;
import com.gmail.pro.glagouy.news.adapters.NewsAdapter;
import com.gmail.pro.glagouy.news.databases.NewsDatabase;
import com.gmail.pro.glagouy.news.models.News;
import com.gmail.pro.glagouy.news.models.NewsList;
import com.gmail.pro.glagouy.news.networks.NewsService;
import com.gmail.pro.glagouy.news.utils.Constants;
import com.gmail.pro.glagouy.news.viewmodels.NewsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import bolts.Continuation;
import bolts.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsFragment extends Fragment{
    private List<News> newsList;
    View rootView;
    RecyclerView recyclerView;
    NewsAdapter adapter;
    NewsDatabase db;
    private NewsViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(NewsViewModel.class);

        /*
        newsViewModel.getNews();

        if(isNetworkConnected()){
            newsViewModel.getNews();
        } else {
            getNewsFromDb();
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recycler, container, false);
        setNews(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model.getNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                adapter.setNews(news);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void saveNews(final List<News> news){
        Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() {
                if(getContext() != null) {
                    db = Room.databaseBuilder(getContext(),
                            NewsDatabase.class, "news-db").build();
                    db.newsDao().insertAll(news);
                }
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) {
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    private void getNewsFromDb() {
        Task.callInBackground(new Callable<List<News>>() {
            @Override
            public List<News> call() {
                if(getContext() != null) {
                    db = Room.databaseBuilder(getContext(),
                            NewsDatabase.class, "news-db").build();
                    return db.newsDao().getAll();
                }
                return null;
            }
        }).continueWith(new Continuation<List<News>, Void>() {
            @Override
            public Void then(Task<List<News>> task) {
                adapter.setNews(task.getResult());
                adapter.notifyDataSetChanged();
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    void setNews(View view){
        recyclerView = view.findViewById(R.id.list_news);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        adapter = new NewsAdapter(new ArrayList<News>());
        recyclerView.setAdapter(adapter);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
