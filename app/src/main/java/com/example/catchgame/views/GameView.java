package com.example.catchgame.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.catchgame.R;

public class GameView extends SurfaceView implements Runnable {

    private Thread gameThread;
    private volatile boolean isRunning;
    private static final int MAX_FPS = 60;
    private static final int MAX_FRAME_TIME = (int) (1000.0 / MAX_FPS);

    private Paint paint;
    private Bitmap background;
    private SurfaceHolder holder;

    private int screenX, screenY;


    public GameView(Context context, int screenX, int screenY) {
        super(context);
        paint = new Paint();
        this.screenX = screenX;
        this.screenY = screenY;
        holder = getHolder();
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
    }

    @Override
    public void run() {
        while(isRunning)
        {
            long frameStartTime = System.currentTimeMillis();
            long timeDiff = System.currentTimeMillis() - frameStartTime;
            long sleepTime = (int)(MAX_FRAME_TIME - timeDiff);
            update();
            draw();
            Log.d("qwe", "sleep: " + sleepTime);
            if(sleepTime > 0){
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void draw() {
        if(holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();
            canvas.drawBitmap(background, 0, 0, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void update() {
    }

    public void start() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void stop() {
        isRunning = false;
    }

}