package com.jxzhang.yourgrades.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jxzhang.yourgrades.R;
import com.jxzhang.yourgrades.util.Constants;

public class AboutActivity extends Activity {


    public static final String FEEDBACK = "关于学院助手";
    public static final String USER_FEEDBACK_SUBJECT = "用户反馈";
    TextView mFeedBack;
    TextView mAppFeature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //初始化控件
        mFeedBack = (TextView)findViewById(R.id.text_feed_back);
        mAppFeature = (TextView)findViewById(R.id.text_feature);

        //设置标题
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(FEEDBACK);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

    }

    /**
     * 监听TextView
     * @param view
     */
    public void aboutLayoutBtnOnClick(View view){

        switch (view.getId()){
            case R.id.text_feed_back:
                composeEmail(Constants.ADDRESS,USER_FEEDBACK_SUBJECT,"请简要描述您遇到的问题和意见,帮助我们做的更好。建议您留下联系方式，方便我们与您联系，感谢您的支持!");
                break;
            case R.id.text_feature:
                Intent intent = new Intent(AboutActivity.this,FeatureActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    /**
     * 反馈
     * @param addresses
     * @param subject
     * @param text
     */
    public void composeEmail(String[] addresses, String subject, String text){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:chocolatepie213@gmail.com")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);                   // 正文
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
        }
    }

    /**
     * 监听返回按键
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
