package com.example.catchgame.models;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.catchgame.other.SkinHolderSingleton;

public class Bomb extends Entity {

    public Bomb(double x, double y, double velocityY) {
        super(x, y, SkinHolderSingleton.getHolder().getBombSkin().getWidth(), SkinHolderSingleton.getHolder().getBombSkin().getHeight(), 0, velocityY);
    }

    @Override
    public Bitmap getSkin() {
        return SkinHolderSingleton.getHolder().getBombSkin();
    }

    @Override
    public Rect getHitBox() {
        return new Rect(
                (int) (x + width / 4),
                (int) (y + 7 * (height / 8)),
                (int) (x + 3 * (width / 4)),
                (int) y + height
        );
    }
}
