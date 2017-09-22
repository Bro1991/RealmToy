package com.memolease.realmtoy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.memolease.realmtoy.model.Book;
import com.memolease.realmtoy.util.BackPressFinishHandler;
import com.memolease.realmtoy.util.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    RecyclerView book_recycler;
    BookAdapter bookAdapter;
    ArrayList<NaverBook> naverBookArrayList = new ArrayList<>();
    List<Book> bookList = new ArrayList<>();

    GridLayoutManager mLayoutManager;
    Context mContext;
    static final int ADD_BOOK = 2004;
    int postion = 0;
    Bus mBus = BusProvider.getInstance();
    private Realm realm;
    private BackPressFinishHandler backPressFinishHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mBus.register(this);
        realm = Realm.getDefaultInstance();
        backPressFinishHandler = new BackPressFinishHandler(this);
        //editText = (EditText) findViewById(R.id.editText);
        //search_button = (Button) findViewById(R.id.search_button);
/*        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        text5 = (TextView) findViewById(R.id.text5);*/

/*        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new NApiInterceptor())
                .addInterceptor(new LoggingIntercepter())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.naver.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bookApiService = retrofit.create(BookApiService.class);*/
        initRecycler();
        initRealm();


/*        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBook();
                //getResponse();
*//*
                try {
                    String query = editText.getText().toString();
                    //query = URLEncoder.encode(query, "UTF-8");
                    //String query = editText.getText().toString();
                    //String target = "book_adv.json";
                    String target = "book.json";

                    Call<ResponseBody> getResponse = bookApiService.getResponse(target, query, 10, 1);
                    getResponse.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            Log.d("찾은 결과", response.toString());
                            textView.setText(response.toString());
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("에러", t.toString());
                        }
                    });

                } catch (Exception e) {
                    Log.d("에러", e.toString());
                }*//*

            }
        });*/

    }

    private void initRecycler() {
        book_recycler = (RecyclerView) findViewById(R.id.book_recycler);
        mLayoutManager = new GridLayoutManager(this, 3);

        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Log.d("booksale", "size position : " + position);
                if (bookAdapter.isLast(position)) {
                    int num = position % 3;
                    Log.d("booksale", "last_check");
                }

                Log.d("islast(position)", String.valueOf(bookAdapter.isLast(position)? (3 - (position % 3)) : 1));
                return bookAdapter.isLast(position)? (3 - (position % 3)) : 1;
            }
        });

        book_recycler.setLayoutManager(mLayoutManager);
        //bookAdapter = new BookAdapter(naverBookArrayList);
        bookAdapter = new BookAdapter(bookList);
        bookAdapter.mContext = this;
        book_recycler.setAdapter(bookAdapter);
    }

    private void initRealm() {
        RealmResults<Book> books = realm.where(Book.class).findAll();
        if (books.size() != 0) {
            for (Book book : books) {
                bookList.add(book);
                postion++;
                Log.d("position값", String.valueOf(postion));
                bookAdapter.bookSize = postion;
                bookAdapter.notifyItemChanged(postion);
            }
        } else {
            //naverBookArrayList.clear();
            bookList.clear();
        }
    }

    private List<NaverBook> fetchResults(retrofit2.Response<Channel> response) {
        Channel channel = response.body();
        channel.getItems();
        return channel.getItems();
    }

    public void searchBook(String search) {
        //java코드로 특정 url에 요청보내고 응답받기
        //기본 자바 API를 활용한 방법

/*        String clientID=""; //네이버 개발자 센터에서 발급받은 clientID입력
        String clientSecret = "";        //네이버 개발자 센터에서 발급받은 clientSecret입력
        URL url = new URL("https://openapi.naver.com/v1/search/book.xml?query=java"); //API 기본정보의 요청 url을 복사해오고 필수인 query를 적어줍니당!

        URLConnection urlConn=url.openConnection(); //openConnection 해당 요청에 대해서 쓸 수 있는 connection 객체

        urlConn.setRequestProperty("X-Naver-Client-ID", clientID);
        urlConn.setRequestProperty("X-Naver-Client-Secret", clientSecret);

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

        String msg = null;
        while((msg = br.readLine())!=null)
        {
            System.out.println(msg);
        }*/

        //String clientId = "YOUR_CLIENT_ID";//애플리케이션 클라이언트 아이디값";
        //String clientSecret = "YOUR_CLIENT_SECRET";//애플리케이션 클라이언트 시크릿값";

        try {
            String text = URLEncoder.encode(search, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/book_adv.json?query=" + text; // json 결과

            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", String.valueOf(R.string.clientId));
            con.setRequestProperty("X-Naver-Client-Secret", String.valueOf(R.string.clientSecret));
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
            Log.d("찾은 결과", response.toString());
            //text1.setText(response.toString());
        } catch (Exception e) {
            System.out.println(e);
            Log.d("에러", e.toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void addNewBook() {
        final CharSequence[] items = { "바코드로 등록", "검색해서 등록", "취소" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("도서 등록");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("바코드로 등록")) {
                    gotoBookSearch();
                } else if (items[item].equals("검색해서 등록")) {
                    gotoBookSearch();
                } else if (items[item].equals("취소")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.add_book:
                addNewBook();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoBookSearch() {
        startActivity(new Intent(this, SearchBookActivity.class));
    }

    @Override
    public void onBackPressed() {
        backPressFinishHandler.onBackPressed();
    }

    @Subscribe
    public void getBook(NaverBook naverBook) {
        //postion = naverBookArrayList.size();
        postion = bookList.size();
        postion++;
        bookAdapter.bookSize = postion;
        addDataToRealm(naverBook);
        //naverBookArrayList.add(naverBook);
        //bookAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
        realm.close();
    }

    private void addDataToRealm(final NaverBook naverBook) {
        //NaverBook getbook = realm.createObject(NaverBook.class);
        final Book book = new Book();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number currentIdNum = realm.where(Book.class).max("id");
                int nextId;
                if(currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                book.setId(nextId);
                book.setNaverBook(naverBook);
                Log.d("book모델", book.getTitle() + book.getAuthor() + book.getPublisher());
                //naverBook.setId(nextId);
                realm.copyToRealmOrUpdate(book);
            }
        });
        bookList.add(book);
        //naverBookArrayList.add(naverBook);
        bookAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void removeRealmdata(final DeleteBookEvent deleteBookEvent) {
        final Book realmResults = realm.where(Book.class).equalTo("id", deleteBookEvent.getId()).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Book realmBook = realmResults;
                realmBook.deleteFromRealm();
                /*NaverBook naverBook = realmResults;
                naverBook.deleteFromRealm();*/
            }
        });
        //bookAdapter.bookSize = naverBookArrayList.size();
        bookAdapter.bookSize = bookList.size();
        bookAdapter.notifyDataSetChanged();
    }
}
