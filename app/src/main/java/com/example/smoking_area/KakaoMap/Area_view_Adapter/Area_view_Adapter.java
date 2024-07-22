package com.example.smoking_area.KakaoMap.Area_view_Adapter;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smoking_area.KakaoMap.Area;
import com.example.smoking_area.KakaoMap.edit.zone_edit_view;
import com.example.smoking_area.R;

import java.util.ArrayList;

public class Area_view_Adapter extends RecyclerView.Adapter<Area_view_Adapter.ViewHolder> {


    ArrayList<Area> items = new ArrayList<>();

    public void addItem(Area area) {
        items.add(area);
    }

    public void setItems(ArrayList<Area> items) {
        this.items = items;
    }

    public Area getItem(int position) {
        return items.get(position);
    }

    public Area_view_Adapter(ArrayList<Area> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.area_view_item, parent, false);
        return new Area_view_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Area item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        ImageView imageView;
        TextView title, address, m;

        LocationManager lm;
        String image_url = "http://gjchungsa.com/assignment/area_image/";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItem(Area area) {
            layout = itemView.findViewById(R.id.area_view_item_linear);
            title = itemView.findViewById(R.id.area_view_item_title_textView);
            address = itemView.findViewById(R.id.area_view_item_address_textView);
            m = itemView.findViewById(R.id.area_view_item_m_textView);
            imageView = itemView.findViewById(R.id.area_view_item_ImageView);

            Glide.with(itemView.getContext())
                    .load(image_url + area.getArea_image1())
                    .into(imageView);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(itemView.getContext(), zone_edit_view.class);
//                    intent.putExtra("Area", area);
//                    itemView.getContext().startActivity(intent);
                }
            });

            title.setText(area.getArea_title());
            address.setText(area.getArea_address());

            lm = (LocationManager) imageView.getContext().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(itemView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(itemView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            double mylocation_x = location.getLongitude();
            double mylocation_y = location.getLatitude();

            double distance = haversine(mylocation_y, mylocation_x, area.getArea_y(), area.getArea_x());


            String formattedDistance = String.format("%.2f", distance);
            m.setText(formattedDistance + "km 떨어져 있습니다.");

        }
        private static double haversine(double lat1, double lon1, double lat2, double lon2) {
            // 지구의 반지름 (단위: 킬로미터)
            double R = 6371.0;

            // 라디안으로 변환
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);

            // 하버사인 공식 적용
            double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin(dLon / 2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            // 거리 계산
            return R * c;
        }
    }
}

