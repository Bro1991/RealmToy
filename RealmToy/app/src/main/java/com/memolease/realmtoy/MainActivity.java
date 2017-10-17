package com.memolease.realmtoy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.memolease.realmtoy.model.Book;
import com.memolease.realmtoy.model.Memo;
import com.memolease.realmtoy.util.BackPressFinishHandler;
import com.memolease.realmtoy.util.BusProvider;
import com.memolease.realmtoy.util.ImageDownload;
import com.memolease.realmtoy.util.MainThreadBus;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {
    RecyclerView book_recycler;
    BookAdapter bookAdapter;
    ArrayList<NaverBook> naverBookArrayList = new ArrayList<>();
    List<Book> bookList = new ArrayList<>();
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    GridLayoutManager mLayoutManager;
    Context mContext;
    MainThreadBus bus = new MainThreadBus();

    private final String SAVE_FOLDER = "/RealmToy_Backup";

    private File path;
    private File outputFile;


    //StickyListHeadersListView mLibraryListView;

    static final int ADD_BOOK = 2004;
    int postion = 0;
    Bus mBus = BusProvider.getInstance();
    private Realm realm;
    private BackPressFinishHandler backPressFinishHandler;
    byte[] getbytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mBus.register(this);
        bus.register(this);

        EventBus.getDefault().register(this);

/*        mLibraryListView = (StickyListHeadersListView) findViewById(R.id.drawer_list);
        mLibraryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/

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

        File files[] = getFilesDir().listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                Log.d("파일리스트", files[i].getName().toString());
            }
        }


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

    public File getAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("경고", "Directory not created");
        }
        return file;
    }



    private void drawerLayoutInit() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.crop__done, R.string.crop__cancel) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //onlyDismissMenu();
                //mBus.post(event);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
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

                Log.d("islast(position)", String.valueOf(bookAdapter.isLast(position) ? (3 - (position % 3)) : 1));
                return bookAdapter.isLast(position) ? (3 - (position % 3)) : 1;
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
            RealmResults<Memo> memos = realm.where(Memo.class).equalTo("bookid", 5).findAll();
            for (int i = 0; i < memos.size(); i++) {
                Log.d("메모 숫자", memos.get(i).getContent());
            }

        } else {
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
        final CharSequence[] items = {"바코드로 등록", "검색해서 등록", "취소"};
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

    public byte[] readFile(String a_sParentPath, String a_sFileName) {
        byte[] bArData = null;
        if (a_sParentPath != null && a_sParentPath.length() > 0) {
            File oDatabFolder = new File(a_sParentPath);
            if (oDatabFolder != null && oDatabFolder.exists() == true && oDatabFolder.isDirectory() == true) {
                String sFile = a_sParentPath + a_sFileName;

                try {
                    FileInputStream oInputStream = new FileInputStream(sFile);
                    int nCount = oInputStream.available();
                    if (nCount > 0) {
                        bArData = new byte[nCount];
                        oInputStream.read(bArData);
                    }

                    if (oInputStream != null) {
                        oInputStream.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bArData;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
        bus.unregister(this);
        EventBus.getDefault().unregister(this);
        realm.close();
    }

    public void addDataToRealm(final NaverBook naverBook) {
        //NaverBook getbook = realm.createObject(NaverBook.class);
        String FileName = "";
        if (naverBook.getIsbn() != null) {
            FileName = naverBook.getIsbn();
        } else {
            FileName = naverBook.getIsbn13();
        }

        savefolder(naverBook.getImage(), FileName);

        final Book book = new Book();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number currentIdNum = realm.where(Book.class).max("id");
                int nextId;
                if (currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                book.setId(nextId);
                book.setNaverBook(naverBook);
                Log.d("book모델", book.getTitle() + book.getAuthor() + book.getPublisher());
                realm.copyToRealmOrUpdate(book);
            }
        });
        bookList.add(book);
        //naverBookArrayList.add(naverBook);
        bookAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void getimagess(final AddBookImageEvent addBookImageEvent) {
        Log.d("북이미지 이벤트버스로 보냄", "책 이미지파일을 받았다");
        //book.setBytes(addBookImageEvent.getBytes());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Book realmResults = realm.where(Book.class).equalTo("isbn", addBookImageEvent.getIsbn()).findFirst();
                realmResults.setBytes(addBookImageEvent.getBytes());
                realm.copyToRealmOrUpdate(realmResults);
                Log.d("북 이미지 file", "file Realm 저장성공");
            }
        });

    }

    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.ASYNC)
    public void getImage(AddBookImageEvent addBookImageEvent) {
        Log.d("북이미지 이벤트", "책 이미지파일을 받았다");
        mBus.post(addBookImageEvent);
/*        Book realmResults = realm.where(Book.class).equalTo("isbn", addBookImageEvent.getIsbn()).findFirst();
        Book book = new Book();

        if (realmResults != null) {
            book = realm.where(Book.class).equalTo("isbn", addBookImageEvent.getIsbn()).findFirst();
        } else {
            book = realm.where(Book.class).equalTo("isbn13", addBookImageEvent.getIsbn()).findFirst();
        }

        final Book finalBook = book;
        book.setBytes(addBookImageEvent.getBytes());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(finalBook);
                Log.d("북 이미지 file", "file Realm 저장성공");
            }
        });*/

    }

    @Subscribe
    public void removeRealmdata(final DeleteBookEvent deleteBookEvent) {
        final Book realmResults = realm.where(Book.class).equalTo("id", deleteBookEvent.getId()).findFirst();
        final RealmResults<Memo> memoRealmResults = realm.where(Memo.class).equalTo("bookid", deleteBookEvent.getId()).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Book realmBook = realmResults;
                realmBook.deleteFromRealm();
                memoRealmResults.deleteAllFromRealm();
                /*NaverBook naverBook = realmResults;
                naverBook.deleteFromRealm();*/
            }
        });
        //bookAdapter.bookSize = naverBookArrayList.size();
        bookAdapter.bookSize = bookList.size();
        bookAdapter.notifyDataSetChanged();
    }

    private void savefolder(String uriString, String filename) {
        ImageDownloads imageDownloads = new ImageDownloads();
        imageDownloads.execute(uriString, filename);
    }


    public class ImageDownloads extends AsyncTask<String, Void, AddBookImageEvent> {
        String filename;

        @Override
        protected AddBookImageEvent doInBackground(String... strings) {
            //다운로드 경로를 지정
            String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + SAVE_FOLDER;
            String sds = getExternalFilesDir(DOWNLOAD_SERVICE).getPath();

            File exdir = new File(sds);
            File dir = new File(savePath);
            Log.d("다운로드 경로 지정", "지정성공");

            Log.d("폴도경로", exdir.getPath());

            //상위 디렉토리가 존재하지 않을 경우 생성
            if (!dir.exists()) {
                Log.d("경로가 없다", "경로만들기");
                dir.mkdirs();
            }

            if (!exdir.exists()) {
                Log.d("경로가 없다", "경로만들기");
                exdir.mkdirs();
            }



            //웹 서버 쪽 파일이 있는 경로
            String fileUrl = strings[0];
            //받아온 filename = book_isbn
            filename = strings[1];

            //저장되는 파일의 경로 값
            //String localPath = savePath + "/" + filename + ".png";
            String localPath = sds + "/" + filename + ".png";

            try {
                URL imgUrl = new URL(fileUrl);
                //서버와 접속하는 클라이언트 객체 생성
                HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();

                int len = conn.getContentLength();
                byte[] tmpByte = new byte[len];

                //입력 스트림을 구한다
                InputStream is = conn.getInputStream();
                File file = new File(localPath);

                //파일이 생성이 안 될수도 있어서 생성시키는 코드
                file.createNewFile();


                String localPaths = filename + ".png";
                //파일 저장 스트림 생성
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                FileOutputStream fos = openFileOutput(filename + ".png", Context.MODE_PRIVATE);
                FileInputStream ins = openFileInput("/" + filename + ".png");
                File file1 = new File(getFilesDir().getPath() + localPaths);
                //FileOutputStream fos = openFileOutput(filename + ".png", Context.MODE_PRIVATE);

                //AddBookImageEvent event = new AddBookImageEvent(filename, tmpByte);
                //AddBookImageEvent event = new AddBookImageEvent(filename, file.getPath());
                AddBookImageEvent event = new AddBookImageEvent(filename, file1.getPath());
                //입력 스트림을 파일로 저장
                byte[] buf = new byte[1024];
                int count;

                while ((count = is.read(buf)) > 0) {
                    //file 생성 및 폴더에 넣기
                    fileOutputStream.write(buf, 0, count);
                    fos.write(buf, 0, count);
                }
                is.close();
                fos.close();
                fileOutputStream.close();
                conn.disconnect();
                Log.d("파일저장", "성공");
                return event;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final AddBookImageEvent event) {
            super.onPostExecute(event);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Book realmResults = realm.where(Book.class).equalTo("isbn", event.getIsbn()).findFirst();
                    //realmResults.setBytes(event.getBytes());
                    realmResults.setImage_path(event.getImagePath());
                    realm.copyToRealmOrUpdate(realmResults);
                    Log.d("북 이미지 file", "file Realm 저장성공");
                }
            });
        }
    }
}
