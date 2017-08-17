package com.memolease.realmtoy;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by bro on 2017-08-17.
 */

public interface BookApiService {
    String API_URL = "https://openapi.naver.com/v1/search/book_adv.json?query=";

    // /v1/search/book_adv.json?query="
    @GET("/v1/search/{target}")
    Call<NaverBook> getBookSearch(@Path("target") String target,
                                  @Query("query") String query,
                                  @Query("display") int display,
                                  @Query("start") int start);

    @GET("/v1/search/{target}")
    Call<ResponseBody> getResponse(@Path("target") String target,
                                   @Query("query") String query,
                                   @Query("display") int display,
                                   @Query("start") int start);

    @GET("/v1/search/{target}")
    Call<ResponseBody> getBody(@Path("target") String target,
                               @Query("query") String query,
                               @Query("display") int display,
                               @Query("start") int start);

    @GET("/v1/search/{target}")
    Call<Channel> getChannel(@Path("target") String target,
                               @Query("query") String query,
                               @Query("display") int display,
                               @Query("start") int start);

}
