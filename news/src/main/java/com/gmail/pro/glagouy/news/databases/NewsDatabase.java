package com.gmail.pro.glagouy.news.databases;

import com.gmail.pro.glagouy.news.databases.daos.NewsDao;
import com.gmail.pro.glagouy.news.models.News;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {News.class}, version = 2, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();
}
