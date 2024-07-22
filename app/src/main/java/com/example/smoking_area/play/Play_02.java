package com.example.smoking_area.play;

import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smoking_area.Area_user;
import com.example.smoking_area.KakaoMap.Area;
import com.example.smoking_area.KakaoMap.Area_user_info;
import com.example.smoking_area.KakaoMap.Area_view_Adapter.Area_view_Adapter;
import com.example.smoking_area.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Play_02 extends Fragment {

    RecyclerView recyclerView;
    Area_view_Adapter adapter;
    ArrayList<Area> area_all_list;
    ArrayList<Area> area_list;
    private String area_like_list_url ="http://gjchungsa.com/assignment/area_list_like.php";

    Area_user_info infoManager;
    Area_user user;

    int page_num=1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_play_02, container, false);

        infoManager = new Area_user_info();
        user = infoManager.getUserInfo();

        recyclerView = rootView.findViewById(R.id.play_02_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        area_all_list = new ArrayList<>();
        area_list = new ArrayList<>();

        area_all_list_setting();

        
        return rootView;
    }

    private void area_list_setting(){

        for(int i=0; i<10 && i<area_all_list.size() ;i++){
            area_list.add(area_all_list.get(i));
        }

        Log.d("Area Volley", "area_all_list : " + Integer.toString(area_all_list.size()) +
                " area_list : " + Integer.toString(area_list.size()));
//        adapter = new Area_view_Adapter(area_list);
        adapter = new Area_view_Adapter(area_list);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                // 스크롤이 끝까지 도달하면 더 많은 데이터를 로드
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                    // 데이터를 로드하고 RecyclerView 어댑터에 추가
                    loadMoreData();
                }
            }
        });
    }
    private void area_all_list_setting() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, area_like_list_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Log.d("Area Volley", "응답 : " + response);
                        Log.d("Area Volley", "응답 : " + user.getUser_nickname());
                        Gson gson = new Gson();
                        area_all_list =new ArrayList<>();
                        Area[] area = gson.fromJson(response, Area[].class);
                        if (area != null) {
                            area_all_list.addAll(Arrays.asList(area));
                        }
                        area_list_setting();
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
                params.put("user_nickname", user.getUser_nickname());

                return params;
            }
        };

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }

    private void loadMoreData() {

        for(int i=(page_num*10)+1; i<=(page_num*10)+10 && i<=area_all_list.size() ;i++){
            area_list.add(area_all_list.get(i));
        }
        adapter.notifyDataSetChanged();

    }
}