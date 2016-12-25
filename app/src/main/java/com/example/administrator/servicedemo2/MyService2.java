package com.example.administrator.servicedemo2;
/**
 * Created by better_001 on 2016/12/21.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 作者：嘉盛 on 2016/12/21 11:13
 */

/**
 * 想和Activity進行交互 这就要使用AIDL来进行跨进程通信了（IPC）
 * AIDL（Android Interface Definition Language）是Android接口定义语言的意思，
 * 它可以用于让某个Service与多个应用程序组件之间进行跨进程通信，
 * 从而可以实现多个应用程序共享同一个Service的功能。
 */
public class MyService2 extends Service{

    public static final String TAG = "MyService";

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
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(TAG,"MyService2  onStartCommand() executed");
        Log.e("ThreadID","Service="+Thread.currentThread().getId());
        Log.d("TAG", "process id is " + Process.myPid());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy() executed");
    }


    public IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int plus(int a, int b) throws RemoteException {
            return a+b;
        }

        @Override
        public String toUpperCase(String str) throws RemoteException {
            if(str!=null){
                return str.toUpperCase();
            }
            return null;
        }
    };
}

