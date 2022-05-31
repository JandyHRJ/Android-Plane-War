package edu.hrj.planewars;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * @描述 新建MyMusic类，用于播放背景音乐等
 */
public class MyMusic {

    private Context context;
    private MediaPlayer mediaPlayer;
    private String fileName;

    public MyMusic(Context context) {
        this.context = context;
        mediaPlayer = new MediaPlayer();
    }

    public void play(String fileName){
        if (fileName.equals(this.fileName)){//重名跳过
            return;
        }
        this.fileName = fileName;
        mediaPlayer.reset();
        try {
            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(fileName);
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
            mediaPlayer.setLooping(true);//循环播放
            mediaPlayer.setVolume(0.3f,0.3f);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    public void start(){
        if (mediaPlayer != null){
            if (!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
        }
    }

}
