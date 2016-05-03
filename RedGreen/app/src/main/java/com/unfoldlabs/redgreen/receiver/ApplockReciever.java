package com.unfoldlabs.redgreen.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.unfoldlabs.redgreen.applock.lock.ShowApplockViewOtherApps;

/**
 * Created by Shareefa on 03-03-2016.
 */
public class ApplockReciever extends BroadcastReceiver{
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        sendIntenttoServiece();

    }
    private void sendIntenttoServiece(){
        Intent intent = new Intent(context, ShowApplockViewOtherApps.class);
        intent.putExtra("LOCK_FROM", "fromService");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
