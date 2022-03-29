package com.example.safwa.Model;

public class ModelDataRealTime {
    String Date;
    String Time;
    String debit;
    String kebocoran;
    String pres1;
    String pres2;
    String tagihan;

    public ModelDataRealTime(String date, String time, String debit, String kebocoran, String pres1, String pres2, String tagihan) {
        Date = date;
        Time = time;
        this.debit = debit;
        this.kebocoran = kebocoran;
        this.pres1 = pres1;
        this.pres2 = pres2;
        this.tagihan = tagihan;
    }

    public String getDate() {return Date;}
    public void setDate(String date) {this.Date = date;}

    public String getTime() {return Time;}
    public void setTime(String time) {this.Time = time;}

    public String getDebit() {return debit;}
    public void setDebit(String debit) {this.debit = debit;}

    public String getKebocoran() {return kebocoran;}
    public void setKebocoran(String kebocoran) {this.kebocoran = kebocoran;}

    public String getPres1() {return pres1;}
    public void setPres1(String pres1) {this.pres1 = pres1;}

    public String getPres2() {return  pres2;}
    public void setPres2(String pres2) {this.pres2 = pres2;}

    public String getTagihan() {return tagihan;}
    public void setTagihan(String tagihan) {this.tagihan = tagihan;}

    public ModelDataRealTime(){

    }

}
