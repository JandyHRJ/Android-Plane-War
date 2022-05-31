package edu.hrj.planewars;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;



/**
修改，使Player继承于Sprite类
 * */
public class Player extends Sprite{

    private ArrayList<Bullet> bullets;
    private int count;

    //添加玩家血量及分数
    private int HP;
    private int score;

    //添加玩家血量显示
    private Bitmap bitmap_HP;

    public Player(Bitmap bitmap) {
        super(bitmap);
        HP = 100;
    }

    public void logic(){
        outOfBounds();
        fire();
        bulletsLogic();
    }

    private void bulletsLogic() {
        if (bullets != null){
            for (Bullet billet :bullets) {
                billet.logic();
            }
        }
    }

    private void fire() {
        if (count ++== 5){
            count = 0;
            if (bullets != null){
                for (Bullet bullet : bullets) {
                    if (!bullet.isVisible()){
                        bullet.setPosition(getX()+getWidth()/2-bullet.getWidth()/2,
                                getY()-bullet.getHeight());
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
    }

    private void bulletsDraw(Canvas canvas) {
        if (bullets != null){
            for (Bullet bullet : bullets) {
                bullet.draw(canvas);
            }
        }
    }

    public void outOfBounds() {
        if (getX() < 0){
            setX(0);
        }
        if (getX() > 768-getWidth()){
            setX(768-getWidth());
        }
        if (getY() < 0){
            setY(0);
        }
        if (getY() > 1280-getHeight()){
            setY(1280-getHeight());
        }
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Bitmap getBitmap_HP() {
        return bitmap_HP;
    }

    public void setBitmap_HP(Bitmap bitmap_HP) {
        this.bitmap_HP = bitmap_HP;
    }
}
