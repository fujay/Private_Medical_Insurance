package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

public class BillContentProvider extends ContentProvider {

    // Used for the UriMatcher
    private static final int BILL_ID = 22;
    private static final int BILL = 11;

    private static final String AUTHORITY = "eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database";
    private static final String BASE_PATH = "gesundheitssystem";

    private static final UriMatcher URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URIMatcher.addURI(AUTHORITY, BASE_PATH, BILL);
        URIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", BILL_ID);
    }

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BASE_PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH;

    // Database
    private PrivateMedicalInsuranceDatabase database;

    @Override
    public boolean onCreate() {
        database = new PrivateMedicalInsuranceDatabase(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // check if the caller has requested a column which does not exists
        checkColumns(projection);
        // Set the table
        queryBuilder.setTables(BillTbl.TABLE_NAME);
        int uriType = URIMatcher.match(uri);
        switch (uriType) {
            case BILL:
                break;
            case BILL_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(BillColumns.ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // Make sure that potential listener are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = URIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case BILL:
                id = sqlDB.insert(BillTbl.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unkown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = URIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case BILL:
                rowsDeleted = sqlDB.delete(BillTbl.TABLE_NAME, selection, selectionArgs);
                break;
            case BILL_ID:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(BillTbl.TABLE_NAME, BillColumns.ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(BillTbl.TABLE_NAME, BillColumns.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = URIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case BILL:
                rowsUpdated = sqlDB.update(BillTbl.TABLE_NAME, values, selection, selectionArgs);
                break;
            case BILL_ID:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(BillTbl.TABLE_NAME, values, BillColumns.ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(BillTbl.TABLE_NAME, values, BillColumns.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    /**
     * Validate that a query only requests valid columns
     * @param projection
     */
    private void checkColumns(String[] projection) {
        if(projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(BillTbl.ALL_COLUMNS));
            // Check if all columns which are requested are available
            if(!availableColumns.contains(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
