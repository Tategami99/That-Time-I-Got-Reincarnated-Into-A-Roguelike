package com.unanglaro.topdown.main;

public class Entity {
    public float health;
    private float xPos, yPos, width, height;
    public float getX(){
        return xPos;
    }
    public float getY(){
        return yPos;
    }
    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }
    public void setX(float x){
        xPos = x;
    }
    public void setY(float y){
        yPos = y;
    }
    public void setWidth(float Width){
        width = Width;
    }
    public void setHeight(float Height){
        height = Height;
    }
}
