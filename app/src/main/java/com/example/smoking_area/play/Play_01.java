package com.example.smoking_area.play;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.smoking_area.Area_user;
import com.example.smoking_area.KakaoMap.Area;
import com.example.smoking_area.KakaoMap.Area_like_recommend;
import com.example.smoking_area.KakaoMap.Area_user_info;
import com.example.smoking_area.R;
import com.google.gson.Gson;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.camera.CameraUpdateFactory;
import com.kakao.vectormap.label.Label;
import com.kakao.vectormap.label.LabelLayer;
import com.kakao.vectormap.label.LabelManager;
import com.kakao.vectormap.label.LabelOptions;
import com.kakao.vectormap.label.LabelStyle;
import com.kakao.vectormap.label.LabelStyles;
import com.kakao.vectormap.label.LodLabelLayer;
import com.kakao.vectormap.route.RouteLine;
import com.kakao.vectormap.route.RouteLineLayer;
import com.kakao.vectormap.route.RouteLineOptions;
import com.kakao.vectormap.route.RouteLineSegment;
import com.kakao.vectormap.route.RouteLineStyle;
import com.kakao.vectormap.route.RouteLineStyles;
import com.kakao.vectormap.route.RouteLineStylesSet;
import com.kakao.vectormap.shape.DotPoints;
import com.kakao.vectormap.shape.Polygon;
import com.kakao.vectormap.shape.PolygonOptions;
import com.kakao.vectormap.shape.PolygonStyle;
import com.kakao.vectormap.shape.PolygonStyles;
import com.kakao.vectormap.shape.ShapeManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Play_01 extends Fragment {

    LocationManager lm;
    MapView mapView;
    String smorking_list_url = "http://gjchungsa.com/assignment/area_list.php";
    String area_like = "http://gjchungsa.com/assignment/area_like_list.php";
    String image_url = "http://gjchungsa.com/assignment/area_image/";
    String delete_area_like_url = "http://gjchungsa.com/assignment/delete_area_like.php";
    String delete_area_recommend_url = "http://gjchungsa.com/assignment/delete_area_recommend.php";
    String insert_area_like_url = "http://gjchungsa.com/assignment/insert_area_like.php";
    String insert_area_recommend_url = "http://gjchungsa.com/assignment/insert_area_recommend.php";
    ArrayList<Area> smoking_yes = new ArrayList<>();
    ArrayList<Area> smoking_no = new ArrayList<>();
    KakaoMap kakaoMap;
    Label my_location_label = null;
    LabelManager labelManager;
    LabelLayer layer;
    LodLabelLayer lodLayer;
    Label[] labels;

    Area_like_recommend now_area_like_recommend = null;         // 현재 좋아요 클릭되어있는가
    Area now_checked_area = null;                  // 현제 선택한 Area
    TextView smoking_title;
    ImageView smoking_imageView;
    LinearLayout play_01_recommend_linear;
    double mylocation_x=126.9343, mylocation_y=35.1400;

    Area_user_info infoManager;
    Area_user user;

    ImageButton my_location_btn, search_zone_btn, play_01_first_start_btn;

    LinearLayout play_01_area_linear, play_01_load_direction_linear, play_01_first_checking;
    TextView play_01_area_title, play_01_area_address, play_01_count_textView;
    ImageView play_01_area_imageView1, play_01_area_imageView2, play_01_like, click_close;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_play_01, container, false);


        infoManager = new Area_user_info();
        user = infoManager.getUserInfo();

        mapView = rootView.findViewById(R.id.play_01_map_view);
        click_close = rootView.findViewById(R.id.click_close);
        play_01_area_linear = rootView.findViewById(R.id.play_01_area_linear);
        play_01_area_title = rootView.findViewById(R.id.play_01_area_title);
        play_01_area_address = rootView.findViewById(R.id.play_01_area_address);
        play_01_count_textView = rootView.findViewById(R.id.play_01_count_textView);
        play_01_area_imageView1 = rootView.findViewById(R.id.play_01_area_imageView1);
        play_01_area_imageView2 = rootView.findViewById(R.id.play_01_area_imageView2);
        play_01_like = rootView.findViewById(R.id.play_01_like);
        play_01_recommend_linear = rootView.findViewById(R.id.play_01_recommend_linear);         //추천
        play_01_load_direction_linear = rootView.findViewById(R.id.play_01_load_direction_linear);  //길안내
        search_zone_btn = rootView.findViewById(R.id.play_01_search_zone_btn);                      //근처 흡연구역 찾기
        play_01_first_start_btn = rootView.findViewById(R.id.play_01_first_start_btn);
        play_01_first_checking = rootView.findViewById(R.id.play_01_first_checking);

        if(infoManager.getFirst() != 0){
            play_01_first_checking.setVisibility(View.GONE);
        } else{
            play_01_first_checking.setVisibility(View.VISIBLE);
        }
        play_01_first_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_01_first_checking.setVisibility(View.GONE);
                infoManager.setFirst(1);
            }
        });

        click_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_01_area_linear.setVisibility(View.GONE);
            }
        });
        search_zone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double short_zone = 10000.0;
                double now_how_long;
                Area choice_area = new Area(0);
                for (int i=0; i<smoking_yes.size(); i++){
                    now_how_long = (smoking_yes.get(i).getArea_x() - mylocation_x)*(smoking_yes.get(i).getArea_x() - mylocation_x) +
                            (smoking_yes.get(i).getArea_y() - mylocation_y) * (smoking_yes.get(i).getArea_y() - mylocation_y);

                    if(short_zone> now_how_long){
                        short_zone = now_how_long;
                        choice_area = smoking_yes.get(i);
                    }
                }
                Toast.makeText(getActivity(), "가장 가까운 흡연구역은 " + choice_area.getArea_title() + "입니다.", Toast.LENGTH_LONG).show();
                click_place(choice_area);
            }
        });

        play_01_load_direction_linear.setOnClickListener(new View.OnClickListener() {               //길안내 버튼 클릭시 알고리즘
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), Loadsearch.class);
                intent.putExtra("startLatitude", mylocation_x);
                intent.putExtra("startLongitude", mylocation_y);
                intent.putExtra("endLatitude", now_checked_area.getArea_x());
                intent.putExtra("endLongitude", now_checked_area.getArea_y());
                startActivity(intent);

