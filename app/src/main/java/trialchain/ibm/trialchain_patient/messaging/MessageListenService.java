package trialchain.ibm.trialchain_patient.messaging;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.JsonParser;

import org.zeromq.ZMQ;

/**
 * Description This is the background service that listens on a publisher for new messages in background.
 * This service runs on a different background thread and broadcasts an intent when it gets a new message.
 * The recevier of the intent triggers a push notification on receipt of the new message.
 * */

public class MessageListenService extends Service {
    ZMQ.Context context;
    ZMQ.Socket subscriber;
    JsonParser jsonParser;
    String session;


    public MessageListenService(String session) {
        this.session = session;
        jsonParser = new JsonParser();
    }

    public MessageListenService() {

        jsonParser = new JsonParser();
    }




    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        context = ZMQ.context(1);
        subscriber = context.socket(ZMQ.SUB);

    }
        @Override
        public int onStartCommand(Intent intent, int flags,int startId)
        {
           // final String listenToMessagesURL = intent.getDataString();
            Log.d("SERVICE","IN ONSTARTCOMMAND");

            //Fork a new thread to listen for messages. We need a new thread because blocking the main thread might result
            //in application not responding error.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        Log.d("IN SERVICE", "THREAD FORK SUCCESSFUL");
                        subscriber.connect("tcp://api.trial-chain.ml:30303");

                        Log.d("IN SERVICE", "CONNECTION TO SERVER SUCCESSFUL");


                        subscriber.subscribe("".getBytes());
                        String result = new String(subscriber.recv(0)); //Message is recevied in 'result' do something with it now.

                        //Prepare to broadcast an intent.
                        Intent broadcastIntent = new Intent();

                        //We set the action of the intent to our custom, defined action.
                        broadcastIntent.setAction(IntentAction.GET_MESSAGES);

                        //Put the message body as a string in the intent.
                        broadcastIntent.putExtra("Message", result);

                        Log.d("MESSAGE IN Service", result);
                        //Send a broadcast to trigger a push notification.
                        sendBroadcast(broadcastIntent);
                    }
                }
            }).start();
            return START_STICKY; //START_STICKY means that if the serive is killed by the OS for some reason, it is restarted by the system without the application's intervention.
        }
}
