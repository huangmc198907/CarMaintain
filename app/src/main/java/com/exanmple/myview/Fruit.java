package com.exanmple.myview;

public class Fruit {
    private String name;
    private byte[] imageIcon;
    private float textSize = 20;

    public Fruit(String name,byte[] iconByte){
        this.name=name;
        this.imageIcon=iconByte;
    }

    public String getName(){
        return name;
    }

    public byte[] getImageIcon(){
        return  imageIcon;
    }

    public float getTextSize(){
        return textSize;
    }

    public void setTextSize(float size){
        textSize = size;
    }
}
