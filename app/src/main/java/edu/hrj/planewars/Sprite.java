package edu.hrj.planewars;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * 通过bullet提取的sprite父类，利于代码复用
 */

class Sprite {
    private Bitmap bitmap;
    private int width;
    private int height;
    private int x;
    private int y;
    private int speedX;
    private int speedY;
    private boolean isVisible;
    //添加多帧动画
    private int frameNumber;
    private int[] frameX;
    private int[] frameY;
    private Rect src;
    private Rect dst;
    private int frameIndex;


    //修改构造方法
    public Sprite(Bitmap bitmap) {
        this(bitmap,bitmap.getWidth(),bitmap.getHeight());
    }

    //添加重写构造方法
    public Sprite(Bitmap bitmap, int width, int height) {
        this.bitmap = bitmap;
        this.width = width;
        this.height = height;
        int w = bitmap.getWidth()/width;//大图分割为小图
        int h = bitmap.getHeight()/height;
        frameNumber = w * h;//总帧数
        frameX = new int[frameNumber];//存放每一帧坐标
        frameY = new int[frameNumber];
        for (int i = 0; i < h; i++) {//行
            for (int j = 0; j < w; j++) {//列
                frameX[w*i+j] = width * j;
                frameY[w*i+j] = height * i;
            }
        }
        src = new Rect();
        dst = new Rect();
    }

    public void logic(){
        move(speedX,speedY);
        outOfBounds();
    }

    public void move(float distanceX, float distanceY) {
        x += distanceX;
        y += distanceY;
    }

    public void draw(Canvas canvas){
        if (isVisible){
            //canvas.drawBitmap(bitmap,x,y,null);
            //修改draw方法，用于显示爆炸多帧动画
            src.set(frameX[frameIndex],frameY[frameIndex],
                    frameX[frameIndex]+width,frameY[frameIndex]+height);
            dst.set(x,y,x+width,y+height);
            canvas.drawBitmap(bitmap,src,dst,null);
        }
    }

    public void outOfBounds() {
        if (getX() < 0 || getX() > 768 || getY() < 0 || getY() > 1280){
            setVisible(false);
        }
    }

    //添加碰撞检测方法（排除法）
    public boolean collisionWith(Sprite sprite){
        if (!isVisible() || !sprite.isVisible()){
            return false;
        }
        if (getX() < sprite.getX() && getX()+getWidth() < sprite.getX()) {
            return false;
        }
        if (sprite.getX() < getX() && sprite.getX()+sprite.getWidth() < getX()){
            return false;
        }
        if (getY() < sprite.getY() && getY()+getHeight() < sprite.getY()){
            return false;
        }
        if (sprite.getY() < getX() && sprite.getY()+sprite.getHeight() < getY()){
            return false;
        }
        if (getY() > sprite.getY()+getWidth()/2 || sprite.getY()-getWidth()/2 < getY()){
            return false;
        }
        return true;
    }

    //添加下一帧方法
    public void nextFrame(){
        setFrameIndex((getFrameIndex()+1)%getFrameNumber());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    //添加frameIndex、frameNumber的get/set方法
    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

}
