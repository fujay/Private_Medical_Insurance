package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Responsible for creating the database.
 * SQLite Databases are not thread-safe. Therefore, the database manager is implemented as a singleton
 */
public class PrivateMedicalInsuranceDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PrivateMedicalInsurance.db";
    private static final int DATABASE_VERSION = 1;

    //private static PrivateMedicalInsuranceDatabase sInstance;
    //private static Object sLock = "";

    /**
     * The database can be managed only by the knowledge of their application. Therefore, the context of the application is passed
     * @param context Context of the calling application
     * @return A copy of the database, which may be used in the application
     */
    /*public static PrivateMedicalInsuranceDatabase getInstance(Context context) {
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
    public PrivateMedicalInsuranceDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Is called by the framework, if the database is accessed but not yet created.
     * @param db representation of the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BillTbl.SQL_CREATE);
    }

    /**
     * Called, if the database version is increased in application code. This method allows to update an existing database schema or to drop the existing database and recreate it via the {@link #onCreate} method.
     * @param db representation of the database
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BillTbl.SQL_DROP);
        onCreate(db);
    }

}
