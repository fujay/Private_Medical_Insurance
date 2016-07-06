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
     * Mandatory field. From whom the service was provided
     */
    String WHOM = "whom";

    /**
     * Mandatory field. DATE := Date of creation
     */
    String DATE = "date";

    /**
     * 1. Photo
     * <br>BLOB
     */
    String PHOTO1 = "firstphoto";

    /**
     * 2. Photo
     * <br>BLOB
     */
    String PHOTO2 = "secondphoto";

    /**
     * 3. Photo
     * <br>BLOB
     */
    String PHOTO3 = "thirdphoto";

}
