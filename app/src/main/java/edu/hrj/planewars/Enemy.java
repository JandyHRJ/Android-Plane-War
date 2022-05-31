package edu.hrj.planewars;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
/**
 * 新建一个Enemy类继承Sprite类
 * */

public class Enemy extends Sprite{

    private ArrayList<Bullet> bullets;
    private int count;
    //添加爆炸效果
    private Blast blast;

    public Enemy(Bitmap bitmap) {
        super(bitmap);
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    //添加blast的get/set方法
    public Blast getBlast() {
        return blast;
    }

    public void setBlast(Blast blast) {
        this.blast = blast;
    }

    public void logic(){
        move(getSpeedX(),getSpeedY());
        outOfBounds();
        fire();
        bulletsLogic();
        //添加爆炸逻辑
        blastLogic();
    }

    //添加爆炸逻辑
    private void blastLogic() {
        if (blast != null){
            blast.logic();
        }
    }

    private void bulletsLogic() {
        if (bullets != null){
            for (Bullet billet :bullets) {
                billet.logic();
            }
        }
    }

    private void fire() {
        if (isVisible() && count ++== 20){//每20回合发射一枚子弹
            count = 0;
            if (bullets != null){
                for (Bullet bullet : bullets) {
                    if (!bullet.isVisible()){
                        //敌机子弹由上往下飞，玩家子弹由下往上飞
                        bullet.setPosition(getX()+getWidth()/2-bullet.getWidth()/2,
                                getY()+bullet.getHeight());
                        bullet.setVisible(true);
                        break;
                    }
                }
            }
        }
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
        bulletsDraw(canvas);
        //添加爆炸绘制自身
        blastDraw(canvas);
    }

    // 添加爆炸绘制自身
    private void blastDraw(Canvas canvas) {
        if (blast != null){
            blast.draw(canvas);
        }
    }

    private void bulletsDraw(Canvas canvas) {
        if (bullets != null){
            for (Bullet bullet : bullets) {
                bullet.draw(canvas);
            }
        }
    }

    public void outOfBounds() {
        if (getX() < 0 || getX() >768 || getY() >1280){
            setVisible(false);
        }
    }

    //添加是否可复用判断(排除法)
    public boolean isReuse(){
        if (isVisible()){//自身显示状态时不可复用
            return  false;
        }
        if (bullets != null){//还有子弹再显示时不可复用
            for (Bullet bullet : bullets) {
                if (bullet.isVisible()){
                    return false;
                }
            }
        }
        if (blast!=null && blast.isVisible()){//还有爆炸效果时不可复用
            return false;
        }
        return true;
    }

}
