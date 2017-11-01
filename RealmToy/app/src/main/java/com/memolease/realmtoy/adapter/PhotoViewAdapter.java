package com.memolease.realmtoy.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.memolease.realmtoy.R;
import com.memolease.realmtoy.event.DeletePhotoMemo;
import com.memolease.realmtoy.model.PhotoMemo;
import com.memolease.realmtoy.util.BusProvider;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by bro on 2017-09-21.
 */

public class PhotoViewAdapter extends RecyclerView.Adapter<PhotoViewAdapter.PhotoMemoViewHolder> {
    public List<PhotoMemo> items;
    public Context mContext;

    public PhotoViewAdapter(Context context, List<PhotoMemo> items) {
        this.mContext = context;
        this.items = items;
    }

    @Override
    public PhotoMemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View rootView = View.inflate(parent.getContext(), R.layout.photoview_item, null);
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photoview_item, parent, false);
        rootView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        PhotoMemoViewHolder photoMemoViewHolder = new PhotoMemoViewHolder(rootView);
        return photoMemoViewHolder;
    }

    private int getDeviceWidth() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private int getDeviceHeight() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    @Override
    public void onBindViewHolder(PhotoMemoViewHolder holder, int position) {
        PhotoMemo photoMemo = items.get(position);
        holder.context = mContext;
        holder.mAdapter = this;

        holder.setmAdapter(this);

        File file = new File(photoMemo.getImagePath());
        long lFileSize = file.length();
        String strFileSize = Long.toString(lFileSize) + " bytes";
        Log.d("가져온 사진의 용량", strFileSize);
        Log.d("가져온 사진의 용량 단위", sizeCalculation(file.length()));

        Glide.with(holder.photoMemo.getContext())
                .load(file)
                .centerCrop()
                .into(holder.photoMemo);
        Log.d("사진을 넣었다", String.valueOf(position));
    }

    public String getFileSize(String size)
    {
        String gubn[] = {"Byte", "KB", "MB" } ;
        String returnSize = new String ();
        int gubnKey = 0;
        double changeSize = 0;
        long fileSize = 0;
        try{
            fileSize =  Long.parseLong(size);
            for( int x=0 ; (fileSize / (double)1024 ) >0 ; x++, fileSize/= (double) 1024 ){
                gubnKey = x;
                changeSize = fileSize;
            }
            returnSize = changeSize + gubn[gubnKey];
        }catch ( Exception ex){ returnSize = "0.0 Byte"; }
        return returnSize;
    }

    /**
     * 용량계산
     * @param size
     * @return
     */
    public static String sizeCalculation(long size) {
        String CalcuSize = null;
        int i = 0;

        double calcu = (double) size;
        while (calcu >= 1024 && i < 5) { // 단위 숫자로 나누고 한번 나눌 때마다 i 증가
            calcu = calcu / 1024;
            i++;
        }
        DecimalFormat df = new DecimalFormat("##0.0");
        switch (i) {
            case 0:
                CalcuSize = df.format(calcu) + "Byte";
                break;
            case 1:
                CalcuSize = df.format(calcu) + "KB";
                break;
            case 2:
                CalcuSize = df.format(calcu) + "MB";
                break;
            case 3:
                CalcuSize = df.format(calcu) + "GB";
                break;
            case 4:
                CalcuSize = df.format(calcu) + "TB";
                break;
            default:
                CalcuSize="ZZ"; //용량표시 불가

        }
        return CalcuSize;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class PhotoMemoViewHolder extends RecyclerView.ViewHolder {
        int id;
        PhotoView photoMemo;
        //ImageView photoMemo;
        Button deleteImageMemo;
        PhotoViewAdapter mAdapter;
        Context context;

        public PhotoMemoViewHolder(View v) {
            super(v);
            photoMemo = (PhotoView) v.findViewById(R.id.photoMemo);
            // photoMemo = (ImageView) v.findViewById(R.id.photoMemo);
            deleteImageMemo = (Button) v.findViewById(R.id.deleteImageMemo);
            deleteImageMemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PhotoMemo photoMemo = items.get(getAdapterPosition());
                    new MaterialDialog.Builder(context)
                            .content("사진 메모를 삭제하시겠습니까?")
                            .positiveText("삭제")
                            .negativeText("취소")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    DeletePhotoMemo deletePhotoMemo = new DeletePhotoMemo();
                                    deletePhotoMemo.setId(photoMemo.getId());
                                    deletePhotoMemo.setPosition(getAdapterPosition());
                                    BusProvider.getInstance().post(deletePhotoMemo);
                                }
                            })
                            .show();
                }
            });
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setmAdapter(PhotoViewAdapter mAdapter) {
            this.mAdapter = mAdapter;
        }

        public void setContext(Context context) {
            this.context = context;
        }

    }
}
