package com.example.smoking_area.KakaoLogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smoking_area.R;
import com.example.smoking_area.play.Play_main;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.camera.CameraUpdateFactory;
import com.kakao.vectormap.label.LabelLayer;
import com.kakao.vectormap.label.LabelOptions;
import com.kakao.vectormap.label.LabelStyle;
import com.kakao.vectormap.label.LabelStyles;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class Kakao_login_main extends AppCompatActivity {
    private static final String TAG = "Kakao_login";
    private View loginButton;
//    , logoutButton;
//    private TextView nickName;
//    private ImageView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login_main);

        loginButton = findViewById(R.id.login);
//        logoutButton = findViewById(R.id.logout);
//        nickName = findViewById(R.id.nickname);
//        profileImage = findViewById(R.id.profile);

        // 카카오가 설치되어 있는지 확인 하는 메서드또한 카카오에서 제공 콜백 객체를 이용함
        Function2<OAuthToken, Throwable, Unit> callback = new  Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                // 이때 토큰이 전달이 되면 로그인이 성공한 것이고 토큰이 전달되지 않았다면 로그인 실패
                if(oAuthToken != null) {

                }
                if (throwable != null) {

                }
                updateKakaoLoginUi();
                return null;
            }
        };
        // 로그인 버튼
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(Kakao_login_main.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(Kakao_login_main.this, callback);
                }else {
                    UserApiClient.getInstance().loginWithKakaoAccount(Kakao_login_main.this, callback);
                }
            }
        });
        // 로그 아웃 버튼
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
//                    @Override
//                    public Unit invoke(Throwable throwable) {
//                        updateKakaoLoginUi();
//                        return null;
//                    }
//                });
//            }
//        });
        updateKakaoLoginUi();
    }
    private  void updateKakaoLoginUi(){
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                // 로그인이 되어있으면
                if (user!=null){

                    Toast.makeText(getApplicationContext(),user.getKakaoAccount().getProfile().getNickname() + "님 환영합니다. ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Play_main.class);
                    startActivity(intent);
                    finish();

//                    // 유저의 아이디
//                    Log.d(TAG,"invoke: id" + user.getId());
//                    // 유저의 어카운트정보에 이메일
//                    Log.d(TAG,"invoke: nickname" + user.getKakaoAccount().getEmail());
//                    // 유저의 어카운트 정보의 프로파일에 닉네임
//                    Log.d(TAG,"invoke: email" + user.getKakaoAccount().getProfile().getNickname());
//                    // 유저의 어카운트 파일의 성별
//                    Log.d(TAG,"invoke: gerder" + user.getKakaoAccount().getGender());
//                    // 유저의 어카운트 정보에 나이
//                    Log.d(TAG,"invoke: age" + user.getKakaoAccount().getAgeRange());
//
//                    nickName.setText(user.getKakaoAccount().getProfile().getNickname());

//                    Glide.with(profileImage).load(user.getKakaoAccount().
//                            getProfile().getProfileImageUrl()).circleCrop().into(profileImage);
                    loginButton.setVisibility(View.GONE);
//                    logoutButton.setVisibility(View.VISIBLE);
                }else {
                    // 로그인이 되어 있지 않다면 위와 반대로
//                    nickName.setText(null);
//                    profileImage.setImageBitmap(null);
                    loginButton.setVisibility(View.VISIBLE);
//                    logoutButton.setVisibility(View.GONE);
                }
                return null;
            }
        });
    }



}