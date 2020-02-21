package com.exanmple.myview;

public class Fruit {
    private String name;
    private byte[] imageIcon;
    private float textSize = 20;
    private String additionalText = "";

    public Fruit(String name){
        this.name=name;
    }

    public Fruit(String name,byte[] iconByte){
        this.name=name;
        this.imageIcon=iconByte;
    }

    public Fruit(String name,byte[] iconByte, String additionalText){
        this.name=name;
        this.imageIcon=iconByte;
        this.additionalText = additionalText;
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

    public String getAdditionalText(){ return this.additionalText; }
}
