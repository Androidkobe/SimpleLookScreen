package com.eagle.locker.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.eagle.locker.R;
import com.eagle.locker.util.ViewUtils;
import com.eagle.locker.widget.TouchToUnLockView;
import com.xdandroid.hellodaemon.IntentWrapper;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

/**
 * Start
 * <p/>
 * User:Rocky(email:1247106107@qq.com)
 * Created by Rocky on 2017/09/17  16:49
 * PACKAGE_NAME com.eagle.locker.activity
 * PROJECT_NAME LockerScreen
 * TODO:
 * Description:
 * <p/>
 * Done
 */
public class LockerActivity extends AppCompatActivity {


    private TouchToUnLockView mUnlockView;

    private Calendar calendar = GregorianCalendar.getInstance();
    private SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMM d", Locale.getDefault());

    private Random random = new Random();

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLockerWindow(getWindow());
        registerLockerReceiver();
        setContentView(R.layout.activity_locker);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterLockerReceiver();
    }

    private void initView() {

        mUnlockView = ViewUtils.get(this, R.id.tulv_UnlockView);

        mUnlockView.setOnTouchToUnlockListener(new TouchToUnLockView.OnTouchToUnlockListener() {
            @Override
            public void onTouchLockArea() {

            }

            @Override
            public void onSlidePercent(float percent) {

            }

            @Override
            public void onSlideToUnlock() {
                finish();
            }

            @Override
            public void onSlideAbort() {

            }
        });

    }

    public void onBackPressed() {
        IntentWrapper.onBackPressed(this);
    }


    protected UIChangingReceiver mUIChangingReceiver;

    public void registerLockerReceiver() {
        if (mUIChangingReceiver != null) {
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        mUIChangingReceiver = new UIChangingReceiver();
        registerReceiver(mUIChangingReceiver, filter);
    }

    public void unregisterLockerReceiver() {
        if (mUIChangingReceiver == null) {
            return;
        }
        unregisterReceiver(mUIChangingReceiver);
        mUIChangingReceiver = null;
    }


    private class UIChangingReceiver extends BroadcastReceiver {

        public UIChangingReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mUnlockView.startAnim();
    }


    @Override
    public void onPause() {
        super.onPause();
        mUnlockView.stopAnim();
    }

    public static void startActivity(Context context) {
        Intent screenIntent = getIntent(context);
        context.startActivity(screenIntent);
    }


    @NonNull
    private static Intent getIntent(Context context) {
        Intent screenIntent = new Intent();
        screenIntent.setClass(context, LockerActivity.class);
        screenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        screenIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        screenIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        return screenIntent;
    }


    private void setLockerWindow(Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        if (Build.VERSION.SDK_INT > 18) {
            lp.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        }
        window.setAttributes(lp);
        window.getDecorView().setSystemUiVisibility(0x0);

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }
}
