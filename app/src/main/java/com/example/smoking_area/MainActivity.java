package com.example.smoking_area;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.smoking_area.KakaoLogin.Kakao_login_main;
import com.example.smoking_area.KakaoMap.Area_user_info;
import com.example.smoking_area.KakaoMap.checking_zone;
import com.example.smoking_area.KakaoMap.edit.zone_edit_list;
import com.example.smoking_area.KakaoMap.edit.zone_insert;
import com.example.smoking_area.Mainscreen.Mainscreen01;
import com.example.smoking_area.Mainscreen.Mainscreen02;
import com.example.smoking_area.Mainscreen.Mainscreen03;
import com.example.smoking_area.Mainscreen.mainpage_adapter;
import com.example.smoking_area.play.Play_main;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {
    Area_user_info area_user_info = new Area_user_info();

    Mainscreen01 mainscreen01;
    Mainscreen02 mainscreen02;
    private LinearLayout layoutIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutIndicator = findViewById(R.id.main_layoutIndicator);
        auto_login();                                       //로그인이 되어있다면 바로 지도까지 넘어가기


//        mainscreen01 = new Mainscreen01();
//        mainscreen02 = new Mainscreen02();
//
//        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mainscreen01).commit();

        ViewPager2 viewPager = findViewById(R.id.main_viewpager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Mainscreen01());
        fragments.add(new Mainscreen02());
        fragments.add(new Mainscreen03());

        mainpage_adapter pagerAdapter = new mainpage_adapter(this, fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
        setupIndicators(3);

    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.icon_mainscreen_select));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.icon_mainscreen_select
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.icon_mainscreen_noselect
                ));
            }
        }
    }

//    public void edit_list(View view){
//        Intent intent = new Intent(getApplicationContext(), zone_edit_list.class);
//        startActivity(intent);
//    }
//
//    public void insert_zone(View view){
//        Intent intent = new Intent(getApplicationContext(), zone_insert.class);
//        startActivity(intent);
//    }
//
//    public void checking_zone(View view){
//        Intent intent = new Intent(getApplicationContext(), checking_zone.class);
//        startActivity(intent);
//    }

    private void auto_login(){      //로그인이 되어있다면 바로 지도까지 넘어가기
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                // 로그인이 되어있으면
                if (user!=null){
                    Area_user area_user = new Area_user(user.getKakaoAccount().getProfile().getNickname());

                    area_user_info.setUserInfo(area_user);
                    area_user_info.setFirst(0);
                }else {

                }
                return null;
            }
        });
    }


    public void kakao_login_btn(View view){
        Intent intent = new Intent(getApplicationContext(), Kakao_login_main.class);
        startActivity(intent);
    }

    public void play_start_btn(View view){
        Intent intent = new Intent(getApplicationContext(), Kakao_login_main.class);
        startActivity(intent);
    }
}


