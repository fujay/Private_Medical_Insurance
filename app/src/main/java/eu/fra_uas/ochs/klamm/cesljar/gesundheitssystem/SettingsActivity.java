package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.encryption.AdvancedEncryptionStandard;

/**
 * Preferences - Android allows to save and retrieve persistent key-value pairs of primitive data type
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = SettingsActivity.class.getSimpleName();

    private static final int DELETE_DIALOG = 1;
    private static final int SAVE_SETTINGS_ID = Menu.FIRST;
    private static final int REQUEST_SECURITY = 7;
    private static final String KEY_LOGIN = "login";
    private static final String KEY_SECURITY = "security";
    private static final String KEY_SECURITY_PW = "pw";
    private static final String KEY_IP = "ip";
    private static final String KEY_PORT = "port";
    private static final String KEY_DELETE = "delete";

    private SharedPreferences settings;
    private SharedPreferences set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.preference_settings);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.registerOnSharedPreferenceChangeListener(this);
        set = getSharedPreferences(MainActivity.TAG, MODE_PRIVATE);

        for(int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
            initSummary(getPreferenceScreen().getPreference(i));
        }

        //ActionBar actionBar = getActionBar();
        //this.getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        settings.registerOnSharedPreferenceChangeListener(this);
        //getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        settings.unregisterOnSharedPreferenceChangeListener(this);
        //getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case SAVE_SETTINGS_ID:
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, SAVE_SETTINGS_ID, Menu.NONE, R.string.action_save);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateReferences(findPreference(key));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DELETE_DIALOG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.txt_settings_delete));
                builder.setMessage(getResources().getString(R.string.txt_settings_sum_delete));
                builder.setCancelable(true);
                builder.setNegativeButton(getResources().getString(R.string.txt_settings_delete_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton(getResources().getString(R.string.txt_settings_delete_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteContent();
                    }
                });
                return builder.create();
            default:
                return super.onCreateDialog(id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences.Editor editor = set.edit();
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_SECURITY) {
                String retValue = data.getStringExtra(InputActivity.RETVAL_KEY); Log.d("TEST", "onActivity: " + retValue);
                AdvancedEncryptionStandard advancedEncryptionStandard = new AdvancedEncryptionStandard(retValue);
                retValue = advancedEncryptionStandard.encode();
                Log.d("TEST", "onActivity-ENC: " + retValue);
                Log.d("TEST", "onActivity-DEC: " + advancedEncryptionStandard.decode());
                editor.putString(KEY_SECURITY_PW, retValue);
                editor.commit();
            }
        } else {
            editor.putString(KEY_SECURITY_PW, "");
            editor.commit();
        }
    }

    private void initSummary(Preference preference) {
        if(preference instanceof PreferenceCategory) {
            PreferenceCategory category = (PreferenceCategory) preference;
            for(int i = 0; i < category.getPreferenceCount(); i++) {
                initSummary(category.getPreference(i));
            }
        } else {
            updateReferences(preference);
        }
    }

    private void updateReferences(Preference preference) {
        switch (preference.getKey()) {
            case KEY_LOGIN:
                ListPreference listPreference = (ListPreference) preference;
                preference.setSummary(listPreference.getEntry());
                break;
            case KEY_IP:
                EditTextPreference editTextPreferenceIP = (EditTextPreference) preference;
                preference.setSummary(editTextPreferenceIP.getText());
                break;
            case KEY_PORT:
                EditTextPreference editTextPreferencePORT = (EditTextPreference) preference;
                preference.setSummary(editTextPreferencePORT.getText());
                break;
            case KEY_SECURITY:
                Log.d("TEST", "CASE: " + set.getString(KEY_SECURITY_PW,"LEER"));
                if((((CheckBoxPreference) preference).isChecked() == true) && set.getString(KEY_SECURITY_PW, "").length() == 0) {
                    Log.d("TEST", "IF-PW: " + set.getString(KEY_SECURITY_PW,"LEER"));
                    Intent intent = new Intent(this, InputActivity.class);
                    intent.putExtra(TAG, getResources().getString(R.string.txt_settings_password));
                    startActivityForResult(intent, REQUEST_SECURITY);
                } else if(((CheckBoxPreference) preference).isChecked() == false) {
                    SharedPreferences.Editor editor = set.edit();
                    editor.putString(KEY_SECURITY_PW, "");
                    editor.commit();
                }
                break;
            case KEY_DELETE:
                if(((CheckBoxPreference) preference).isChecked()) {
                    showDialog(DELETE_DIALOG);
                    ((CheckBoxPreference) preference).setChecked(false);
                }
                break;
            default:
                break;
        }
    }

    private void deleteContent() {

    }

}
