package com.aixuan.switchflightmode;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by aixuan on 2017/9/14.
 */

public class AirplaneHelper {
    private static AirplaneHelper instance;
    public final String ACTION_OPEN_AIRPLANE = "com.ax.action.OPEN_AIRPLANE";
    public final String ACTION_CLOSE_AIRPLANE = "com.ax.action.CLOSE_AIRPLANE";


    public static AirplaneHelper getInstance(){
        if (instance == null){
            instance = new AirplaneHelper();
        }
        return instance;
    }

    public void airplaneHelperInit(XC_LoadPackage.LoadPackageParam loadPackageParam){
        if (!"com.android.settings".equals(loadPackageParam.packageName))
            return;
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int value = 0;
                switch (intent.getAction()){
                    case ACTION_OPEN_AIRPLANE:
                        value = 1;
                        break;
                    case ACTION_CLOSE_AIRPLANE:
                        value = 0;
                        break;
                }
                switchAirplaneMode(context, value);
            }
        };
        XposedHelpers.findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Context context = (Context) param.thisObject;
                IntentFilter openFilter = new IntentFilter();
                openFilter.addAction(ACTION_OPEN_AIRPLANE);
                openFilter.addAction(ACTION_CLOSE_AIRPLANE);
                openFilter.setPriority(Integer.MAX_VALUE);
                context.registerReceiver(receiver, openFilter);

                /*IntentFilter closeFilter = new IntentFilter();
                closeFilter.addAction(ACTION_CLOSE_AIRPLANE);
                closeFilter.setPriority(Integer.MAX_VALUE);
                context.registerReceiver(receiver, closeFilter);*/
            }
        });
    }

    public Intent getOpenIntent(){
        return new Intent(ACTION_OPEN_AIRPLANE);
    }

    public Intent getCloseIntent(){
        return new Intent(ACTION_CLOSE_AIRPLANE);
    }

    private void switchAirplaneMode(Context context, int value){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //if less than verson 4.2
            Settings.System.putInt(
                    context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON,value);
        } else {
            Settings.Global.putInt(
                    context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON,value);
        }
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        context.sendBroadcast(intent);
    }

}
