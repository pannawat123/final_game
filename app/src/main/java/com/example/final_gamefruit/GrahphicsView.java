package com.example.final_gamefruit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Random;

public class GrahphicsView extends View implements View.OnTouchListener {

    Paint p1;
    Bitmap bitmap,image[];
    float Width,Height,picY[],picX[];
    int StatusScreen = 0;
    int score,time,img[];
    CountDownTimer timer1,timer2;
    int res_image[] =
            {R.drawable.apple,R.drawable.apricot,
            R.drawable.banana, R.drawable.cherry,
            R.drawable.mango,R.drawable.watermalon,
            R.drawable.pear,R.drawable.strawberry};
    Random rnd = new Random();
    MediaPlayer player,player2;
    public GrahphicsView(Context context) {
        super(context);
        setOnTouchListener(this);
        p1 = new Paint();
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruits);
        score = 0;
        time = 0;
        image = new Bitmap[8];
        picY = new float[8];
        picX = new float[8];
        img = new int[8];
        for (int n=0;n<res_image.length;n++){
            image[n] = BitmapFactory.decodeResource(getResources(),res_image[n]);
            newSpawn(n);
        }
        player = MediaPlayer.create(context,R.raw.sound_click);
        player2 = MediaPlayer.create(context, R.raw.sound_game);

        timer1 = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time++;
                invalidate();
            }

            @Override
            public void onFinish() {
                StatusScreen = 2;
                timer2.cancel();
                invalidate();
            }
        };

        timer2 = new CountDownTimer(30000,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                for (int n = 0; n < picY.length; n++) {
                    picY[n] += 20;
                    if (picY[n] > Height + image[0].getHeight()) {
                        picY[n] = 0;
                        newSpawn(n);
                    }
                }
                invalidate();
            }

            @Override
            public void onFinish() {
                StatusScreen = 2;
                invalidate();
            }
        };

    }

    private void newSpawn(int n) {
        int imgg = rnd.nextInt(8);
        img[n] = imgg;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Width = getWidth();
        Height = getHeight();
        setBackgroundColor(Color.rgb(241,163,161));
        p1.setColor(Color.BLUE);
        p1.setTextSize(60);
        p1.setTextAlign(Paint.Align.CENTER);

        if(StatusScreen == 0) {
            canvas.drawText("Welcome to Fruit Game", Width / 2, 300, p1);
            canvas.drawText("6406021620106 เคนจัง", Width / 2, 450, p1);
            canvas.drawBitmap(bitmap,250,600,null);
            canvas.drawText("Are you ready ?", Width / 2, 1300, p1);
            canvas.drawText("Touch for Play Game", Width / 2, 1450, p1);
        } else if(StatusScreen == 1) {
            canvas.drawText("Score : " + score, 130, 60, p1);
            p1.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("Time : " + time, Width-50, 60, p1);
            for (int n=0;n<picY.length;n++){
                picX[n] = ((image[0].getWidth()+10)*n);
                canvas.drawBitmap(image[img[n]],picX[n],picY[n],null);
            }
        } else if(StatusScreen == 2){
            p1.setTextSize(65);
            p1.setColor(Color.rgb(215,71,156));
            canvas.drawText("E N D  G A M E.", Width / 2, 400, p1);
            p1.setTextSize(55);
            canvas.drawText("Your Score : "+score, Width / 2, 700, p1);
            canvas.drawText("Touch for Play Game", Width / 2, 900, p1);
        }
}

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(StatusScreen == 0){
            StatusScreen = 1;
            timer1.start();
            timer2.start();
            time = 0;
            score = 0;
            for (int n=0;n<picY.length;n++)
                picY[n] =0;
            // เริ่มเล่นเพลงเมื่อเกมเริ่ม
            player2.start();
        }else if(StatusScreen == 2){
            StatusScreen = 1;
            timer1.start();
            timer2.start();
            time = 0;
            score = 0;
            for (int n=0;n<picY.length;n++)
                picY[n] =0;
        }else if(StatusScreen == 1) {
            float x = event.getX();
            float y = event.getY();
            if (HitHit(x, y)) {
                score++;
                player.start();
                invalidate();
            }
        }
        invalidate();
        return true;
    }

    private boolean HitHit(float x, float y) {
        for (int n =0;n<picY.length;n++){
            if(x > picX[n] && x < picX[n]+image[0].getWidth()){
                if(y > picY[n] && y < picY[n]+image[0].getHeight()) {
                    HitSpawn(n);
                    return true;
                }
            }
        }
        return false;
    }

    private void HitSpawn(int n) {
        picY[n] = 0;
        newSpawn(n);
    }
}

