package com.example.smoking_area.KakaoMap;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smoking_area.KakaoMap.edit.zone_edit_view;
import com.example.smoking_area.R;

import java.util.ArrayList;

public class Area_Adapter extends  RecyclerView.Adapter<Area_Adapter.ViewHolder>{


    ArrayList<Area> items =new ArrayList<>();

    public void addItem(Area area){
        items.add(area);
    }
    public void setItems(ArrayList<Area> items){
        this.items = items;
    }
    public Area getItem(int position){
        return items.get(position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.area_edit_item, parent, false);
        return new Area_Adapter.ViewHolder(itemView);
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
        TextView title, can, situation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setItem(Area area){
            layout = itemView.findViewById(R.id.Area_item_linear);
            title = itemView.findViewById(R.id.Area_title_textView);
            can = itemView.findViewById(R.id.Area_can_textView);
            situation = itemView.findViewById(R.id.Area_situation_textView);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), zone_edit_view.class);
                    intent.putExtra("Area", area);
                    itemView.getContext().startActivity(intent);
                }
            });

            title.setText(area.getArea_title());
            can.setText(area.getArea_can());
            situation.setText(area.getArea_situation());
        }
    }
}

