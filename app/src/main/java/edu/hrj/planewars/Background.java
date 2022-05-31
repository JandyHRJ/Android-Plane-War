package edu.hrj.planewars;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Background {
    private Bitmap bitmap;
    private int y1;
    private int y2;

    public Background(Bitmap bitmap) {
        this.bitmap = bitmap;
        y1 = -bitmap.getHeight();
        y2 = 0;
    }

    public void logic(){
        y1 += 2;
        y2 += 2;
        if (y1 >= bitmap.getHeight()){
            y1 = -bitmap.getHeight();
        }
        if (y2 >= bitmap.getHeight()){
            y2 = -bitmap.getHeight();
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap,0,y1,null);
        canvas.drawBitmap(bitmap,0,y2,null);
    }
}
