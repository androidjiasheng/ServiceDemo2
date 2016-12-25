package com.example.administrator.servicedemo2;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String UPLOAD_RESULT = "com.zhy.blogcodes.intentservice.UPLOAD_RESULT";
    private LinearLayout activitymain;
//    Service和Broadcast一样，都运行在主线程中，所以在这两个里面我们无法做一些长时间的耗时操作
//    使用了IntentService最起码有两个好处，一方面不需要自己去new Thread了；另一方面不需要考虑在什么时候关闭该Service了。

    private MyService.MyBinder myBinder;
    private IMyAidlInterface myBinder2;
    private Boolean isBind = false;
    private Boolean isBind2 = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = (MyService.MyBinder) iBinder;
            myBinder.startDownload();
        }


//        只有在service因异常而断开连接的时候，这个方法才会用到。
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("showme","onServiceDisconnected excuted");
        }
    };

    private ServiceConnection mConnection2 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder2 = IMyAidlInterface.Stub.asInterface(iBinder);
            try {
                int a = myBinder2.plus(10,15);
                String b = myBinder2.toUpperCase("abc");
                Toast.makeText(MainActivity.this,b+"顯示一下"+a,Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        //        只有在service因异常而断开连接的时候，这个方法才会用到。
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("showme","onServiceDisconnected excuted");
        }
    };

    private BroadcastReceiver uploadImgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == UPLOAD_RESULT) {
                String path = intent.getStringExtra(UploadImgService.EXTRA_IMG_PATH);
                handleResult(path);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ThreadID","Service="+Thread.currentThread().getId());
        setContentView(R.layout.activity_main);
        this.activitymain = (LinearLayout) findViewById(R.id.activity_main);
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_RESULT);
        registerReceiver(uploadImgReceiver, filter);
    }

    int i = 0;

    private void handleResult(String path) {
        TextView tv = (TextView) activitymain.findViewWithTag(path);
        tv.setText(path + "   upload success");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (uploadImgReceiver != null)
            unregisterReceiver(uploadImgReceiver);
    }


    private void GoToService(Class<?> mclass){
        Intent intent = new Intent(MainActivity.this,mclass);
        startService(intent);
    }

    private void StopService(Class<?> mclass){
        Intent intent = new Intent(MainActivity.this,mclass);
        stopService(intent);
    }
    //    按鈕點擊事件
    public void addTask(View v) {
//        模擬路徑
        String path = "/sdcard/imgs" + (++i) + ".png";
        UploadImgService.startUploadImg(this, path);

        TextView tv = new TextView(this);
        activitymain.addView(tv);
        tv.setText(path + "is uploading...");
        tv.setText(path);
        tv.setTag(path);
    }

//    一个Service必须要在既没有和任何Activity关联又处理停止状态的时候才会被销毁。
//    Service其实是运行在主线程里的

//    啟動服務
    public void startService(View v){
        GoToService(MyService.class);
    }

//    停止服務
    public void stopService(View v){
        StopService(MyService.class);
        StopService(MyService2.class);
        isBind = false;
        isBind2 = false;
    }

//    綁定服務
    public void BindService(View v){
        Intent ibind = new Intent(MainActivity.this,MyService.class);
//        bindService()方法接收三个参数，第一个参数就是刚刚构建出的Intent对象，
// 第二个参数是前面创建出的ServiceConnection的实例，
// 第三个参数是一个标志位，这里传入BIND_AUTO_CREATE表示在Activity和Service建立关联后自动创建Service，
// 这会使得MyService中的onCreate()方法得到执行，但onStartCommand()方法不会执行。

        isBind = bindService(ibind,mConnection,BIND_AUTO_CREATE);
    }

//    解綁服務
    public void unBindService(View v){
        if(isBind&&mConnection!=null){
            isBind = false;
            unbindService(mConnection);
        }
    }

//    使用遠程Service
    public void long_distanceService(View v){
        GoToService(MyService2.class);
    }

//    使用遠程Service與Activity
    public void long_distanceService_DATA(View v){
        Intent ibind = new Intent(MainActivity.this,MyService2.class);
        ibind.putExtra("test","传递测试数据");
        isBind2 = bindService(ibind,mConnection2,BIND_AUTO_CREATE);
    }

    public void long_distanceService_UNBIND(View v){
        if(isBind2&&mConnection2!=null){
            isBind2 = false;
            unbindService(mConnection2);
        }
    }
}
