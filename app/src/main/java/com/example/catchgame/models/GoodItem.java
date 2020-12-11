package com.example.catchgame.models;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.catchgame.other.SkinHolderSingleton;

public class GoodItem extends Entity {

    private final int skinIndex;

    public GoodItem(double x, double y, double velocityY) {
        super(x, y, SkinHolderSingleton.getHolder().getGoodItemSkin(0).getWidth(), SkinHolderSingleton.getHolder().getGoodItemSkin(0).getHeight(), 0, velocityY);
        skinIndex = SkinHolderSingleton.getHolder().getRandomSkinIndex();
    }

    @Override
    public Bitmap getSkin() {
        return SkinHolderSingleton.getHolder().getGoodItemSkin(skinIndex);
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
