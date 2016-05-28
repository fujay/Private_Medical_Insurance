package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database;

/**
 * Columns for table 'Bill'
 */
public interface BillColumns {

    /**
     * Primary-Key
     */
    String ID = "_id";

    /**
     * Mandatory field. BILL := Invoice number
     * <br>TEXT
     */
    String BILL = "billnumber";

    /**
     *
     * Mandatory field. AMOUNT := Sum of treatment
     * <br>REAL
     */
    String AMOUNT = "amount";

    /**
     * Mandatory field. REFUND := Refunded amount
     * <br>REAL
     */
    String REFUND = "refund";

    /**
     * Mandatory field. KIND := Type of costs, either medication or treatment
     */
    String KIND = "kind";

    /**
     * Mandatory field. DATE := Date of creation
     */
    String DATE = "date";

    /**
     * Date of last update
     * <br>Integer
     */
    String TIMESTAMP = "timestamp";

}
