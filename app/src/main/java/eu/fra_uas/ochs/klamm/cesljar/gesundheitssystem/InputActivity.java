package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
        setDialogTitle();

        editTextInput = (EditText) findViewById(R.id.et_input);
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
        //editTextInput.setHint(PMIFragment.REQUEST_ID_NUMBER);

    }

    private void setDialogTitle() {

    }
}
