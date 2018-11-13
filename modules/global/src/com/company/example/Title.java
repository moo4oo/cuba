package com.company.example;
public class Title {
    private String docType = "";
    private String regNumber = "";
    private String date = "";
    private String addresse = "";
    private String topic = "";

    public String getStringTitle() {
        return docType + " №" + regNumber + " от " + date + " в " + addresse + ", " + topic;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}