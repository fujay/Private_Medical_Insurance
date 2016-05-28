package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private ToolTipView toolTipView = null;
    private ToolTipRelativeLayout toolTipRelativeLayout = null;
    private View rootView = null;
    private TextView tvMedicationsPos, tvMedicationsNeg;

    /**
     * Required empty public constructor
     */
    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_fragment, container, false);

        tvMedicationsPos = (TextView) rootView.findViewById(R.id.tvTreatmentPos);
        tvMedicationsNeg = (TextView) rootView.findViewById(R.id.tvMedicationsNeg);

        //tvMedicationsPos.setText(getString(R.string.tv_treatments, 100,"1000,00€"));
        //tvMedicationsNeg.setText(getString(R.string.tv_pieces, 2,"200"));

        toolTipRelativeLayout = (ToolTipRelativeLayout) rootView.findViewById(R.id.activity_main_tooltipframelayout);
        return rootView;

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.main_fragment, container, false);
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
