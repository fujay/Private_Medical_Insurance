package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PMIFragment extends Fragment implements View.OnLongClickListener, View.OnClickListener {

    public static final int REQUEST_ID_NAME = 11;
    public static final int REQUEST_ID_NUMBER = 22;
    public static final int REQUEST_ID_MAIL = 33;
    public static final int REQUEST_ID_TEL = 44;
    public static final int REQUEST_ID_FAX = 55;
    public static final int REQUEST_ID_CONTACT = 66;

    private View rootView;
    private TextView tvInsuredName, tvInsuredNumber, tvInsuredMail, tvInsuredTel, tvInsuredFax, tvInsuredContact;
    private TextView tvInsuredNameFilled, tvInsuredNumberFilled, tvInsuredMailFilled, tvInsuredTelFilled, tvInsuredFaxFilled, tvInsuredContactFilled;

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
            case R.id.tv_insured_name_filled:
                break;
            case R.id.tv_insured_number_filled:
                break;
            case R.id.tv_insured_mail_filled:
                break;
            case R.id.tv_insured_tel_filled:
                break;
            case R.id.tv_insured_fax_filled:
                break;
            case R.id.tv_insured_contact_filled:
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
            case R.id.tv_insured_name:
                startActivityForResult(intent, REQUEST_ID_NAME);
                break;
            case R.id.tv_insured_number:
                startActivityForResult(intent, REQUEST_ID_NUMBER);
                break;
            case R.id.tv_insured_mail:
                startActivityForResult(intent, REQUEST_ID_MAIL);
                break;
            case R.id.tv_insured_tel:
                startActivityForResult(intent, REQUEST_ID_TEL);
                break;
            case R.id.tv_insured_fax:
                startActivityForResult(intent, REQUEST_ID_FAX);
                break;
            case R.id.tv_insured_contact:
                startActivityForResult(intent, REQUEST_ID_CONTACT);
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK) {
            String retValue;
            switch (requestCode) {
                case REQUEST_ID_NAME:
                    retValue = data.getStringExtra(InputActivity.RETVAL_KEY);
                    tvInsuredNameFilled.setText(retValue);
                    break;
                case REQUEST_ID_NUMBER:
                    retValue = data.getStringExtra(InputActivity.RETVAL_KEY);
                    tvInsuredNumberFilled.setText(retValue);
                    break;
                case REQUEST_ID_MAIL:
                    retValue = data.getStringExtra(InputActivity.RETVAL_KEY);
                    tvInsuredMailFilled.setText(retValue);
                    break;
                case REQUEST_ID_TEL:
                    retValue = data.getStringExtra(InputActivity.RETVAL_KEY);
                    tvInsuredTelFilled.setText(retValue);
                    break;
                case REQUEST_ID_FAX:
                    retValue = data.getStringExtra(InputActivity.RETVAL_KEY);
                    tvInsuredFaxFilled.setText(retValue);
                    break;
                case REQUEST_ID_CONTACT:
                    retValue = data.getStringExtra(InputActivity.RETVAL_KEY);
                    tvInsuredContactFilled.setText(retValue);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
