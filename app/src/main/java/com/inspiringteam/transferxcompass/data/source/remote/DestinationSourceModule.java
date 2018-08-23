package com.inspiringteam.transferxcompass.data.source.remote;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inspiringteam.transferxcompass.di.scopes.AppScoped;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.inspiringteam.transferxcompass.util.Constants.DESTINATION_API_BASE_URL;

@Module
public class DestinationSourceModule {
    @AppScoped
    @Provides
    DestinationService provideDestinationService(Retrofit retrofit) {
        return retrofit.create(DestinationService.class);
    }

    @Provides
    @AppScoped
    Retrofit provideRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(DESTINATION_API_BASE_URL)
                .build();
    }

    @Provides
    @AppScoped
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }
}
