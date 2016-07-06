package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database;

public final class BillTbl implements BillColumns {

    /**
     * Name of the database table
     */
    public static final String TABLE_NAME = "bills";

    /**
     * SQL Statement for schema definition
     */
    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    BillColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BillColumns.BILL + " TEXT NOT NULL, " +
                    BillColumns.AMOUNT + " REAL, " +
                    BillColumns.REFUND + " REAL, " +
                    BillColumns.KIND + " TEXT, " +
                    BillColumns.WHOM + " TEXT, " +
                    BillColumns.DATE + " TEXT " +
                    //BillColumns.TIMESTAMP + " INTEGER " +
                    ");";

    /**
     * Sorted by descending date
     */
    public static final String DEFAULT_SORT_ORDER =
            BillColumns.DATE.toUpperCase() + " DESC";

    /**
     * SQL statement for deleting the table
     */
    public static final String SQL_DROP =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    /**
     * SQL statement for creating a bill with billnumber
     */
    public static final String STMT_BILL_INSERT =
            "INSERT INTO " + TABLE_NAME + " " +
                    "(" + BillColumns.BILL + ")" +
                    "VALUES (?)";

    /**
     * SQL statement for deleting all bills
     */
    public static final String STMT_BILL_DELETE = "DELETE " + TABLE_NAME;

    /**
     * SQL statement for deleting a bill by its id
     */
    public static final String STMT_BILL_DELETE_BY_ID =
            "DELETE " + TABLE_NAME + " WHERE " + BillColumns.ID + " = ?";

    public static final String WHERE_ID_EQUALS =
            BillColumns.ID + "= ?";

    /**
     * List all attributes
     */
    public static final String[] ALL_COLUMNS = new String[] {
            BillColumns.ID,
            BillColumns.BILL,
            BillColumns.AMOUNT,
            BillColumns.REFUND,
            BillColumns.KIND,
            BillColumns.WHOM,
            BillColumns.DATE,
            //BillColumns.TIMESTAMP
    };

    /**
     * Private final class, contains only constants
     */
    private BillTbl() {

    }

}
