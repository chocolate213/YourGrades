package com.jxzhang.yourgrades.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.jxzhang.yourgrades.activity.PushMessageReceiverActivity;
import com.jxzhang.yourgrades.util.MyApplication;

import java.util.List;

/**
 * Created by J.X.Zhang on 2015/10/29.
 */
public class MyPushManagerReceiver extends PushMessageReceiver {
    //获取本类类名
    public static final String TAG = MyPushManagerReceiver.class.getSimpleName();

    /**
     * PushManager.startWork()的回调函数
     *
     * @param context       BroadcastReceiver的执行Context
     * @param errorCode     绑定接口返回值，0 - 成功
     * @param appid         应用id。errorCode非0时为null
     * @param userId        应用user id。errorCode非0时为null
     * @param channelId     应用channel id。errorCode非0时为null  根据端上设备属性生成，具备唯一性
     * @param requestId     向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(Context context, int errorCode, String appid, String userId, String channelId, String requestId) {
        Log.d(TAG, "METHOD=onBind" + "\nErrorCode=" + errorCode + "\nAppid=" + appid + "\nuserId=" + userId + "\nChannelId=" + channelId + "\nrequestId=" + requestId);
        if (errorCode == 0){
            Toast.makeText(MyApplication.getContext(), "绑定成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MyApplication.getContext(), "绑定失败，请把这段错误代码告诉我："+errorCode+"非常感谢", Toast.LENGTH_LONG).show();
        }

    }
    /**
     * PushManager.stopWork()的回调函数。
     *
     * @param context       上下文
     * @param errorCode     错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId     分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        Log.d(TAG, "METHOD=onUnbind" + "\nErrorCode=" + errorCode + "\nrequestId=" + requestId);
        if(errorCode == 0){
            Toast.makeText(MyApplication.getContext(), "解绑成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MyApplication.getContext(), "解绑失败，请把这段错误代码告诉我："+errorCode+ "非常感谢", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * PushManager.setTags()的回调函数。
     *
     * @param context       上下文
     * @param errorCode     错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param successTags   设置成功的tag
     * @param failTags      设置失败的tag
     * @param requestId     分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode, List<String> successTags, List<String> failTags, String requestId) {

    }
    /**
     * PushManager.delTags() 的回调函数。
     *
     * @param context       上下文
     * @param errorCode     错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param successTags   成功删除的tag
     * @param failTags      删除失败的tag
     * @param requestId     分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode, List<String> successTags, List<String> failTags, String requestId) {

    }

    /**
     * PushManager.listTags() 的回调函数。
     *
     * @param context        上下文
     * @param errorCode      错误码。0表示列举tag成功；非0表示失败。
     * @param tags           当前应用设置的所有tag。
     * @param requestId      分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags, String requestId) {

    }

    /**
     * 接收透传消息的回调函数。当系统接收到一条透传消息的时候该回调函数会被调用
     * 透传消息：当设备接收到透传消息时，消息不会在通知栏展示，只是静默回调开发者自定义Receiver中的onMessage函数。手机收到消息后不会有任何现象，因为透传消息对用户完全透明，用户无法感知，便于开发者在不影响用户的情况下对app进行操作。开发者可以通过log内容和自定义的回调函数onMessage中打印该内容，确认消息到达，并执行开发者指定操作。
     *
     * @param context               上下文
     * @param message               推送的消息
     * @param customContentString   自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message, String customContentString) {
        Log.d(TAG,"METHOD=onMessage"+"\nMessage="+message+"\ncustomContentString="+ customContentString);

    }

    /**
     *  接收通知点击的函数，点击状态栏中的普通通知内容之后该回调函数会被调用
     *
     * @param context               上下文
     * @param title                 推送的通知的标题
     * @param description           推送的通知的描述
     * @param customContentString   自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title, String description, String customContentString) {
        Log.d(TAG, "METHOD=onNotificationClickedRun" + "\nTitle=" + title + "\nDescription=" + description + "\ncustomContentString=" + customContentString);
//        Intent intent = new Intent(context.getApplicationContext(),PushMessageReceiverActivity.class);
//        intent.putExtra("title",title);
//        intent.putExtra("description", description);
//        context.getApplicationContext().startActivity(intent);
    }


    /**
     *  接收通知到达的函数，当推送的一条普通通知到达的时候该回调函数会被调用
     *
     * @param context               上下文
     * @param title                 推送的通知的标题
     * @param description           推送的通知的描述
     * @param customContentString   自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationArrived(Context context, String title, String description, String customContentString) {
        Log.d(TAG, "METHOD=onNotificationArrived" + "\nTitle=" + title + "\nDescription=" + description + "\ncustomContentString=" + customContentString);
    }
}
