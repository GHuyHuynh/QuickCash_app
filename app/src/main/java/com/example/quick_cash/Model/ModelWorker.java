package com.example.quick_cash.Model;

/**
 * Worker model includes the image and description
 */
public class ModelWorker {
    private int img;
    private String des;

    public ModelWorker() {
    }

    public ModelWorker(int img, String des) {
        this.img = img;
        this.des = des;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
