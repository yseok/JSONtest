package com.yuseok.android.jsontest;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by YS on 2017-04-21.
 */

public class APIService {
    public static final String API_URL = "http://eb-client.ap-northeast-2.elasticbeanstalk.com/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if(retrofit == null) {

        }
        return retrofit;
    }

    public interface mapAndUser {
        @GET("api/search/")
        Call<MapAndUser> getMapAndUer (@Header("Authorization") String token, @Query("keyword") String keyword);
    }
}
