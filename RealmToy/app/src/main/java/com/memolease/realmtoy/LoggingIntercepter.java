package com.memolease.realmtoy;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by bro on 2017-08-17.
 */

public class LoggingIntercepter implements Interceptor {
    private static final String TAG = LoggingIntercepter.class.getSimpleName();

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Log.d(TAG, "inside intercept callback");
        Request request = chain.request();
        long t1 = System.nanoTime();
        String requestLog = String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers());
        if (request.method().compareToIgnoreCase("post") == 0) {
            requestLog = "\n" + requestLog + "\n" + bodyToString(request);
        }
        Log.d(TAG, "Request" + "\n" + requestLog);
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        String responseLog = String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers());

        String bodyString = response.body().string();

        Log.d(TAG, "Response Data" + "\n" + bodyString);

        Log.d(TAG, "Response Header" + "\n" + responseLog + "\n" + bodyString);

        try {
            JSONObject bodyObject = new JSONObject(bodyString);
            if (bodyObject.getString("error").equals("계속하려면 로그인하거나 가입해야 합니다.")) {
                //BusProvider.getInstance().post(new LogoutEvent());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), bodyString))
                .build();
    }

    public static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
