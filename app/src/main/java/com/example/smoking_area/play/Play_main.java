package com.example.smoking_area.play;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.smoking_area.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Play_main extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_main);

        bottomNavigationView = findViewById(R.id.play_main_bottombar);

        getSupportFragmentManager().beginTransaction().add(R.id.play_main_framelayout, new Play_01()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getTitle().equals("현위치")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.play_main_framelayout, new Play_01()).commit();
                } else if (item.getTitle().equals("위시리스트")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.play_main_framelayout, new Play_02()).commit();
                } else if (item.getTitle().equals("장소추가")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.play_main_framelayout, new Play_03()).commit();
                } else if (item.getTitle().equals("마이페이지")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.play_main_framelayout, new Play_04()).commit();
                }
                return true;
            }
        });
    }
}