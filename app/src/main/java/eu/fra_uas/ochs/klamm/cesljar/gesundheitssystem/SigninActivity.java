package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SigninActivity  extends Activity {

    private Button bLogin, bCancel;
    private EditText etName, etPassword;
    private TextView tvAttempts;
    private int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);

        bLogin = (Button) findViewById(R.id.b_login);
        bCancel = (Button) findViewById(R.id.b_cancel);
        etName = (EditText) findViewById(R.id.et_login_name);
        etPassword = (EditText) findViewById(R.id.et_password);
        tvAttempts = (TextView) findViewById(R.id.tv_attempts);
        tvAttempts.setText(String.valueOf(counter));

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().equals("") && etPassword.getText().toString().equals("")) { // Login correct
                    Toast.makeText(getApplicationContext(), R.string.login_correct, Toast.LENGTH_SHORT).show();
                } else { // Login fail
                    Toast.makeText(getApplicationContext(), R.string.login_fail, Toast.LENGTH_SHORT).show();
                    counter--;
                    if (counter <= 0) {
                        finish();
                    }
                    tvAttempts.setText(String.valueOf(counter));
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
}
