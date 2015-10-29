package com.jxzhang.yourgrades.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.X.Zhang on 2015/9/21.
 * 教务网登录后台处理模块
 */
public class HttpUtil {
    String passWord;
    String userID;
    String zfx_flag;
    String zxf;
    String sel_xq;
    String SJ;
    String SelXNXQ;
    String sel_xn;
    List<Cookie> cookies;
    private int WRONG_INFO_FLAG = Constants.LOGIN_SUCCESS;
    //传入用户名密码的构造函数：使用默认查询信息
    public HttpUtil(String userID,String passWord) {
        this.userID = userID;
        this.passWord = passWord;
    }
    //传入信息的构造函数
    public HttpUtil(String passWord, String userID, String zfx_flag,String zxf,String sel_xq,
                    String SJ,String SelXNXQ,String sel_xn) {
        this.zfx_flag = zfx_flag;
        this.zxf = zxf;
        this.sel_xq = sel_xq;
        this.SJ = SJ;
        this.SelXNXQ = SelXNXQ;
        this.sel_xn = sel_xn;
        this.passWord = passWord;
        this.userID = userID;
    }



    /**
     * 登录验证模块
     *
     * @return
     */
    public boolean loginVerify() {
        Log.d("Test","loginVerify run");
        HttpClient client = new DefaultHttpClient();
        HttpResponse httpResponse;
        String url = Constants.LOGIN_URL_IN;
        HttpPost httpRequest = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();    //Post数据给服务器：学号，密码，权限
        params.add(new BasicNameValuePair("UserID", userID));
        params.add(new BasicNameValuePair("PassWord", passWord));
        params.add(new BasicNameValuePair("Sel_Type", "STU"));

        try {
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            httpResponse = client.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();  //拿到Cookie,返回List集合，里面只有一个Cookie
                boolean isPasswordRight = judgePassword();
                if(isPasswordRight){
                    setErrorMessageCode(Constants.LOGIN_SUCCESS);
                    return true;
                } else {
                    setErrorMessageCode(Constants.LOGIN_PASSWORD_WRONG);
                    return false;
                }
            } else {
                setErrorMessageCode(Constants.LOGIN_NETWORK_WRONG);
                return false;
            }
        } catch (Exception e) {
            setErrorMessageCode(Constants.LOGIN_NETWORK_WRONG);
            return false;
        }
    }

    /**
     * 密码验证模块
     * @return
     */
    private boolean judgePassword() {
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("stu_info", Context.MODE_PRIVATE);
        String result = "";
        String url = Constants.STU_GET_SCORE_URL_IN;
        HttpPost httpRequest = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("zfx_flag", "0"));
        params.add(new BasicNameValuePair("zxf", "0"));
        params.add(new BasicNameValuePair("sel_xq", "1"));
        params.add(new BasicNameValuePair("SJ", "1"));
        params.add(new BasicNameValuePair("SelXNXQ", "2"));
        params.add(new BasicNameValuePair("sel_xn", "2014"));
        Log.d("Test",cookies.get(0).getValue()+"");
        httpRequest.setHeader("Cookie", "ASP.NET_SessionId=" + cookies.get(0).getValue());
        try {
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                StringBuffer sb = new StringBuffer();
                HttpEntity entity = httpResponse.getEntity();
                InputStream is = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "GB2312"));
                String data = "";
                while ((data = br.readLine()) != null) {
                    sb.append(data);
                }
                result = sb.toString();

                Log.d("Test",result);
                if (result.contains("系统提示")) {
                    Log.d("Test","密码错误");
                    return true;
                } else {
                    SharedPreferences.Editor edit =  preferences.edit();
                    edit.clear();
                    edit.putString("stu_grade_info",result);
                    edit.commit();
                    Log.d("Test","密码正确");
                    return true;
                }
            } else {
                return false;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public int getErrorMessageCode(){
        return WRONG_INFO_FLAG;
    }
    public void setErrorMessageCode(int errorCode){
        WRONG_INFO_FLAG = errorCode;
    }
}



