package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.Bill;
import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.BillDataSource;

public class BillDetailActivity extends AppCompatActivity {

    private static final int REQUEST_ID_CONTACT = 1;
    private static final int REQUEST_IMAGE_CAPTURE_1 = 2;
    private static final int REQUEST_IMAGE_CAPTURE_2 = 3;
    private static final int REQUEST_IMAGE_CAPTURE_3 = 4;

    private BillDataSource dataSource;

    private Bill bill;
    private Button buttonUpdate;
    private EditText etInvoice, etWhom, etAmount, etRefund;
    private TextView tvDate;
    private Spinner sKind;
    private ImageButton ibContact, ibDate;
    private ImageView ivPhoto1, ivPhoto2, ivPhoto3;
    private Bitmap photo1, photo2, photo3;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int yearBill, monthBill, dayBill;

    // Access Uri for Image
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_detail_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.nav_edit);

        dataSource = new BillDataSource(this);
        dataSource.open();

        bill = dataSource.getBill((int) getIntent().getLongExtra(NewBillFragment.DIALOG_BILL, 0));

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
        etWhom = (EditText) findViewById(R.id.et_detail_whom);
        etAmount = (EditText) findViewById(R.id.et_detail_sum);
        etRefund = (EditText) findViewById(R.id.et_detail_refund);
        tvDate = (TextView) findViewById(R.id.tv_detail_date);
        sKind = (Spinner) findViewById(R.id.spinner_detail_kind);
        buttonUpdate = (Button) findViewById(R.id.update_bill);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill.setBillnumber(getBillNumber());
                bill.setWhom(getWhom());
                bill.setAmount(getAmount());
                bill.setRefund(getRefund());
                bill.setDate(getDate());
                bill.setKind(getKind());
                bill.setPhoto1(getPhoto1());
                bill.setPhoto2(getPhoto2());
                bill.setPhoto3(getPhoto3());
                dataSource.updateBill(bill);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.tv_bill_updated), Toast.LENGTH_SHORT).show();
                dataSource.close();
                finish();
            }
        });

        calendar = Calendar.getInstance();
        yearBill = calendar.get(Calendar.YEAR);
        monthBill = calendar.get(Calendar.MONTH) + 1;
        dayBill = calendar.get(Calendar.DAY_OF_MONTH);

        etInvoice.setText(bill.getBillnumber());
        etWhom.setText(bill.getWhom());
        etAmount.setText("" + bill.getAmount());
        etRefund.setText("" + bill.getRefund());
        tvDate.setText(bill.getDate());
        setType();
        if(bill.getPhoto1() != null) {

            photo1 = BitmapFactory.decodeByteArray(bill.getPhoto1(), 0, bill.getPhoto1().length);
            // Picture size
            float width = photo1.getWidth();
            float height = photo1.getHeight();
            // Scale the picture for showing
            int scaleHeight = 150;
            int scaleWidth = (int) (width / height * (float) scaleHeight);
            Bitmap scaleBitmap = photo1.createScaledBitmap(photo1, scaleWidth, scaleHeight, false);
            ivPhoto1.setImageBitmap(scaleBitmap);

            //ivPhoto1.setImageBitmap(photo1);
        }
        if(bill.getPhoto2() != null) {

            photo2 = BitmapFactory.decodeByteArray(bill.getPhoto2(), 0, bill.getPhoto2().length);
            // Picture size
            float width = photo2.getWidth();
            float height = photo2.getHeight();
            // Scale the picture for showing
            int scaleHeight = 150;
            int scaleWidth = (int) (width / height * (float) scaleHeight);
            Bitmap scaleBitmap = photo2.createScaledBitmap(photo2, scaleWidth, scaleHeight, false);
            ivPhoto2.setImageBitmap(scaleBitmap);

            //ivPhoto2.setImageBitmap(photo2);
        }
        if(bill.getPhoto3() != null) {

            photo3 = BitmapFactory.decodeByteArray(bill.getPhoto3(), 0, bill.getPhoto3().length);
            // Picture size
            float width = photo3.getWidth();
            float height = photo3.getHeight();
            // Scale the picture for showing
            int scaleHeight = 150;
            int scaleWidth = (int) (width / height * (float) scaleHeight);
            Bitmap scaleBitmap = photo3.createScaledBitmap(photo3, scaleWidth, scaleHeight, false);
            ivPhoto3.setImageBitmap(scaleBitmap);

            //ivPhoto3.setImageBitmap(photo3);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bill_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_save:
                buttonUpdate.performClick();
                return true;
            case R.id.action_delete:
                dataSource.deleteBill(bill);
                finish();
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
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
                Cursor cursor = getContentResolver().query(uriContact, projection, null, null, null);
                if(cursor.moveToFirst()) {
                    etWhom.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                }
            } else if(requestCode == REQUEST_IMAGE_CAPTURE_1) {
                try {
                    photo1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    // Picture size
                    float width = photo1.getWidth();
                    float height = photo1.getHeight();
                    // Scale the picture for showing
                    int scaleHeight = 150;
                    int scaleWidth = (int) (width / height * (float) scaleHeight);
                    Bitmap scaleBitmap = photo1.createScaledBitmap(photo1, scaleWidth, scaleHeight, false);
                    ivPhoto1.setImageBitmap(scaleBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(requestCode == REQUEST_IMAGE_CAPTURE_2) {
                try {
                    photo2 = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    // Picture size
                    float width = photo2.getWidth();
                    float height = photo2.getHeight();
                    // Scale the picture for showing
                    int scaleHeight = 150;
                    int scaleWidth = (int) (width / height * (float) scaleHeight);
                    Bitmap scaleBitmap = photo2.createScaledBitmap(photo2, scaleWidth, scaleHeight, false);
                    ivPhoto2.setImageBitmap(scaleBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(requestCode == REQUEST_IMAGE_CAPTURE_3) {
                try {
                    photo3 = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    // Picture size
                    float width = photo3.getWidth();
                    float height = photo3.getHeight();
                    // Scale the picture for showing
                    int scaleHeight = 150;
                    int scaleWidth = (int) (width / height * (float) scaleHeight);
                    Bitmap scaleBitmap = photo3.createScaledBitmap(photo3, scaleWidth, scaleHeight, false);
                    ivPhoto3.setImageBitmap(scaleBitmap);
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
     * Set the date for the TextView in format dd.mm.yyyy
     */
    private void setDate() {
        tvDate.setText(dayBill + "." + monthBill + "." + yearBill);
    }

    private void setType() {
        String[] text = getResources().getStringArray(R.array.treatment_kind);
        if (text[0].equals(bill.getKind())) {
            sKind.setSelection(0);
        } else if (text[1].equals(bill.getKind())) {
            sKind.setSelection(1);
        } else {
            sKind.setSelection(2);
        }
    }

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
        return sKind.getSelectedItem().toString();
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

}
