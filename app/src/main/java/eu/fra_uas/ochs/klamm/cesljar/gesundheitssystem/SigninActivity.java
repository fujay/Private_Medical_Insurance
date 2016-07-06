package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;


public class SigninActivity extends Activity {

    public static final String TAG = SigninActivity.class.getSimpleName();
    public static final String RETVAL_KEY = SigninActivity.class.getSimpleName();

    private Button bLogin, bCancel;
    private EditText etPassword;
    private TextView tvAttempts;
    private SharedPreferences settings;
    private String password;
    private int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);

        settings = getSharedPreferences(MainActivity.TAG, MODE_PRIVATE);
        try {
            password = AESCrypt.decrypt(MainActivity.TAG, settings.getString(SettingsActivity.KEY_SECURITY_PW, ""));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        bLogin = (Button) findViewById(R.id.b_login);
        bCancel = (Button) findViewById(R.id.b_cancel);
        etPassword = (EditText) findViewById(R.id.et_password);
        tvAttempts = (TextView) findViewById(R.id.tv_attempts);
        tvAttempts.setText(String.valueOf(counter));

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassword.getText().toString().equals(password)) { // Login correct
                    Toast.makeText(getApplicationContext(), R.string.login_correct, Toast.LENGTH_SHORT).show();
                    Intent resultData = new Intent();
                    resultData.putExtra(RETVAL_KEY, true);
                    setResult(Activity.RESULT_OK, resultData);
                    finish();
                } else { // Login fail
                    Toast.makeText(getApplicationContext(), R.string.login_fail, Toast.LENGTH_SHORT).show();
                    counter--;
                    if (counter <= 0) {
                        Intent resultData = new Intent();
                        resultData.putExtra(RETVAL_KEY, false);
                        setResult(Activity.RESULT_CANCELED, resultData);
                        finish();
                    }
                    tvAttempts.setText(String.valueOf(counter));
                    etPassword.setText("");
                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * * Overriden backbutton from android. Prevent for exiting the Activity
     */
    @Override
    public void onBackPressed() {
    }
}
