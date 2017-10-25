package com.memolease.realmtoy.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.memolease.realmtoy.BuildConfig;
import com.memolease.realmtoy.event.EditMemoEvent;
import com.memolease.realmtoy.adapter.MemoAdapter;
import com.memolease.realmtoy.event.PutChangeStateBookEvent;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.event.deleteMemo;
import com.memolease.realmtoy.model.Book;
import com.memolease.realmtoy.model.Memo;
import com.memolease.realmtoy.model.PhotoMemo;
import com.memolease.realmtoy.util.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class BookDetailActivity extends AppCompatActivity {
    ImageView mBookImageView;
    TextView mTitleView;
    TextView mTitleParse;
    TextView mAuthorView;
    TextView mDescriptionView;
    TextView textView4;
    Button mAddMemoButton;
    Button addPhotoMemoButton;
    RadioGroup mBookStateGroup;
    RadioButton bookStateSeg_1, bookStateSeg_2, bookStateSeg_3, bookStateSeg_4;
    Book mBook;
    Bus mBus = BusProvider.getInstance();
    Realm realm;

    RealmList<Memo> memoList = new RealmList<>();
    RecyclerView recycler_memo;
    LinearLayoutManager linearLayoutManager;

    MemoAdapter memoAdapter;
    //String ids = getIntent().getStringExtra("id");

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;
    private Uri photoUri;
    private String mCurrentPhotoPath;
    private static final int PICK_FROM_CAMERA = 1;

    private final String SAVE_FOLDER = "/RealmToy_Backup";
    private final String SAVE_PHOTO_MEMO = "/photo_memo";


    int id;
    int readState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        realm = Realm.getDefaultInstance();
        mBus.register(this);
        id = getIntent().getExtras().getInt("id");
        readState = getIntent().getExtras().getInt("readState");
        //id = getIntent().getIntExtra("id", 1);

        Log.d("가져온 ID 값", String.valueOf(id));

        initUI();
        initRecycler();
        initPhotomemo();
        getbook();
        createDirectoryFolder();
    }

    private void initRecycler() {
        recycler_memo = (RecyclerView) findViewById(R.id.recycler_memo);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recycler_memo.setLayoutManager(linearLayoutManager);
        mBook = realm.where(Book.class).equalTo("id", id).findFirst();

        RealmList<Memo> memos = mBook.getMemoList();
        if (memos.size() != 0) {
            for (Memo memo : memos) {
                memoList.add(memo);
            }
        } else {
            memoList.clear();
        }
        memoAdapter = new MemoAdapter(this, memoList);
        memoAdapter.context = this;
        recycler_memo.setAdapter(memoAdapter);
    }

    private void initPhotomemo() {
        mBook = realm.where(Book.class).equalTo("id", id).findFirst();
        RealmList<PhotoMemo> memos = mBook.getPhotoMemoList();
        if (memos.size() != 0) {
            for (PhotoMemo memo : memos) {
                Log.d("저장된 파일 경로", memo.getImagePath());
            }
        }
    }

    private void getbook() {
/*        String[] chunks = mBook.getTitle().split("\\(");
        if (chunks.length > 1) {
            String[] afterChunks = chunks[1].split("\\)");

            if (afterChunks.length == 1) {
                mTitleParse.setVisibility(View.VISIBLE);
                mTitleParse.setText("- " + afterChunks[0]);
                mTitleView.setText(chunks[0]);
            }
        } else {
            mTitleParse.setVisibility(View.GONE);
            mTitleView.setText(mBook.getTitle());
        }*/

        if (mBook.getSub_title() == null) {
            mTitleParse.setVisibility(View.GONE);
            mTitleView.setText(mBook.getTitle());
        } else {
            mTitleParse.setVisibility(View.VISIBLE);
            mTitleParse.setText(mBook.getSub_title());
            mTitleView.setText(mBook.getTitle());
        }

        switch (mBook.getReadState()) {
            case 1:
                bookStateSeg_1.setChecked(true);
                break;
            case 2:
                bookStateSeg_2.setChecked(true);
                break;
            case 3:
                bookStateSeg_3.setChecked(true);
                break;
            case 4:
                bookStateSeg_4.setChecked(true);
                break;
        }

        mAuthorView.setText(mBook.getAuthor());
        mDescriptionView.setText(makeBookDescription(mBook));
/*        Glide.with(mBookImageView.getContext())
                .load(mBook.getImage())
                .into(new GlideDrawableImageViewTarget(mBookImageView) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, null);
                        mBookImageView.setScaleType(ImageView.ScaleType.MATRIX);
                    }
                });*/

        //File file = new File(getFilesDir().getPath() + mBook.getImage_path());
        File file = new File(mBook.getImagePath());
        Log.d("파일경로", file.getPath());
        Glide.with(mBookImageView.getContext())
                .load(file)
                .into(mBookImageView);
    }


    private void initUI() {
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setTextColor(Color.parseColor("#333333"));

        mTitleParse = (TextView) findViewById(R.id.titleDesc);
        mTitleParse.setTextColor(Color.parseColor("#427ED9"));

        mAuthorView = (TextView) findViewById(R.id.author);
        mAuthorView.setTextColor(Color.parseColor("#555555"));

        mDescriptionView = (TextView) findViewById(R.id.bookDescription);
        mDescriptionView.setTextColor(Color.parseColor("#555555"));

        textView4 = (TextView) findViewById(R.id.textView4);

        mBookImageView = (ImageView) findViewById(R.id.imageView);
        mAddMemoButton = (Button) findViewById(R.id.addMemoButton);
        mAddMemoButton.setTextColor(Color.parseColor("#5667ff"));
        mAddMemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addMemo = new Intent(BookDetailActivity.this, MemoEditActivity.class);
                addMemo.putExtra("isNew", true);
                startActivity(addMemo);
            }
        });

        addPhotoMemoButton = (Button) findViewById(R.id.addPhotoMemoButton);
        addPhotoMemoButton.setTextColor(Color.parseColor("#5667ff"));
        addPhotoMemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
                takePhoto();

            }
        });

        mBookStateGroup = (RadioGroup) findViewById(R.id.bookStateGroup);
        bookStateSeg_1 = (RadioButton) findViewById(R.id.bookStateSeg_1);
        bookStateSeg_2 = (RadioButton) findViewById(R.id.bookStateSeg_2);
        bookStateSeg_3 = (RadioButton) findViewById(R.id.bookStateSeg_3);
        bookStateSeg_4 = (RadioButton) findViewById(R.id.bookStateSeg_4);

        mBookStateGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PutChangeStateBookEvent event = new PutChangeStateBookEvent();
                event.setBookid(id);
                int selectedIdx = 0;
                switch (checkedId) {
                    case R.id.bookStateSeg_1:
                        selectedIdx = 1;
                        break;

                    case R.id.bookStateSeg_2:
                        selectedIdx = 2;
                        break;

                    case R.id.bookStateSeg_3:
                        selectedIdx = 3;
                        break;

                    case R.id.bookStateSeg_4:
                        selectedIdx = 4;
                        break;
                }

                event.setReadState(selectedIdx);
                mBus.post(event);
            }
        });
    }

    private void createDirectoryFolder() {
        String dirPath = getFilesDir().getAbsolutePath() + SAVE_FOLDER;
        //String dirPath = dirPath + "RealmToy_BackUp";
        //String createFolderPath = dirPath + "RealmToy_BackUp" + "/photo_memo" ;
        String createFolderPath = dirPath + SAVE_PHOTO_MEMO;

        File createFolder = new File(createFolderPath);
        Log.d("다운로드 경로 지정", "지정성공");

        //상위 디렉토리가 존재하지 않을 경우 생성
        if (!createFolder.exists()) {
            Log.d("저장할 폴더가 없다", "폴더 만들기");
            createFolder.mkdirs();
            Log.d("저장할 폴더생성", "photo_memo 폴더 만들기 성공");
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(BookDetailActivity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (photoFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//sdk 24 이상, 누가(7.0)
//                photoUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                photoUri = FileProvider.getUriForFile(BookDetailActivity.this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
            } else {//sdk 23 이하, 7.0 미만
                photoUri = Uri.fromFile(photoFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        String fileName;
        if (mBook.getIsbn13() != null) {
            fileName = mBook.getIsbn13();
        } else {
            fileName = mBook.getIsbn();
        }

        String photoMemoFileName = fileName + "_" + timeStamp;
        /*File storageDir = new File(Environment.getExternalStorageDirectory() + "/NOSTest/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }*/
        //다운로드 경로를 지정
        String dirPath = getFilesDir().getAbsolutePath() + SAVE_FOLDER;
        //String savePath = dirPath + "photo_memo";
        String savePath = dirPath + SAVE_PHOTO_MEMO;
        String localPath = photoMemoFileName + ".jpg";

        //입력 스트림을 구한다
        //InputStream is = openFileInput(savePath);

        File saveFile = new File(savePath, localPath);

        //파일이 생성이 안 될수도 있어서 생성시키는 코드
        saveFile.createNewFile();

/*        //파일 저장 스트림 생성
        FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
        Log.d("생성된 파일 경로", saveFile.getPath());
        Log.d("생성된 파일 절대경로", saveFile.getAbsolutePath());

        //입력 스트림을 파일로 저장하기 위한 코드
        byte[] buf = new byte[1024];
        int count;
        while ((count = is.read(buf)) > 0) {
            //file 생성 및 폴더에 넣기
            fileOutputStream.write(buf, 0, count);
        }
        is.close();
        fileOutputStream.close();
        Log.d("파일저장", "성공");*/
        //File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = saveFile.getPath();
        return saveFile;
    }

    @Subscribe
    public void getMemo(final Memo memo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number currentIdNum = realm.where(Memo.class).max("id");
                int nextId;
                if (currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                memo.setBookid(id);
                memo.setId(nextId);
                mBook.memoList.add(memo);
            }
        });
        memoList.add(memo);
        memoAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void deleteMemos(final deleteMemo deleteMemo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mBook.memoList.remove(deleteMemo.getPosition());
            }
        });
        memoList.remove(deleteMemo.getPosition());
        memoAdapter.notifyItemRemoved(deleteMemo.getPosition());

    }

    @Subscribe
    public void editMemo(EditMemoEvent editMemoEvent) {
        //Memo memo = memoList.get(editMemoEvent.getPosition());
        //memo.setTitle(editMemoEvent.getTitle());
        //memo.setContent(editMemoEvent.getContent());
        //memo.setUpdatedAt(editMemoEvent.getUpdatedAt());
        memoAdapter.notifyItemChanged(editMemoEvent.getPosition());
    }

    @Subscribe
    public void editReadState(final PutChangeStateBookEvent event) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Book book = realm.where(Book.class).equalTo("id", event.getBookid()).findFirst();
                book.setReadState(event.getReadState());
                realm.copyToRealmOrUpdate(book);
            }
        });

    }

    public void addPhotoMemo(final String path) {
        final PhotoMemo photoMemo = new PhotoMemo();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number currentIdNum = realm.where(PhotoMemo.class).max("id");
                int nextId;
                if (currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy.MM.dd");
                final String strNow = sdfNow.format(date);
                photoMemo.setId(nextId);
                photoMemo.setBookid(id);
                photoMemo.setImagePath(path);
                photoMemo.setCreateAt(strNow);
                mBook.photoMemoList.add(photoMemo);
            }
        });
        Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();

    }

    private String makeBookDescription(Book book) {
        return book.getPublisher() + " / " + book.getPubdateWithDot().replace("-", ". ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
        realm.close();
    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if (requestCode == PICK_FROM_CAMERA) {
            //photoUri = data.getData();
            String path = compressImage(photoUri);
            Log.d("사진경로", compressImage(photoUri));
            addPhotoMemo(path);
        }
    }

    public String compressImage(Uri imageUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.grantUriPermission("com.android.camera", imageUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
/*        final PackageManager packageManager = this.getPackageManager();
        final List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolvedIntentInfo : activities) {
            final String packageName = resolvedIntentInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, permissions);
        }*/
        //String filePath = getRealPathFromURI(imageUri);
        String filePath = mCurrentPhotoPath;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 1024.0f;
        float maxWidth = 1024.0f;
        float imgRatio = (float) actualWidth / actualHeight;
        float maxRatio = (float) maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFilename() {
        //String filename = createDirectoryFolder();
        String dirPath = getFilesDir().getAbsolutePath() + SAVE_FOLDER;
        String savePath = dirPath + SAVE_PHOTO_MEMO;
        String fileName;
        if (mBook.getIsbn13() != null) {
            fileName = mBook.getIsbn13();
        } else {
            fileName = mBook.getIsbn();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        String photoMemoFileName = fileName + "_" + timeStamp;
        //String uriSting = (createFolder.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        String uriSting = (savePath + "/" + photoMemoFileName + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(Uri contentUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.grantUriPermission("com.android.camera", contentUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Cursor cursor = this.getApplicationContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }


    //Android N crop image
    /*public void cropImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.grantUriPermission("com.android.camera", photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/NOSTest/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(BookDetailActivity.this,
                    getApplication().getPackageName(), tempFile);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            Intent i = new Intent(intent);
            //ResolveInfo res = list.get(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                grantUriPermission(res.activityInfo.packageName, photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            //startActivityForResult(i, CROP_FROM_CAMERA);
        }
    }*/


}