//                double startLatitude = mylocation_y;
//                double startLongitude = mylocation_x;
//
//                double endLatitude = now_checked_area.getArea_y();
//                double endLongitude = now_checked_area.getArea_x();
//
////                String url = "kakaomap://route?sp=37.537229,127.005515&ep=37.4979502,127.0276368&by=FOOT";
//                Log.d("route" , Double.toString(startLatitude) +" " + Double.toString(startLongitude));
//                Log.d("route" , Double.toString(endLatitude) +" " + Double.toString(startLongitude));
//
//                String kakao_url = "kakaomap://route?sp=" + Double.toString(startLatitude) + "," + Double.toString(startLongitude) +
//                        "&ep=" + Double.toString(endLatitude) + "," + Double.toString(endLongitude) +
//                        "&by=FOOT";
//
//                String naver_url = "namp://route/walk?slat=" + Double.toString(startLatitude) + "&slng=" +
//                        Double.toString(startLongitude) + "&dlat=" + Double.toString(endLatitude) +"&dlng=" +
//                        Double.toString(endLongitude);// +"&appname=com.example.smoking_area";
//
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(kakao_url));
//                getActivity().startActivity(intent);
            }
        });

        play_01_like.setOnClickListener(new View.OnClickListener() {    //좋아요 imageView의 클릭
            @Override
            public void onClick(View view) {
                if(now_area_like_recommend.getArea_like_checked() == 0){
                    play_01_like.setImageResource(R.drawable.icon_favorite);
                    now_area_like_recommend.setArea_like_checked(1);
                    area_like_checking();
                } else{
                    play_01_like.setImageResource(R.drawable.icon_favorite_border_);
                    now_area_like_recommend.setArea_like_checked(0);
                    area_like_not_checking();
                }
            }
        });
        play_01_recommend_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = now_checked_area.getCount();
                if(now_area_like_recommend.getArea_recommend_checked() ==0){
                    play_01_recommend_linear.setBackgroundResource(R.drawable.rounded_border_full);
                    now_area_like_recommend.setArea_recommend_checked(1);
                    play_01_count_textView.setText(Integer.toString(count+1));
                    now_checked_area.setCount(count+1);
                    area_recommend_checking();
                } else{
                    play_01_recommend_linear.setBackgroundResource(R.drawable.rounded_border);
                    now_area_like_recommend.setArea_recommend_checked(0);
                    play_01_count_textView.setText(Integer.toString(count-1));
                    now_checked_area.setCount(count-1);
                    area_recommend_not_checking();

                }
            }
        });

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        my_location_btn = rootView.findViewById(R.id.play_01_mylocation_btn);

        play_01_my_location_setting();          //내위치 아이콘

        mapView.start(new MapLifeCycleCallback() {
            @Override
            public void onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
                Toast.makeText(getActivity().getApplicationContext(), "api 종료", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMapError(Exception error) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Toast.makeText(getActivity().getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        }, new KakaoMapReadyCallback() {
            @NonNull
            @Override
            public LatLng getPosition() {   //지도 처음시작위치
                return LatLng.from(35.1400, 126.9343);
            }

            @NonNull
            @Override
            public int getZoomLevel() {     //지도 처음 줌 크기
                return 17;
            }

            @Override
            public void onMapReady(KakaoMap kakao) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                kakaoMap =kakao;
                   //여기에 추가
                smoking_area_add();
            }

        });


        return rootView;
    }

    private void area_recommend_not_checking() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, delete_area_recommend_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        area_like_setting(now_checked_area);
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
                params.put("area_no", Integer.toString(now_checked_area.getArea_no()));
                params.put("user_nickname", user.getUser_nickname());
                return params;
            }
        };

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }

    private void area_recommend_checking() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, insert_area_recommend_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        area_like_setting(now_checked_area);
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
                params.put("area_no", Integer.toString(now_checked_area.getArea_no()));
                params.put("user_nickname", user.getUser_nickname());
                return params;
            }
        };

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }

    private void area_like_not_checking() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, delete_area_like_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        area_like_setting(now_checked_area);
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
                params.put("area_no", Integer.toString(now_checked_area.getArea_no()));
                params.put("user_nickname", user.getUser_nickname());
                return params;
            }
        };

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }

    private void area_like_checking() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, insert_area_like_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        area_like_setting(now_checked_area);
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
                params.put("area_no", Integer.toString(now_checked_area.getArea_no()));
                params.put("user_nickname", user.getUser_nickname());
                return params;
            }
        };

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }


    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
            String provider = location.getProvider();  // 위치정보
            mylocation_x = location.getLongitude(); // 위도
            mylocation_y = location.getLatitude(); // 경도


            my_location_label.hide();
