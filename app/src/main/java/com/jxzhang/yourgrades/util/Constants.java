package com.jxzhang.yourgrades.util;

/**
 * 常量值
 * Created by J.X.Zhang on 2015/9/21.
 */
public class Constants {

    /**
     * Email地址
     */
    public static final String[] ADDRESS = {"chocolatepie213@gmail.com"};

    /*
     * ErrorInfo
     */
    public static final int LOGIN_SUCCESS = 0x00000000;           //登陆成功
    public static final int LOGIN_NETWORK_WRONG = 0x00000001;     //网络错误
    public static final int LOGIN_PASSWORD_WRONG = 0x00000002;    //密码错误
    public static final int LOGIN_SYSTEM_WRONG = 0x00000003;      //系统错误
    public static final int LOGIN_NO_SEARCH_BOOKS = 0x00000004;   //没有找到相关书籍
    /*
     * URL
     */
    public static final String LOGIN_URL_OUT= "http://jwgl.hhhxy.cn/_data/home_login.aspx";
    public static final String LOGIN_URL_IN= "http://172.16.100.1/_data/home_login.aspx";
    public static final String STU_GET_SCORE_URL_OUT= "http://jwgl.hhhxy.cn/xscj/Stu_MyScore_rpt.aspx";
    public static final String STU_GET_SCORE_URL_IN= "http://172.16.100.1/xscj/Stu_MyScore_rpt.aspx";
    /*
     * 学年
     */
    public static final String YEAR_2013_2014 = "2013";
    public static final String YEAR_2014_2015 = "2014";
    public static final String YEAR_2015_2016 = "2015";

    /*
     *  主辅修标记：
     */
    public static final String ZFX_FLAG_MAGOR = "0";	//主修
    public static final String ZFX_FLAG_MINOR = "1";	//辅修
    /*
     * 学期：
     */
    public static final String FIRST_SEMESTER = "0";
    public static final String SECOND_SEMESTER = "1";
    /*
     * 原始成绩/有效成绩
     */
    public static final String GRADE_ORIGINAL = "0";
    public static final String GRADE_VALID = "1";

    /*
     * 查询成绩类别：
     */
    public static final String TYPE_ALL = "0";
    public static final String TYPE_YEAR = "1";
    public static final String TYPE_SEMESTER = "2";
}
