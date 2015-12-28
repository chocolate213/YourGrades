package com.jxzhang.yourgrades.service;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.jxzhang.yourgrades.R;
import com.jxzhang.yourgrades.activity.MainActivity;
import com.jxzhang.yourgrades.receiver.AlarmReceiver;
import com.jxzhang.yourgrades.security.DES3Utils;
import com.jxzhang.yourgrades.util.HTMLParser;
import com.jxzhang.yourgrades.util.HTMLParser_2;
import com.jxzhang.yourgrades.util.HttpUtil;
import com.jxzhang.yourgrades.util.MyApplication;
import com.jxzhang.yourgrades.util.StudentInfo;

import org.json.JSONException;

import java.util.List;

/**
 * Created by J.X.Zhang on 2015/9/25.
 * @Description: 教务网后台服务模块
 */
public class LongRunningRequestService extends Service{
    @Override
    public void onCreate() {
        super.onCreate();

    }


    byte[] mPasswordByteData;
    private static final int NOTIFICATION_ID = 0x00000001;
    private static final int _5MINUTE = 5 * 60 * 1000;          //五分钟请求一次服务器
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Test","服务运行");
        //启动进程
        RequestTask requestTask = new RequestTask();
        requestTask.execute((Void) null);

        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("stu_info", Context.MODE_PRIVATE);      //获取存储成绩SharedPreference
        SharedPreferences mPreferences = MyApplication.getContext().getSharedPreferences("list_size", Context.MODE_PRIVATE);    //获取存储成绩科目数SharedPreference
        String mData = preferences.getString("stu_grade_info", "");     //取出成绩信息
        HTMLParser_2 htmlParser = new HTMLParser_2(mData);                  //解析
        List<StudentInfo> list_stu = htmlParser.getList();              //获取解析到的List集合
        SharedPreferences.Editor mEdit = mPreferences.edit();
        int num = mPreferences.getInt("list_length", 0);                //获取解析到List集合的大小
        int newNum = list_stu.size();                                   //当前List集合的大小
        if (newNum > num){                                              //如果当前科目数大于存储科目数
            int theNum = newNum - num;                                  //获取差值
            mEdit.putInt("list_length",newNum);                         //将新值存入到SharedPreference中
            mEdit.commit();                                             //提交更改
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent_1 = new Intent(MyApplication.getContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    MyApplication.getContext(), 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);
            Notification notification = new Notification.Builder(MyApplication.getContext())
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setTicker("又有"+theNum+"门成绩出来啦！！快看看~~")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("又有"+theNum+"门成绩出来啦！！快看看~~")
                    .setContentTitle("新消息提示")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
            manager.notify(NOTIFICATION_ID, notification);
            Log.d("Test", "onClick");
        }
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        long triggerAtTime = System.currentTimeMillis()+_5MINUTE;
        Intent i = new Intent(this,AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.RTC_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(MyApplication.getContext(), "自动刷新成绩服务已关闭", Toast.LENGTH_SHORT).show();
        //终止广播自动启动服务
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this,AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.cancel(pi);
        Log.d("Test","服务被终止");
    }

    class RequestTask extends AsyncTask<Void,Intent,Boolean>{
        //初始化学号密码
        private String student_id;
        private String password;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SharedPreferences mSharedPreferences = MyApplication.getContext().getSharedPreferences("service_password", MODE_PRIVATE);
            student_id = mSharedPreferences.getString("student_id","");
            String encrypt_password = mSharedPreferences.getString("password", "");
            int password_length = mSharedPreferences.getInt("password_length", 0);
            try {
                //将String密码转换为二进制数组
                byte[] decrypt_password = DES3Utils.string2byteArray(encrypt_password, password_length);
                //将二进制数组扔给解密模块解密返回二进制数组
                mPasswordByteData = DES3Utils.decryptMode(decrypt_password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //将二进制数组new成String对象获取String类型的密码
            password = new String(mPasswordByteData);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d("Test","后台执行任务运行");
            HttpUtil httpUtil = new HttpUtil(student_id,password);
            httpUtil.loginVerify();
            return null;
        }
    }

}
