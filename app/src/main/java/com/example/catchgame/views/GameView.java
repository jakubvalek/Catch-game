package com.example.catchgame.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.catchgame.models.Bomb;
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
    private static final int FONT_SIZE = 80;
    private static final int GOOD_ITEM = 0;
    private static final int BOMB = 1;
    private static final int MAX_CHANCE = 100;
    private static final int BOMB_CHANCE = 20;
    private static final int[] decisions = new int[]{GOOD_ITEM, BOMB};

    private Paint paint;
    private int screenX, screenY;
    private Player player;
    private List<GoodItem> goodItemList;
    private List<Bomb> bombsList;
    private SurfaceHolder holder;
    private Random random;
    private int score = 0;
    private long lastCalledRun = 0;


    public GameView(Context context, int screenX, int screenY) {
        super(context);
        paint = new Paint();
        paint.setTextSize(FONT_SIZE);
        random = new Random();
        this.screenX = screenX;
        this.screenY = screenY;
        holder = getHolder();
        SkinHolderSingleton.getHolder().generateSkins(getResources(), screenX, screenY);
        goodItemList = new ArrayList<>();
        bombsList = new ArrayList<>();
        goodItemList.add(new GoodItem(100, 100));
        player = new Player(getGameBackground().getWidth() / 2.5, getGameBackground().getHeight() / HAT_GROUND_VALUE_Y);
    }

    @Override
    public void run() {
        while (isRunning) {
            long frameStartTime = System.currentTimeMillis();
            update();
            draw();
            long timeDiff = System.currentTimeMillis() - frameStartTime;
            spawnNewItem(timeDiff);
            long sleepTime = (int) (MAX_FRAME_TIME - timeDiff);
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
            // Player - show hitBox
            //canvas.drawRect(player.getHitBox(), paint);
            for (GoodItem item : goodItemList) {
                // Item - show hitBox
                //canvas.drawRect(item.getHitBox(), paint);
                canvas.drawBitmap(item.getSkin(), (float) (item.x), (float) (item.y), paint);
            }
            for (Bomb bomb : bombsList) {
                // Item - show hitBox
                //canvas.drawRect(bomb.getHitBox(), paint);
                canvas.drawBitmap(bomb.getSkin(), (float) (bomb.x), (float) (bomb.y), paint);
            }
            canvas.drawText("Score: " + score, FONT_SIZE, FONT_SIZE, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void update() {
        resolveEntityMovement();
        resolveHitBoxes();
    }

    private void resolveHitBoxes() {
        List<GoodItem> toDeleteItems = new ArrayList<>();
        List<Bomb> toDeleteBombs = new ArrayList<>();
        for (GoodItem item : goodItemList) {
            if(item.getHitBox().intersect(player.getHitBox())){
                score++;
                toDeleteItems.add(item);
            }
            if(item.y > screenY)
                toDeleteItems.add(item);
        }
        for (Bomb bomb : bombsList) {
            if(bomb.getHitBox().intersect(player.getHitBox())){
                isRunning = false;
            }
            if(bomb.y > screenY)
                toDeleteBombs.add(bomb);
        }
        goodItemList.removeAll(toDeleteItems);
        bombsList.removeAll(toDeleteBombs);
    }

    private void resolveEntityMovement() {
        if (!player.isStanding) {
            if (player.isMovingLeft && player.x < (screenX - player.getSkin().getWidth())) {
                player.x += player.velocityX;
            } else if (!player.isMovingLeft && player.x > 0) {
                player.x -= player.velocityX;
            }
        }
        for (GoodItem item : goodItemList) {
            item.y += item.velocityY;
        }
        for (Bomb bomb : bombsList) {
            bomb.y += bomb.velocityY;
        }
    }

    private void spawnNewItem(long timeDiff) {
        lastCalledRun+=timeDiff;
        if(lastCalledRun >= 1000){
            lastCalledRun = 0;

            int decision = random.nextInt(MAX_CHANCE) <= BOMB_CHANCE ? BOMB : GOOD_ITEM;

            int itemWidth;
            switch (decisions[decision]){
                case BOMB:
                    itemWidth = SkinHolderSingleton.getHolder().getGoodItemSkin(0).getWidth();
                    bombsList.add(new Bomb(random.nextInt(screenX-itemWidth), -100));
                    break;
                case GOOD_ITEM:
                    itemWidth = SkinHolderSingleton.getHolder().getGoodItemSkin(0).getWidth();
                    goodItemList.add(new GoodItem(random.nextInt(screenX-itemWidth), -100));
                    break;
            }
        }
    }

    private Bitmap getGameBackground() {
        return SkinHolderSingleton.getHolder().getBackground();
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