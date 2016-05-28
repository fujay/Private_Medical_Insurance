package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite Databases are not thread-safe. Therefore, the database manager is implemented as a singleton
 */
public class PrivateMedicalInsuranceDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PrivateMedicalInsurance.db";
    private static final int DATABASE_VERSION = 1;

    private static PrivateMedicalInsuranceDatabase sInstance;
    private static Object sLock = "";

    /**
     * The database can be managed only by the knowledge of their application. Therefore, the context of the application is passed
     * @param context Context of the calling application
     * @return A copy of the database, which may be used in the application
     */
    public static PrivateMedicalInsuranceDatabase getInstance(Context context) {
        if(sInstance == null) {
            synchronized (sLock) {
                if(sInstance == null && context != null) {
                    sInstance = new PrivateMedicalInsuranceDatabase(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * The constructor can only be called by {@link #getInstance(Context context)} to prevent multiple copies in an application.
     * @param context Context of the calling application
     */
    private PrivateMedicalInsuranceDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BillTbl.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BillTbl.SQL_DROP);
        onCreate(db);
    }

}
