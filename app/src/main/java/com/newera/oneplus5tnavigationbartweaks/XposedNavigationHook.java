package com.newera.oneplus5tnavigationbartweaks;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.XModuleResources;
import android.os.Environment;

import com.newera.oneplus5tnavigationbartweaks.utils.Constants;

import java.io.File;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;

public class XposedNavigationHook implements IXposedHookZygoteInit, IXposedHookInitPackageResources {
    private static String MODULE_PATH = null;

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        if (!resparam.packageName.equals("com.android.systemui"))
            return;
        log("In SystemUI.apk");
        XSharedPreferences xSharedPreferences = getPreferences();
        XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);
        log(xSharedPreferences.getFile().getAbsolutePath());
        int index = xSharedPreferences.getInt("index", -1);
        log("Index is " + index);
        if (index > 0) {
            resparam.res.setReplacement("com.android.systemui", "drawable", "ic_sysbar_home2", modRes.fwd(xSharedPreferences.getInt("home", xSharedPreferences.getInt("home", R.drawable.class.getDeclaredField("n" + index + "__home").getInt(null)))));
            resparam.res.setReplacement("com.android.systemui", "drawable", "ic_sysbar_back_ime2", modRes.fwd(xSharedPreferences.getInt("back",xSharedPreferences.getInt("back", R.drawable.class.getDeclaredField("n" + index + "__back").getInt(null)))));
            resparam.res.setReplacement("com.android.systemui", "drawable", "ic_sysbar_back2", modRes.fwd(xSharedPreferences.getInt("back", xSharedPreferences.getInt("back",R.drawable.class.getDeclaredField("n" + index + "__back").getInt(null)))));
            resparam.res.setReplacement("com.android.systemui", "drawable", "ic_sysbar_recent2", modRes.fwd(xSharedPreferences.getInt("recent", xSharedPreferences.getInt("recent",R.drawable.class.getDeclaredField("n" + index + "__recent").getInt(null)))));
            resparam.res.setReplacement("com.android.systemui", "drawable", "ic_sysbar_menu2", modRes.fwd(xSharedPreferences.getInt("menu", xSharedPreferences.getInt("menu",R.drawable.class.getDeclaredField("n" + index + "__menu").getInt(null)))));
        } else {
            log("Skipping Resources Replacement For now.");
        }
    }

    @SuppressLint("SetWorldReadable")
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }

    private XSharedPreferences getPreferences() {
        File dest = new File(Environment.getExternalStorageDirectory(), Constants.SHARED_SETTINGS_FILE);
        return new XSharedPreferences(dest);
    }

    private void log(String log) {
        XposedBridge.log("AmniX " + log);
    }
}