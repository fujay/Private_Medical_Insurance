package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;


import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.BillColumns;
import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.BillTbl;
import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.PrivateMedicalInsuranceDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillList extends ListFragment {

    private static final String[] DB_EXAMINED_COLUMNS = new String[] {
            BillColumns.ID,
            BillColumns.BILL,
            BillColumns.AMOUNT,
            BillColumns.REFUND,
            BillColumns.KIND,
            BillColumns.DATE
    };

    private static final String[] DISPLAY_ROW = new String[] {
            BillColumns.BILL,
            BillColumns.AMOUNT,
            /*BillColumns.REFUND,
            BillColumns.KIND,
            BillColumns.DATE*/
    };

    private View rootView;
    private PrivateMedicalInsuranceDatabase PMIDatabase;

    /**
     * Required empty public constructor
     */
    public BillList() {
        PMIDatabase = PrivateMedicalInsuranceDatabase.getInstance(getActivity());
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bill_list_fragment, container, false);

        //PMIDatabase = PrivateMedicalInsuranceDatabase.getInstance(getActivity());

        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void showBills() {
        Cursor bills = PMIDatabase.getReadableDatabase().query(BillTbl.TABLE_NAME, DB_EXAMINED_COLUMNS, null, null, BillColumns.DATE, null, null);

        int[] widgetKey = new int[] {
                android.R.id.text1,
                android.R.id.text2
        };

        SimpleCursorAdapter billAdapter = new SimpleCursorAdapter(this.getActivity(), android.R.layout.simple_list_item_2, bills, DISPLAY_ROW, widgetKey);
        setListAdapter(billAdapter);

        //startManagingCursor(bills);
    }

}
