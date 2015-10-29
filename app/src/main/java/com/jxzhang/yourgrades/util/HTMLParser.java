package com.jxzhang.yourgrades.util;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.X.Zhang on 2015/9/22.
 * 教务网HTML解析模块
 */
public class HTMLParser {
    List<StudentInfo> list = new ArrayList<StudentInfo>();

    String mData;
    public HTMLParser(String data){
        mData = data;
        mPaster();
    }
    public List<StudentInfo> getList() {
        return list;
    }
    public void mPaster(){
        StringBuilder gradeInfo = new StringBuilder();
        int i = 0,j = 0,k = 0,x = 0;
        Document doc = Jsoup.parse(mData);
        Elements classNames = doc.select("td[width=23%]");	//带有width=23%属性的td元素	里面装的是课程名称
        Elements grades = doc.select("td[width=5%]");		//带有width=5%属性的td元素	里面装的是成绩和学分绩点
        Elements classesType = doc.select("td[width=14%]");	//带有width=14%属性的td元素	里面装的是课程性质
        String[] grade = new String[grades.size()];
        String[] classs = new String[classNames.size()];
        String[] classType = new String[classesType.size()];
        for (Element element : grades) {
            String str = element.text();
            grade[i++] = str;
        }
        for (Element element :classesType){
            String str = element.text();
            classType[k++] = str;
        }
        for (Element element : classNames){
            String className = element.text();
            StudentInfo si = new StudentInfo(className,classType[x++],grade[j]);
            j = j + 2;
            list.add(si);
        }
    }
}