//            kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(mylocation_y, mylocation_x)));

            LabelStyles styles = kakaoMap.getLabelManager()
                    .addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.circle_s).setZoomLevel(12),
                            LabelStyle.from(R.drawable.circle).setZoomLevel(16)));
            LabelOptions options = LabelOptions.from(LatLng.from(mylocation_y, mylocation_x))
                    .setStyles(styles);
            LabelLayer layer = kakaoMap.getLabelManager().getLayer();

            my_location_label = layer.addLabel(options);
            my_location_label.show(true);

        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };

    public void smoking_area_add(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, smorking_list_url,
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

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }

    public void area_like_setting(Area area){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, area_like,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Log.d("Area Volley1", "응답 : " + response +"   " +Integer.toString(area.getArea_no()));
                        Area_like_recommend_list(response);

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
                params.put("area_no", Integer.toString(area.getArea_no()));
                return params;
            }
        };

        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }

    private void Area_like_recommend_list(String response) {
        Gson gson = new Gson();
        Area_like_recommend[] area_like_recommend = gson.fromJson(response, Area_like_recommend[].class);
        now_area_like_recommend = new Area_like_recommend(0, 0, now_checked_area.getArea_no(),
                "없음",0, 0);
        if(!response.equals("")){
            for(int i=0;i<area_like_recommend.length;i++){
                if(area_like_recommend[i].getUser_nickname().equals(user.getUser_nickname())){
                    now_area_like_recommend = area_like_recommend[i];
                }
            }

            if(now_area_like_recommend.getArea_like_checked() ==1)
                play_01_like.setImageResource(R.drawable.icon_favorite);
            else
                play_01_like.setImageResource(R.drawable.icon_favorite_border_);
            if(now_area_like_recommend.getArea_recommend_checked() ==1)
                play_01_recommend_linear.setBackgroundResource(R.drawable.rounded_border_full);
            else
                play_01_recommend_linear.setBackgroundResource(R.drawable.rounded_border);
        }
    }

    private void Area_list_setting(String response) {       //Area_list_setting 나누기
        Gson gson = new Gson();
        ArrayList<Area> areas =new ArrayList<>();
        Area[] area = gson.fromJson(response, Area[].class);
        smoking_no= new ArrayList<>();
        smoking_yes = new ArrayList<>();

        if (area != null) {
            areas.addAll(Arrays.asList(area));
        }

        for(int i=0; i<areas.size();i++){
            Area now_area = areas.get(i);
            if(now_area.getArea_can().equals("흡연 구역")){
                smoking_yes.add(now_area);
            }
            else if(now_area.getArea_can().equals("금연 구역")){
                smoking_no.add(now_area);
            }

        }
        smoking_no_zone(smoking_no);
        smoking_yes_zone(smoking_yes);

    }

    private void smoking_yes_zone(ArrayList<Area> areas){

        ShapeManager shapeManager = kakaoMap.getShapeManager();

        labelManager = kakaoMap.getLabelManager();
        layer = labelManager.getLayer();
        lodLayer = labelManager.getLodLayer();

        for(int i=0;i<areas.size();i++){

            PolygonStyles styles1 = PolygonStyles.from(PolygonStyle.from(Color.parseColor("#5000ff00")));
            PolygonOptions circleOptions1 = PolygonOptions.from(DotPoints.fromCircle(LatLng.from(
                    areas.get(i).getArea_y(), areas.get(i).getArea_x()), areas.get(i).getArea_size()*10),styles1);
            Polygon circle = shapeManager.getLayer().addPolygon(circleOptions1);
            circle.show();

        }

        labels = new Label[areas.size()];

        for(int i=0; i<areas.size();i++){
            LabelStyles styles =null;
            int num = i;
            styles = LabelStyles.from("smoking_yes" + Integer.toString(num),
                    LabelStyle.from(R.drawable.smoking_zone_icon).setZoomLevel(15));
            //,
            //                    LabelStyle.from(R.drawable.smoking_zone_icon).setZoomLevel(20)

            LabelOptions options = LabelOptions.from(LatLng.from(
                            areas.get(i).getArea_y(),
                            areas.get(i).getArea_x()))
                    .setClickable(true)
                    .setStyles(styles);
            Label label = layer.addLabel(options);


            labels[i] =label;

        }
        kakaoMap.setOnLabelClickListener(new KakaoMap.OnLabelClickListener() {
            @Override
            public void onLabelClicked(KakaoMap kakaoMap, LabelLayer layer, Label label) {
                for(int i=0; i<areas.size();i++){
                    String str1 = "smoking_yes" + Integer.toString(i);
                    if(label.getStyles().getStyleId().equals(str1)){
                        click_place(areas.get(i));
                    }
                }
            }
        });

    }


    private void click_place(Area area) {   //클릭시
        now_checked_area = area;
        play_01_area_linear.setVisibility(View.VISIBLE);
        play_01_area_title.setText(area.getArea_title());
        play_01_area_address.setText(area.getArea_address());
        play_01_count_textView.setText(Integer.toString(area.getCount()));
        area_like_setting(area);                                                //위시리스트

        Glide.with(this)
                .load(image_url + area.getArea_image1())
                .into(play_01_area_imageView1);

        Glide.with(this)
                .load(image_url + area.getArea_image2())
                .into(play_01_area_imageView2);

        kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(area.getArea_y(), area.getArea_x())));


