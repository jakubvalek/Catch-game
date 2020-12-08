package com.example.catchgame.other;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.catchgame.R;

import java.util.Random;

/**
 * Singleton pattern
 * Loads skins once and then retrieves the already loaded skins
 */
public class SkinHolderSingleton {

    private static SkinHolderSingleton holder;
    private Bitmap[] goodItemSkins = null;
    private Bitmap background;
    private Bitmap playerSkin;

    private Random rnd;
    private Bitmap bombSkin;

    private SkinHolderSingleton() {
        rnd = new Random();
    }

    public int getRandomSkinIndex() {
        return rnd.nextInt(6);
    }

    public static SkinHolderSingleton getHolder() {
        if (holder == null)
            holder = new SkinHolderSingleton();
        return holder;
    }

    public void generateSkins(Resources resources, int screenX, int screenY) {
        if (goodItemSkins != null && background != null)
            return;
        Bitmap skin = BitmapFactory.decodeResource(resources, R.drawable.icons);
        int maxRows = 2;
        int maxColumns = 3;
        int trueIndex = 0;
        goodItemSkins = new Bitmap[maxColumns * maxRows];
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < maxColumns; j++) {
                goodItemSkins[trueIndex] = Bitmap.createBitmap(skin, (skin.getWidth() / maxColumns) * j, (skin.getHeight() / maxRows) * i, skin.getWidth() / maxColumns, skin.getHeight() / maxRows);
                goodItemSkins[trueIndex] = Bitmap.createScaledBitmap(goodItemSkins[trueIndex], goodItemSkins[trueIndex].getWidth() / 2, goodItemSkins[trueIndex].getHeight() / 2, false);
                trueIndex++;
            }
        }

        background = BitmapFactory.decodeResource(resources, R.drawable.background);
        background = Bitmap.createScaledBitmap(background, background.getWidth(), screenY, false);

        playerSkin = BitmapFactory.decodeResource(resources, R.drawable.hat);
        playerSkin = Bitmap.createScaledBitmap(playerSkin, playerSkin.getWidth() / 4, playerSkin.getHeight() / 4, false);

        bombSkin = BitmapFactory.decodeResource(resources, R.drawable.bomb);
        //bombSkin.setWidth(goodItemSkins[0].getWidth() / 4);
        //bombSkin.setHeight(goodItemSkins[0].getHeight() / 4);
        bombSkin = Bitmap.createScaledBitmap(bombSkin, goodItemSkins[0].getWidth(), goodItemSkins[0].getHeight(), false);

    }

    public Bitmap getBackground() {
        return background;
    }

    public Bitmap getPlayerSkin() {
        return playerSkin;
    }

    public Bitmap getGoodItemSkin(int index) {
        return goodItemSkins[index];
    }

    public Bitmap getBombSkin() {
        return bombSkin;
    }
}
