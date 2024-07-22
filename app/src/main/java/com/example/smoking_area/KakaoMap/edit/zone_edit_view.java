package com.example.smoking_area.KakaoMap.edit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.example.smoking_area.KakaoMap.Area;
import com.example.smoking_area.MainActivity;
import com.example.smoking_area.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class zone_edit_view extends AppCompatActivity {

    Area now_area;
    EditText title_editText, type_editText, size_editText, x_editText, y_editText;
    Spinner can_spinner, situation_spinner;
    LinearLayout x_linear, y_linear;
    ImageView zone_image;
    Bitmap bitmap;
    String area_can_str, situation_str, title_str, type_str, size_str, x_str, y_str;
    int my_locaiontxy=0, image_use=0;
    RequestManager requestManager;
    RequestBuilder<Drawable> requestBuilder;
    private ProgressDialog progressDialog;
    public RequestQueue requestQueue;

    String area_image_url ="http://gjchungsa.com/assignment/area_image/";
    String area_update_url ="http://gjchungsa.com/assignment/zone_update.php";

    JSONObject jsonObject = new JSONObject();
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_eidt_view);

        Intent get_intent = getIntent();
        now_area = (Area) get_intent.getSerializableExtra("Area");


        title_editText = findViewById(R.id.zone_edit_view_title_editText);
        type_editText = findViewById(R.id.zone_edit_view_type_editText);
        size_editText = findViewById(R.id.zone_edit_view_size_editText);
        can_spinner = findViewById(R.id.zone_edit_view_zone_can_spinner);
        situation_spinner = findViewById(R.id.zone_edit_view_zone_situation_spinner);
        x_editText = findViewById(R.id.zone_edit_view_x_editTextText);
        y_editText = findViewById(R.id.zone_edit_view_y_editTextText);
        x_linear = findViewById(R.id.zone_edit_view_x_linear);
        y_linear = findViewById(R.id.zone_edit_view_y_linear);
        zone_image = findViewById(R.id.zone_edit_view_zone_image);

        all_setting();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("업데이트 중입니다...");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //이미지 버튼 클릭시 이미지 불러오기
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK) {
                    try {

                        requestManager.clear(zone_image);

                        zone_image.setVisibility(View.VISIBLE);
                        Uri imageUri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        zone_image.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }


    }

    public void zone_edit_view_image_btn(View view){   //이미지 추가 버튼
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 1);
    }

    private void all_setting(){
        title_editText.setText(now_area.getArea_title());
        type_editText.setText(now_area.getArea_type());
        size_editText.setText(Integer.toString(now_area.getArea_size()));
        can_spinner_setting();
        x_editText.setText(Double.toString(now_area.getArea_x()));
        y_editText.setText(Double.toString(now_area.getArea_y()));
        situation_spinner_setting();


        requestManager = Glide.with(this);
        requestBuilder = requestManager.load(area_image_url + now_area.getArea_image1());
        requestBuilder.into(zone_image);
        zone_image.setVisibility(View.VISIBLE);

    }
    private void situation_spinner_setting(){
        ArrayList<String> List = new ArrayList<>();
        List.add("확인");
        List.add("보류");
        List.add("취소");
        ArrayList<String> Listk = new ArrayList<>();
        Listk.add("yes");
        Listk.add("hold");
        Listk.add("no");

        situation_spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_folder, List));

        situation_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 사용자가 항목을 선택했을 때 실행되는 코드
                if(List.get(position).equals("확인"))
                    situation_str = "yes";
                else if(List.get(position).equals("보류")) {
                    situation_str = "hold";
                }else if(List.get(position).equals("취소")) {
                    situation_str = "no";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무 항목도 선택하지 않았을 때 실행되는 코드
            }
        });

        for(int i=0;i<List.size();i++){
            if(now_area.getArea_situation().equals(Listk.get(i))){
                situation_spinner.setSelection(i);
            }
        }

    }
    private void can_spinner_setting(){
        ArrayList<String> List = new ArrayList<>();
        List.add("금연 구역");
        List.add("흡연 구역");

        can_spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_folder, List));
        Toast.makeText(getApplicationContext(), now_area.getArea_can() , Toast.LENGTH_SHORT).show();

        can_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        for(int i=0;i<List.size();i++){
            if(now_area.getArea_can().equals(List.get(i))){
                can_spinner.setSelection(i);
            }
        }
    }

    public void zone_eidt_x_and_y_btn(View view){
        Button x_and_y = findViewById(R.id.zone_edit_view_x_and_y);
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
    private void update_setting(){
        title_str = title_editText.getText().toString();
        type_str = type_editText.getText().toString();
        size_str = size_editText.getText().toString();

        if(my_locaiontxy==0) {

            x_str = Double.toString(now_area.getArea_x());
            y_str = Double.toString(now_area.getArea_y());

        } else if(my_locaiontxy ==1){

            x_str = x_editText.getText().toString();
            y_str = y_editText.getText().toString();

        }
    }

    public void zone_edit_update_btn(View view){
        progressDialog.show();
        update_setting();
        String image ="";
        String url_name = "";

        if(bitmap != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }


        try {
            jsonObject.put("no",Integer.toString(now_area.getArea_no()));
            jsonObject.put("title",title_str);
            jsonObject.put("type", type_str);
            jsonObject.put("can", area_can_str);
            jsonObject.put("x", x_str);
            jsonObject.put("y", y_str);
            jsonObject.put("size", size_str);
            jsonObject.put("situation", situation_str);
            jsonObject.put("url_name", url_name);
            jsonObject.put("image", image);

            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, area_update_url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(),"업로드 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                            Log.d("zone_update_add", response.toString());
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 모든 엑티비티를 종료하고 메인 엑티비티를 스택의 맨 위로 가져옴
                            startActivity(intent);;


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
            progressDialog.dismiss();
            Log.d("zone_update_add", e.toString());

            Toast.makeText(getApplicationContext(), situation_str, Toast.LENGTH_SHORT).show();
        }
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

}