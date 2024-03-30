package lk.payhere.androidsdk;

import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import lk.bhasha.sdk.configs.Constantz;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes7.dex */
public class PayHereBaseActivity extends AppCompatActivity {
    public static boolean DISCONNECTED = false;
    public static long DISCONNECT_TIMEOUT = 300000;
    public static final long DISCONNECT_TIMEOUT_HELAPAY = 600000;
    public static final String TAG = "PayHereBaseActivity";
    private static boolean isDisabled = false;
    private long backgroundTimer = System.currentTimeMillis();
    private boolean isBackground = false;
    private final Handler disconnectHandler = new Handler(new Handler.Callback() { // from class: lk.payhere.androidsdk.PayHereBaseActivity.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            return false;
        }
    });
    private final Runnable disconnectCallback = new Runnable() { // from class: lk.payhere.androidsdk.PayHereBaseActivity.2
        @Override // java.lang.Runnable
        public void run() {
            if (PayHereBaseActivity.this.isBackground) {
                return;
            }
            PayHereBaseActivity.this.terminateSession();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void finishActivity() {
        PHResponse pHResponse = new PHResponse(-7, "session has expired", null);
        Intent intent = new Intent();
        intent.putExtra(PHConstants.INTENT_EXTRA_RESULT, pHResponse);
        setResult(-1, intent);
        finish();
    }

    private void showDisconnectedDialog() {
        String str;
        DISCONNECTED = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Session timeout");
        builder.setMessage("Your session has expired");
        builder.setPositiveButton(Constantz.ALERT_OK_BUTTON_MESSAGE, new DialogInterface.OnClickListener() { // from class: lk.payhere.androidsdk.PayHereBaseActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                PayHereBaseActivity.this.finishActivity();
            }
        });
        try {
            builder.create().show();
        } catch (Exception e) {
            if (e.getMessage() != null) {
                str = e.getMessage();
            } else {
                str = "Error on Session popup";
            }
            Log.d(TAG, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void terminateSession() {
        Log.d(TAG, "Session timer Finished");
        if (isDisabled) {
            return;
        }
        showDisconnectedDialog();
    }

    public void enableTimer() {
        Log.d(TAG, "Session timer enabled");
        isDisabled = false;
        resetDisconnectTimer();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        enableTimer();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.isBackground = true;
        this.backgroundTimer = System.currentTimeMillis();
        stopDisconnectTimer();
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService("keyguard");
        if (keyguardManager != null && keyguardManager.inKeyguardRestrictedInputMode()) {
            terminateSession();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.isBackground = false;
        if (System.currentTimeMillis() - this.backgroundTimer < DISCONNECT_TIMEOUT) {
            resetDisconnectTimer();
        } else {
            terminateSession();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        if (!((PowerManager) getSystemService("power")).isScreenOn()) {
            terminateSession();
        }
    }

    @Override // android.app.Activity
    public void onUserInteraction() {
        resetDisconnectTimer();
    }

    public void resetDisconnectTimer() {
        if (isDisabled) {
            return;
        }
        Log.d(TAG, "Session timer Restarted");
        this.disconnectHandler.removeCallbacks(this.disconnectCallback);
        this.disconnectHandler.postDelayed(this.disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        Log.d(TAG, "Session timer Stopped");
        this.disconnectHandler.removeCallbacks(this.disconnectCallback);
    }
}
