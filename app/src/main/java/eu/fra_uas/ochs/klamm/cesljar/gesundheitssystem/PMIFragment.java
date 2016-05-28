package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class PMIFragment extends Fragment implements View.OnLongClickListener, View.OnClickListener {

    public static final String TAG = PMIFragment.class.getSimpleName();

    public static final int REQUEST_ID_NAME = 11;
    public static final int REQUEST_ID_NUMBER = 22;
    public static final int REQUEST_ID_MAIL = 33;
    public static final int REQUEST_ID_TEL = 44;
    public static final int REQUEST_ID_FAX = 55;
    public static final int REQUEST_ID_CONTACT = 66;
    public static final int REQUEST_ID_IMAGE = 77;
    public static final int REQUEST_ID_PICK = 88;
    public static final int REQUEST_ID_ALLOWANCE = 99;
    public static final int REQUEST_ID_DEDUCTIBLE = 111;


    private View rootView;
    private ImageView ivInsuredLogo;
    private ImageButton ibAddContact;
    private CheckBox cbAllowance;
    private EditText etDeductible;
    private TextView tvInsuredName, tvInsuredNumber, tvInsuredMail, tvInsuredTel, tvInsuredFax, tvInsuredContact;
    private TextView tvInsuredNameFilled, tvInsuredNumberFilled, tvInsuredMailFilled, tvInsuredTelFilled, tvInsuredFaxFilled, tvInsuredContactFilled;
    private SharedPreferences settings;
    private Uri uriContact;
    private String contactID;

    /**
     * Required empty public constructor
     */
    public PMIFragment() {
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
        rootView = inflater.inflate(R.layout.pmi_fragment, container, false);

        settings = getActivity().getPreferences(Context.MODE_PRIVATE);

        ibAddContact = (ImageButton) rootView.findViewById(R.id.ibAddContact);
        ibAddContact.setOnClickListener(this);
        ivInsuredLogo = (ImageView) rootView.findViewById(R.id.iv_pmi_logo);
        ivInsuredLogo.setOnClickListener(this);
        cbAllowance = (CheckBox) rootView.findViewById(R.id.cb_insured_allowance);
        cbAllowance.setOnCheckedChangeListener(onCheckedChangeListener);
        etDeductible = (EditText) rootView.findViewById(R.id.et_insured_deductible);
        tvInsuredName = (TextView) rootView.findViewById(R.id.tv_insured_name);
        tvInsuredName.setOnLongClickListener(this);
        tvInsuredNumber = (TextView) rootView.findViewById(R.id.tv_insured_number);
        tvInsuredNumber.setOnLongClickListener(this);
        tvInsuredMail = (TextView) rootView.findViewById(R.id.tv_insured_mail);
        tvInsuredMail.setOnLongClickListener(this);
        tvInsuredTel = (TextView) rootView.findViewById(R.id.tv_insured_tel);
        tvInsuredTel.setOnLongClickListener(this);
        tvInsuredFax = (TextView) rootView.findViewById(R.id.tv_insured_fax);
        tvInsuredFax.setOnLongClickListener(this);
        tvInsuredContact = (TextView) rootView.findViewById(R.id.tv_insured_contact);
        tvInsuredContact.setOnLongClickListener(this);

        tvInsuredNameFilled = (TextView) rootView.findViewById(R.id.tv_insured_name_filled);
        tvInsuredNameFilled.setOnClickListener(this);
        tvInsuredNumberFilled = (TextView) rootView.findViewById(R.id.tv_insured_number_filled);
        tvInsuredNumberFilled.setOnClickListener(this);
        tvInsuredMailFilled = (TextView) rootView.findViewById(R.id.tv_insured_mail_filled);
        tvInsuredMailFilled.setOnClickListener(this);
        tvInsuredTelFilled = (TextView) rootView.findViewById(R.id.tv_insured_tel_filled);
        tvInsuredTelFilled.setOnClickListener(this);
        tvInsuredFaxFilled = (TextView) rootView.findViewById(R.id.tv_insured_fax_filled);
        tvInsuredFaxFilled.setOnClickListener(this);
        tvInsuredContactFilled = (TextView) rootView.findViewById(R.id.tv_insured_contact_filled);
        tvInsuredContactFilled.setOnClickListener(this);

        tvInsuredNameFilled.setText(settings.getString(String.valueOf(REQUEST_ID_NAME), ""));
        tvInsuredNumberFilled.setText(settings.getString(String.valueOf(REQUEST_ID_NUMBER), ""));
        tvInsuredMailFilled.setText(settings.getString(String.valueOf(REQUEST_ID_MAIL), ""));
        tvInsuredTelFilled.setText(settings.getString(String.valueOf(REQUEST_ID_TEL), ""));
        tvInsuredFaxFilled.setText(settings.getString(String.valueOf(REQUEST_ID_FAX), ""));
        tvInsuredContactFilled.setText(settings.getString(String.valueOf(REQUEST_ID_CONTACT), ""));
        cbAllowance.setChecked(settings.getBoolean(String.valueOf(REQUEST_ID_ALLOWANCE), false));
        etDeductible.setEnabled(settings.getBoolean(String.valueOf(REQUEST_ID_DEDUCTIBLE), false));
        restoreContactPhoto(settings.getString(String.valueOf(REQUEST_ID_IMAGE), null));

        return rootView;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.main_fragment, container, false);
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibAddContact:
                Intent intentPickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);//Uri.parse("content://contacts"));
                startActivityForResult(intentPickContact, REQUEST_ID_PICK);
                break;
            case R.id.tv_insured_name_filled:
                Intent intentWebSearch = new Intent(Intent.ACTION_WEB_SEARCH);
                intentWebSearch.putExtra(SearchManager.QUERY, tvInsuredNameFilled.getText().toString());
                startActivity(intentWebSearch);
                break;
            case R.id.tv_insured_number_filled:
                break;
            case R.id.tv_insured_mail_filled:
                Intent intentMail = new Intent(Intent.ACTION_SEND);
                intentMail.putExtra(Intent.EXTRA_EMAIL, new String[]{tvInsuredMailFilled.getText().toString()});
                intentMail.putExtra(Intent.EXTRA_SUBJECT, tvInsuredNumberFilled.getText().toString());
                intentMail.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.txt_mail_greetings));
                intentMail.setType("text/plain");
                //startActivity(intentMail);
                startActivity(Intent.createChooser(intentMail, getResources().getString(R.string.msg_chooseEmail)));
                break;
            case R.id.tv_insured_tel_filled:
                Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvInsuredTelFilled.getText().toString()));
                startActivity(intentCall);
                break;
            case R.id.tv_insured_fax_filled:
                break;
            case R.id.tv_insured_contact_filled:
                Intent intentMailContact = new Intent(Intent.ACTION_SEND);
                intentMailContact.putExtra(Intent.EXTRA_EMAIL, new String[]{tvInsuredMailFilled.getText().toString()});
                intentMailContact.putExtra(Intent.EXTRA_SUBJECT, tvInsuredNumberFilled.getText().toString());
                if(tvInsuredContactFilled != null && tvInsuredContactFilled.length() != 0) {
                    intentMailContact.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.txt_mail_grettings_name, tvInsuredContactFilled.getText().toString()));
                } else {
                    intentMailContact.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.txt_mail_greetings));
                }
                intentMailContact.setType("text/plain");
                startActivity(Intent.createChooser(intentMailContact, getResources().getString(R.string.msg_chooseEmail)));
                break;
        }
    }

    /**
     *
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        Intent intent = new Intent(getActivity(), InputActivity.class);
        switch (v.getId()) {
            case R.id.iv_pmi_logo:
                break;
            case R.id.tv_insured_name:
                intent.putExtra(TAG, getResources().getString(R.string.tv_insured_name));
                startActivityForResult(intent, REQUEST_ID_NAME);
                break;
            case R.id.tv_insured_number:
                intent.putExtra(TAG, getResources().getString(R.string.tv_insured_number));
                startActivityForResult(intent, REQUEST_ID_NUMBER);
                break;
            case R.id.tv_insured_mail:
                intent.putExtra(TAG, getResources().getString(R.string.tv_insured_mail));
                startActivityForResult(intent, REQUEST_ID_MAIL);
                break;
            case R.id.tv_insured_tel:
                intent.putExtra(TAG, getResources().getString(R.string.tv_insured_tel));
                startActivityForResult(intent, REQUEST_ID_TEL);
                break;
            case R.id.tv_insured_fax:
                intent.putExtra(TAG, getResources().getString(R.string.tv_insured_Fax));
                startActivityForResult(intent, REQUEST_ID_FAX);
                break;
            case R.id.tv_insured_contact:
                intent.putExtra(TAG, getResources().getString(R.string.tv_insured_contact));
                startActivityForResult(intent, REQUEST_ID_CONTACT);
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            String retValue;
            SharedPreferences.Editor editor = settings.edit();
            switch (requestCode) {
                case REQUEST_ID_NAME:
                    retValue = data.getStringExtra(InputActivity.RETVAL_KEY);
                    tvInsuredNameFilled.setText(retValue);
                    editor.putString(String.valueOf(REQUEST_ID_NAME), retValue);
                    editor.commit();
                    break;
                case REQUEST_ID_NUMBER:
                    retValue = data.getStringExtra(InputActivity.RETVAL_KEY);
                    tvInsuredNumberFilled.setText(retValue);
                    editor.putString(String.valueOf(REQUEST_ID_NUMBER), retValue);
                    editor.commit();
                    break;
                case REQUEST_ID_MAIL:
                    retValue = data.getStringExtra(InputActivity.RETVAL_KEY);
                    tvInsuredMailFilled.setText(retValue);
                    editor.putString(String.valueOf(REQUEST_ID_MAIL), retValue);
                    editor.commit();
                    break;
                case REQUEST_ID_TEL:
                    retValue = data.getStringExtra(InputActivity.RETVAL_KEY);
                    tvInsuredTelFilled.setText(retValue);
                    editor.putString(String.valueOf(REQUEST_ID_TEL), retValue);
                    editor.commit();
                    break;
                case REQUEST_ID_FAX:
                    retValue = data.getStringExtra(InputActivity.RETVAL_KEY);
                    tvInsuredFaxFilled.setText(retValue);
                    editor.putString(String.valueOf(REQUEST_ID_FAX), retValue);
                    editor.commit();
                    break;
                case REQUEST_ID_CONTACT:
                    retValue = data.getStringExtra(InputActivity.RETVAL_KEY);
                    tvInsuredContactFilled.setText(retValue);
                    editor.putString(String.valueOf(REQUEST_ID_CONTACT), retValue);
                    editor.commit();
                    break;
                case REQUEST_ID_PICK:
                    uriContact = data.getData();

                    retrieveContactName();
                    retrieveContactNumber();
                    retrieveContactPhoto();
                    retrieveContactEmail();

                    /*Uri contactUri = data.getData();
                    String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.Contacts.Data.DATA3};
                    Cursor cursor = getContext().getContentResolver().query(contactUri, projection, null, null, null);
                    cursor.moveToFirst();
                    tvInsuredTelFilled.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    tvInsuredNameFilled.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    tvInsuredFaxFilled.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.Data.DATA3)));
                    */
                    editor.putString(String.valueOf(REQUEST_ID_NAME), tvInsuredNameFilled.getText().toString());
                    editor.putString(String.valueOf(REQUEST_ID_TEL), tvInsuredTelFilled.getText().toString());
                    editor.putString(String.valueOf(REQUEST_ID_MAIL), tvInsuredMailFilled.getText().toString());
                    editor.putString(String.valueOf(REQUEST_ID_IMAGE), contactID);

                    editor.commit();
            }
        }
    }

    private void restoreContactPhoto(String id) {
        if (id != null) /*length() > 1)*/ {
            try {
                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(), ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));
                if (inputStream != null) {
                    Bitmap photo = BitmapFactory.decodeStream(inputStream);
                    if (photo != null) {
                        ivInsuredLogo.setImageBitmap(photo);
                    }
                } else {
                    ivInsuredLogo.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            ivInsuredLogo.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        }
    }

    private void retrieveContactPhoto() {
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(), ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));
            if(inputStream != null) {
                Bitmap photo = BitmapFactory.decodeStream(inputStream);
                if(photo != null) {
                    ivInsuredLogo.setImageBitmap(photo);
                }
            } else {
                ivInsuredLogo.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
            }
            if(inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void retrieveContactNumber() {
        Cursor cursorID = getContext().getContentResolver().query(uriContact, new String[]{ContactsContract.Contacts._ID}, null, null, null);

        if(cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ", new String[]{contactID}, null);

        if(cursorPhone.moveToFirst()) {
            tvInsuredTelFilled.setText(cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

        }
        cursorPhone.close();
    }

    private void retrieveContactName() {
        // querying contact data store
        Cursor cursor = getContext().getContentResolver().query(uriContact, null, null, null, null);

        if(cursor.moveToFirst()) {
            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER = An indicator of whether this contact has at least one phone number.
            tvInsuredNameFilled.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
        }
        cursor.close();
    }

    private void retrieveContactEmail() {
        Cursor cursorEmail = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{contactID}, null);
        if(cursorEmail.moveToFirst()) {
            String phone = cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            tvInsuredMailFilled.setText(phone);
        } else {
            tvInsuredMailFilled.setText("");
        }
        cursorEmail.close();
    }

    private final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            etDeductible.setEnabled(isChecked);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(String.valueOf(REQUEST_ID_ALLOWANCE), isChecked);
            editor.putBoolean(String.valueOf(REQUEST_ID_DEDUCTIBLE), isChecked);
            editor.commit();
            if(isChecked) {
                cbAllowance.setText(getString(R.string.txt_insured_allowance_on));
            } else {
                cbAllowance.setText(getString(R.string.txt_insured_allowance_off));
            }
        }
    };
}
