package com.jxzhang.yourgrades.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jxzhang.yourgrades.R;
import com.jxzhang.yourgrades.security.DES3Utils;
import com.jxzhang.yourgrades.service.LongRunningRequestService;
import com.jxzhang.yourgrades.util.Constants;
import com.jxzhang.yourgrades.util.HttpUtil;
import com.jxzhang.yourgrades.util.MyApplication;

import org.json.JSONException;

import java.util.Random;

/**
 * Created by J.X.Zhang on 2015/9/26.
 * 登录界面Activity
 */
public class LoginActivity extends Activity{

    private UserLoginTask mAuthTask = null;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEdit;
    private SharedPreferences serviceSharedPreference;
    private SharedPreferences.Editor serviceSharedPreferenceEdit;

    byte[] mPasswordByteData;

    // 声明UI控件.
    private EditText mStudentIdView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mHintText;
    private CheckBox mCheckBoxRememberPassword;
    private CheckBox mCheckBoxAutoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        if (MainActivity.isLongRunningRequestServiceWorked()) {
            //如果服务正在运行，那么结束这个服务
            Intent longRunningRequestServiceIntent = new Intent(LoginActivity.this, LongRunningRequestService.class);
            stopService(longRunningRequestServiceIntent);
        }


        //实例化UI控件
        mStudentIdView = (EditText) findViewById(R.id.student_number);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mHintText = (TextView)findViewById(R.id.login_hint_text);

        //彩蛋- -
        setLoginHnitText();

        Button mStudentIdSignInButton = (Button) findViewById(R.id.student_number_sign_in_button);
        mStudentIdSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        //记住密码SharedPreference初始化
        mSharedPreferences = MyApplication.getContext().getSharedPreferences("remembered_password", MODE_PRIVATE);
        //ServiceSharedPreference初始化
        serviceSharedPreference = MyApplication.getContext().getSharedPreferences("service_password",MODE_PRIVATE);

        mCheckBoxRememberPassword = (CheckBox)findViewById(R.id.checkbox_remember_password);

        boolean isRemember = mSharedPreferences.getBoolean("remembered_password",false);
        if(isRemember){
            String student_id = mSharedPreferences.getString("student_id","");
            String encrypt_password = mSharedPreferences.getString("password","");
            int password_length = mSharedPreferences.getInt("password_length",0);
            try {
                byte[] decrypt_password = DES3Utils.string2byteArray(encrypt_password, password_length);
                mPasswordByteData = DES3Utils.decryptMode(decrypt_password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String password = new String(mPasswordByteData);
            mStudentIdView.setText(student_id);
            mPasswordView.setText(password);
            mCheckBoxRememberPassword.setChecked(true);
        }
    }

    /**
     * 彩蛋~~
     */
    private void setLoginHnitText(){
        Random random = new Random();
        int randomInt = random.nextInt(10);
        switch (randomInt){
            case 0:
                mHintText.setText(getString(R.string.easter_egg_0));
                break;
            case 1:
                mHintText.setText(getString(R.string.easter_egg_1));
                break;
            case 2:
                mHintText.setText(getString(R.string.easter_egg_2));
                break;
            case 3:
                mHintText.setText(getString(R.string.easter_egg_3));
                break;
            case 4:
                mHintText.setText(getString(R.string.easter_egg_4));
                break;
            case 5:
                mHintText.setText(getString(R.string.easter_egg_5));
                break;
            case 6:
                mHintText.setText(getString(R.string.easter_egg_6));
                break;
            case 7:
                mHintText.setText(getString(R.string.easter_egg_7));
                break;
            case 8:
                mHintText.setText(getString(R.string.easter_egg_8));
                break;
            case 9:
                mHintText.setText(getString(R.string.easter_egg_9));
                break;
            default:
                break;
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // 重置错误信息
        mStudentIdView.setError(null);
        mPasswordView.setError(null);

        // 获取用户输入的用户名和密码
        String studentId = mStudentIdView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 检测用户是否输入密码，如果输入，将标记设为true
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // 检测用户是否输入用户名
        if (TextUtils.isEmpty(studentId)) {
            mStudentIdView.setError(getString(R.string.error_field_required));
            focusView = mStudentIdView;
            cancel = true;
        }else if (!isStudentIdValid(studentId)) {
            mStudentIdView.setError(getString(R.string.error_invalid_student_id));
            focusView = mStudentIdView;
            cancel = true;
        }

        if (cancel) {
            //如果cancle为false，不登录并重置FocusView
            focusView.requestFocus();
        } else {
            //否则显示ProgressBar，并登陆
            showProgress(true);
            mAuthTask = new UserLoginTask(studentId, password);
            mAuthTask.execute((Void) null);
        }
    }
    private boolean isStudentIdValid(String studentId) {
        return studentId.length() > 9;
    }
    /**
     * 显示ProgressBar隐藏LoginForm逻辑
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            mHintText.setVisibility(show ? View.VISIBLE : View.GONE);

        } else {
            mHintText.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 启动主Activity
     */
    private void startMainActivity(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }
    /**
     * 登录逻辑代码（异步）
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mStudentId;
        private final String mPassword;
        private HttpUtil httpUtil;
        UserLoginTask(String _mStudentId, String _password) {
            mStudentId = _mStudentId;
            mPassword = _password;
            httpUtil = new HttpUtil(mStudentId,mPassword);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                boolean isPasswordCorrect = httpUtil.loginVerify();
                if(isPasswordCorrect){
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        /**
         * success参数代表DoInBackground返回值，返回true或者false，登陆成功或者失败
         * @param success
         */
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                mEdit = mSharedPreferences.edit();
                serviceSharedPreferenceEdit = serviceSharedPreference.edit();
                //将明文密码加密为二进制数组
                byte[] secretArr = DES3Utils.encryptMode(mPassword.getBytes());
                //将二进制数组转换为String字符串
                String encryptPassword = DES3Utils.byteArray2String(secretArr);
                if(mCheckBoxRememberPassword.isChecked()){
                    //将密码长度、密码、学号存入SharedPreference中
                    mEdit.putInt("password_length", secretArr.length);
                    mEdit.putString("student_id", mStudentId);
                    mEdit.putString("password", encryptPassword);
                    mEdit.putBoolean("remembered_password", true);
                } else {
                    //如果没有选择记住密码，清空记住密码SharedPreference中的内容
                    mEdit.clear();
                }
                //无论是否记住密码都将密码存进ServiceSharedPreference
                serviceSharedPreferenceEdit.putString("student_id",mStudentId);
                serviceSharedPreferenceEdit.putString("password",encryptPassword);
                serviceSharedPreferenceEdit.putInt("password_length", secretArr.length);
                //提交操作
                mEdit.commit();
                serviceSharedPreferenceEdit.commit();

                //启动主Activity
                startMainActivity();
            } else {
                int errorCode = httpUtil.getErrorMessageCode();
                Log.d("Test","errorCode:"+errorCode);
                switch (errorCode){
                    case Constants.LOGIN_NETWORK_WRONG:
                        Toast.makeText(MyApplication.getContext(),getString(R.string.error_network),Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.LOGIN_PASSWORD_WRONG:
                        Toast.makeText(MyApplication.getContext(), getString(R.string.error_incorrect_password), Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.LOGIN_SYSTEM_WRONG:
                        Toast.makeText(MyApplication.getContext(), getString(R.string.error_system), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}