package com.jasmine.intercept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (null != action && action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            final Bundle bundle = intent.getExtras();

            if (bundle != null) {
                Intent j = new Intent(context, MainService.class);
                j.putExtras(bundle);
                context.startService(j);
            }
        }
    }
}
