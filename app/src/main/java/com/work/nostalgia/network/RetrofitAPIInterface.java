package com.work.nostalgia.network;

import com.work.nostalgia.model.WeatherData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPIInterface {

    @GET("weather")
    Observable<WeatherData> getWeatherInfo(@Query("q") String city,
                                           @Query("appid") String appID);


}
