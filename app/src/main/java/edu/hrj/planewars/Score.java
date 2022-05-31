package edu.hrj.planewars;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * @描述 玩家得分
 */
public class Score extends Sprite {

    private int score;


    public Score(Bitmap bitmap, int width, int height) {
        super(bitmap, width, height);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void logic() {
        super.logic();
        setVisible(true);
        setFrameIndex(getScore());
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
