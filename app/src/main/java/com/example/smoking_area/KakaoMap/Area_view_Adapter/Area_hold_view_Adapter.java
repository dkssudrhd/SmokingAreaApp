package com.example.smoking_area.KakaoMap.Area_view_Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smoking_area.KakaoMap.Area;
import com.example.smoking_area.R;

import java.util.ArrayList;

public class Area_hold_view_Adapter extends  RecyclerView.Adapter<Area_hold_view_Adapter.ViewHolder>{


    ArrayList<Area> items = new ArrayList<>();

    public void addItem(Area area){
        items.add(area);
    }
    public void setItems(ArrayList<Area> items){
        this.items = items;
    }
    public Area getItem(int position){
        return items.get(position);
    }

    public Area_hold_view_Adapter(ArrayList<Area> items){
        this.items = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.area_hold_view_item, parent, false);
        return new Area_hold_view_Adapter.ViewHolder(itemView);
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
        TextView title, content, m;

        String image_url = "http://gjchungsa.com/assignment/area_image/";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setItem(Area area){
            layout = itemView.findViewById(R.id.area_hold_view_item_linear);
            title = itemView.findViewById(R.id.area_hold_view_item_title_textView);
            content = itemView.findViewById(R.id.area_hold_view_item_content_textView);
            m = itemView.findViewById(R.id.area_hold_view_item_m_textView);
            imageView = itemView.findViewById(R.id.area_hold_view_item_ImageView);

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
            content.setText(area.getArea_content());
            String m_str = area.getArea_creation() + "|" + area.getUser_nickname();
            m.setText("신청자 : " + area.getUser_nickname() + " | " + area.getArea_creation());
        }
    }
}

