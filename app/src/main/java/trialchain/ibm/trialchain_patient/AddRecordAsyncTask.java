package trialchain.ibm.trialchain_patient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonObject;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.libsodium.jni.keys.KeyPair;
import org.libsodium.jni.keys.SigningKey;

import trialchain.ibm.trialchain_patient.database.DBHelper;

public class AddRecordAsyncTask extends AsyncTask<AddRecordToken,Void,String> {

    private Context context;
    private SigningKey signingKey;
    private String alias;



    public AddRecordAsyncTask(Context context,String alias)
    {
        this.context  =  context;
        this.alias = alias;
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
    protected String doInBackground(AddRecordToken[] params)
    {
        //TODO: Change keypair to signingkey.
        //TODO: Save the signing key to database.
        //TODO: Use these for login as well.
        AddRecordToken addRecordToken = params[0];

        signingKey = new SigningKey();

         //Get the public component of the the key pair
        String publicKey = Base64.encodeToString(signingKey.getVerifyKey().toBytes(),Base64.URL_SAFE); //Encode the public key using a Base64, URL Safe encoding.

        //Construct the JSON Object
        JsonObject recordObject = new JsonObject();

        recordObject.addProperty("key",publicKey);
        recordObject.addProperty("clinic-key",addRecordToken.getClinic_id());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token",addRecordToken.getToken());
        jsonObject.add("record",recordObject);

        SyncHttpClient syncHttpClient = new SyncHttpClient();

        //Prepare the HTTP request with the JSON object just built.
        RequestParams requestParams = new RequestParams();
        //requestParams.put("record",jsonObject.toString());
        requestParams.put("token",addRecordToken.getToken().toString());
        requestParams.put("record",recordObject.toString());
        Log.d("RECORD POST",jsonObject.toString());

        syncHttpClient.post("http://api.trial-chain.ml/records",requestParams,new ResponseHandler());

        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        //TODO Add logic for writing to database...
        DBHelper helper = new DBHelper(context);
        helper.addRecord(alias,signingKey.toString());
    }
}
