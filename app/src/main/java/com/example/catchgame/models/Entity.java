package com.example.catchgame.models;

import android.graphics.Bitmap;
import android.graphics.Rect;

public abstract class Entity {
    public double x;
    public double y;
    public int width;
    public int height;
    public double velocityX;
    public double velocityY;

    public Entity(double x, double y, int width, int height, double velocityX, double velocityY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public abstract Bitmap getSkin();
    public abstract Rect getHitBox();

}
