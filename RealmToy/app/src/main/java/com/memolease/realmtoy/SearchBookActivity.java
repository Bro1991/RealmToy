package com.memolease.realmtoy;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.memolease.realmtoy.util.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchBookActivity extends AppCompatActivity {
    EditText editText;
    Button mSearchButton;
    RecyclerView recycler_book_search;
    SearchAdapter searchAdapter;
    Retrofit retrofit;
    BookApiService bookApiService;
    ArrayList<NaverBook> naverBookArrayList = new ArrayList<>();
    LinearLayoutManager mLinearLayoutManager;
    Boolean isLoading = false;
    int startIndex = 1;
    Bus mBus = BusProvider.getInstance();
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        mContext = this;
        initRetrofit();
        initUI();
        initRecycler();
    }

    private void initUI() {
        editText = (EditText) findViewById(R.id.editText);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    mSearchButton.performClick();
                    return true;
                }
                return false;
            }
        });

        mSearchButton = (Button) findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchBookEvent event = new SearchBookEvent();
                event.setQuery(editText.getText().toString());
                event.setStart(startIndex);
                mBus.post(event);

                //searchBook(startIndex);
                editText.setEnabled(false);
            }
        });
    }

    private void initRecycler() {
        recycler_book_search = (RecyclerView) findViewById(R.id.recycler_book_search);
        recycler_book_search.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        searchAdapter = new SearchAdapter(this, naverBookArrayList);

        recycler_book_search.setLayoutManager(mLinearLayoutManager);
        recycler_book_search.setAdapter(searchAdapter);

        recycler_book_search.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if( !isLoading && !recyclerView.canScrollVertically(1) ){
                    isLoading = true;

                    NextSearchBookEvent event = new NextSearchBookEvent();
                    event.setStart(++startIndex);
                    event.setQuery(editText.getText().toString());
                    mBus.post(event);

                    //nextSearch(++startIndex);

                }
            }
        });
    }

    private void initRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new NApiInterceptor())
                .addInterceptor(new LoggingIntercepter())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.naver.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bookApiService = retrofit.create(BookApiService.class);
    }

    private void searchBook(int startIndex) {
        String query = editText.getText().toString();
        String target = "book.json";
        Call<Channel> getChannel = bookApiService.getChannel(target, query, 10, startIndex);
        getChannel.enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, retrofit2.Response<Channel> response) {
                //int postion = 0;
                Log.d("받은값", response.body().toString());
                List<NaverBook> naverBooks = fetchResults(response);

                for (NaverBook naverBook : naverBooks) {
                    naverBookArrayList.add(naverBook);
                   //postion++;
                }
                //int lastPosition = naverBookArrayList.size() - 1;
                //searchAdapter.bookSize = lastPosition + postion;
                isLoading = false;
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.d("에러", t.toString());
            }
        });

    }

    private void nextSearch(int startIndex) {
        String query = editText.getText().toString();
        String target = "book.json";
        int start = (startIndex -1) * 10 +1;
        Call<Channel> getChannel = bookApiService.getChannel(target, query, 10, start);
        getChannel.enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, retrofit2.Response<Channel> response) {
                //int postion = 0;
                Log.d("받은값", response.body().toString());
                List<NaverBook> naverBooks = fetchResults(response);

                for (NaverBook naverBook : naverBooks) {
                    naverBookArrayList.add(naverBook);
                    //postion++;
                }
                //int lastPosition = naverBookArrayList.size() - 1;
                //searchAdapter.bookSize = lastPosition + postion;
                isLoading = false;
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.d("에러", t.toString());
            }
        });

    }

    @Subscribe
    public void onSuccessFetchBookSearchs(ResponseBookSearchsEvent event){
        isLoading = false;
        editText.setEnabled(true);

        if (event.getItems() != null) {
            searchAdapter.addItems(event.getItems());
        }

    }

    private List<NaverBook> fetchResults(retrofit2.Response<Channel> response) {
        Channel channel = response.body();
        channel.getItems();
        return channel.getItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    public void finishActivity() {
        finish();
    }

}
