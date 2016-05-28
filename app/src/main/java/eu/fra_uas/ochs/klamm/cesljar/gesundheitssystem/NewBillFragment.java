package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;


public class NewBillFragment extends Fragment {

    private View rootView;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private TextView tvDate;
    private EditText etInvoice, etAmount;
    private ImageButton ibDate;
    private int yearBill, monthBill, dayBill;

    /**
     * Required empty public constructor
     */
    public NewBillFragment() {
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.new_bill_fragment, container, false);

        ibDate = (ImageButton) rootView.findViewById(R.id.ib_date);
        ibDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, yearBill, monthBill - 1, dayBill);
                datePickerDialog.show();
            }
        });

        etInvoice = (EditText) rootView.findViewById(R.id.et_invoice);
        etAmount = (EditText) rootView.findViewById(R.id.et_sum);
        tvDate = (TextView) rootView.findViewById(R.id.tv_date);
        calendar = Calendar.getInstance();
        yearBill = calendar.get(Calendar.YEAR);
        monthBill = calendar.get(Calendar.MONTH) + 1;
        dayBill = calendar.get(Calendar.DAY_OF_MONTH);

        setDate();

        return rootView;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.help_fragment, container, false);
    }

    /**
     * The callback used to indicate the user is done filling in the date
     */
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        /**
         * How the parent is notified that the date is set
         * @param view The context the dialog is to run in
         * @param year The date year
         * @param monthOfYear The date month
         * @param dayOfMonth The date day of month
         */
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            yearBill = year;
            monthBill = monthOfYear + 1;
            dayBill = dayOfMonth;
            setDate();
        }
    };

    /**
     * Set the date for the TextView in format dd.mm.yyyy
     */
    private void setDate() {
        tvDate.setText(dayBill + "." + monthBill + "." + yearBill);
    }
}
