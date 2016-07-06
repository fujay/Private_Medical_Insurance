package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;


import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        dataSource = new BillDataSource(getActivity());
        dataSource.open();

        List<Bill> values = dataSource.getAllBills();

        ArrayAdapter<Bill> adapter = new ArrayAdapter<Bill>(getActivity(), android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);

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
