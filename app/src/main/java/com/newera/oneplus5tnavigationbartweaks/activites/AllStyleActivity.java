package com.newera.oneplus5tnavigationbartweaks.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.newera.oneplus5tnavigationbartweaks.R;
import com.newera.oneplus5tnavigationbartweaks.adapters.StyleAdapter;
import com.newera.oneplus5tnavigationbartweaks.listener.OnFileCopyListener;
import com.newera.oneplus5tnavigationbartweaks.listener.OnRecyclerViewClickListener;
import com.newera.oneplus5tnavigationbartweaks.provider.StyleProvider;
import com.newera.oneplus5tnavigationbartweaks.tasks.FileCopyTask;
import com.newera.oneplus5tnavigationbartweaks.utils.Constants;

import java.io.File;

public class AllStyleActivity extends AppCompatActivity implements OnRecyclerViewClickListener, OnFileCopyListener {
    private StyleProvider styleProvider;
    private SharedPreferences sharedPreferences;
    private ProgressDialog.Builder progressDialogBuilder;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_style);
        progressDialogBuilder = new ProgressDialog.Builder(this).setMessage("Working. Please Wait.").setCancelable(false);
        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        styleProvider = new StyleProvider(getResources());
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        StyleAdapter styleAdapter = new StyleAdapter(styleProvider, sharedPreferences.getInt("index", -1),getNavigationBarHeight(), this);
        ((RecyclerView) findViewById(R.id.recyclerView)).setAdapter(styleAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 440);
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onRecyclerViewItemClick(int position, Object... objects) {
        StyleProvider.Icons icons = styleProvider.getIconByIndex(position);
        sharedPreferences.edit().putInt("home", icons.home).commit();
        sharedPreferences.edit().putInt("back", icons.back).commit();
        sharedPreferences.edit().putInt("recent", icons.recent).commit();
        sharedPreferences.edit().putInt("down", icons.down).commit();
        sharedPreferences.edit().putInt("search", icons.search).commit();
        sharedPreferences.edit().putInt("index", position).commit();
        checkPermission();
        File prefsDir = new File(getApplicationInfo().dataDir, "shared_prefs");
        File source = new File(prefsDir, getPackageName() + ".xml");
        File dest = new File(Environment.getExternalStorageDirectory(), Constants.SHARED_SETTINGS_FILE);
        progressDialog = progressDialogBuilder.show();
        new FileCopyTask(source, dest, this).execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermission();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        if (styleProvider != null)
            styleProvider.release();
    }

    @Override
    public void onSuccess() {
        progressDialog.dismiss();
        new AlertDialog.Builder(this).setMessage("New Settings Has been Applied. Choose Your Option Now!").setPositiveButton("Reboot", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot"});
                    proc.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton("Cancel", null).setNeutralButton("Hot Reboot", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "killall zygote"});
                    proc.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).show();
    }

    @Override
    public void onFailed() {
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Unable to Save new settings. Please Try Again", Toast.LENGTH_LONG).show();
        checkPermission();
    }

    private int getNavigationBarHeight(){
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
