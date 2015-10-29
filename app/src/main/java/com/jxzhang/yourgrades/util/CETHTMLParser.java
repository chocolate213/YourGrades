package com.jxzhang.yourgrades.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.X.Zhang on 2015/10/26.
 */
public class CETHTMLParser {

    public CETInfo getCETInfo(){
        String html = getCETInfoHTML();
        CETInfo cetInfo = new CETInfo();
        Document bHTMLInfoDoc = Jsoup.parse(html);
        Elements bAllInfoElements = bHTMLInfoDoc.select("table[class=cetTable]");          //解析出所有图书信息
        String[] str = null;
        for (Element element : bAllInfoElements) {
            str = element.text().split(" ");
        }
        //将信息装入对象
        cetInfo.setCetName(str[1]);
        cetInfo.setCetSchool(str[3]);
        cetInfo.setCetType(str[5]);
        cetInfo.setCetID(str[7]);
        cetInfo.setCetTime(str[9]);
        cetInfo.setCetPointsTotal(str[11]);
        cetInfo.setCetPointsListen(str[13]);
        cetInfo.setCetPointsRead(str[15]);
        cetInfo.setCetPointsWriteAndTranslation(str[17]);
        return cetInfo;
    }

    public String getCETInfoHTML(){
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("cet_info", Context.MODE_PRIVATE);
        String htlm = preferences.getString("html_cet_info", "no_matching_books");
        return htlm;
    }
}
