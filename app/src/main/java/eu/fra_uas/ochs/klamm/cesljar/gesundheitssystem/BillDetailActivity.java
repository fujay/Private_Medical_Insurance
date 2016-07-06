package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.BillDataSource;

public class BillDetailActivity extends AppCompatActivity {

    private static final int REQUEST_ID_CONTACT = 1;
    private static final int REQUEST_IMAGE_CAPTURE_1 = 2;
    private static final int REQUEST_IMAGE_CAPTURE_2 = 3;
    private static final int REQUEST_IMAGE_CAPTURE_3 = 4;

    private BillDataSource dataSource;

    private Button buttonUpdate;
    private EditText etInvoice, etWhom, etAmount, etRefund;
    private TextView tvDate;
    private Spinner sKind;
    private ImageButton ibContact, ibDate;
    private ImageView ivPhoto1, ivPhoto2, ivPhoto3;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int yearBill, monthBill, dayBill;

    // Access Uri for Image
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_detail_fragment);

        dataSource = new BillDataSource(this);
        dataSource.open();

        getIntent().getLongExtra(NewBillFragment.DIALOG_BILL, 0);

        ibDate = (ImageButton) findViewById(R.id.ib_detail_date);
        ibDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(getApplicationContext(), dateSetListener, yearBill, monthBill - 1, dayBill);
                datePickerDialog.show();
            }
        });
        ibContact = (ImageButton) findViewById(R.id.ib_detail_whom);
        ibContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_ID_CONTACT);
            }
        });

        ivPhoto1 = (ImageView) findViewById(R.id.iv_detail_blob1);
        ivPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "TITLE1");
                values.put(MediaStore.Images.Media.DESCRIPTION, "DESCRIPTION1");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_1);
            }
        });
        ivPhoto2 = (ImageView) findViewById(R.id.iv_detail_blob2);
        ivPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "TITLE2");
                values.put(MediaStore.Images.Media.DESCRIPTION, "DESCRIPTION2");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_2);
            }
        });
        ivPhoto3 = (ImageView) findViewById(R.id.iv_detail_blob3);
        ivPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "TITLE3");
                values.put(MediaStore.Images.Media.DESCRIPTION, "DESCRIPTION3");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_3);
            }
        });


        etInvoice = (EditText) findViewById(R.id.et_detail_invoice);
        etWhom= (EditText) findViewById(R.id.et_detail_whom);
        etAmount = (EditText) findViewById(R.id.et_detail_sum);
        etRefund = (EditText) findViewById(R.id.et_detail_refund);

        calendar = Calendar.getInstance();
        yearBill = calendar.get(Calendar.YEAR);
        monthBill = calendar.get(Calendar.MONTH) + 1;
        dayBill = calendar.get(Calendar.DAY_OF_MONTH);

        setDate();


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
