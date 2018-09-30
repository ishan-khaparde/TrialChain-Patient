package trialchain.ibm.trialchain_patient;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.libsodium.jni.keys.SigningKey;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.util.Date;

public class TokenPOSTAsyncTask extends AsyncTask<Token,Void,Boolean> {



    private Context context;
    private ProgressDialog progressDialog;
    public TokenPOSTAsyncTask(Context context)
    {
        this.context = context;
    }

    @Override
    public void onPreExecute()
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Working...");
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

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
    protected Boolean doInBackground(Token... params) {
        Token token = params[0];

        String postURL;
        postURL = "http://"+token.getHost().replace('\"','\b')+"/proxy/"+token.getSessionId().replace('\"','\b');
        Log.d("POST URL:",postURL);

        SyncHttpClient syncHttpClient = new SyncHttpClient();
        JSONObject jsonObject = new JSONObject();

        //TODO Retain this signing key.Use SharedPrefs to store this in an encrypted fashion.
        SharedPreferences sharedPreferences = context.getSharedPreferences("sign",Context.MODE_PRIVATE);
        SigningKey signingKey;
        if(!sharedPreferences.contains("signingkey")) {
             signingKey = new SigningKey();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(signingKey);
            editor.putString("signingkey",json);
            editor.apply();
        }
        else
        {
            Gson gson = new Gson();
            String key = sharedPreferences.getString("signingkey",null);
            signingKey = gson.fromJson(key,SigningKey.class);
        }

        try
        {
            jsonObject.put("id",Base64.encodeToString(signingKey.getVerifyKey().toBytes(),Base64.URL_SAFE));
            jsonObject.put("exp",TimestampHelper.getExpDate(new Date(),1));
            Log.d("Prepared JSON object",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestParams requestParams = new RequestParams();

        byte[] signedToken = signingKey.sign(jsonObject.toString().getBytes());

        String auth_token = Base64.encodeToString(jsonObject.toString().getBytes(),Base64.URL_SAFE);
        Log.d("Encoded JSON",auth_token);

        auth_token = auth_token + "."+ Base64.encodeToString(signedToken,Base64.URL_SAFE);
        Log.d("SIGNED TOKEN",auth_token);

        requestParams.put("token",auth_token);
        syncHttpClient.post(postURL,requestParams,new ResponseHandler());


        return null;
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        progressDialog.dismiss();
        Intent intent = new Intent(context,Dashboard.class);
        context.startActivity(intent);
    }

}
