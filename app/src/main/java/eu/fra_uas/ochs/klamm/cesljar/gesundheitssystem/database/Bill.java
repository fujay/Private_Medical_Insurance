package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database;

public class Bill {

    private long id;
    private String billnumber;
    private double amount;
    private double refund;
    private String kind;
    private String whom;
    private String date;
    private byte[] photo1;
    private byte[] photo2;
    private byte[] photo3;

    public Bill() {
    }

    public Bill(String billnumber, double amount, double refund, String kind, String whom, String date, byte[] photo1, byte[] photo2, byte[] photo3) {
        this.billnumber = billnumber;
        this.amount = amount;
        this.refund = refund;
        this.kind = kind;
        this.whom = whom;
        this.date = date;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBillnumber() {
        return billnumber;
    }

    public void setBillnumber(String billnumber) {
        this.billnumber = billnumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRefund() {
        return refund;
    }

    public void setRefund(double refund) {
        this.refund = refund;
    }

    public String getKind() {
        return kind.toString();
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getWhom() {
        return whom;
    }

    public void setWhom(String whom) {
        this.whom = whom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte[] getPhoto1() {
        return photo1;
    }

    public void setPhoto1(byte[] photo) {
        this.photo1 = photo;
    }

    public byte[] getPhoto2() {
        return photo2;
    }

    public void setPhoto2(byte[] photo) {
        this.photo2 = photo;
    }

    public byte[] getPhoto3() {
        return photo3;
    }

    public void setPhoto3(byte[] photo) {
        this.photo3 = photo;
    }

    @Override
    public String toString() {
        return billnumber + " | " + amount + "€ | " + refund + "€ | " + kind + " | " + whom + " | " + date;
    }
}
