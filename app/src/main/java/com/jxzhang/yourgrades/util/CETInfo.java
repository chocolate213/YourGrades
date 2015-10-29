package com.jxzhang.yourgrades.util;

/**
 * Created by J.X.Zhang on 2015/10/26.
 */
public class CETInfo {
    private String cetName;
    private String cetSchool;
    private String cetType;
    private String cetID;
    private String cetTime;
    private String cetPointsTotal;
    private String cetPointsListen;
    private String cetPointsRead;
    private String cetPointsWriteAndTranslation;
    public CETInfo(){}
    public CETInfo(String cetName, String cetSchool, String cetType, String cetID, String cetTime, String cetPointsTotal, String cetPointsListen, String cetPointsRead, String cetPointsWriteAndTranslation) {
        this.cetName = cetName;
        this.cetSchool = cetSchool;
        this.cetType = cetType;
        this.cetID = cetID;
        this.cetTime = cetTime;
        this.cetPointsTotal = cetPointsTotal;
        this.cetPointsListen = cetPointsListen;
        this.cetPointsRead = cetPointsRead;
        this.cetPointsWriteAndTranslation = cetPointsWriteAndTranslation;
    }

    public String getCetName() {
        return cetName;
    }

    public void setCetName(String cetName) {
        this.cetName = cetName;
    }

    public String getCetSchool() {
        return cetSchool;
    }

    public void setCetSchool(String cetSchool) {
        this.cetSchool = cetSchool;
    }

    public String getCetType() {
        return cetType;
    }

    public void setCetType(String cetType) {
        this.cetType = cetType;
    }

    public String getCetID() {
        return cetID;
    }

    public void setCetID(String cetID) {
        this.cetID = cetID;
    }

    public String getCetTime() {
        return cetTime;
    }

    public void setCetTime(String cetTime) {
        this.cetTime = cetTime;
    }

    public String getCetPointsTotal() {
        return cetPointsTotal;
    }

    public void setCetPointsTotal(String cetPointsTotal) {
        this.cetPointsTotal = cetPointsTotal;
    }

    public String getCetPointsListen() {
        return cetPointsListen;
    }

    public void setCetPointsListen(String cetPointsListen) {
        this.cetPointsListen = cetPointsListen;
    }

    public String getCetPointsRead() {
        return cetPointsRead;
    }

    public void setCetPointsRead(String cetPointsRead) {
        this.cetPointsRead = cetPointsRead;
    }

    public String getCetPointsWriteAndTranslation() {
        return cetPointsWriteAndTranslation;
    }

    public void setCetPointsWriteAndTranslation(String cetPointsWriteAndTranslation) {
        this.cetPointsWriteAndTranslation = cetPointsWriteAndTranslation;
    }
}
