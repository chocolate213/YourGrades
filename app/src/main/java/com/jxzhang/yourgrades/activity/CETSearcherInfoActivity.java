package com.jxzhang.yourgrades.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.jxzhang.yourgrades.R;
import com.jxzhang.yourgrades.util.MyApplication;

public class CETSearcherInfoActivity extends Activity {

    final Activity activity = this;
    WebView cetMarksInfoWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetsearcher_info);

        //初始化控件
        cetMarksInfoWebview = (WebView)findViewById(R.id.cet_marks_info_webview);
        //将页面标题设置为书名
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("CET分数解释");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

        cetMarksInfoWebview.getSettings().setJavaScriptEnabled(true);
        cetMarksInfoWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Toast.makeText(MyApplication.getContext(), "Oh no! " + description,
                        Toast.LENGTH_SHORT).show();
            }

            /**
             * 在当前窗口打开页面链接
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //加载页面
        cetMarksInfoWebview.loadUrl("file:///android_asset/cet_info.html" );
    }

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
