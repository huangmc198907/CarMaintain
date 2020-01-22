package com.exanmple.carmaintain;

public class Fruit {
    private String name;
    private int imageId;
    private float textSize = 0;

    public Fruit(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }

    public String getName(){
        return name;
    }

    public int getImageId(){
        return  imageId;
    }

    public float getTextSize(){
        return textSize;
    }

    public void setTextSize(float size){
        textSize = size;
    }
}
