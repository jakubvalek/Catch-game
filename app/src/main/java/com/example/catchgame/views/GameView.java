package com.example.catchgame.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.catchgame.models.Bomb;
import com.example.catchgame.models.GoodItem;
import com.example.catchgame.models.Player;
import com.example.catchgame.models.Score;
import com.example.catchgame.other.SkinHolderSingleton;
import com.example.catchgame.services.MusicService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private static final int MAX_FPS = 60;
    private static final int MAX_FRAME_TIME = (int) (1000.0 / MAX_FPS);
    private final double HAT_GROUND_VALUE_Y = 1.3;
    private final int FONT_SIZE = 80;
    private final int GOOD_ITEM = 0;
    private final int BOMB = 1;
    private final int MAX_CHANCE = 100;
    private final int[] DECISIONS = new int[]{GOOD_ITEM, BOMB};
    private final Context context;

    // Game area fields
    private Paint paint;
    private SurfaceHolder holder;
    private int screenX, screenY;
    private Thread gameThread;
    private volatile boolean isRunning;

    // Entity holding fields
    private Player player;
    private List<GoodItem> goodItemList;
    private List<Bomb> bombsList;

    // Gameplay decision fields
    private Random random;
    private int bombChance;
    private int spawnTimerMs;
    private Score score;
    private long lastCalledRun = 0;
    private double baseEntityVelocityY;
    private boolean lost = false;
    private GameViewListener gameViewListener;


    public GameView(Context context, int screenX, int screenY, Score score, GameViewListener listener, AttributeSet set) {
        super(context, set);
        this.context = context;
        if(listener == null){
            try {
                throw new Exception("To use this view implement GameView.GameViewListener!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        gameViewListener = listener;
        this.screenX = screenX;
        this.screenY = screenY;
        this.score = score;

        random = new Random();
        holder = getHolder();
        paint = new Paint();
        goodItemList = new ArrayList<>();
        bombsList = new ArrayList<>();

        switch (score.difficulty){
            case "Insane":
                bombChance = 70;
                spawnTimerMs = 200;
                baseEntityVelocityY = 70;
                break;
            case "Hard":
                bombChance = 50;
                spawnTimerMs = 500;
                baseEntityVelocityY = 50;
                break;
            default:
            case "Normal":
                bombChance = 30;
                spawnTimerMs = 1000;
                baseEntityVelocityY = 40;
                break;
        }

        paint.setTextSize(FONT_SIZE);
        SkinHolderSingleton.getHolder().generateSkins(getResources(), screenX, screenY);
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
            canvas.drawText("Score: " + score.score, FONT_SIZE, FONT_SIZE, paint);

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
                Intent playSound = new Intent();
                playSound.setAction(MusicService.PLAY_CATCH_SOUND);
                context.sendBroadcast(playSound);
                score.score++;
                toDeleteItems.add(item);
            }
            if(item.y > screenY)
                toDeleteItems.add(item);
        }
        for (Bomb bomb : bombsList) {
            if(bomb.getHitBox().intersect(player.getHitBox())){
                Intent playSound = new Intent();
                playSound.setAction(MusicService.PLAY_EXPLOSION_SOUND);
                context.sendBroadcast(playSound);
                lost = true;
                gameOver();
            }
            if(bomb.y > screenY)
                toDeleteBombs.add(bomb);
        }
        goodItemList.removeAll(toDeleteItems);
        bombsList.removeAll(toDeleteBombs);
    }

    private void gameOver() {
        isRunning = false;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(lost?"Game over!":"You won!");
        builder.setMessage("Your score: " + score.score);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gameViewListener.stopTheGame(score);
            }
        });
        builder.setCancelable(false);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                builder.create().show();
            }
        });
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
        if(lastCalledRun >= spawnTimerMs){
            lastCalledRun = 0;

            int decision = random.nextInt(MAX_CHANCE) <= bombChance ? BOMB : GOOD_ITEM;

            int itemWidth;
            switch (DECISIONS[decision]){
                case BOMB:
                    itemWidth = SkinHolderSingleton.getHolder().getGoodItemSkin(0).getWidth();
                    bombsList.add(new Bomb(random.nextInt(screenX-itemWidth), -100, baseEntityVelocityY));
                    break;
                case GOOD_ITEM:
                    itemWidth = SkinHolderSingleton.getHolder().getGoodItemSkin(0).getWidth();
                    goodItemList.add(new GoodItem(random.nextInt(screenX-itemWidth), -100, baseEntityVelocityY));
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
    public void finishGame() {
        lost = false;
        gameOver();
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

    public interface GameViewListener{
        void stopTheGame(Score score);
    }


}