//        RouteLineLayer routeLineLayer = kakaoMap.getRouteLineManager().getLayer();
//        RouteLineStylesSet stylesSet = RouteLineStylesSet.from("blueStyles",
//                RouteLineStyles.from(RouteLineStyle.from(16, Color.BLUE)));
//
//        RouteLineSegment segment = RouteLineSegment.from(Arrays.asList(
//                        LatLng.from(mylocation_y,mylocation_x),
//                        LatLng.from(area.getArea_y(),area.getArea_x())))
//                .setStyles(stylesSet.getStyles(0));
//
//        RouteLineOptions options = RouteLineOptions.from(segment)
//                .setStylesSet(stylesSet);
//        RouteLine routeLine = routeLineLayer.addRouteLine(options);

    }

    private void smoking_no_zone(ArrayList<Area> areas){
        ShapeManager shapeManager = kakaoMap.getShapeManager();

        for(int i=0;i<areas.size();i++){

            PolygonStyles styles1 = PolygonStyles.from(PolygonStyle.from(Color.parseColor("#50ff0000")));
            PolygonOptions circleOptions1 = PolygonOptions.from(DotPoints.fromCircle(LatLng.from(
                    areas.get(i).getArea_y(), areas.get(i).getArea_x()), areas.get(i).getArea_size()*10),styles1);
            Polygon circle = shapeManager.getLayer().addPolygon(circleOptions1);
            circle.show();

        }

    }

    public void play_01_my_location_setting(){
        my_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 없는 경우 권한 요청
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    Toast.makeText(getActivity().getApplicationContext(),"접근허용 안되었음", Toast.LENGTH_SHORT).show();
                }

                else{
                    // 가장최근 위치정보 가져오기
                    Toast.makeText(getActivity().getApplicationContext(),"접근허용 되었음", Toast.LENGTH_SHORT).show();

                    lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null) {
                        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }

                    if(location != null) {
                        String provider = location.getProvider();
                        mylocation_x = location.getLongitude();
                        mylocation_y = location.getLatitude();

                        double altitude = location.getAltitude();

                        if(my_location_label!=null){
                            my_location_label.hide();
                            my_location_label=null;
                        }
                        else {

                            LabelStyles styles = kakaoMap.getLabelManager()
                                    .addLabelStyles(LabelStyles.from("my_location", LabelStyle.from(R.drawable.circle)));
                            LabelOptions options = LabelOptions.from(LatLng.from(mylocation_y, mylocation_x))
                                    .setStyles(styles);
                            LabelLayer layer = kakaoMap.getLabelManager().getLayer();
                            my_location_label = layer.addLabel(options);
                            my_location_label.show();

                            kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(mylocation_y, mylocation_x)));
                        }
                    }

                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            100,
                            1,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            100,
                            1,
                            gpsLocationListener);

                    // 위치정보를 원하는 시간, 거리마다 갱신해준다.

                }
            }
        });

    }

}