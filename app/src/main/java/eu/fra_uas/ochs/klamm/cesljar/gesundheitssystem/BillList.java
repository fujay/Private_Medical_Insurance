package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.Bill;
import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.BillDataSource;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillList extends Fragment {

    private View rootView;
    private BillDataSource dataSource;
    private ListView listView;
    private Spinner spinner;


    /**
     * Required empty public constructor
     */
    public BillList() {

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

        listView = (ListView) rootView.findViewById(R.id.listview);
        spinner = (Spinner) rootView.findViewById(R.id.s_sorting);

        dataSource = new BillDataSource(getActivity());
        dataSource.open();

        final List<Bill> values = dataSource.getAllBills();

        final ArrayAdapter<Bill> adapter = new ArrayAdapter<Bill>(getActivity(), android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bill bill = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), BillDetailActivity.class);
                intent.putExtra(NewBillFragment.DIALOG_BILL, bill.getId());
                //intent.putExtra(NewBillFragment.DIALOG_BILL, bill.getBillnumber());
                //intent.putExtra(NewBillFragment.DIALOG_WHOM, bill.getWhom());
                //intent.putExtra(NewBillFragment.DIALOG_AMOUNT, bill.getAmount());
                //intent.putExtra(NewBillFragment.DIALOG_REFUND, bill.getRefund());
                //intent.putExtra(NewBillFragment.DIALOG_DATE, bill.getDate());
                //intent.putExtra(NewBillFragment.DIALOG_TYPE, bill.getKind());
                //intent.putExtra(NewBillFragment.DIALOG_PHOTO1, bill.getPhoto1());
                //intent.putExtra(NewBillFragment.DIALOG_PHOTO2, bill.getPhoto2());
                //intent.putExtra(NewBillFragment.DIALOG_PHOTO3, bill.getPhoto3());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Bill bill = adapter.getItem(position);
                dataSource.deleteBill(bill);
                adapter.remove(bill);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Default
                        break;
                    case 1: // Bill number
                        break;
                    case 2: // Date
                        //Arrays.sort(values.toArray());
                        //adapter.notifyDataSetChanged();
                        break;
                    default: // No existing Spinner value
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        dataSource.close();
        super.onPause();
    }
}
