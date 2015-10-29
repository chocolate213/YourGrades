package com.jxzhang.yourgrades.util;

/**
 * Created by J.X.Zhang on 2015/10/22.
 * 图书馆图书信息对象
 */

public class BookInfo {

    private String b_1_Name;                // 1. 书名
    private String b_2_ISBN;                // 2. ISBN
    private String b_3_Author;              // 3. 编者
    private String b_4_CallNumber;          // 4. 索书号
    private String b_5_Publisher;           // 5. 出版商
    private String b_6_PublicationPlace;    // 6. 出版地
    private String b_7_PublicationTime;     // 7. 出版时间
    private String b_8_Price;               // 8. 价格
    private String b_9_PageNumber;          // 9. 页码
    private String b_10_TotalRecord;        // 10. 总记录数
    private String b_11_Codes;              // 11. 图书代码

    public BookInfo(String b_1_Name, String b_2_ISBN, String b_3_Author,
                    String b_4_CallNumber, String b_5_Publisher,
                    String b_6_PublicationPlace, String b_7_PublicationTime,
                    String b_8_Price, String b_9_PageNumber, String b_10_TotalRecord,
                    String b_11_Codes) {
        this.b_1_Name = b_1_Name;
        this.b_2_ISBN = b_2_ISBN;
        this.b_3_Author = b_3_Author;
        this.b_4_CallNumber = b_4_CallNumber;
        this.b_5_Publisher = b_5_Publisher;
        this.b_6_PublicationPlace = b_6_PublicationPlace;
        this.b_7_PublicationTime = b_7_PublicationTime;
        this.b_8_Price = b_8_Price;
        this.b_9_PageNumber = b_9_PageNumber;
        this.b_10_TotalRecord = b_10_TotalRecord;
        this.b_11_Codes = b_11_Codes;

    }

    public String getB_10_TotalRecord() {
        return b_10_TotalRecord;
    }

    public void setB_10_TotalRecord(String mTotalRecord) {
        this.b_10_TotalRecord = mTotalRecord;
    }

    public String getB_1_Name() {
        return b_1_Name;
    }

    public void setB_1_Name(String b_1_Name) {
        this.b_1_Name = b_1_Name;
    }

    public String getB_2_ISBN() {
        return b_2_ISBN;
    }

    public void setB_2_ISBN(String b_2_ISBN) {
        this.b_2_ISBN = b_2_ISBN;
    }

    public String getB_3_Author() {
        return b_3_Author;
    }

    public void setB_3_Author(String b_3_Author) {
        this.b_3_Author = b_3_Author;
    }

    public String getB_4_CallNumber() {
        return b_4_CallNumber;
    }

    public void setB_4_CallNumber(String b_4_CallNumber) {
        this.b_4_CallNumber = b_4_CallNumber;
    }

    public String getB_5_Publisher() {
        return b_5_Publisher;
    }

    public void setB_5_Publisher(String b_5_Publisher) {
        this.b_5_Publisher = b_5_Publisher;
    }

    public String getB_6_PublicationPlace() {
        return b_6_PublicationPlace;
    }

    public void setB_6_PublicationPlace(String b_6_PublicationPlace) {
        this.b_6_PublicationPlace = b_6_PublicationPlace;
    }

    public String getB_7_PublicationTime() {
        return b_7_PublicationTime;
    }

    public void setB_7_PublicationTime(String b_7_PublicationTime) {
        this.b_7_PublicationTime = b_7_PublicationTime;
    }

    public String getB_8_Price() {
        return b_8_Price;
    }

    public void setB_8_Price(String b_8_Price) {
        this.b_8_Price = b_8_Price;
    }

    public String getB_9_PageNumber() {
        return b_9_PageNumber;
    }

    public void setB_9_PageNumber(String b_9_PageNumber) {
        this.b_9_PageNumber = b_9_PageNumber;
    }

    public String b_10_TotalRecord() {
        return b_10_TotalRecord;
    }

    public void b_10_TotalRecord(String b_11_TotalRecord) {
        this.b_10_TotalRecord = b_10_TotalRecord;
    }

    public String getB_11_Codes() {
        return b_11_Codes;
    }

    public void setB_11_Codes(String b_11_Codes) {
        this.b_11_Codes = b_11_Codes;
    }

}