package com.berpesan.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by itdel on 3/30/17.
 */

public class DataSms{
    @Expose
    private String id;

    @Expose
    private String sender;

    @Expose
    private String body;

    @Expose
    private Date date;

    @Expose
    private String sms;

    @Expose
    private String label;

    public DataSms(){

    }

    public DataSms(String sms, String label){
        this.sms = sms;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLabel() {
        return label;
    }

    public String getSms() {
        return sms;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

}
