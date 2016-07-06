package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.R;

public class BillDataSource {

    private Context context;
    private SQLiteDatabase database;
    private PrivateMedicalInsuranceDatabase dbHelper;

    public BillDataSource(Context context) {
        this.context = context;
        dbHelper = new PrivateMedicalInsuranceDatabase(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void read() {
        database = dbHelper.getReadableDatabase();
    }

    /**
     * Count the datasets of the database and return the size
     * @return the size of the database
     */
    public int getSize() {
        if(database.isOpen()) {
            Cursor cursor = database.query(BillTbl.TABLE_NAME, BillTbl.ALL_COLUMNS, null, null, null, null, null);
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    public double getTreatmentPositive() {
        String kind[] = context.getResources().getStringArray(R.array.treatment_kind);
        double value = 0;
        if(database.isOpen()) {
            Cursor cursor = database.query(BillTbl.TABLE_NAME, BillTbl.ALL_COLUMNS, BillColumns.REFUND + ">0" + " AND " + BillColumns.KIND + "='" + kind[0] + "'", null, null, null, null);
            if(cursor != null) {
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    value += cursor.getDouble(3);
                    cursor.moveToNext();
                }
            }
            return value;
        } else {
            return value;
        }
    }

    public int getTreatmentPosSize() {
        String kind[] = context.getResources().getStringArray(R.array.treatment_kind);
        int size = 0;
        if (database.isOpen()) {
            Cursor cursor = database.query(BillTbl.TABLE_NAME, BillTbl.ALL_COLUMNS, BillColumns.REFUND + ">0" + " AND " + BillColumns.KIND + "='" + kind[0] + "'", null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    size++;
                    cursor.moveToNext();
                }
            }
            return size;
        } else {
            return size;
        }
    }

    public double getTreatmentNegative() {
        String kind[] = context.getResources().getStringArray(R.array.treatment_kind);
        double value = 0;
        if(database.isOpen()) {
            Cursor cursor = database.query(BillTbl.TABLE_NAME, BillTbl.ALL_COLUMNS, BillColumns.AMOUNT + ">0" + " AND " + BillColumns.KIND + "='" + kind[0] + "'", null, null, null, null);
            if(cursor != null) {
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    value += cursor.getDouble(2);
                    cursor.moveToNext();
                }
            }
            return value;
        } else {
            return value;
        }
    }

    public int getTreatmentNegSize() {
        String kind[] = context.getResources().getStringArray(R.array.treatment_kind);
        int size = 0;
        if (database.isOpen()) {
            Cursor cursor = database.query(BillTbl.TABLE_NAME, BillTbl.ALL_COLUMNS, BillColumns.AMOUNT + ">0" + " AND " + BillColumns.KIND + "='" + kind[0] + "'", null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    size++;
                    cursor.moveToNext();
                }
            }
            return size;
        } else {
            return size;
        }
    }

    public Bill getBill(int id) {
        Bill bill = new Bill();
        //Cursor cursor = database.query(BillTbl.TABLE_NAME, BillTbl.ALL_COLUMNS, BillColumns.ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        Cursor cursor = database.query(BillTbl.TABLE_NAME, BillTbl.ALL_COLUMNS, BillColumns.ID + "=" + id, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            bill.setId(cursor.getLong(0));
            bill.setBillnumber(cursor.getString(1));
            bill.setAmount(cursor.getDouble(2));
            bill.setRefund(cursor.getDouble(3));
            bill.setKind(cursor.getString(4));
            bill.setWhom(cursor.getString(5));
            bill.setDate(cursor.getString(6));
            cursor.close();
        }
        return bill;
    }

    public void close() {
        dbHelper.close();
    }

    public Bill createBill(String billnumber, double amount, double refund, String kind, String whom, String date) {
        ContentValues values = new ContentValues();
        values.put(BillColumns.BILL, billnumber);
        values.put(BillColumns.AMOUNT, amount);
        values.put(BillColumns.REFUND, refund);
        values.put(BillColumns.KIND, kind);
        values.put(BillColumns.WHOM, whom);
        values.put(BillColumns.DATE, date);
        long insertId = database.insert(BillTbl.TABLE_NAME, null, values);
        Log.d("TEST", "insertID: " + insertId);

        Cursor cursor = database.query(BillTbl.TABLE_NAME, BillTbl.ALL_COLUMNS, BillColumns.ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.Bill newBill = cursorToBill(cursor);
        cursor.close();
        return newBill;
    }

    public int updateBill(Bill bill) {
        return 0;
    }

    public void deleteBill(Bill bill) {
        long id = bill.getId(); Log.d("TEST", "DELETEid: " + id);
        database.delete(BillTbl.TABLE_NAME, BillColumns.ID + " = " + id, null);
    }

    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<Bill>();

        Cursor cursor = database.query(BillTbl.TABLE_NAME, BillTbl.ALL_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Bill bill = cursorToBill(cursor);
            bills.add(bill);
            cursor.moveToNext();
        }

        cursor.close();
        return bills;
    }

    private Bill cursorToBill(Cursor cursor) {
        Bill bill = new Bill();
        bill.setId(cursor.getLong(0));
        bill.setBillnumber(cursor.getString(1));
        bill.setAmount(cursor.getDouble(2));
        bill.setRefund(cursor.getDouble(3));
        bill.setKind(cursor.getString(4));
        bill.setWhom(cursor.getString(5));
        bill.setDate(cursor.getString(6));

        return bill;
    }

}
