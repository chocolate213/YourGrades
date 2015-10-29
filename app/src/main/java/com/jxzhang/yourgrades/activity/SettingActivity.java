package com.jxzhang.yourgrades.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.jxzhang.yourgrades.R;
import com.jxzhang.yourgrades.service.LongRunningRequestService;
import com.jxzhang.yourgrades.util.MyApplication;
import com.jxzhang.yourgrades.util.Utils;

import java.util.ArrayList;

/**
 * Created by J.X.Zhang on 2015/9/26.
 * 设置Activity
 */
public class SettingActivity extends Activity {
    //声明控件
    private TextView mFinishAllText;
    private TextView mSwitchAccountText;
    private TextView mUpdataLogText;
    private Switch mAutoRefreshSwitchButton;
    private Switch mPushReceiverSwitchButton;
    private TextView mAboutText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //初始化控件
        mFinishAllText = (TextView) findViewById(R.id.text_finish_all_activity);
        mSwitchAccountText = (TextView) findViewById(R.id.text_switch_account);
        mUpdataLogText = (TextView) findViewById(R.id.text_updata_log);
        mAutoRefreshSwitchButton = (Switch) findViewById(R.id.auto_refresh_switch);
        mPushReceiverSwitchButton = (Switch)findViewById(R.id.auto_receive_message);

        //初始化SharedPreference
        final SharedPreferences isSwitchCheckedPreference = MyApplication.getContext().getSharedPreferences("is_switch_checked", MODE_PRIVATE);
        final SharedPreferences.Editor isSwitchCheckedEditor = isSwitchCheckedPreference.edit();

        //初始化ServiceSharedPreference
        final SharedPreferences serviceSharedPreference = MyApplication.getContext().getSharedPreferences("service_password",MODE_PRIVATE);
        final SharedPreferences.Editor serviceSharedPreferenceEdit = serviceSharedPreference.edit();



        //获取SharedPreference中存储的is_auto_refresh_switch_checked是true还是false
        boolean isAutoRefreshSwitchChecked = isSwitchCheckedPreference.getBoolean("is_auto_refresh_switch_checked", false);
        if (isAutoRefreshSwitchChecked) {     //如果SharedPreference中存储的是true，那将按钮设置为true
            mAutoRefreshSwitchButton.setChecked(true);
        }
        //获取SharedPreference中存储的is_auto_refresh_switch_checked是true还是false
        boolean isAutoReceiveMessageChecked = isSwitchCheckedPreference.getBoolean("is_auto_receive_message", false);
        if (isAutoReceiveMessageChecked) {     //如果SharedPreference中存储的是true，那将按钮设置为true
            mPushReceiverSwitchButton.setChecked(true);
            //并直接绑定
            PushManager.startWork(MyApplication.getContext(), PushConstants.LOGIN_TYPE_API_KEY, Utils.getMetaValue(MyApplication.getContext(), "api_key"));
        }


        mAboutText = (TextView)findViewById(R.id.text_about);
        mAboutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });

        mPushReceiverSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isSwitchCheckedEditor.putBoolean("is_auto_receive_message", true);
                    isSwitchCheckedEditor.apply();
                    //启动无账号绑定服务
                    PushManager.startWork(MyApplication.getContext(),PushConstants.LOGIN_TYPE_API_KEY,Utils.getMetaValue(MyApplication.getContext(), "api_key"));

                } else {
                    isSwitchCheckedEditor.putBoolean("is_auto_receive_message", false);
                    isSwitchCheckedEditor.apply();
                    //解绑无账号绑定服务
                    PushManager.stopWork(MyApplication.getContext());
                }
            }
        });

        /**
         * 监听Switch
         */
        mAutoRefreshSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(SettingActivity.this, "自动刷新成绩服务已开启", Toast.LENGTH_LONG).show();
                    //如果按钮处于on，向SharedPreference中存入数据true
                    isSwitchCheckedEditor.putBoolean("is_auto_refresh_switch_checked", true);
                    isSwitchCheckedEditor.apply();
                    if (MainActivity.isLongRunningRequestServiceWorked()) {
                        //如果服务已经在运行，此处不执行任何操作
                    } else {
                        //如果服务没有在运行，此处启动服务
                        Intent longRunningRequestServiceIntent = new Intent(SettingActivity.this, LongRunningRequestService.class);
                        startService(longRunningRequestServiceIntent);
                    }
                } else {
                    //如果按钮处于off，向SharedPreference中存入false
                    isSwitchCheckedEditor.putBoolean("is_auto_refresh_switch_checked", false);
                    isSwitchCheckedEditor.apply();
                    if (MainActivity.isLongRunningRequestServiceWorked()) {
                        //如果服务正在运行，那么结束这个服务
                        Intent longRunningRequestServiceIntent = new Intent(SettingActivity.this, LongRunningRequestService.class);
                        stopService(longRunningRequestServiceIntent);
                    }
                }
            }

        });
        /**
         * 切换账号
         */
//        mSwitchAccountText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);        //第一步 获取AlertDialogBuilder对象
//
//                dialog.setTitle("切换账户？");
//                dialog.setMessage("将会注销当前账户并返回登录界面");
//                dialog.setCancelable(false);
//                dialog.setNegativeButton("确认", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (MainActivity.isLongRunningRequestServiceWorked()) {
//                            //如果切换账户，那么关闭当前账户获取成绩信息服务
//                            Log.d("Test", "切换账户：当前服务状态" + MainActivity.isLongRunningRequestServiceWorked());
//                            Intent longRunningRequestServiceIntent = new Intent(SettingActivity.this, LongRunningRequestService.class);
//                            stopService(longRunningRequestServiceIntent);
//                        }
//                        //将标记设置为false
//                        isSwitchCheckedEditor.putBoolean("is_auto_refresh_switch_checked",false);
//                        isSwitchCheckedEditor.commit();
//                        Intent startLoginIntent = new Intent(SettingActivity.this, LoginActivity.class);
//                        startActivity(startLoginIntent);
//                        //关闭主Activity
//                        MainActivity.mMainActivityInstance.finish();
//                        //关闭ServiceActivity
//                        finish();
//                    }
//                });
//                dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//
//                dialog.show();
//
//            }
//        });
        /**
         * 启动UpdataLogActivity
         */
        mUpdataLogText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updataLogIntent = new Intent(SettingActivity.this, UpdataLogActivity.class);
                startActivity(updataLogIntent);
            }
        });
        /**
         * 退出程序
         */
        mFinishAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);        //第一步 获取AlertDialogBuilder对象

                dialog.setTitle("确认退出？");
                dialog.setMessage("退出程序后后台自动获取教务网成绩信息功能将自动关闭");
                dialog.setCancelable(false);
                dialog.setNegativeButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //如果退出,关闭服务
                        if (MainActivity.isLongRunningRequestServiceWorked()) {
                            Log.d("Test", "确认退出：当前服务状态" + MainActivity.isLongRunningRequestServiceWorked());
                            Intent longRunningRequestServiceIntent = new Intent(SettingActivity.this, LongRunningRequestService.class);
                            stopService(longRunningRequestServiceIntent);
                        }
                        //将标记设置为false
                        isSwitchCheckedEditor.putBoolean("is_auto_refresh_switch_checked",false);
                        isSwitchCheckedEditor.commit();
                        MainActivity.mMainActivityInstance.finish();
                        finish();
                    }
                });
                dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.show();
            }
        });

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("设置");
        actionBar.setDisplayShowHomeEnabled(false);
    }

    /**
     * 监听ActionBar上的返回键
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
