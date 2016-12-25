package com.example.administrator.servicedemo2;/**
 * Created by better_001 on 2016/12/21.
 */

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 作者：嘉盛 on 2016/12/21 09:44
 */
public class UploadImgService extends IntentService {

    private static final String ACTION_UPLOAD_IMG = "com.zhy.blogcodes.intentservice.action.UPLOAD_IMAGE";
    public static final String EXTRA_IMG_PATH = "com.zhy.blogcodes.intentservice.extra.IMG_PATH";
    public static final String NAME = "UploadImgService";

    public static void startUploadImg(Context mContext,String path){
        Log.e("TAG","startUploadImg");
        Intent intent = new Intent(mContext,UploadImgService.class);
        intent.setAction(ACTION_UPLOAD_IMG);
        intent.putExtra(EXTRA_IMG_PATH, path);
        mContext.startService(intent);
    }



    public UploadImgService() {
        super("UploadImgService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("TAG","startUploadImg");
        if(intent !=null){
            String action = intent.getAction();
            if(ACTION_UPLOAD_IMG.equals(action)){
                String path = intent.getStringExtra(EXTRA_IMG_PATH);
                handleUploadImg(path);
            }
        }
    }

    private void handleUploadImg(String path){
        Log.e("TAG","handleUploadImg");
        try {
//            模擬耗時上傳
            Thread.sleep(3000);
            Intent intent = new Intent(MainActivity.UPLOAD_RESULT);
            intent.putExtra(EXTRA_IMG_PATH,path);
            sendBroadcast(intent);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG","onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG","onDestroy");
    }
}
