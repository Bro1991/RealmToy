package com.memolease.realmtoy.client;

import android.util.Log;

import com.memolease.realmtoy.BookApiService;
import com.memolease.realmtoy.LoggingIntercepter;
import com.memolease.realmtoy.NApiInterceptor;
import com.memolease.realmtoy.NaverBook;
import com.memolease.realmtoy.NextSearchBookEvent;
import com.memolease.realmtoy.ResponseBookSearchsEvent;
import com.memolease.realmtoy.SearchBookEvent;
import com.memolease.realmtoy.util.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bro on 2017-08-18.
 */

public class SearchClient {
    private static SearchClient mSearchClient;
    private Bus mBus;
    private BookApiService bookApiService;

    public static SearchClient getClient() {
        if (mSearchClient == null) {
            mSearchClient = new SearchClient();
        }

        return mSearchClient;
    }

    private SearchClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new NApiInterceptor())
                .addInterceptor(new LoggingIntercepter())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.naver.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bookApiService = retrofit.create(BookApiService.class);
        this.mBus = BusProvider.getInstance();
        this.mBus.register(this);
    }

    @Subscribe
    public void nextSearchBook(NextSearchBookEvent event) {
        String query = event.getQuery();
        int display = 10;
        int start = (event.getStart()-1) * display + 1;
        String target = "book.json";
        bookApiService.getResponseBook(target, query, display, start).enqueue(new Callback<ResponseBookSearchsEvent>() {
            @Override
            public void onResponse(Call<ResponseBookSearchsEvent> call, Response<ResponseBookSearchsEvent> response) {
                if (response.isSuccessful()) {
                    mBus.post(response.body());
                    Log.d("다음 결과값", String.valueOf(response.body().getItems().size()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBookSearchsEvent> call, Throwable t) {

            }
        });

    }

    @Subscribe
    public void searchBook(SearchBookEvent event) {
        String query = event.getQuery();
        int display = 10;
        int start = 1;
        String target = "book.json";

        bookApiService.getResponseBook(target, query, display, start).enqueue(new Callback<ResponseBookSearchsEvent>() {
            @Override
            public void onResponse(Call<ResponseBookSearchsEvent> call, Response<ResponseBookSearchsEvent> response) {
                if (response.isSuccessful()) {
                    mBus.post(response.body());
                    Log.d("찾아온 결과값", String.valueOf(response.body().getItems().size()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBookSearchsEvent> call, Throwable t) {

            }
        });

    }
}
