package com.example.catchgame.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.catchgame.models.GoodItem;
import com.example.catchgame.models.Player;
import com.example.catchgame.other.SkinHolderSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread gameThread;
    private volatile boolean isRunning;
    private static final int MAX_FPS = 60;
    private static final int MAX_FRAME_TIME = (int) (1000.0 / MAX_FPS);
    private static final double HAT_GROUND_VALUE_Y = 1.3;

    private Paint paint;
    private int screenX, screenY;
    private Player player;
    private List<GoodItem> goodItemList;
    private SurfaceHolder holder;
    private Random random;

    private Bitmap getGameBackground() {
        return SkinHolderSingleton.getHolder().getBackground();
    }


    public GameView(Context context, int screenX, int screenY) {
        super(context);
        paint = new Paint();
        random = new Random();
        this.screenX = screenX;
        this.screenY = screenY;
        holder = getHolder();
        SkinHolderSingleton.getHolder().generateSkins(getResources(), screenX, screenY);
        goodItemList = new ArrayList<>();
        goodItemList.add(new GoodItem(100, 100));
        player = new Player(getGameBackground().getWidth() / 2.5, getGameBackground().getHeight() / HAT_GROUND_VALUE_Y);
    }

    @Override
    public void run() {
        while (isRunning) {
            long frameStartTime = System.currentTimeMillis();
            long timeDiff = System.currentTimeMillis() - frameStartTime;
            long sleepTime = (int) (MAX_FRAME_TIME - timeDiff);
            update();
            draw();
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void draw() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();
            canvas.drawBitmap(getGameBackground(), 0, 0, paint);
            canvas.drawBitmap(player.getSkin(), (float) (player.x), (float) (player.y), paint);
            for (GoodItem item : goodItemList) {
                //canvas.drawRect((float)item.x, (float)item.y, (float)item.x+item.skin.getWidth(), (float)item.y+ item.skin.getHeight(), paint);
                canvas.drawBitmap(item.getSkin(), (float) (item.x), (float) (item.y), paint);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void update() {
        if (!player.isStanding) {
            if (player.isMovingLeft && player.x < (screenX - player.getSkin().getWidth())) {
                player.x += 40;
            } else if (!player.isMovingLeft && player.x > 0) {
                player.x -= 40;
            }
        }

    }

    public void start() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int X = (int) event.getX();
        int Y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                player.isMovingLeft = X >= screenX / 2;
                player.isStanding = false;
                break;
            case MotionEvent.ACTION_UP:
                player.isStanding = true;
                break;
        }
        return true;
    }


}