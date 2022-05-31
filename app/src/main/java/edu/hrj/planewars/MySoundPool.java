package edu.hrj.planewars;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;

/**
 * @描述 新建MySoundPool，用于缓存音乐池（短即时音效）
 */
public class MySoundPool {

    private Context context;
    private SoundPool soundPool;
    private final int sound_enemy_die;//爆炸
    private final int sound_enemy_bullet;//敌机子弹
    private final int sound_player_bullet;//玩家子弹
    private final int sound_player_fail;//玩家失败
    private final int sound_player_victory;//玩家胜利
    private final int sound_bullet_hit;//玩家中弹
    private final int sound_warning;//敌机出现警告


    public MySoundPool(Context context) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(7).build();
        }else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }
        sound_enemy_die = getSound("music/planeDead.ogg");
        sound_enemy_bullet = getSound("music/bullet.ogg");
        sound_player_bullet = getSound("music/bulletPlayer.ogg");
        sound_player_fail = getSound("music/missionFail.mp3");
        sound_player_victory = getSound("music/victory.ogg");
        sound_bullet_hit = getSound("music/bulletHit.mp3");
        sound_warning = getSound("music/warning.ogg");
    }

    private int getSound(String fileName) {
        int soundID = 0;
        try {
            soundID = soundPool.load(context.getAssets().openFd(fileName),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return soundID;
    }

    public void play(int soundID){
        soundPool.play(soundID,1,1,1,0,1);
    }

    public int getSound_enemy_die(){
        return sound_enemy_die;
    }

    public int getSound_enemy_bullet() {
        return sound_enemy_bullet;
    }

    public int getSound_player_bullet() {
        return sound_player_bullet;
    }

    public int getSound_player_fail() {
        return sound_player_fail;
    }

    public int getSound_player_victory() {
        return sound_player_victory;
    }

    public int getSound_bullet_hit() {
        return sound_bullet_hit;
    }

    public int getSound_warning() {
        return sound_warning;
    }
}
