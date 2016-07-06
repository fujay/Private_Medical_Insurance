package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class InputActivity extends Activity {

    public static final String RETVAL_KEY = InputActivity.class.getSimpleName();

    private Button buttonOK;
    private EditText editTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_layout);

        String textInfo = null;
        Intent intent = getIntent();
        if(intent.getStringExtra(PMIFragment.TAG) != null) {
            textInfo = intent.getStringExtra(PMIFragment.TAG);
        } else if(intent.getStringExtra(SettingsActivity.TAG) != null) {
            textInfo = intent.getStringExtra(SettingsActivity.TAG);
        }
        setTitle(textInfo);

        editTextInput = (EditText) findViewById(R.id.et_input);
        editTextInput.setHint(textInfo);


        if(textInfo.equals(getResources().getString(R.string.tv_insured_tel))) {
            editTextInput.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if(textInfo.equals(getResources().getString(R.string.tv_insured_mail))) {
            editTextInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        } else if(textInfo.equals(getResources().getString(R.string.tv_insured_name))) {
            editTextInput.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if(textInfo.equals(getResources().getString(R.string.txt_settings_password))) {
            editTextInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        buttonOK = (Button) findViewById(R.id.bOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultData = new Intent();
                resultData.putExtra(RETVAL_KEY, editTextInput.getText().toString());
                setResult(Activity.RESULT_OK, resultData);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent resultData = new Intent();
        resultData.putExtra(RETVAL_KEY, editTextInput.getText().toString());
        setResult(Activity.RESULT_OK, resultData);
        super.onBackPressed();
    }
}
