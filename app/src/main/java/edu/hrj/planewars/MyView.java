package edu.hrj.planewars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class MyView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private final SurfaceHolder    holder;
    private final GestureDetector  gestureDetector;
    private final float            scaleX;
    private final float            scaleY;
    private       boolean          isRun;
    private       Paint            paint;
    private       boolean          gameOver;
    private       Background       background;
    private       Player           player;
    // 添加敌机代码
    private       int              step;
    private       ArrayList<Enemy> enemies;
    private       Random           random;

    // 添加敌机出现频率
    private int LEVELS;

    //添加玩家击落敌机分数和并升级，血量
    private Bitmap    number;
    private Score     score1;
    private Score     score2;
    private Score     score3;
    private int       score;
    private GameState gameState;

    // 修改背景、敌机、玩家及子弹为全局变量
    private Bitmap bitmap_background;
    private Bitmap bitmap_player;
    private Bitmap bitmap_enemy;
    private Bitmap bitmap_playerBullet;
    private Bitmap bitmap_enemyBullet;
    // 添加积分、生命背景
    private Bitmap bitmap_score;
    private Bitmap bitmap_HP;

    // 添加飞机爆炸音效及背景音乐
    private MySoundPool soundPool;
    private MyMusic bgMusic;

    // 添加菜单区域点击监听矩形
    private RectF rectF;

    // 添加boss关卡
    private BOSS boss;

    private String TAG = "MyView";

    public MyView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Point screenSize = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(
                   Context.WINDOW_SERVICE);
        if (windowManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                //包含虚拟按键
                windowManager.getDefaultDisplay().getRealSize(screenSize);
            } else {
                windowManager.getDefaultDisplay().getSize(screenSize);
            }
        }
        //计算屏幕缩放比例
        scaleX = (float) screenSize.x / 768;
        scaleY = (float) screenSize.y / 1280;

        bitmap_background = getBitmap("background/bg0.png");
        bitmap_player = getBitmap("plane/player/player1.png");
        bitmap_enemy = getBitmap("plane/enemy/enemy1.png");
        bitmap_playerBullet = getBitmap("bullet/player/player_bullet1.png");
        bitmap_enemyBullet = getBitmap("bullet/enemy/enemy_bullet1.png");

        // 添加boss
        boss = new BOSS(getBitmap("plane/enemy/boss1.png"));
        boss.setHP(2000);
        Blast blast = new Blast(getBitmap("blast_boss.png"), 80, 80);
        boss.setBlast(blast);

        // 添加爆炸音效和背景音乐
        soundPool = new MySoundPool(context);
        bgMusic = new MyMusic(context);
        init();
        gameState = GameState.LOGO;
        gestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        if (gameOver) {//双击从新开始游戏
                            mode1();
                        }
                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        player.move(-distanceX*scaleX, -distanceY*scaleY);
                        return super.onScroll(e1, e2, distanceX*scaleX, distanceY*scaleY);
                    }

                    // 添加全局单击事件监听
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        singleTap(e);
                        return super.onSingleTapConfirmed(e);
                    }
                });
    }

    //添加单击事件监听，完成跳转等功能
    private void singleTap(MotionEvent e) {
        if (!gameOver)
        switch (gameState){
            case LOGO:
                gameState = GameState.CG;
                step = 100;
                break;
            case CG:
                gameState = GameState.MENU;
                step = 300;
                break;
            case MENU://预留菜单
                rectF.set(240*scaleX,510*scaleY,550*scaleX,610*scaleY);
                if (rectF.contains(e.getX(),e.getY())){
                    mode1();
                }
                break;
            case LEVEL1:
                break;
            case LEVEL2:
                break;
            case BOSS:
                break;
            case FAIL:
                gameState = GameState.MENU;
                break;
            case VICTOR:
                gameState = GameState.MENU;
                break;
            case CONTINUE:
                gameState = GameState.MENU;
                break;
        }
    }

    public void mode1(){
        gameState = GameState.LEVEL1;
        bgMusic.start();
        score = 0;
        step = 300;
        player.setHP(100);
        bitmap_background = getBitmap("background/bg0.png");
        bitmap_player = getBitmap("plane/player/player1.png");
        bitmap_enemy = getBitmap("plane/enemy/enemy1.png");
        bitmap_playerBullet = getBitmap("bullet/player/player_bullet1.png");
        bitmap_enemyBullet = getBitmap("bullet/enemy/enemy_bullet1.png");
        init();
    }

    public void mode2(){
        gameState = GameState.LEVEL2;
        score = 0;
        step = 1000;
        player.setHP(200);
        bitmap_background = getBitmap("background/bg1.jpg");
        bitmap_player = getBitmap("plane/player/player2.png");
        bitmap_playerBullet = getBitmap("bullet/player/l2.png");
        bitmap_enemy = getBitmap("plane/enemy/enemy2.png");
        bitmap_enemyBullet = getBitmap("bullet/enemy/enemy_bullet2.png");
        init();
    }

    public void modeBoss(){
        gameState = GameState.BOSS;
        bgMusic.start();
        score = 0;
        step = 8000;
        player.setHP(300);
        bitmap_background = getBitmap("background/bgBOSS.jpg");
        bitmap_player = getBitmap("plane/player/player3.png");
        bitmap_playerBullet = getBitmap("bullet/player/l3.png");
        bitmap_enemy = getBitmap("plane/enemy/enemy2.png");
        bitmap_enemyBullet = getBitmap("bullet/enemy/enemy_bullet3.png");
        init();
    }

    private void init() {
        gameOver = false;
        background = new Background(bitmap_background);
        player = new Player(bitmap_player);
        player.setPosition((768 - player.getWidth()) / 2, 1280 - player.getHeight());
        player.setVisible(true);
        ArrayList<Bullet> bullets_player = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Bullet bullet = new Bullet(bitmap_playerBullet);
            //控制子弹发射速度
            bullet.setSpeedY(-30);
            bullets_player.add(bullet);
        }
        player.setBullets(bullets_player);

        ArrayList<Bullet> bullets_boss = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Bullet bullet = new Bullet(getBitmap("bullet/enemy/boss_bullet1.png"));
            //控制子弹发射速度
            bullet.setSpeedY(30);
            bullets_boss.add(bullet);
        }
        boss.setBullets(bullets_boss);

        player.setBitmap_HP(getBitmap("HP.png"));

        enemies = new ArrayList<>();
        step = 0;
        random = new Random();

        //添加积分背景板
        bitmap_score = getBitmap("scorebg.png");
        bitmap_HP = getBitmap("HPbg.png");

        // 实例化监听矩形区域
        rectF = new RectF();

        //添加初始化玩家等级和分数
        number = getBitmap("bmfontScore.png");
        score = 0;
        score1 = new Score(number,30,45);
        score1.setScore(0);
        score1.setVisible(true);
        score1.setPosition(100,100);
        score1.logic();

        score2 = new Score(number,30,45);
        score2.setScore(0);
        score2.setVisible(true);
        score2.setPosition(130,100);
        score2.logic();

        score3 = new Score(number,30,45);
        score3.setScore(0);
        score3.setVisible(true);
        score3.setPosition(160,100);
        score3.logic();

    }

    private Bitmap getBitmap(String path) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContext().getAssets().open(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isRun = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isRun = false;
    }

    @Override
    public void run() {
        while (isRun) {
            long startTime = System.currentTimeMillis();
            logic();
            redraw();
            long endTime = System.currentTimeMillis();
            long diffTime = endTime - startTime;
            if (diffTime < 1000 / 60) {
                try {
                    Thread.sleep(1000 / 60 - diffTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void logic() {
        if (!gameOver) {
            switch (gameState){
                case LOGO:
                    step += 5;
                    if (step == 100){
                        gameState = GameState.CG;
                    }
                    break;
                case CG:
                    step += 5;
                    if (step == 300){
                        gameState = GameState.MENU;
                    }
                    break;
                case MENU:
                    bitmap_background = getBitmap("background/menu.png");
                    bgMusic.play("music/bgm_menu.ogg");
                    break;
                case LEVEL1:
                    bgMusic.play("music/bgm_Lv1.mp3");
                    step += 5;
                    background.logic();
                    player.logic();
                    //添加敌机逻辑
                    enemyLogic();
                    //添加碰撞检测
                    collision();
                    if (score == 300){
                        bgMusic.stop();
                        soundPool.play(soundPool.getSound_player_victory());
                        mode2();
                    }
                    break;
                case LEVEL2:
                    bgMusic.play("music/bgm_Lv2.ogg");
                    step += 5;
                    background.logic();
                    player.logic();
                    //添加敌机逻辑
                    enemyLogic();
                    //添加碰撞检测
                    collision();
                    if (score == 500){
                        bgMusic.stop();
                        soundPool.play(soundPool.getSound_player_victory());
                        modeBoss();
                    }
                    break;
                case BOSS:
                    bgMusic.play("music/bgm_boss.ogg");
                    step += 5;
                    background.logic();
                    player.logic();
                    //添加敌机逻辑
                    enemyLogic();
                    //添加碰撞检测
                    collision();
                    if (score == 800){
                        gameState = GameState.MENU;
                    }
                    break;
                case FAIL:
                    break;
                case VICTOR:
                    break;
            }
        }
    }

    //添加碰撞检测
    private void collision() {
        if (enemies != null) {
            for (Enemy enemy : enemies) {
                if (enemy.collisionWith(player)) {//敌机碰撞玩家,
                    // 添加敌机碰撞一次扣20
                    if (player.getHP()>20){
                        player.setHP(player.getHP() - 20);
                        soundPool.play(soundPool.getSound_enemy_die());
                        enemy.setVisible(false);
                        return;
                    }else{
                        //添加玩家死亡爆炸音效
                        bgMusic.stop();
                        soundPool.play(soundPool.getSound_player_fail());
                        enemy.setVisible(false);
                        player.setVisible(false);
                        gameOver = true;
                        gameState = GameState.FAIL;
                        return;
                    }
                }
                if (enemy.getBullets() != null) {
                    //敌机子弹碰到玩家
                    for (Bullet bullet : enemy.getBullets()) {
                        if (bullet.collisionWith(player)) {
                            soundPool.play(soundPool.getSound_player_bullet());
                            if (player.getHP() > 10){
                                player.setHP(player.getHP() - 10);
                                soundPool.play(soundPool.getSound_bullet_hit());
                                bullet.setVisible(false);
                                return;
                            }else {
                                bgMusic.stop();
                                soundPool.play(soundPool.getSound_player_fail());
                                bullet.setVisible(false);
                                player.setVisible(false);
                                gameOver = true;
                                gameState = GameState.FAIL;
                                return;
                            }
                        }
                    }
                }
                //boss子弹碰到玩家
                if (boss.getBullets() != null){
                    for (Bullet bullet : boss.getBullets()) {
                        if (bullet.collisionWith(player)){
                            soundPool.play(soundPool.getSound_player_bullet());
                            if (player.getHP() > 10){
                                player.setHP(player.getHP() - 10);
                                soundPool.play(soundPool.getSound_bullet_hit());
                                bullet.setVisible(false);
                                return;
                            }else {
                                bgMusic.stop();
                                soundPool.play(soundPool.getSound_player_fail());
                                bullet.setVisible(false);
                                player.setVisible(false);
                                gameOver = true;
                                gameState = GameState.FAIL;
                                return;
                            }
                        }
                    }
                }
                //玩家子弹碰到敌机
                if (player.getBullets() != null) {
                    for (Bullet bullet : player.getBullets()) {
                        if (bullet.collisionWith(enemy) && player.getHP()>0) {
                            bullet.setVisible(false);
                            enemy.setVisible(false);
                            Blast blast = enemy.getBlast();
                            blast.setPosition(enemy.getX(), enemy.getY());

                            //添加爆炸音效
                            soundPool.play(soundPool.getSound_enemy_die());

                            blast.setVisible(true);//爆炸可见
                            score += 10;//积分+10
                            showScore(score);
                        }
                    }
                }

                //玩家子弹碰到boss
                if (player.getBullets() != null) {
                    for (Bullet bullet : player.getBullets()) {
                        if (bullet.collisionWith(boss)&&player.getHP()>0) {
                            bullet.setVisible(false);
                            score += 10;//积分+10
                            boss.setHP(boss.getHP()-100);
                            soundPool.play(soundPool.getSound_bullet_hit());
                            showScore(score);
                            if (boss.getHP() == 0){
                                boss.setVisible(false);
                                Blast blast = boss.getBlast();
                                blast.setPosition(380, 320);
                                // 添加爆炸音效
                                soundPool.play(soundPool.getSound_enemy_die());
                                boss.setVisible(false);
                                blast.setVisible(true);//爆炸可见
                                soundPool.play(soundPool.getSound_player_victory());
                                gameState =GameState.VICTOR;
                            }
                        }
                    }
                }
            }
        }
    }

    public void showScore(int score){
        score1.setPosition(100,100);
        score1.setScore((score/100)%10);//百位
        score2.setPosition(130,100);
        score2.setScore((score/10)%10);//十位
        score3.setPosition(160,100);
        score3.setScore(score%10);//个位
        score1.logic();
        score2.logic();
        score3.logic();
    }

    //添加敌机逻辑
    private void enemyLogic() {
        step++;//敌机出现延迟计时
        boss.logic();
        if (enemies != null) {
            for (Enemy enemy : enemies) {
                enemy.logic();
            }
        }
        if (step == 1200) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 2*i+1; j++) {//飞机按1、3、5、7、9排列
                    Enemy enemy = new Enemy(bitmap_enemy);
                    //添加警告音效
                    soundPool.play(soundPool.getSound_warning());

                    enemy.setPosition((768-enemy.getWidth())/2 + 80*(i-j),
                            -enemy.getHeight() - 80*i);
                    enemy.setSpeedY(5);//斜着飞
                    enemy.setVisible(true);
                    //添加敌机爆炸效果
                    Blast blast = new Blast(getBitmap("blast1.png"), 56, 56);
                    enemy.setBlast(blast);
                    enemies.add(enemy);
                }
            }
        }
        if (gameState==GameState.BOSS && step==2004){
            boss.setPosition((768-boss.getWidth())/2,-boss.getHeight());
            boss.setVisible(true);
            boss.setSpeedY(8);
            ArrayList<Bullet> bullets = new ArrayList<>();//敌机弹夹
            for (int i = 0; i < 5; i++) {
                Bullet bullet = new Bullet(getBitmap("bullet/enemy/boss_bullet1.png"));
                soundPool.play(soundPool.getSound_enemy_bullet());
                bullet.setSpeedY(5);
                bullets.add(bullet);
            }
            boss.setBullets(bullets);
        }
        if (step==2340){
            boss.setSpeedY(0);
            boss.setSpeedX(0);
        }
        if (gameState == GameState.LEVEL1){
            LEVELS = 250;
        }else if (gameState == GameState.LEVEL2){
            LEVELS = 150;
        }else if (gameState == GameState.BOSS){
            LEVELS = 100;
        }
        if (step>1500 && step<50000 && step%LEVELS==0) {//控制敌机出现频率及时间长短
            if (enemies != null) {
                for (Enemy enemy : enemies) {
                    //if (!enemy.isVisible()){//循环使用一隐藏的敌机
                    // 修改隐藏敌机判定方法
                    if (enemy.isReuse()) {
                        enemy.setPosition(random.nextInt(768 - enemy.getWidth()),
                                -enemy.getHeight());
                        enemy.setVisible(true);
                        //控制敌机移动速度
                        enemy.setSpeedY(5 + random.nextInt(5));//5~9的随机速度
                        if (enemy.getX() < (768-enemy.getWidth()) / 2) {
                            enemy.setSpeedX(random.nextInt(3));
                        } else {
                            enemy.setSpeedX(-random.nextInt(3));
                        }
                        ArrayList<Bullet> bullets = new ArrayList<>();//敌机弹夹
                        for (int i = 0; i < 5; i++) {
                            Bullet bullet = new Bullet(bitmap_enemyBullet);
                            soundPool.play(soundPool.getSound_enemy_bullet());
                            bullet.setSpeedY(5);
                            bullets.add(bullet);
                        }
                        enemy.setBullets(bullets);
                        break;
                    }
                }
            }
        }

    }

    private void redraw() {
        Canvas lockCanvas = holder.lockCanvas();
        try {
            synchronized (holder) {
                myDraw(lockCanvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (lockCanvas != null) {
                holder.unlockCanvasAndPost(lockCanvas);
            }
        }
    }

    private void myDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(scaleX, scaleY);
        paint.reset();
        //添加场景切换
        switch (gameState){
            case LOGO:
                canvas.drawBitmap(getBitmap("logo.jpg"),0,0,paint);
                break;
            case CG:
                canvas.drawBitmap(getBitmap("cg.png"),0,0,paint);
                break;
            case MENU:
                canvas.drawBitmap(getBitmap("background/menu.png"),0,0,paint);
                break;
            case LEVEL1:
                background.draw(canvas);
                player.draw(canvas);
                drawOther(canvas);//绘制记分板、血量等
                break;
            case LEVEL2:
                background.draw(canvas);
                player.draw(canvas);
                drawOther(canvas);//绘制记分板、血量等
                break;
            case BOSS:
                background.draw(canvas);
                player.draw(canvas);
                boss.draw(canvas);
                drawOther(canvas);//绘制记分板、血量等
                break;
            case VICTOR:
                canvas.drawBitmap(getBitmap("victor.png"),0,0,paint);
                break;
            case FAIL:
                canvas.drawBitmap(getBitmap("fail.png"),0,0,paint);
                break;
            case CONTINUE:
                canvas.drawBitmap(getBitmap("toBeContinue.png"),240,1080,paint);
                break;
        }
        canvas.restore();
    }

    public void drawOther(Canvas canvas){
        if (gameOver) {
            bgMusic.stop();
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(36);
            paint.setColor(Color.BLACK);
            canvas.drawText("游戏结束，双击屏幕重新开始", 384, 640, paint);
        }

        //添加敌机绘自身
        if (enemies != null) {
            for (Enemy enemy : enemies) {
                enemy.draw(canvas);
            }
        }

        // 添加积分版和HP背景
        canvas.drawBitmap(bitmap_score,30,110,null);
        canvas.drawBitmap(bitmap_HP,30,50,null);

        for (int i = 0; i < player.getHP() / 10; i++) {
            if (player.getHP() != 0){
                canvas.drawBitmap(getBitmap("HP.png"),30+i*30,50,paint);
            }
        }

        //绘制积分
        score1.setPosition(100,100);
        score1.draw(canvas);
        score2.setPosition(136,100);
        score2.draw(canvas);
        score3.setPosition(172,100);
        score3.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        //重写onTouchEvent后，系统默认的performClick给屏蔽了，手动调用
        performClick();
        return true;
    }

    @Override
    public boolean performClick() {
        //完成点击，performClick()的作用是调用你在setOnClickListener时设置的onClick方法
        return super.performClick();
    }
}