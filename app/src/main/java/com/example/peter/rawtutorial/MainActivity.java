package com.example.peter.rawtutorial;

import android.app.ActivityManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.sample);

        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        // システムの使用可能メモリ合計
        Log.d("ActivityManager","memoryInfo availMem  :"+memoryInfo.availMem);
        // 現在のlowMemoryフラグ true:メモリ不足状態
        Log.d("ActivityManager","memoryInfo lowMemory :"+memoryInfo.lowMemory);
        // 閾値
        // 下回った際に、バックグラウンドサービスや、無関係なプロセスからkill
        Log.d("ActivityManager","memoryInfo threshold :"+memoryInfo.threshold);

        //エラー情報の取得
        List<ActivityManager.ProcessErrorStateInfo> errorStateInfo = activityManager.getProcessesInErrorState();

        if(errorStateInfo != null){
            for (ActivityManager.ProcessErrorStateInfo error : errorStateInfo){

                // エラー情報
                // エラー状態
                Log.d("ActivityManager","error.condition  :"+error.condition); //   CRASHED,NOT_RESPONDING,NO_ERROR
                // エラーの説明文詳細
                Log.d("ActivityManager","error.longMsg    :"+error.longMsg);
                // エラーの説明文概要
                Log.d("ActivityManager","error.shortMsg   :"+error.shortMsg);
                // 親プロセスID, ない場合0
                Log.d("ActivityManager","error.pid        :"+error.pid);
                // エラー、クラッシュしたプロセス名
                Log.d("ActivityManager","error.processName:"+error.processName);
                // エラー発生時のスタックトレース
                Log.d("ActivityManager","error.stackTrace :"+error.stackTrace);
                // エラー情報のタグ
                Log.d("ActivityManager","error.tag        :"+error.tag);
                // このプロセスに割り当てられているカーネルUserId
                Log.d("ActivityManager","error.uid        :"+error.uid);
            }
        }


        //起動中のタスク情報
        // <uses-permission android:name="android.permission.GET_TASKS" /> パーミッションが必要
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(5);//直近5つを取得

        if(taskInfo != null){
            for (ActivityManager.RunningTaskInfo task : taskInfo){

                //エラー情報
                // ユニークなtaskId
                Log.d("ActivityManager","task.id           :"+task.id);
                // 現在の状態の概要
                Log.d("ActivityManager","task.description  :"+task.description);
                // タスクのactivity数
                Log.d("ActivityManager","task.numActivities:"+task.numActivities);
                // running状態のActivity数
                Log.d("ActivityManager","task.numRunning   :"+task.numRunning);
            }
        }}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                mediaPlayer.start();
                break;
            case R.id.pause:
                mediaPlayer.pause();
                break;
            case R.id.stop:
                mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(view.getContext(),R.raw.sample);
                break;
        }
    }
}
