package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.Bill;
import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.BillDataSource;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComplaintFragment extends Fragment {

    public static final int REQUEST_ID_CONTACT = 7;

    private View rootView;
    private BillDataSource dataSource;
    private ListView listView;
    private EditText etTO, etMessage;
    private Spinner spinner_complaint;
    private ImageButton ibContact;
    private Button bSend;
    private String subject;
    private Uri uriContact;
    private String name;
    private Bill bill;
    private String contactID;

    public ComplaintFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.complaint_fragment, container, false);

        listView = (ListView) rootView.findViewById(R.id.list_invoice);
        spinner_complaint = (Spinner) rootView.findViewById(R.id.spinner_subject);
        spinner_complaint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etTO = (EditText) rootView.findViewById(R.id.et_to);
        etMessage = (EditText) rootView.findViewById(R.id.et_information);
        ibContact = (ImageButton) rootView.findViewById(R.id.ib_contact);
        ibContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactIntent, REQUEST_ID_CONTACT);
            }
        });
        bSend = (Button) rootView.findViewById(R.id.b_send_complaint);
        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, getEmailTo());
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getSubject());
                emailIntent.putExtra(Intent.EXTRA_TEXT, getText());
                //emailIntent.putExtra(Intent.EXTRA_STREAM, getAttachment());
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, getAttachment());
                startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.msg_chooseEmail)));
            }
        });

        dataSource = new BillDataSource(getActivity());
        dataSource.open();

        List<Bill> values = dataSource.getAllBills();

        final ArrayAdapter<Bill> adapter = new ArrayAdapter<Bill>(getActivity(), android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bill = ((Bill) parent.getItemAtPosition(position));
            }
        });

        return rootView;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.help_fragment, container, false);
    }

    @Override
    public void onResume() {
        dataSource.read();
        super.onResume();
    }

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
            switch (requestCode) {
                case REQUEST_ID_CONTACT:
                    uriContact = data.getData();
                    retrieveContactID();
                    retrieveContactName();
                    retrieveContactEmail();
                    break;
            }
        }
    }

    /**
     *
     */
    private void retrieveContactID() {
        Cursor cursorID = getContext().getContentResolver().query(uriContact, new String[]{ContactsContract.Contacts._ID}, null, null, null);

        if(cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();
    }

    /**
     *
     */
    private void retrieveContactName() {
        // querying contact data store
        Cursor cursor = getContext().getContentResolver().query(uriContact, null, null, null, null);

        if(cursor.moveToFirst()) {
            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER = An indicator of whether this contact has at least one phone number.
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
    }

    /**
     *
     */
    private void retrieveContactEmail() {
        Cursor cursorEmail = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactID, null, null);
        if(cursorEmail.moveToFirst()) {
            String phone = cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            etTO.setText(phone);
        } else {
            etTO.setText("");
        }
        cursorEmail.close();
    }

    private String[] getEmailTo() {
        if (checkInvalidDomain() && checkForgottenProvider() && checkAllowedSing()) {
            return new String[] {etTO.getText().toString()};
        }
        return new String[] {""};
    }

    private String getSubject() {
        if (bill != null) {
            return subject + ": " + bill.getBillnumber() + " " + getResources().getString(R.string.txt_from_date) + " " + bill.getDate();
        }
        return subject;
    }

    private String getText() {
        if (bill != null) {
            return getResources().getString(R.string.txt_mail_greetings) + "\n\n" + etMessage.getText().toString() + "\n\n" + getResources().getString(R.string.tv_short_billnumber) + bill.getBillnumber() + "\n" + getResources().getString(R.string.tv_amount) + bill.getAmount() + "\n" + getResources().getString(R.string.tv_refund) + bill.getRefund() + "\n" + getResources().getString(R.string.tv_kind) + bill.getKind() + "\n" + getResources().getString(R.string.tv_whom) + bill.getWhom() + "\n" + getResources().getString(R.string.tv_date) + bill.getDate() + "\n\n" + getResources().getString(R.string.txt_greetings);
        }
        return getResources().getString(R.string.txt_mail_greetings) + "\n\n" + etMessage.getText().toString() + "\n\n" + getResources().getString(R.string.txt_greetings);
    }

    private ArrayList<Uri> getAttachment() {
        ArrayList<Uri> uris = new ArrayList<Uri>();
        if (bill != null) {
            try {

                /*Bitmap bitmap = BitmapFactory.decodeByteArray(bill.getPhoto1(), 0, bill.getPhoto1().length);
                File file = new File(getContext().getCacheDir(), bill.getBillnumber() +"1" + ".png");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                file.setReadable(true, false);
                uris.add(Uri.fromFile(file));
                */

                Bitmap bmp = BitmapFactory.decodeByteArray(bill.getPhoto1(), 0, bill.getPhoto1().length);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + bill.getBillnumber() + "1.jpg");
                file.createNewFile();

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(byteArrayOutputStream.toByteArray());
                fileOutputStream.close();

                String imagePath = Environment.getExternalStorageDirectory() + File.separator + bill.getBillnumber() + "1.jpg";
                File imageFile = new File(imagePath);
                uris.add(Uri.fromFile(imageFile));

            } catch (Exception e) {
                e.printStackTrace();
            }


        /*    try {
                File tempDir = Environment.getExternalStorageDirectory();
                tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
                tempDir.mkdir();

                Bitmap bmp = BitmapFactory.decodeByteArray(bill.getPhoto1(), 0, bill.getPhoto1().length);
                File tempFile = File.createTempFile("Title", ".jpeg", tempDir);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] bitmapData = byteArrayOutputStream.toByteArray();

                // Write the bytes in temporary file
                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                fileOutputStream.write(bitmapData);
                fileOutputStream.flush();
                fileOutputStream.close();

                uris.add(Uri.fromFile(tempFile));
            } catch (Exception e) {
                e.printStackTrace();
            }*/



        /*ArrayList<Uri> uris = new ArrayList<Uri>();
        if (bill != null) {
            if (bill.getPhoto1() != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bill.getPhoto1(), 0, bill.getPhoto1().length);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bmp, "Title", null);
                uris.add(Uri.parse(path));
            }
        }*/
        }
        return uris;
    }

    private boolean checkInvalidDomain() {
        String toProvider = etTO.getText().toString().substring(etTO.getText().toString().indexOf("@") + 1);
        if (toProvider.indexOf(".") == (-1)) {
            Toast.makeText(getActivity(), R.string.txt_missing_separator, Toast.LENGTH_SHORT).show();
            return false;
        }
        String toTLD = toProvider.substring(toProvider.indexOf(".") + 1);
        if (toTLD.length() < 2) {
            Toast.makeText(getActivity(), R.string.txt_wrong_provider, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Casting the input char to an Integer to get its ASCII value. In the
     * following if - statement specified 4 ranges in which the char is allowed
     * to be. 45 and 46 are '-' and '.' . Between 48 and 57 are the numbers. 65
     * till 90 are the capital letters (64 is included for the '@' sign. 95 is
     * the '_'. Finally, between 97 and 122 you can find the small letters.
     *
     * @param sign
     *            Character that should be checked for allowed signs
     * @return true if the char sign is allowed or false if not.
     */
    private boolean isThisSignAllowed(char sign) {
        int ascii = (int) sign;

        if (((ascii >= 45 && ascii <= 46) || (ascii >= 48 && ascii <= 57)
                || (ascii == 95) || (ascii >= 64 && ascii <= 90) || (ascii >= 97 && ascii <= 122))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkAllowedSing() {
        for (char sign : etTO.getText().toString().toCharArray()) {
            if (isThisSignAllowed(sign) == false) {
                Toast.makeText(getActivity(), getResources().getString(R.string.txt_wrong_sign, sign), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the provider has been forgotten in the FROM and TO field by
     * means of missing '@'. Even checks whether the provider has been forgotten
     * in the fields CC and BC when the fields are not empty.
     * @return true if the EditText with the Email address contains a Provider with '@'. false if there is no ‘@‘ char.
     */
    private boolean checkForgottenProvider() {
        if(etTO.getText().toString().indexOf("@") == (-1)) {
            Toast.makeText(getActivity(), R.string.txt_forgotten_provider, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}
