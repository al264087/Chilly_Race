package com.al264087.chillyrace.model;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Sprite {

    private Bitmap bitmapToRender;
    private boolean hFlip;
    private float x;
    private float y;
    private int speedX;
    private int speedY;
    private int sizeX;
    private int sizeY;
    private List<Animation> animations;
    private Rect currentFrame;
    private boolean animated;

    public Sprite(Bitmap bitmapToRender, boolean hFlip, float x, float y, int speedX, int speedY, int sizeX, int sizeY) {
        this.bitmapToRender = bitmapToRender;
        this.hFlip = hFlip;
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.animations = null;
        this.currentFrame = null;
        this.animated = false;
    }

    public void move(float time) {
        setX(getX() + getSpeedX() * time);
        setY(getY() + getSpeedY() * time);

    }


    public void moveObstacle(float time) {
        setX(getX() + getSpeedX() * time);
        setY(getY() + getSpeedY() * time);

    }


    public boolean overlapBoundingBoxes(Sprite obstacle) {

        if (obstacle.getX() <= (getX() + (getSizeX() / 1.8)) && (obstacle.getX() + (obstacle.getSizeX() / 1.8)) >= getX()) {

            if (obstacle.getY() <= (getY() + (getSizeY() / 1.8)) && (obstacle.getY() + (obstacle.getSizeY() / 1.8)) >= getY()) {
                return true;
            } else
                return false;
        } else
            return false;
    }

  /*  public boolean overlapBoundingBoxesWitObstacles(Sprite obstacle) {

        if (obstacle.getX() <= (getX() + (getSizeX())) && (obstacle.getX() + (obstacle.getSizeX()) >= getX())){

            if (obstacle.getY() <= (getY() + getSizeY()) && (obstacle.getY() + obstacle.getSizeY()) >= getY()){
                return true;
            } else
                return false;
        } else
            return false;
    }
    */
    public void addAnimation(Animation animation)
    {
        if(animations!=null) {
            animations.add(animation);
        }
        else{
            animations=new ArrayList<>();
            animations.add(animation);
        }
        animated=true;
    }

    public Animation getAnimation(int id){
        return animations.get(id);
    }

    public Animation getAnimation(){
        return animations.get(0);
    }
    public List<Animation> getAnimations(){
        return animations;
    }

    public boolean isAnimated() {
        return animated;
    }

    public Bitmap getBitmapToRender() {
        return bitmapToRender;
    }

    public void setBitmapToRender(Bitmap bitmapToRender) {
        this.bitmapToRender = bitmapToRender;
    }

    public boolean ishFlip() {
        return hFlip;
    }

    public void sethFlip(boolean hFlip) {
        this.hFlip = hFlip;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public Rect getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(Rect currentFrame) {
        this.currentFrame = currentFrame;
    }



}
