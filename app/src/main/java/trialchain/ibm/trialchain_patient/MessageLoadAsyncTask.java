package trialchain.ibm.trialchain_patient;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ishankhaparde on 12/04/17.
 */

public class MessageLoadAsyncTask extends AsyncTask<String,Void,ArrayList<Message>> {
        ArrayList messageList;


    private FetchedMessagesEvent fetchedMessagesEvent;

  public MessageLoadAsyncTask(ArrayList<Message> messageList,FetchedMessagesEvent fetchedMessagesEvent)
  {
      this.messageList = messageList;
      this.fetchedMessagesEvent = fetchedMessagesEvent;
  }
    @Override
    public void onPreExecute()
    {
        messageList = new ArrayList<>();
    }
    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected ArrayList<Message> doInBackground(String... params)
    {
        final String recordID = params[0];

        //http://198.23.112.251/api/records/RecordPK/messages

        String URL = "http://198.23.112.251/api/records/"+recordID+"/messages";
        Log.d("Getting messages object",URL);

        SyncHttpClient syncHttpClient = new SyncHttpClient();

        syncHttpClient.get(URL, new AsyncHttpResponseHandler() {
             @Override
             public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final JsonParser jsonParser = new JsonParser();

                 String messages = new String(responseBody);
                 JsonElement jsonElement = jsonParser.parse(messages);

                 JsonObject jsonObject = jsonElement.getAsJsonObject();
                 JsonArray jsonArray = jsonObject.get("messages").getAsJsonArray();


                 String messagesURL = "http://198.23.112.251/api/records/"+recordID+"/messages/";
                 for(int i=0;i<jsonArray.size();i++)
                 {
                     SyncHttpClient getMessageClient = new SyncHttpClient();
                     String doURL = messagesURL + jsonArray.get(i).getAsString();
                     Log.d("DOING THIS URL",doURL);
                     getMessageClient.get(doURL, null, new AsyncHttpResponseHandler() {
                         @Override
                         public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                             Log.d("Message recevied",new String(responseBody));

                             JsonObject messageobj = jsonParser.parse(new String(responseBody)).getAsJsonObject();

                             Log.d("okay.",messageobj.get("message").getAsString());
                             Message temp = new Message();
                             temp.setMessage(messageobj.get("message").getAsString());
                             temp.setRecordId(recordID);
                             messageList.add(temp);
                         }

                         @Override
                         public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                         }
                     });
                 }

             }

             @Override
             public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("JSON FAILED",new String(responseBody));
             }
         });


        return messageList;
    }

    public ArrayList setMessageList()
    {
       return messageList;
    }

    @Override
    protected void onPostExecute(ArrayList<Message> result)
    {
        super.onPostExecute(result);
        Log.d("Result size",messageList.size()+"");
        fetchedMessagesEvent.didMessageLoad(result);
    }

    interface FetchedMessagesEvent
    {
        void didMessageLoad(ArrayList<Message> list);
    }
}
