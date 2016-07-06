package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.BillDataSource;

public class NewBillFragment extends Fragment implements CheckBillDialogFragment.DialogListener {

    public static final int REQUEST_ID_CONTACT = 6;

    public static final String DIALOG_BILL = "bill";
    public static final String DIALOG_WHOM = "whom";
    public static final String DIALOG_AMOUNT = "amount";
    public static final String DIALOG_REFUND = "refund";
    public static final String DIALOG_DATE = "date";
    public static final String DIALOG_TYPE = "type";

    private View rootView;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private TextView tvDate;
    private EditText etInvoice, etAmount, etRefund, etWhom;
    private ImageButton ibContact, ibDate;
    private Button buttonAdd;
    private int yearBill, monthBill, dayBill;
    private Spinner spinnerType;
    private BillDataSource dataSource;

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

        dataSource = new BillDataSource(getActivity());
        dataSource.open();

        ibDate = (ImageButton) rootView.findViewById(R.id.ib_date);
        ibDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, yearBill, monthBill - 1, dayBill);
                datePickerDialog.show();
            }
        });
        ibContact = (ImageButton) rootView.findViewById(R.id.ib_whom);
        ibContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_ID_CONTACT);
            }
        });

        etInvoice = (EditText) rootView.findViewById(R.id.et_invoice);
        etAmount = (EditText) rootView.findViewById(R.id.et_sum);
        etRefund = (EditText) rootView.findViewById(R.id.et_refund);
        etWhom = (EditText) rootView.findViewById(R.id.et_whom);
        tvDate = (TextView) rootView.findViewById(R.id.tv_date);
        spinnerType = (Spinner) rootView.findViewById(R.id.spinner_kind);
        buttonAdd = (Button) rootView.findViewById(R.id.add_bill);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etInvoice.length() < 1) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.tv_bill_missing), Toast.LENGTH_SHORT).show();
                } else {
                    if(PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(SettingsActivity.KEY_SUMMARY, true)) {
                        Bundle args = new Bundle();
                        args.putString(DIALOG_BILL, getBillNumber());
                        args.putString(DIALOG_WHOM, getWhom());
                        args.putDouble(DIALOG_AMOUNT, getAmount());
                        args.putDouble(DIALOG_REFUND, getRefund());
                        args.putString(DIALOG_DATE, getDate());
                        args.putString(DIALOG_TYPE, getKind());
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        CheckBillDialogFragment checkBillDialogFragment = new CheckBillDialogFragment();
                        checkBillDialogFragment.setTargetFragment(NewBillFragment.this, 7);
                        checkBillDialogFragment.setArguments(args);
                        checkBillDialogFragment.show(fragmentTransaction, "dialog");
                    } else {
                        dataSource.createBill(getBillNumber(), getAmount(), getRefund(), getKind(), getWhom(), getDate());
                        Toast.makeText(getActivity(), getResources().getString(R.string.tv_bill_created), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
     * Called if the activity get visible again and the user starts interacting with the activity again
     * Reopen the database connection
     */
    @Override
    public void onResume() {
        dataSource.open();
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

    /**
     *
     * @param requestCode The request code you passed to startActivityForResult()
     * @param resultCode A result code specified by the second activity
     * @param data An Intent that carries the result data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_ID_CONTACT) {
                String[] projection = {ContactsContract.Contacts.DISPLAY_NAME};
                Uri uriContact = data.getData();
                Cursor cursor = getContext().getContentResolver().query(uriContact, projection, null, null, null);
                if(cursor.moveToFirst()) {
                    etWhom.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                }
            }
        }
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
     * Determined the string of the document number (Invoice) and return it
     * @return String with the document number
     */
    private String getBillNumber() {
        return etInvoice.getText().toString();
    }

    /**
     * Determined the value of the amount and return it, if the value is not a digit, it will return 0 as value
     * @return double amount
     */
    private double getAmount() {
        double value = 0;
        if (Pattern.matches("[a-zA-Z]+", etAmount.getText().toString()) == false && etAmount.getText().toString().length() > 1) {
            value = Double.parseDouble(etAmount.getText().toString());
        }
        return value;
    }

    /**
     * Determined the value of the refund and return it, if the value is not a digit, it will return 0 as value
     * @return double refund
     */
    private double getRefund() {
        double value = 0;
        if (Pattern.matches("[a-zA-Z]+", etRefund.getText().toString()) == false && etRefund.getText().toString().length() > 1) {
            value = Double.parseDouble(etRefund.getText().toString());
        }
        return value;
    }

    /**
     * Determined from the spinner the selected item and return it as String
     * @return String with the kind of the type from bill
     */
    private String getKind() {
        return spinnerType.getSelectedItem().toString();
    }

    /**
     * Uses the EditText content and convert it in a string
     * @return String from whom was the cost created
     */
    private String getWhom() {
        return etWhom.getText().toString();
    }

    /**
     * Creates with the choosen date ({@link #yearBill}, {@link #monthBill}, {@link #dayBill}) a {@link SimpleDateFormat} with the format dd.MM.yyyy
     * @return String with choosen date in format dd.MM.yyyy
     */
    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        calendar.set(yearBill, monthBill - 1, dayBill);
        return sdf.format(calendar.getTime());
    }

    /**
     * Set the date for the TextView in format dd.mm.yyyy
     */
    private void setDate() {
        tvDate.setText(dayBill + "." + monthBill + "." + yearBill);
    }

    @Override
    public void onFinishDialog() {
        dataSource.createBill(getBillNumber(), getAmount(), getRefund(), getKind(), getWhom(), getDate());
        Toast.makeText(getActivity(), getResources().getString(R.string.tv_bill_created), Toast.LENGTH_SHORT).show();
    }
}
