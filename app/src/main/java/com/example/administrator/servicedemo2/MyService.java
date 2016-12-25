package com.example.administrator.servicedemo2;
/**
 * Created by better_001 on 2016/12/21.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * 作者：嘉盛 on 2016/12/21 11:13
 */

/**
 * onCreate()方法只会在Service第一次被创建的时候调用，如果当前Service已经被创建过了，
 * 不管怎样调用startService()方法，onCreate()方法都不会再执行。
 * 因此你可以再多点击几次Start Service按钮试一次，
 * 每次都只会有onStartCommand()方法中的打印日志。
 */
public class MyService extends Service{

    public static final String TAG = "MyService";

    private MyBinder mBinder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG,"onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("ThreadID","Service="+Thread.currentThread().getId());
        Log.d("TAG", "process id is " + Process.myPid());
        Log.e(TAG,"onCreate() executed");
        Notification.Builder notification = new Notification.Builder(this);
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        notification.setContentTitle("有通知到來");
        notification.setContentText("這裡會顯示在哪裡");
        notification.setWhen(System.currentTimeMillis());
        Notification notification1 = notification.build();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        startForeground(1, notification1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy() executed");
    }

    class MyBinder extends Binder{
        public void startDownload(){
            Log.e(TAG,"startDownload() executed");
            new MyThread().start();
            Toast.makeText(getApplicationContext(),"顯示一下",Toast.LENGTH_SHORT).show();
        }
    }


    class MyThread extends Thread {
        private int i = 0;
        @Override
        public void run() {
            super.run();
            while (true){
                try {
                    i++;
                    sleep(1000);
                    Log.e(TAG,"Downloading executed"+i);
                    if(i==10){
                        Log.e(TAG,"Downloading break"+i);
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

