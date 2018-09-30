package trialchain.ibm.trialchain_patient.messaging;

import android.app.ActivityManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.app.Notification;
import android.app.NotificationManager;
import android.util.Log;

import java.util.List;

import trialchain.ibm.trialchain_patient.MessageReplyActivity;

/**
 * Created by ishankhaparde on 25/04/17.
 */

public class MessageBroadcastRecevier extends BroadcastReceiver {
    /**
     * This method is called when the BroadcastReceiver is receiving an Intent
     * broadcast.  During this time you can use the other methods on
     * BroadcastReceiver to view/modify the current result values.  This method
     * is always called within the main thread of its process, unless you
     * explicitly asked for it to be scheduled on a different thread using
     * {@link Context#registerReceiver(BroadcastReceiver, * IntentFilter, String, Handler)}. When it runs on the main
     * thread you should
     * never perform long-running operations in it (there is a timeout of
     * 10 seconds that the system allows before considering the receiver to
     * be blocked and a candidate to be killed). You cannot launch a popup dialog
     * in your implementation of onReceive().
     * <p>
     * <p><b>If this BroadcastReceiver was launched through a &lt;receiver&gt; tag,
     * then the object is no longer alive after returning from this
     * function.</b>  This means you should not perform any operations that
     * return a result to you asynchronously -- in particular, for interacting
     * with services, you should use
     * {@link Context#startService(Intent)} instead of
     * {@link Context#bindService(Intent, ServiceConnection, int)}.  If you wish
     * to interact with a service that is already running, you can use
     * {@link #peekService}.
     * <p>
     * <p>The Intent filters used in {@link Context#registerReceiver}
     * and in application manifests are <em>not</em> guaranteed to be exclusive. They
     * are hints to the operating system about how to find suitable recipients. It is
     * possible for senders to force delivery to specific recipients, bypassing filter
     * resolution.  For this reason, {@link #onReceive(Context, Intent) onReceive()}
     * implementations should respond only to known actions, ignoring any unexpected
     * Intents that they may receive.
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {

        if(!isAppOnForeground(context, "trialchain.ibm.trialchain_patient.messaging"))
        {
            //Build and trigger a push notification to notify the user for incoming message.
            Notification.Builder messageNotification = new Notification.Builder(context).setContentTitle("New Message Recevied").setContentText(intent.getStringExtra("Message")).
                    setSmallIcon(android.support.design.R.drawable.notification_template_icon_bg).setDefaults(Notification.DEFAULT_ALL);

            Intent authIntent = new Intent(context,AuthorisationActivity.class);
            authIntent.putExtra("Message",intent.getStringExtra("Message"));

            Intent authoriseIntent = new Intent(context,AuthoriseService.class);

            PendingIntent pendingAuthIntent = PendingIntent.getActivity(context,0,authIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent authPendingIntent = PendingIntent.getService(context,0,authoriseIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            messageNotification.setContentIntent(pendingAuthIntent);
            messageNotification.addAction(android.R.drawable.ic_menu_send,"Authorise",authPendingIntent);


            NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.notify(1,messageNotification.build());
        }
        else
        {
            Intent redirectIntent = new Intent(context, MessageReplyActivity.class);
            context.startActivity(redirectIntent);
        }



        Log.d("Message In recevier:",intent.getStringExtra("Message"));
    }

    private boolean isAppOnForeground(Context context,String appPackageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = appPackageName;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {

                return true;
            }
        }
        return false;
    }
}
