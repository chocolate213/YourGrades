package com.jxzhang.yourgrades.util;

/**
 * Created by J.X.Zhang on 2015/10/22.
 * 图书馆后台访问模块
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;

public class LibraryHttpUtil {
    String mBookNameURL;
    private int WRONG_INFO_FLAG = Constants.LOGIN_SUCCESS;

    public LibraryHttpUtil(String mBookNameURL) {
        this.mBookNameURL = mBookNameURL;
    }

    public boolean getBookInfo() {

        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("book_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        HttpClient httpClient = new DefaultHttpClient();
        try {
            mBookNameURL = URLEncoder.encode(mBookNameURL, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            setErrorMessageCode(Constants.LOGIN_SYSTEM_WRONG);
        }
        String url = "http://lib.hhhxy.cn:88/ggjs/jszjl.jsp?xz=&jstj=%D5%FD%CC%E2%C3%FB&value1=" + mBookNameURL + "&jsfs=%D6%D0%BC%E4%C6%A5%C5%E4&pxtj=ZTM&pagesize=500&mynewsubmit=%CC%E1%BD%BB%B2%E9%D1%AF&goNum=1&servertype=&ljh=&tslx=zw&ckyy=yy&page=0&jstj=%D5%FD%CC%E2%C3%FB&value1=" + mBookNameURL + "&jsfs=%D6%D0%BC%E4%C6%A5%C5%E4&pxtj=ZTM&pagesize=500";

        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = null;
        String response = null;
        try {
            httpResponse = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
            setErrorMessageCode(Constants.LOGIN_NETWORK_WRONG);
            return false;
        }

        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = httpResponse.getEntity();
            try {
                response = EntityUtils.toString(he, "GB2312");
            } catch (IOException e) {
                e.printStackTrace();
                setErrorMessageCode(Constants.LOGIN_NETWORK_WRONG);
                return false;
            }
        } else {
            setErrorMessageCode(Constants.LOGIN_NETWORK_WRONG);
            return false;
        }

        editor.clear();
        editor.putString("html_book_info", response);
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
