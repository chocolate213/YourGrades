package com.jxzhang.yourgrades.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * Created by J.X.Zhang on 2015/10/26.
 */
public class CETHTTPUtil {
    private String mCETId;
    private String mCETName;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private int WRONG_INFO_FLAG = Constants.LOGIN_SUCCESS;

    public CETHTTPUtil(String mCETId, String mCETName) {
        this.mCETId = mCETId;
        this.mCETName = mCETName;
        preferences = MyApplication.getContext().getSharedPreferences("cet_info", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public boolean searchPoints(){
        HttpClient httpClient = new DefaultHttpClient();
        String CETName = null;
        try {
            CETName = URLEncoder.encode(mCETName, "GB2312");
        } catch (UnsupportedEncodingException e) {
            setErrorMessageCode(Constants.LOGIN_SYSTEM_WRONG);
            e.printStackTrace();
            return false;
        }
        URI uri = null;
        try {
            uri = new URI("http://www.chsi.com.cn/cet/query?zkzh="+mCETId+"&xm="+mCETName);
        } catch (URISyntaxException e) {
            setErrorMessageCode(Constants.LOGIN_SYSTEM_WRONG);
            e.printStackTrace();
            return false;
        }
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("Referer","http://www.chsi.com.cn/cet/");
        HttpResponse httpResponse = null;
        HttpEntity he;
        String response  = null;
        try {
            httpResponse = httpClient.execute(httpGet);
        } catch (IOException e) {
            setErrorMessageCode(Constants.LOGIN_NETWORK_WRONG);
            e.printStackTrace();
            return false;
        }
        if(httpResponse.getStatusLine().getStatusCode() == 200){
            he = httpResponse.getEntity();
            try {
                response = EntityUtils.toString(he, "GB2312");
            } catch (IOException e) {
                setErrorMessageCode(Constants.LOGIN_NETWORK_WRONG);
                e.printStackTrace();
                return false;
            }
        }
        editor.clear();
        editor.putString("html_cet_info", response);
        editor.commit();
        return true;
    }

    public int getErrorMessageCode() {
        return WRONG_INFO_FLAG;
    }

    public void setErrorMessageCode(int errorCode) {
        WRONG_INFO_FLAG = errorCode;
    }

}
