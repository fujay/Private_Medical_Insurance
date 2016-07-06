package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;

import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.ListActivity;
import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.database.PrivateMedicalInsuranceDatabase;
import eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem.network.SocketActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_ID = 2510;

    private static final int DISCLAIMER_DIALOG = 1;
    private static final String DISCLAIMER_AGREEMENT = "DISCLAIMER";

    private Fragment fragment;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private SharedPreferences settings;
    private String deviceUniqueID;

    private ToolTipView toolTipView = null;
    private ToolTipRelativeLayout toolTipRelativeLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getPreferences(MODE_PRIVATE);

        // Initially fragment (frontend)
        if(savedInstanceState == null) {
            fragment = new MainFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsActivity.KEY_SECURITY, false)) {
                Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivityForResult(intent, REQUEST_ID);
            }
        }

        // ToolTips
        toolTipRelativeLayout = (ToolTipRelativeLayout) findViewById(R.id.activity_main_tooltipframelayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.snackbar, Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(!settings.getBoolean(DISCLAIMER_AGREEMENT, false)) {
            showDialog(DISCLAIMER_DIALOG);
        }

        switch (PreferenceManager.getDefaultSharedPreferences(this).getString(SettingsActivity.KEY_LOGIN, "")) {
            case "androidid":
                deviceUniqueID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                break;
            case "imei":
                TelephonyManager telephonyManager = (TelephonyManager) getBaseContext().getSystemService(TELEPHONY_SERVICE);
                deviceUniqueID = telephonyManager.getDeviceId();
                break;
            case "google":
                AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
                Account[] list = manager.getAccounts();

                for(Account account: list)
                {
                    if(account.type.equalsIgnoreCase("com.google"))
                    {
                        deviceUniqueID = account.name;
                        break;
                    }
                }
                break;
            case "namepw":
                break;
        }
        LinearLayout headerViewLayout = (LinearLayout) navigationView.getHeaderView(0);
        TextView tvUniqueID = (TextView) headerViewLayout.findViewById(R.id.textViewID);
        tvUniqueID.setText(deviceUniqueID);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_help) {
            //startActivity(new Intent(this, HelpActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            setTitle(R.string.nav_home);
            floatingActionButton.show();
            fragment = new MainFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_bill) {
            setTitle(R.string.nav_bill);
            floatingActionButton.hide();
            fragment = new NewBillFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_list) {
            //startActivity(new Intent(this, ListActivity.class));
            setTitle(R.string.nav_list);
            floatingActionButton.hide();
            fragment = new BillList();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_camera) {
            setTitle(R.string.nav_camera);
            floatingActionButton.hide();
        } else if (id == R.id.nav_gallery) {
            setTitle(R.string.nav_gallery);
            floatingActionButton.show();
        } else if (id == R.id.nav_complaint) {
            setTitle(R.string.nav_complaint);
            floatingActionButton.hide();
            fragment = new ComplaintFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_pmi) {
            setTitle(R.string.nav_pmi);
            floatingActionButton.hide();
            fragment = new PMIFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_send) {
            setTitle(R.string.nav_send);
            floatingActionButton.hide();
        } else if (id == R.id.nav_sync) {
            setTitle(R.string.nav_sync);
            floatingActionButton.show();
            synchronizeData();
        } else if (id == R.id.nav_info) {
            setTitle(R.string.nav_info);
            floatingActionButton.hide();
            fragment = new HelpFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
        //toggle.onConfigurationChanged(new Configuration());
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        //toolbar.setTitle(title);
    }

    /**
     *
     * @param requestCode The request code you passed to startActivityForResult()
     * @param resultCode A result code specified by the second activity
     * @param data An Intent that carries the result data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_ID) {
                Boolean bool = data.getBooleanExtra(SigninActivity.RETVAL_KEY, true);
                if(!bool) {
                    finish();
                }
            }
        } else {
            finish();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DISCLAIMER_DIALOG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.disclaimer_title);
                builder.setMessage(R.string.disclaimer);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean(DISCLAIMER_AGREEMENT, Boolean.TRUE);
                        editor.commit();
                    }
                });
                builder.setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                return builder.create();
            default:
                return super.onCreateDialog(id);
        }
    }

    private void synchronizeData() {
        startActivity(new Intent(this, SocketActivity.class));
        int dataSets = 50;
        int inkrement = 1;
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.synchronize));
        progressDialog.setMessage(getResources().getString(R.string.synchronize_data));
        //progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setIcon(R.drawable.ic_menu_sync);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(dataSets);
        progressDialog.show();

        progressDialog.setProgress(inkrement);
    }
}
