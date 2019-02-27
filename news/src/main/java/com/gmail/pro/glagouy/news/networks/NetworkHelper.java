package com.gmail.pro.glagouy.news.networks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.gmail.pro.glagouy.news.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  attention aux warnings
 */
public class NetworkHelper {
    private static boolean isConnected;

    public static void init(final Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnected();
    }

    public static boolean getNetworkStatus(){
        return isConnected;
    }

    public static Retrofit startRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(Constants.getUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
