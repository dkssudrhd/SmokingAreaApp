package com.example.smoking_area;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this, "17d3ee87913857a324f9c6c6c997e2d5");
    }
}
