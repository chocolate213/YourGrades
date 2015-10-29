package com.jxzhang.yourgrades.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jxzhang.yourgrades.R;

public class PushMessageReceiverActivity extends Activity {


    TextView mPushMessageTitle;
    TextView mPushMessageDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_message_receiver);


        mPushMessageDescription = (TextView)findViewById(R.id.push_message_description_text);
        mPushMessageTitle = (TextView)findViewById(R.id.push_message_title_text);


        Intent intent = getIntent();
        String mTitle = intent.getStringExtra("title");
        String mDestriction = intent.getStringExtra("description");

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("通知消息");
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        mPushMessageDescription.setText(mDestriction);
        mPushMessageTitle.setText(mTitle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_push_message_receiver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
