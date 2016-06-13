package com.jungle.exercise.third.dao;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2016/6/12.
 */
public interface ToutiaoNewsService {
    @GET("/api/article/real_time_news/?utm_source=toutiao")
    Call<Map<String, Object>> fetchNewsListData(@QueryMap Map<String, String> options);
}
