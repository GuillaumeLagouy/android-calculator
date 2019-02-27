package com.gmail.pro.glagouy.news.viewmodels;

import com.gmail.pro.glagouy.news.databases.DatabaseHelper;
import com.gmail.pro.glagouy.news.databases.NewsDatabase;
import com.gmail.pro.glagouy.news.networks.NetworkHelper;
import com.gmail.pro.glagouy.news.models.News;
import com.gmail.pro.glagouy.news.models.NewsList;
import com.gmail.pro.glagouy.news.networks.NewsService;
import com.gmail.pro.glagouy.news.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import bolts.Continuation;
import bolts.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 *  Gère les données relatifs à l'interface utilisateur
 */
public class NewsViewModel extends ViewModel {
    private MutableLiveData<List<News>> newsLiveData;
    private MutableLiveData<News> selected = new MutableLiveData<>();

    public LiveData<List<News>> getNews(){
        if(newsLiveData == null) {
            newsLiveData = new MutableLiveData<>();
            if(NetworkHelper.getNetworkStatus()){
                loadNews("test");
            } else {
                getNewsFromDb();
            }
        }
        return newsLiveData;
    }

    /**
     * @eamosse - Tu pourrais initialiser Retrofit dans NetworkHelper en variable statique
     * ainsi tu pourrais utiliser la meme instance pour faire tous tes appels
     */
    public void loadNews(String query){
        Retrofit retrofit = NetworkHelper.startRetrofit();

        NewsService service = retrofit.create(NewsService.class);

        final Call<NewsList> news = service.listNews(query, Constants.getApiKey());
        news.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(@NonNull Call<NewsList> call, @NonNull Response<NewsList> response) {
                NewsList responseBody = response.body();
                if(responseBody != null){
                    List<News> newsList = responseBody.getArticles();
                    newsLiveData.setValue(newsList);
                    saveNews(newsList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsList> call, @NonNull Throwable t) {
                System.out.println("Fail");
            }
        });
    }

    //Insérer les données dans la BDD
    private void saveNews(final List<News> news){
        Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() {
                DatabaseHelper.getDatabase().newsDao().insertAll(news);
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) {
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    public void updateOneNews(final News news){
        Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() {
                DatabaseHelper.getDatabase().newsDao().update(news);
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) {
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    //Récupérer les données de la BDD
    private void getNewsFromDb() {
        Task.callInBackground(new Callable<List<News>>() {
            @Override
            public List<News> call() {
                List<News> news = DatabaseHelper.getDatabase().newsDao().getAll();
                return news;
            }
        }).continueWith(new Continuation<List<News>, Void>() {
            @Override
            public Void then(Task<List<News>> task) {
                newsLiveData.setValue(task.getResult());
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    public void setSelected(News news){
        selected.postValue( news );
    }

    public LiveData<News> getSelected(){
        return selected;
    }
}
