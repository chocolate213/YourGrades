package com.jxzhang.yourgrades.util;

/**
 * Created by J.X.Zhang on 2015/9/21.
 * 学生成绩信息对线
 */
public class StudentInfo {
    String className;
    String classType;
    String grade;

    public StudentInfo() {
    }

    public StudentInfo(String className, String classType, String grade) {
        this.className = className;
        this.classType = classType;
        this.grade = grade;
    }
    public StudentInfo(String className, String grade) {
        this.className = className;
        this.grade = grade;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

}
