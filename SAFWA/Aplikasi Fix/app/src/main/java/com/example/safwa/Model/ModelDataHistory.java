package com.example.safwa.Model;

public class ModelDataHistory {

    String debit;
    String tagihan;
    String timestamp;
    String kebocoran;

    public ModelDataHistory(String debit, String tagihan, String timestamp, String kebocoran) {
        this.debit = debit;
        this.tagihan = tagihan;
        this.timestamp = timestamp;
        this.kebocoran = kebocoran;
    }

    public String getTagihan() { return tagihan;}

    public void setTagihan (String tagihan) {this.tagihan = tagihan;}

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDebit() {return debit;}

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getKebocoran() {return kebocoran;}

    public void setKebocoran(String kebocoran) {
        this.kebocoran = kebocoran;
    }

    public ModelDataHistory(){

    }
}
