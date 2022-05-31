package edu.hrj.planewars;

import android.graphics.Bitmap;

/**
 * @描述 爆炸效果
 */
public class Blast extends Sprite {

    public Blast(Bitmap bitmap, int width, int height) {
        super(bitmap, width, height);
    }

    @Override
    public void logic() {
        if (isVisible()){
            nextFrame();//播放下一帧爆炸动画
        }
        if (getFrameIndex() == 0){
            setVisible(false);//播放完毕后隐藏
        }
    }

}
