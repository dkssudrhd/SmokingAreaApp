package com.example.smoking_area.KakaoMap.edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smoking_area.KakaoMap.Area;
import com.example.smoking_area.KakaoMap.Area_Adapter;
import com.example.smoking_area.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class zone_edit_list extends AppCompatActivity {

    public ArrayList<Area> area_list = new ArrayList<>();
    private String area_list_no_url ="http://gjchungsa.com/assignment/area_list_no.php";
    RecyclerView recyclerView;
    int page_num =1;
    ImageButton left_btn, right_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_edit_list);

        recyclerView = findViewById(R.id.zone_edit_list_recyclerview);
        left_btn = findViewById(R.id.left_btn);
        right_btn = findViewById(R.id.right_btn);
        page_num=1;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, area_list_no_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Log.d("Area Volley", "응답 : " + response);
                        Area_list_setting(response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.i("Area Volley", "Area Volley 오류");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void Area_list_setting(String response) {       //Area_list_setting 나누기
        Gson gson = new Gson();
        area_list =new ArrayList<>();
        Area[] area = gson.fromJson(response, Area[].class);
        if (area != null) {
            area_list.addAll(Arrays.asList(area));
        }
        list_recycler();
    }

    private void list_recycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Area_Adapter adapter = new Area_Adapter();

        for(int i=0;i<area_list.size() && i<10;i++){
            adapter.addItem(area_list.get(i));
        }
        recyclerView.setAdapter(adapter);

        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num > 1){
                    page_num--;
                    Area_Adapter adapter = new Area_Adapter();

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< area_list.size() ; i++){
                        adapter.addItem(area_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num*10 < area_list.size()){
                    page_num++;
                    Area_Adapter adapter = new Area_Adapter();

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< area_list.size() ; i++){
                        adapter.addItem(area_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

}