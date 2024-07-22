package com.example.smoking_area.play;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.smoking_area.R;

public class Loadsearch extends AppCompatActivity {

    Button loadsearch_naver_btn, loadsearch_kakao_btn;
    ImageView loadsearch_cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadsearch);

        loadsearch_naver_btn = findViewById(R.id.loadsearch_naver_btn);
        loadsearch_kakao_btn = findViewById(R.id.loadsearch_kakao_btn);
        loadsearch_cancel_btn = findViewById(R.id.loadsearch_cancel_btn);

        Intent get_intent =  getIntent();
        double startLatitude = get_intent.getDoubleExtra("startLatitude",0.0);
        double startLongitude = get_intent.getDoubleExtra("startLongitude",0.0);

        double endLatitude = get_intent.getDoubleExtra("endLatitude",0.0);
        double endLongitude = get_intent.getDoubleExtra("endLongitude",0.0);

//        endLatitude = Double.parseDouble(String.format("%.5f", endLatitude));
//        endLongitude = Double.parseDouble(String.format("%.5f", endLongitude));

//                String url = "kakaomap://route?sp=37.537229,127.005515&ep=37.4979502,127.0276368&by=FOOT";
        Log.d("route" , Double.toString(startLatitude) +" " + Double.toString(startLongitude));
        Log.d("route" , Double.toString(endLatitude) +" " + Double.toString(endLongitude));

        String kakao_url = "kakaomap://route?sp=" + Double.toString(startLongitude) + "," + Double.toString(startLatitude) +
                "&ep=" + Double.toString(endLongitude) + "," + Double.toString(endLatitude) +
                "&by=FOOT";

        String naver_url = "nmap://route/walk?slat=" + Double.toString(startLongitude) + "&slng=" +
                Double.toString(startLatitude) + "&sname=내 위치&dlat=" + Double.toString(endLongitude) +"&dlng=" +
                Double.toString(endLatitude) + "&dname=도착" +"&appname=com.example.smoking_area";

//        String naver_url = "nmap://route/walk?dlat=" + Double.toString(endLongitude) +"&dlng=" +
//                Double.toString(endLatitude) + "&appname=com.example.smoking_area";

//        String naver_url = "nmap://route/walk?slat=37.4640070&slng=126.9522394&sname=a&dlat=37.4764356&dlng=126.9618302&dname=동원낙성대아파트&appname=com.example.smoking_area";

        Log.d("route" , naver_url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(kakao_url));
//        getActivity().startActivity(intent);

        loadsearch_naver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(naver_url));
                startActivity(intent);
            }
        });

        loadsearch_kakao_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(kakao_url));
                startActivity(intent);
            }
        });

        loadsearch_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}