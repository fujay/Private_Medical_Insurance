package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.BillDataSource;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private ToolTipView toolTipView = null;
    private ToolTipRelativeLayout toolTipRelativeLayout = null;
    private View rootView = null;
    private TextView tvTreatmentPos, tvTreatmentNeg, tvMedicationsPos, tvMedicationsNeg, tvTotal, tvDifference, tvRecords;
    private BillDataSource dataSource;

    /**
     * Required empty public constructor
     */
    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_fragment, container, false);

        dataSource = new BillDataSource(getActivity());
        dataSource.read();

        tvTreatmentPos = (TextView) rootView.findViewById(R.id.tvTreatmentPos);
        tvTreatmentNeg = (TextView) rootView.findViewById(R.id.tvTreatmentNeg);

        tvMedicationsPos = (TextView) rootView.findViewById(R.id.tvMedicationsPos);
        tvMedicationsNeg = (TextView) rootView.findViewById(R.id.tvMedicationsNeg);
        tvRecords = (TextView) rootView.findViewById(R.id.tv_records);

        tvRecords.setText(getResources().getString(R.string.tv_records, dataSource.getSize()));

        tvTreatmentPos.setText(getString(R.string.tv_treatments, dataSource.getTreatmentPosSize(), '+', dataSource.getTreatmentPositive()));
        tvTreatmentNeg.setText(getString(R.string.tv_treatments, dataSource.getTreatmentNegSize(), '-', dataSource.getTreatmentNegative()));

        tvMedicationsPos.setText(getString(R.string.tv_pieces, dataSource.getMedicationPosSize(), '+', dataSource.getMedicationPositive()));
        tvMedicationsNeg.setText(getString(R.string.tv_pieces, dataSource.getMedicationNegSize(), '-', dataSource.getMedicationNegative()));


        toolTipRelativeLayout = (ToolTipRelativeLayout) rootView.findViewById(R.id.activity_main_tooltipframelayout);
        return rootView;

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.main_fragment, container, false);
    }

    /**
     * Called if the activity get visible again and the user starts interacting with the activity again
     * Reopen the database connection
     */
    @Override
    public void onResume() {
        dataSource.read();
        super.onResume();
    }

    /**
     * Called once another activity gets into the foreground. Always called before the activity is not visible anymore. Used to release resources or save application data
     * Close the database connection
     */
    @Override
    public void onPause() {
        dataSource.close();
        super.onPause();
    }

    private void showToolTipView() {

        /*
            if(toolTipView == null) {
                showToolTipView();
            } else {
                toolTipView.remove();
                toolTipView = null;
            }
         */

        ToolTip toolTip = new ToolTip()
                .withText("A beautiful Text")
                .withColor(getResources().getColor(R.color.holoToolTipOrange))
                .withShadow();
        //toolTipView = toolTipRelativeLayout.showToolTipForView(toolTip, rootView.findViewById(R.id.tv1));
    }

}
