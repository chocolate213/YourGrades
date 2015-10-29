package com.jxzhang.yourgrades.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jxzhang.yourgrades.service.LongRunningRequestService;

/**
 * Created by J.X.Zhang on 2015/9/25.
 * 后台服务运行支持Receiver
 */
public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, LongRunningRequestService.class);
        context.startService(i);
        Log.d("Test", "广播接收器运行");
    }
}
