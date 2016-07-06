package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database;

public class Bill {

    private long id;
    private String billnumber;
    private double amount;
    private double refund;
    private String kind;
    private String whom;
    private String date;

    public Bill() {
    }

    public Bill(String billnumber, double amount, double refund, String kind, String whom, String date) {
        this.billnumber = billnumber;
        this.amount = amount;
        this.refund = refund;
        this.kind = kind;
        this.whom = whom;
        this.date = date;
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

    @Override
    public String toString() {
        return billnumber + " " + amount + "€ " + refund + "€ " + kind + " " + whom + " " + date;
    }
}
