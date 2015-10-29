package com.jxzhang.yourgrades.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by J.X.Zhang on 2015/9/22.
 * 全局获取Context类
 */
public class MyApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }


    public static Context getContext(){
        return context;
    }
}
