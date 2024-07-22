package com.example.smoking_area.KakaoMap;

public class Area_like_recommend {
    int area_like_no;
    int area_recommend_no;
    int area_no;
    String user_nickname;
    int area_like_checked;
    int area_recommend_checked;

    public Area_like_recommend(int area_like_no, int area_recommend_no, int area_no, String user_nickname, int area_like_checked, int area_recommend_checked) {
        this.area_like_no = area_like_no;
        this.area_recommend_no = area_recommend_no;
        this.area_no = area_no;
        this.user_nickname = user_nickname;
        this.area_like_checked = area_like_checked;
        this.area_recommend_checked = area_recommend_checked;
    }

    public int getArea_like_no() {
        return area_like_no;
    }

    public void setArea_like_no(int area_like_no) {
        this.area_like_no = area_like_no;
    }

    public int getArea_recommend_checked() {
        return area_recommend_checked;
    }

    public void setArea_recommend_checked(int area_recommend_checked) {
        this.area_recommend_checked = area_recommend_checked;
    }

    public int getArea_no() {
        return area_no;
    }

    public void setArea_no(int area_no) {
        this.area_no = area_no;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public int getArea_like_checked() {
        return area_like_checked;
    }

    public void setArea_like_checked(int area_like_checked) {
        this.area_like_checked = area_like_checked;
    }

    public int getArea_recommend_no() {
        return area_recommend_no;
    }

    public void setArea_recommend_no(int area_recommend_no) {
        this.area_recommend_no = area_recommend_no;
    }
}
