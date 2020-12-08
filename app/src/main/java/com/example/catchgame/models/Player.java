package com.example.catchgame.models;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.catchgame.other.SkinHolderSingleton;

public class Player extends Entity {

    public boolean isMovingLeft;
    public boolean isStanding;

    public Player(double x, double y) {
        super(x, y, SkinHolderSingleton.getHolder().getPlayerSkin().getWidth(), SkinHolderSingleton.getHolder().getPlayerSkin().getHeight(), 30, 0);
        isMovingLeft = false;
        isStanding = true;
    }

    @Override
    public Bitmap getSkin() {
        return SkinHolderSingleton.getHolder().getPlayerSkin();
    }

    @Override
    public Rect getHitBox() {
        return new Rect(
                (int) x + width/4,
                (int) y,
                (int) (x + 3*(width/4)),
                (int) (y + height / 8)
        );
    }

}
