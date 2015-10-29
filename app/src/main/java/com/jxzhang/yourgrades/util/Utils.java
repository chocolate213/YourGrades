package com.jxzhang.yourgrades.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Utils {
    public static final String TAG = "PushDemoActivity";
    public static final String RESPONSE_METHOD = "method";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";

    public static String logStringCache = "";

    // 获取ApiKey

    /**
     *
     *  百度自己写的
     *      装逼指数：★★★★★
     *      实用指数：★☆☆☆☆
     *
     * @param context   上下文
     * @param metaKey   android:name=属性所定义的值，也就是key
     * @return
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {           //如果啥也没有就返回Null
            return null;
        }
        try {
            //获取应用信息            用context获取包名 -->   获取ApplicationInfo    (获取包名，获取META_DATA的包管理器)
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            if (null != ai) {           //如果应用信息不是空
                metaData = ai.metaData; //那么获取包含metaData标签信息的Bundle对象那个
            }
            if (null != metaData) {     //如果这个对象不为空
                apiKey = metaData.getString(metaKey);   //通过Key获取值
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;                  //返回这个值
    }

    /**
     * 鄙人自己写的
     *      装逼指数：★★☆☆☆
     *      实用指数：★★★★★
     * @return
     */
    public static String getApiKey(){
        return "TAGWpIy073QWwoaIQGGN7YEG";
    }

    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static String getLogText(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("log_text", text);
        editor.commit();
    }

}
