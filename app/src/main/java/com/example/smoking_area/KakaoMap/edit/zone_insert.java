package com.example.smoking_area.KakaoMap.edit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smoking_area.Area_user;
import com.example.smoking_area.KakaoMap.Area_user_info;
import com.example.smoking_area.MainActivity;
import com.example.smoking_area.R;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.camera.CameraUpdateFactory;
import com.kakao.vectormap.label.LabelLayer;
import com.kakao.vectormap.label.LabelOptions;
import com.kakao.vectormap.label.LabelStyle;
import com.kakao.vectormap.label.LabelStyles;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class zone_insert extends AppCompatActivity {

    Button photo_upload;
    ImageView zone_image1, zone_image2;
    Bitmap bitmap1= null, bitmap2 = null;
    JsonObjectRequest jsonObjectRequest;
    JSONObject jsonObject = new JSONObject();
    public RequestQueue requestQueue;
    Area_user_info infoManager; //로그인 확인
    Area_user user;  //유저
    String zone_insert_url = "http://gjchungsa.com/assignment/zone_insert.php";

    EditText title_editTextText, type_editTextText, size_editTextText, x_editTextText, y_editTextText;
    Spinner zone_can_spinner;
    private ProgressDialog progressDialog;
    String area_can_str, title_str, type_str, size_str, y_str, x_str;
    int my_locaiontxy=0;
    LocationManager lm;
    double now_x, now_y;
    int image_num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_insert);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        my_location_now();
        zone_image1 = findViewById(R.id.zone_image1);
        zone_image2 = findViewById(R.id.zone_image2);
        title_editTextText = findViewById(R.id.title_editTextText);
        type_editTextText = findViewById(R.id.type_editTextText);
        size_editTextText = findViewById(R.id.size_editTextText);
        zone_can_spinner = findViewById(R.id.zone_can_spinner);
        x_editTextText = findViewById(R.id.x_editTextText);
        y_editTextText = findViewById(R.id.y_editTextText);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("업로드 중입니다...");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        user = infoManager.getUserInfo();
        spinner_add();
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
            String provider = location.getProvider();  // 위치정보
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도

            now_y = latitude;
            now_x = longitude;

        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };


    public void spinner_add(){
        ArrayList<String> List = new ArrayList<>();
        List.add("금연 구역");
        List.add("흡연 구역");

        zone_can_spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_folder, List));

        zone_can_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 사용자가 항목을 선택했을 때 실행되는 코드
                if(List.get(position).equals("금연 구역"))
                    area_can_str = "금연 구역";
                else if(List.get(position).equals("흡연 구역")) {
                    area_can_str = "흡연 구역";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무 항목도 선택하지 않았을 때 실행되는 코드
            }
        });

    }

    public void image_btn(View view){   //이미지 추가 버튼
        image_num++;
        if(image_num<3) {
            Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(select, image_num);
        }
    }
    public void upload_setting(){
        title_str = title_editTextText.getText().toString();
        type_str = type_editTextText.getText().toString();
        size_str = size_editTextText.getText().toString();
        if (my_locaiontxy==1){
            x_str = x_editTextText.getText().toString();
            y_str = y_editTextText.getText().toString();
        }
        else{
            x_str = Double.toString(now_x);
            y_str = Double.toString(now_y);
        }
    }

    public void x_and_y_btn(View view){
        Button x_and_y = findViewById(R.id.x_and_y);
        LinearLayout x_linear = findViewById(R.id.x_linear);
        LinearLayout y_linear = findViewById(R.id.y_linear);
        if(my_locaiontxy==0) {
            my_locaiontxy=1;
            x_linear.setVisibility(View.VISIBLE);
            y_linear.setVisibility(View.VISIBLE);
            x_and_y.setText("내 위치사용");
        }
        else {
            my_locaiontxy =0;
            x_and_y.setText("다른위치사용");
            x_linear.setVisibility(View.GONE);
            y_linear.setVisibility(View.GONE);
        }
    }

    public void upload_btn(View view) {
        progressDialog.show();
        upload_setting();
        String image1 ="";
        String url_name1 = "";
        String image2 ="";
        String url_name2 = "";

        if(bitmap1 != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image1 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name1 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }
        if(bitmap2 != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image2 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name2 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }



        try {
            jsonObject.put("title",title_str);
            jsonObject.put("type", type_str);
            jsonObject.put("can", area_can_str);
            jsonObject.put("x", x_str);
            jsonObject.put("y", y_str);
            jsonObject.put("size", size_str);
            jsonObject.put("nickname", user.getUser_nickname());
            jsonObject.put("url_name1", url_name1);
            jsonObject.put("image1", image1);
            jsonObject.put("url_name2", url_name2);
            jsonObject.put("image2", image2);

            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, zone_insert_url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(),"업로드 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                            Log.d("zone_insert_add", response.toString());
                            finish();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                }
            }


            );
        } catch (JSONException e) {
            e.printStackTrace();
        }


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //이미지 버튼 클릭시 이미지 불러오기
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK) {
                    try {
                        zone_image1.setVisibility(View.VISIBLE);
                        Uri imageUri = data.getData();
                        bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        zone_image1.setImageBitmap(bitmap1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
            case 2: {
                if (resultCode == RESULT_OK) {
                    try {
                        zone_image2.setVisibility(View.VISIBLE);
                        Uri imageUri = data.getData();
                        bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        zone_image2.setImageBitmap(bitmap2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }


    }

    public void my_location_now(){
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0 );

        }
        else{
            // 가장최근 위치정보 가져오기
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                String provider = location.getProvider();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double altitude = location.getAltitude();

                now_x = latitude;
                now_y = longitude;
            }

            // 위치정보를 원하는 시간, 거리마다 갱신해준다.
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    100,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    100,
                    1,
                    gpsLocationListener);
        }
    }

    public void back_btn(View view){
        finish();
    }
}