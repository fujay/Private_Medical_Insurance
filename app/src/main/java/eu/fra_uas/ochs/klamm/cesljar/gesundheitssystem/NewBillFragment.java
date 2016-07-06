package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.BillDataSource;

public class NewBillFragment extends Fragment implements CheckBillDialogFragment.DialogListener {

    public static final int REQUEST_ID_CONTACT = 6;
    public static final int REQUEST_IMAGE_CAPTURE_1 = 7;
    public static final int REQUEST_IMAGE_CAPTURE_2 = 8;
    public static final int REQUEST_IMAGE_CAPTURE_3 = 9;

    public static final String DIALOG_BILL = "bill";
    public static final String DIALOG_WHOM = "whom";
    public static final String DIALOG_AMOUNT = "amount";
    public static final String DIALOG_REFUND = "refund";
    public static final String DIALOG_DATE = "date";
    public static final String DIALOG_TYPE = "type";
    public static final String DIALOG_PHOTO1 = "photo1";
    public static final String DIALOG_PHOTO2 = "photo2";
    public static final String DIALOG_PHOTO3 = "photo3";

    private View rootView;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private TextView tvDate;
    private EditText etInvoice, etAmount, etRefund, etWhom;
    private ImageButton ibContact, ibDate;
    private ImageView ivBlob1, ivBlob2, ivBlob3;
    private Bitmap photo1, photo2, photo3;
    private Button buttonAdd;
    private int yearBill, monthBill, dayBill;
    private Spinner spinnerType;
    private BillDataSource dataSource;

    // Access Uri for Image
    private Uri imageUri;

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
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

        ivBlob1 = (ImageView) rootView.findViewById(R.id.iv_blob1);
        ivBlob1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "TITLE1");
                values.put(MediaStore.Images.Media.DESCRIPTION, "DESCRIPTION1");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_1);
            }
        });
        ivBlob2 = (ImageView) rootView.findViewById(R.id.iv_blob2);
        ivBlob2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "TITLE2");
                values.put(MediaStore.Images.Media.DESCRIPTION, "DESCRIPTION2");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_2);
            }
        });
        ivBlob3 = (ImageView) rootView.findViewById(R.id.iv_blob3);
        ivBlob3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "TITLE3");
                values.put(MediaStore.Images.Media.DESCRIPTION, "DESCRIPTION3");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_3);
            }
        });

        etInvoice = (EditText) rootView.findViewById(R.id.et_invoice);
        etAmount = (EditText) rootView.findViewById(R.id.et_sum);
        etRefund = (EditText) rootView.findViewById(R.id.et_refund);
        etWhom = (EditText) rootView.findViewById(R.id.et_whom_txt);
        tvDate = (TextView) rootView.findViewById(R.id.tv_date);
        spinnerType = (Spinner) rootView.findViewById(R.id.spinner_kind);
        buttonAdd = (Button) rootView.findViewById(R.id.add_bill);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getRefund() > getAmount()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.tv_bill_amount_refund), Toast.LENGTH_SHORT).show();
                    return;
                }
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
                        dataSource.createBill(getBillNumber(), getAmount(), getRefund(), getKind(), getWhom(), getDate(), getPhoto1(), getPhoto2(), getPhoto3());
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
            } else if(requestCode == REQUEST_IMAGE_CAPTURE_1) {
                try {
                    photo1 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                    // Picture size
                    float width = photo1.getWidth();
                    float height = photo1.getHeight();
                    // Scale the picture for showing
                    int scaleHeight = 150;
                    int scaleWidth = (int) (width / height * (float) scaleHeight);
                    Bitmap scaleBitmap = photo1.createScaledBitmap(photo1, scaleWidth, scaleHeight, false);
                    ivBlob1.setImageBitmap(scaleBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(requestCode == REQUEST_IMAGE_CAPTURE_2) {
                try {
                    photo2 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                    // Picture size
                    float width = photo2.getWidth();
                    float height = photo2.getHeight();
                    // Scale the picture for showing
                    int scaleHeight = 150;
                    int scaleWidth = (int) (width / height * (float) scaleHeight);
                    Bitmap scaleBitmap = photo2.createScaledBitmap(photo2, scaleWidth, scaleHeight, false);
                    ivBlob2.setImageBitmap(scaleBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(requestCode == REQUEST_IMAGE_CAPTURE_3) {
                try {
                    photo3 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                    // Picture size
                    float width = photo3.getWidth();
                    float height = photo3.getHeight();
                    // Scale the picture for showing
                    int scaleHeight = 150;
                    int scaleWidth = (int) (width / height * (float) scaleHeight);
                    Bitmap scaleBitmap = photo3.createScaledBitmap(photo3, scaleWidth, scaleHeight, false);
                    ivBlob3.setImageBitmap(scaleBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
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

    private byte[] getPhoto1() {
        if (photo1 != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo1.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            return null;
        }
    }

    private byte[] getPhoto2() {
        if (photo2 != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            return null;
        }
    }

    private byte[] getPhoto3() {
        if (photo3 != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo3.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            return null;
        }
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
        dataSource.createBill(getBillNumber(), getAmount(), getRefund(), getKind(), getWhom(), getDate(), getPhoto1(), getPhoto2(), getPhoto3());
        Toast.makeText(getActivity(), getResources().getString(R.string.tv_bill_created), Toast.LENGTH_SHORT).show();
    }
}
