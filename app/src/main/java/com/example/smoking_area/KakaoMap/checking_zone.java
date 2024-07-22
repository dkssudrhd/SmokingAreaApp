package com.example.smoking_area.KakaoMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.smoking_area.MainActivity;
import com.example.smoking_area.R;
import com.google.gson.Gson;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapType;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.MapViewInfo;
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
import com.kakao.vectormap.shape.ShapeLayer;
import com.kakao.vectormap.shape.ShapeLayerOptions;
import com.kakao.vectormap.shape.ShapeManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class checking_zone extends AppCompatActivity {
    LocationManager lm;
    MapView mapView;
//    final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    String smorking_list_url = "http://gjchungsa.com/assignment/test.php";

    ArrayList<Area> smoking_yes, smoking_no;
    KakaoMap kakaoMap;
    Label my_location_label = null;
    LabelManager labelManager;
    LabelLayer layer;
    LodLabelLayer lodLayer;
    Label[] labels;

    TextView smoking_title;
    ImageView smoking_imageView;
    LinearLayout smoking_area_linear;
    double mylocation_x=126.813754, mylocation_y=35.153788;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_zone);

        mapView = findViewById(R.id.map_view);
        smoking_title = findViewById(R.id.checking_zone_area_title);
        smoking_imageView = findViewById(R.id.checking_zone_area_imageView);
        smoking_area_linear = findViewById(R.id.checking_zone_area_linear);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mapView.start(new MapLifeCycleCallback() {
            @Override
            public void onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
                Toast.makeText(getApplicationContext(), "api 종료", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMapError(Exception error) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        }, new KakaoMapReadyCallback() {
            @NonNull
            @Override
            public LatLng getPosition() {   //지도 처음시작위치
                return LatLng.from(35.153788, 126.813754);
            }

            @NonNull
            @Override
            public int getZoomLevel() {     //지도 처음 줌 크기
                return 17;
            }

            @Override
            public void onMapReady(KakaoMap kakao) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                Toast.makeText(getApplicationContext(), "api 성공", Toast.LENGTH_SHORT).show();
                kakaoMap =kakao;

                //여기에 추가
                smoking_area_add();


            }

        });

    }
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
            String provider = location.getProvider();  // 위치정보
            mylocation_y = location.getLongitude(); // 위도
            mylocation_x = location.getLatitude(); // 경도


            my_location_label.hide();
            kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(mylocation_y, mylocation_x)));

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

        Volley.newRequestQueue(this).add(stringRequest);
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

    private void click_place(Area area) {
        String image_url = "http://gjchungsa.com/assignment/area_image/";
        smoking_area_linear.setVisibility(View.VISIBLE);
        smoking_title.setText(area.getArea_title());
        Glide.with(this)
                .load(image_url + area.getArea_image1())
                .into(smoking_imageView);

        RouteLineLayer routeLineLayer = kakaoMap.getRouteLineManager().getLayer();
        RouteLineStylesSet stylesSet = RouteLineStylesSet.from("blueStyles",
                RouteLineStyles.from(RouteLineStyle.from(16, Color.BLUE)));

        RouteLineSegment segment = RouteLineSegment.from(Arrays.asList(
                        LatLng.from(mylocation_y,mylocation_x),
                        LatLng.from(area.getArea_y(),area.getArea_x())))
                .setStyles(stylesSet.getStyles(0));

        RouteLineOptions options = RouteLineOptions.from(segment)
                .setStylesSet(stylesSet);
        RouteLine routeLine = routeLineLayer.addRouteLine(options);

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

    public void my_location(View view){
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 경우 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            Toast.makeText(getApplicationContext(),"접근허용 안되었음", Toast.LENGTH_SHORT).show();
        }

        else{
            // 가장최근 위치정보 가져오기
            Toast.makeText(getApplicationContext(),"접근허용 되었음", Toast.LENGTH_SHORT).show();

            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
                    Toast.makeText(getApplicationContext(),"null이 아님", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"null 맞음", Toast.LENGTH_SHORT).show();

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

            // 위치정보를 원하는 시간, 거리마다 갱신해준다.
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    10,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    10,
                    gpsLocationListener);
        }
    }

}