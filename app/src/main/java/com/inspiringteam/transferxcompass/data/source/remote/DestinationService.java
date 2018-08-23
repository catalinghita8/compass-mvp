package com.inspiringteam.transferxcompass.data.source.remote;

import com.inspiringteam.transferxcompass.data.models.DestinationWrapper;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface DestinationService {
    @GET("http://transferx.ddns.net:3000/")
    Single<DestinationWrapper> getDestination();
}
