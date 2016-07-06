package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.R;

public class ListActivity extends android.app.ListActivity {

    private BillDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_list_layout);

        dataSource = new BillDataSource(this);
        dataSource.open();

        List<Bill> values = dataSource.getAllBills();

        ArrayAdapter<Bill> adapter = new ArrayAdapter<Bill>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    public void onClick(View view) {
        ArrayAdapter<Bill> adapter = (ArrayAdapter<Bill>) getListAdapter();
        Bill bill = null;
        switch (view.getId()) {
            case R.id.add:
                //bill = dataSource.createBill("111", 123.2, 100.2, Bill.Kind.treatment.toString(), "Dr Who", Calendar.getInstance().toString());
                adapter.add(bill);
                break;
            case R.id.delete:
                if(getListAdapter().getCount() > 0) {
                    bill = (Bill) getListAdapter().getItem(0);
                    dataSource.deleteBill(bill);
                    adapter.remove(bill);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

}
