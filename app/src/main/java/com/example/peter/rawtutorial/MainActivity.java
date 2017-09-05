package com.example.peter.rawtutorial;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;

/**
 * 音楽の再生、停止、一時停止を行うクラス
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Runnable, MediaPlayer.OnCompletionListener, RadioGroup.OnCheckedChangeListener {

    private MediaPlayer mediaPlayer;
    private Button mBtnPlayPause;
    private SeekBar mSeekBarPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRadioGroup();

        setClickListener();

        initSeekBar();

        Thread thread = new Thread(this);
        thread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("MediaPlayer", "onDestroy");
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

    /**
     * RadioGroup初期化
     */
    private void initRadioGroup() {
        RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.music_group);
        mRadioGroup.check(R.id.music_1);
        mediaPlayer = MediaPlayer.create(this, R.raw.hydrangea);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * クリックリスナーをセットするメソッド
     */
    private void setClickListener() {
        //PLAY・PAUSEボタン
        mBtnPlayPause = (Button) findViewById(R.id.play_pause);
        mBtnPlayPause.setOnClickListener(this);
        //STOPボタン
        Button mButtonStop = (Button) findViewById(R.id.stop);
        mButtonStop.setOnClickListener(this);
    }

    /**
     * SeekBar初期化
     */
    private void initSeekBar() {
        mSeekBarPosition = (SeekBar) findViewById(R.id.seek);
        mSeekBarPosition.setProgress(0);
        mSeekBarPosition.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //PLAY・PAUSEボタン押下時
            case R.id.play_pause:
                mediaPlayPause();
                break;
            //STOPボタン押下時
            case R.id.stop:
                mediaStop();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mediaPlayer.pause();
            mBtnPlayPause.setBackgroundResource(android.R.drawable.ic_media_play);
            mediaPlayer.seekTo(progress);
            mSeekBarPosition.setProgress(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void run() {
        try {
            while (mediaPlayer != null) {                                   //メディアプレーヤーが起動中の場合
                mediaPlayer.setOnCompletionListener(this);
                int currentPosition = mediaPlayer.getCurrentPosition();    //現在の再生位置を取得
                Message msg = new Message();
                msg.what = currentPosition;
                threadHandler.sendMessage(msg);                    //ハンドラへのメッセージ送信
            }
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private final Handler threadHandler = new Handler() {
        public void handleMessage(Message msg) {
            mSeekBarPosition.setProgress(msg.what);
        }
    };

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.v("MediaPlayer", "Stop!!!");
        // 音声終了時、ボタンの画像を再生の画像に戻す。
        mBtnPlayPause.setBackgroundResource(android.R.drawable.ic_media_play);
    }

    /**
     * プレーヤーの再生・一時停止を行うメソッド
     */
    private void mediaPlayPause() {
        if (mediaPlayer.isPlaying()) {                          //PLAY中の場合
            mediaPlayer.pause();                                //PAUSE
            mBtnPlayPause.setBackgroundResource(android.R.drawable.ic_media_play);

        } else {                                            //PAUSE中の場合
            mediaPlayer.start();                                //再生スタート
            int mTotalTime = mediaPlayer.getDuration();
            mSeekBarPosition.setMax(mTotalTime);            //SeekBarの最大値を設定
            mBtnPlayPause.setBackgroundResource(android.R.drawable.ic_media_pause);   //ボタンのテキストを[Pause]へ変更
        }
    }

    /**
     * プレーヤーを停止するメソッド
     */
    private void mediaStop() {
        mediaPlayer.stop();
        try {
            mediaPlayer.prepare();                          //プレーヤー準備
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        mediaPlayer.seekTo(0);                              //0にして先頭に戻す
        mBtnPlayPause.setBackgroundResource(android.R.drawable.ic_media_play);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        mediaStop();
        switch (checkedId) {
            case R.id.music_1:
                mediaPlayer = MediaPlayer.create(this, R.raw.hydrangea);
                break;
            case R.id.music_2:
                mediaPlayer = MediaPlayer.create(this, R.raw.canon);
                break;
        }
    }
}
