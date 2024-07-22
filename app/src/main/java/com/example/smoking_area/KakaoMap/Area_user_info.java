package com.example.smoking_area.KakaoMap;

import com.example.smoking_area.Area_user;

public class Area_user_info {
    public static Area_user area_user;
    public static int first;

    public static int getFirst() {
        return first;
    }

    public static void setFirst(int first) {
        Area_user_info.first = first;
    }

    public static Area_user getUserInfo(){
        return area_user;
    }
    public static void setUserInfo(Area_user user){
        area_user = user;
    }
}
