package com.jxzhang.yourgrades.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jxzhang.yourgrades.R;
import com.jxzhang.yourgrades.util.CETHTMLParser;
import com.jxzhang.yourgrades.util.CETHTTPUtil;
import com.jxzhang.yourgrades.util.CETInfo;
import com.jxzhang.yourgrades.util.Constants;
import com.jxzhang.yourgrades.util.MyApplication;

public class CETSearcherActivity extends Activity {

    private SearchCETTask mAuthTask = null;

    private EditText CETCHK;
    private EditText CETName;
    private Button mCetSearchButton;

    private View mSearchForm;                       //显示登录界面
    private LinearLayout mSearchProgress;           //显示进度条
    private LinearLayout mCetPointsFormLayout;      //显示分数

    private TextView mCETName;
    private TextView mCETSChool;
    private TextView mCETType;
    private TextView mCETId;
    private TextView mCETDate;
    private TextView mCETPointTotal;
    private TextView mCETPointListen;
    private TextView mCETPointRead;
    private TextView mCETPointWriteAndTranslation;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    SharedPreferences preferences_init;
    SharedPreferences.Editor editor_init;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetsearcher);

        //初始化控件

        CETCHK = (EditText)findViewById(R.id.cet_zkh);
        CETName = (EditText)findViewById(R.id.cet_name);
        mCetSearchButton = (Button)findViewById(R.id.cet_search_button);
        mSearchForm = findViewById(R.id.cet_search_form);
        mSearchProgress = (LinearLayout)findViewById(R.id.cet_search_progress);
        mCetPointsFormLayout = (LinearLayout)findViewById(R.id.cet_points_form_layout);

        mCETName = (TextView)findViewById(R.id.cet_name_text);
        mCETSChool = (TextView)findViewById(R.id.cet_school_text);
        mCETType = (TextView)findViewById(R.id.cet_type_text);
        mCETId = (TextView)findViewById(R.id.cet_id_text);
        mCETDate = (TextView)findViewById(R.id.cet_date_text);
        mCETPointTotal = (TextView)findViewById(R.id.cet_points_total_text);
        mCETPointListen = (TextView)findViewById(R.id.cet_points_listen_text);
        mCETPointRead = (TextView)findViewById(R.id.cet_points_read_text);
        mCETPointWriteAndTranslation = (TextView)findViewById(R.id.cet_points_write_translation_text);


        preferences = MyApplication.getContext().getSharedPreferences("cet_form_flag", Context.MODE_PRIVATE);
        editor = preferences.edit();
        preferences_init = MyApplication.getContext().getSharedPreferences("cet_form_flag", Context.MODE_PRIVATE);
        editor_init = preferences_init.edit();
        if(preferences_init.getBoolean("cet_flag",false)){
            String zhk = preferences_init.getString("zhk","");
            String name = preferences_init.getString("name","");
            CETCHK.setText(zhk);
            CETName.setText(name);
        }

        //将页面标题设置为书名
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("CET分数查询");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

        mCetSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSearch();
            }
        });
    }

    /**
     * 准备查询
     */
    public void attemptSearch(){

        boolean flag = false;
        View focusView = null;

        String CETZKHText = CETCHK.getText().toString();
        String CETNameText = CETName.getText().toString();

        if (TextUtils.isEmpty(CETZKHText)) {
            Toast.makeText(CETSearcherActivity.this, "准考证号不能为空", Toast.LENGTH_SHORT).show();
            focusView = CETCHK;
            flag = true;
        } else if (!isZKHValid(CETZKHText)){
            Toast.makeText(CETSearcherActivity.this, "请输入15位准考证号", Toast.LENGTH_SHORT).show();
            focusView = CETCHK;
            flag = true;
        }
        if (TextUtils.isEmpty(CETNameText)) {
            Toast.makeText(CETSearcherActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            focusView = CETName;
            flag = true;
        } else if(!isNameValid(CETNameText)){
            Toast.makeText(CETSearcherActivity.this, "姓名在2-3位之间，可以只输入前3位", Toast.LENGTH_SHORT).show();
            focusView = CETName;
            flag = true;
        }

        if (flag) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new SearchCETTask(CETZKHText, CETNameText);
            mAuthTask.execute((Void) null);

        }

    }

    public void showProgress(final boolean show){
        mSearchProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        mSearchForm.setVisibility(show ? View.GONE : View.VISIBLE);
        mCetPointsFormLayout.setVisibility(show ? View.GONE : View.VISIBLE);
    }
    public void showCETPoints(final boolean show){
        mSearchProgress.setVisibility(show ? View.GONE : View.VISIBLE);
        mSearchForm.setVisibility(show ? View.GONE : View.VISIBLE);
        mCetPointsFormLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    public void showLoginForm(final boolean show){
        mSearchProgress.setVisibility(show ? View.GONE : View.VISIBLE);
        mSearchForm.setVisibility(show ? View.VISIBLE : View.GONE);
        mCetPointsFormLayout.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * 判断准考证号是否合法
     * @param zkh
     * @return
     */
    public boolean isZKHValid(String zkh){
        return zkh.length()>14&&zkh.length()<16;
    }

    /**
     * 判断考生姓名是否合法
     * @param name
     * @return
     */
    public boolean isNameValid(String name){
        return name.length()>1&&name.length()<4;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cetsearcher, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.cet_searcher_item:
                Intent intent = new Intent(this,CETSearcherInfoActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public class SearchCETTask extends AsyncTask<Void, Void, Boolean> {

        private final String CETZKHText;
        private final String CETNameText;
        private CETHTTPUtil cethttpUtil;
        private CETInfo cetInfo;
        public SearchCETTask(String CETZKHText, String CETNameText) {
            this.CETNameText = CETNameText;
            this.CETZKHText = CETZKHText;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            cethttpUtil = new CETHTTPUtil(CETZKHText,CETNameText);
            return cethttpUtil.searchPoints();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mAuthTask = null;
            if (result) {
                SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("cet_info", Context.MODE_PRIVATE);
                String resopnse = preferences.getString("html_cet_info","no_data");
                if(resopnse.contains("无法找到对应的分数")){
                    showProgress(false);            //不显示进度条
                    showCETPoints(false);           //不显示分数
                    Toast.makeText(MyApplication.getContext(), "无法找到对应的分数，请确认你输入的准考证号及姓名无误", Toast.LENGTH_LONG).show();
                    showLoginForm(true);            //显示登录表单
                } else {
                    editor_init.clear();
                    editor_init.putString("zhk",CETZKHText);
                    editor_init.putString("name",CETNameText);
                    editor_init.putBoolean("cet_flag",true);
                    editor_init.commit();
                    CETHTMLParser cethtmlParser = new CETHTMLParser();
                    cetInfo = cethtmlParser.getCETInfo();
                    //设置分数
                    mCETName.setText(cetInfo.getCetName());
                    mCETSChool.setText(cetInfo.getCetSchool());
                    mCETType.setText(cetInfo.getCetType());
                    mCETId.setText(cetInfo.getCetID());
                    mCETDate.setText(cetInfo.getCetTime());
                    mCETPointTotal.setText(cetInfo.getCetPointsTotal());
                    mCETPointListen.setText(cetInfo.getCetPointsListen());
                    mCETPointRead.setText(cetInfo.getCetPointsRead());
                    mCETPointWriteAndTranslation.setText(cetInfo.getCetPointsWriteAndTranslation());

                    showProgress(false);
                    showLoginForm(false);
                    showCETPoints(true);
                }

            } else {
                showProgress(false);
                showCETPoints(false);
                showLoginForm(true);
                int errorCode = cethttpUtil.getErrorMessageCode();
                switch (errorCode){
                    case Constants.LOGIN_NETWORK_WRONG:
                        Toast.makeText(MyApplication.getContext(), "啊哦...网络好像出了点问题...", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.LOGIN_SYSTEM_WRONG:
                        Toast.makeText(MyApplication.getContext(), "系统错误，请重试", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.LOGIN_NO_SEARCH_BOOKS:
                        Toast.makeText(MyApplication.getContext(), "没有找到相关书籍，请检查书名是否正确或尝试重新检索", Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
            showLoginForm(true);
            showCETPoints(false);
        }

    }
}
