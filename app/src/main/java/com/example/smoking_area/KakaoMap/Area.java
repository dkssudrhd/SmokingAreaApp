package com.example.smoking_area.KakaoMap;

import java.io.Serializable;

public class Area implements Serializable {
    int area_no;        //구역 고유번호
    String area_title;  //구역 타이틀
    String area_type;   //구역 타입
    String area_address; //구역 주소
    String area_can;    //흡연 가능여부
    double area_x;      //구역의 경도
    double area_y;      //구역의 위도
    int area_size;      //구역의 사이즈
    String area_situation;
    String user_nickname;
    String area_image1;
    String area_image2;
    int area_recommned;     // 따봉수
    String area_creation;   // insert시간
    String area_content;    // 내용
    int count;              // 추천수

    public Area(int area_no) {
        this.area_no = area_no;
    }

    public Area(int area_no, String area_title, String area_type, String area_can, double area_x, double area_y, int area_size, String area_situation, String user_nickname, String area_image1) {
        this.area_no = area_no;
        this.area_title = area_title;
        this.area_type = area_type;
        this.area_can = area_can;
        this.area_x = area_x;
        this.area_y = area_y;
        this.area_size = area_size;
        this.area_situation = area_situation;
        this.user_nickname = user_nickname;
        this.area_image1 = area_image1;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getArea_content() {
        return area_content;
    }

    public void setArea_content(String area_content) {
        this.area_content = area_content;
    }

    public String getArea_creation() {
        return area_creation;
    }

    public void setArea_creation(String area_creation) {
        this.area_creation = area_creation;
    }

    public int getArea_recommned() {
        return area_recommned;
    }

    public void setArea_recommned(int area_recommned) {
        this.area_recommned = area_recommned;
    }

    public String getArea_address() {
        return area_address;
    }

    public void setArea_address(String area_address) {
        this.area_address = area_address;
    }

    public String getArea_situation() {
        return area_situation;
    }

    public void setArea_situation(String area_situation) {
        this.area_situation = area_situation;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getArea_image1() {
        return area_image1;
    }

    public void setArea_image1(String area_image) {
        this.area_image1 = area_image;
    }

    public String getArea_image2() {
        return area_image2;
    }

    public void setArea_image2(String area_image) {
        this.area_image2 = area_image;
    }

    public int getArea_no() {
        return area_no;
    }

    public void setArea_no(int area_no) {
        this.area_no = area_no;
    }

    public String getArea_title() {
        return area_title;
    }

    public void setArea_title(String area_title) {
        this.area_title = area_title;
    }

    public String getArea_type() {
        return area_type;
    }

    public void setArea_type(String area_type) {
        this.area_type = area_type;
    }

    public String getArea_can() {
        return area_can;
    }

    public void setArea_can(String area_can) {
        this.area_can = area_can;
    }

    public double getArea_x() {
        return area_x;
    }

    public void setArea_x(double area_x) {
        this.area_x = area_x;
    }

    public double getArea_y() {
        return area_y;
    }

    public void setArea_y(double area_y) {
        this.area_y = area_y;
    }

    public int getArea_size() {
        return area_size;
    }

    public void setArea_size(int area_size) {
        this.area_size = area_size;
    }
}
