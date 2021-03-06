package com.gmail.pro.glagouy.news.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.gmail.pro.glagouy.news.R;
import com.gmail.pro.glagouy.news.adapters.NewsAdapter;
import com.gmail.pro.glagouy.news.listeners.NewsListener;
import com.gmail.pro.glagouy.news.models.News;
import com.gmail.pro.glagouy.news.viewmodels.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 */
public class NewsFragment extends Fragment implements NewsListener {
    private NewsAdapter adapter;
    private NewsViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(NewsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler, container, false);

        search(rootView);
        setNews(rootView);

        return rootView;
    }

    /**
     * il est préférable d'observer la liste des articles avec getViewLifecycleOwner() au lieu de this
     * pourquoi?
     * en this faire référence au fragment qui ne se détruit pas tout de suite et par conséquent
     * tu continues d'obsrver les changements sur cette liste même quand la vue est détruite
     * par contre, si ton tu observes avec getViewLifecycleOwner(), quand la vue est détruite
     * comme par exemple, lors d'un changement de frgment, ton fragment arrête d'observer les
     * changements sur ce livedata.
     * Alors, à quel moment faut-il observer avec this ou avec getViewLifecycleOwner()??
     * La réponse est simple:
     * 1. si les changments nécessitent de modifier la vue, utilise getViewLifecycleOwner()
     * 2. sinon, utilise this
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model.getNews().observe(getViewLifecycleOwner(), new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                adapter.setNews(news);
                adapter.notifyDataSetChanged();
            }
        });
    }

    void setNews(View view){
        RecyclerView recyclerView = view.findViewById(R.id.list_news);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        adapter = new NewsAdapter(new ArrayList<News>(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSelect(News news) {
        NewsDetailFragment detailFragment = new NewsDetailFragment();
        FragmentTransaction detailTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        detailTransaction.replace(R.id.fragment_container, detailFragment);
        detailTransaction.addToBackStack(null);
        detailTransaction.commit();

        model.setSelected(news);
    }

    @Override
    public void onLike(News news) {
        this.model.updateOneNews(news);
    }

    public void search(View view){
        SearchView searchView = view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                model.loadNews(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
