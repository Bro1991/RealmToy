package com.memolease.realmtoy.util;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by bro on 2017-08-22.
 */

public class BackPressFinishHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressFinishHandler(Activity activity) {
        this.activity = activity;
    }


    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            ActivityCompat.finishAffinity(activity);
            toast.cancel();
        }
    }

    private void showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
