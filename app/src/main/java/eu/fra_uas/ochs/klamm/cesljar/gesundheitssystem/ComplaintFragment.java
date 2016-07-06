package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.ListMenuItemView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.Bill;
import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.BillDataSource;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComplaintFragment extends Fragment {

    private View rootView;
    private BillDataSource dataSource;
    private ListView listView;

    public ComplaintFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.complaint_fragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_invoice);

        dataSource = new BillDataSource(getActivity());
        dataSource.open();

        List<Bill> values = dataSource.getAllBills();

        final ArrayAdapter<Bill> adapter = new ArrayAdapter<Bill>(getActivity(), android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), adapter.getItem(position).getBillnumber() + " / " + dataSource.getSize(), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.help_fragment, container, false);
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
