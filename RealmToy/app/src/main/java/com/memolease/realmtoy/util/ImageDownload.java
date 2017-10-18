package com.memolease.realmtoy.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.squareup.otto.Bus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.realm.Realm;

/**
 * Created by bro on 2017-10-16.
 */

public class ImageDownload extends AsyncTask<String, Void, Void> {
    Context mContext;
    private String fileName;
    private String DownloadURL;
    File path;
    Bus mBus = BusProvider.getInstance();
    private final String SAVE_FOLDER = "/RealmToy_Backup";

    public ImageDownload(Context context, String fileName, String DownloadURL) {
        this.mContext = context;
        this.fileName = fileName;
        this.DownloadURL = DownloadURL;
    }

    @Override
    protected Void doInBackground(String... params) {
    /*    //다운로드 경로를 지정
        Log.d("다운로드 경로 지정", "지정하자");
        path = new File(mContext.getFilesDir(), "RealmToy");
        Log.d("다운로드 경로 지정", "지정성공");
        //상위 디렉토리가 존재하지 않을 경우 생성
        if (!path.exists()) {
            Log.d("경로가 없다", "경로만들기");
            path.mkdirs();
            Log.d("경로생성", "성공");
        }
        //웹 서버 쪽 파일이 있는 경로
        String fileUrl = DownloadURL;

        try {
            URL imgUrl = new URL(fileUrl);
            //서버와 접속하는 클라이언트 객체 생성
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();

            int len = conn.getContentLength();
            byte[] tmpByte = new byte[len];

            //입력 스트림을 구한다
            InputStream is = conn.getInputStream();
            File outputFile = new File(path, fileName);
            //파일 저장 스트림 생성
            //FileOutputStream fos = new FileOutputStream(outputFile);
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

            //입력 스트림을 파일로 저장
            byte[] buf = new byte[1024];
            int count;
            while ((count = is.read(buf)) > 0) {
                fileOutputStream.write(buf, 0, count);
            }
            // 접속 해제
            conn.disconnect();
            //fileOutputStream.write(FileName.getBytes());
            fileOutputStream.close();
            Log.d("파일저장", "성공");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;*/


        //다운로드 경로를 지정
        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + SAVE_FOLDER;
        String savePath2 = Environment.getRootDirectory().getAbsolutePath();
        Log.d("다운로드 경로 지정", "지정하자");
        File dir = new File(savePath2);
        Log.d("다운로드 경로 지정", "지정성공");

        //상위 디렉토리가 존재하지 않을 경우 생성
        if (!dir.exists()) {
            Log.d("경로가 없다", "경로만들기");
            dir.mkdirs();
            Log.d("경로생성", "성공");
        }
        //웹 서버 쪽 파일이 있는 경로
        String fileUrl = params[0];

        String localPath = savePath + "/" + fileName + ".png";

        try {
            URL imgUrl = new URL(fileUrl);
            //서버와 접속하는 클라이언트 객체 생성
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();

            int len = conn.getContentLength();
            byte[] tmpByte = new byte[len];

            //입력 스트림을 구한다
            InputStream is = conn.getInputStream();
            File file = new File(localPath);
            file.createNewFile();

            //파일 저장 스트림 생성
            //FileOutputStream fos = new FileOutputStream(outputFile);
            FileOutputStream fos = new FileOutputStream(file);
            Log.d("저장경로", localPath);

            //입력 스트림을 파일로 저장
/*            byte[] buf = new byte[1024];
            int count;
            while ((count = is.read(buf)) > 0) {
                fileOutputStream.write(buf, 0, count);
            }
            // 접속 해제
            conn.disconnect();
            //fileOutputStream.write(FileName.getBytes());
            fileOutputStream.close();*/
            //입력 스트림을 파일로 저장

            /*int read;
            for (;;) {
                read = is.read(tmpByte);
                if (read <= 0) {
                    break;
                }
                fos.write(tmpByte, 0, read); //file 생성
            }*/

            byte[] buf = new byte[1024];
            int count;
            while( (count = is.read(buf)) > 0 ) {
                fos.write(buf, 0, count);
            }
            is.close();
            fos.close();
            conn.disconnect();
            Log.d("파일저장", "성공");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*@Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        //저장한 이미지 열기
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String targetDir = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;
        File file = new File(targetDir + "/" + fileName + ".jpg");
        //type 지정 (이미지)
        i.setDataAndType(Uri.fromFile(file), "image*//*");
        getApplicationContext().startActivity(i);
        //이미지 스캔해서 갤러리 업데이트
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }*/

}