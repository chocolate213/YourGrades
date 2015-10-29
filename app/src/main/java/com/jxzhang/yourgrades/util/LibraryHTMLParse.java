package com.jxzhang.yourgrades.util;

/**
 * Created by J.X.Zhang on 2015/10/22.
 * 图书馆HTML解析模块
 */
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.SharedPreferences;

public class LibraryHTMLParse {
    private static String b_1_Name;						//1.	书名
    private static String b_2_ISBN;						//2.	ISBN
    private static String b_3_Author;					//3.	编者
    private static String b_4_CallNumber;				//4.	索书号
    private static String b_5_Publisher;				//5.	出版商
    private static String b_6_PublicationPlace;			//6.	出版地
    private static String b_7_PublicationTime;			//7.	出版时间
    private static String b_8_Price;					//8.	价格
    private static String b_9_PageNumber;				//9.	页码
    private static String b_10_TotalRecord;                    //10.    总记录数
    private static String b_11_Codes;                          //11.    图书编码

    public ArrayList<BookInfo> getList(){
        String html = getBookInfoHTML();
        ArrayList<BookInfo> list = new ArrayList<BookInfo>();
        int a = 0, b = 1, c = 0, d = 1;                 //数组角标操作变量
        Document bHTMLInfoDoc = Jsoup.parse(html);
        Elements bAllInfoElements = bHTMLInfoDoc.select("td[class=bordertd]");          //解析出所有图书信息
        Elements bTotalRecordsElements = bHTMLInfoDoc.select("font[color=#CC0000]");    //解析出图书总数信息
        Elements bCodesElements = bHTMLInfoDoc.select("a[onClick*=zyk]");               //解析出图书代码信息

        String[] bAllInfoArray = new String[bAllInfoElements.size()];                           //定义数组存储所有图书信息
        String[] bCodesArray = new String[bCodesElements.size()];                           //定义数组存储所有图书代码信息

        /**
         * 遍历Elements，解析书Codes
         */
        for (Element element : bCodesElements) {
            bCodesArray[c++] = element.attr("onClick");
        }

        /**
         * 遍历Elements，解析所有图书信息
         */
        for (Element element : bAllInfoElements) {
            bAllInfoArray[a++] = element.text();
        }
        /**
         * 获取图书总数
         */
        for (Element element : bTotalRecordsElements) {
            b_10_TotalRecord = element.text();
        }
        /**
         *  将图书信息封装成BookInfo对象并存储在ArrayList中
         */
        for (String bookInfo : bAllInfoArray) {
            switch (b % 9) {
                case 1:
                    b_1_Name = bookInfo;
                    b++;
                    break;
                case 2:
                    b_2_ISBN = bookInfo;
                    b++;
                    break;
                case 3:
                    b_3_Author = bookInfo;
                    b++;
                    break;
                case 4:
                    b_4_CallNumber = bookInfo;
                    b++;
                    break;
                case 5:
                    b_5_Publisher = bookInfo;
                    b++;
                    break;
                case 6:
                    b_6_PublicationPlace = bookInfo;
                    b++;
                    break;
                case 7:
                    b_7_PublicationTime = bookInfo;
                    b++;
                    break;
                case 8:
                    b_8_Price = bookInfo;
                    b++;
                    break;
                case 0:
                    b_9_PageNumber = bookInfo;
                    b_11_Codes = bCodesArray[d].substring(7, 17);
                    b++;
                    d = d + 2;
                    list.add(new BookInfo(b_1_Name, b_2_ISBN, b_3_Author, b_4_CallNumber,
                            b_5_Publisher, b_6_PublicationPlace, b_7_PublicationTime, b_8_Price,
                            b_9_PageNumber, b_10_TotalRecord, b_11_Codes));
                    break;
                default:
                    break;
            }
        }
        return list;
    }
    public String getBookInfoHTML(){
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("book_info", Context.MODE_PRIVATE);
        String htlm = preferences.getString("html_book_info", "no_matching_books");
        return htlm;
    }
}
