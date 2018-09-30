package trialchain.ibm.trialchain_patient;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class QRCodeScannerActivity extends AppCompatActivity {

    private IntentIntegrator qrScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
        qrScanner = new IntentIntegrator(this);
    }


    @Override
    public void onStart()
    {
        super.onStart();

        qrScanner.initiateScan();
        qrScanner.setBeepEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult qrScan = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(qrScan!=null)
        {
            if(qrScan.getContents()==null)
            {
                Toast.makeText(this,"Nothing Read...",Toast.LENGTH_SHORT).show();
                //TODO If unable to read a URL from QR Code, produce a Snackbar for notification. I should remove Toast from the app.
                finish();
            }
            else
            {
                //TODO wire this thing up to a async task to post a signed token to the URL read by the QR Scanner.

                int callingActivity = getIntent().getIntExtra("calling-activity",0);
                String scannedContents = qrScan.getContents();
                Log.d("SCANNED STUFF",scannedContents);
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(scannedContents);
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                switch (callingActivity)
                {
                    case 1:

                        Log.d("SCANNED",scannedContents);

                        Log.d("Host",jsonObject.get("host").getAsString());
                        Log.d("Session ID",jsonObject.get("session-id").getAsString());

                        Token token = new Token();

                        token.setPublicKey(getIntent().getStringExtra("publickey"));
                        token.setSk(getIntent().getStringExtra("privatekey"));
                        token.setHost(jsonObject.get("host").getAsString());
                        token.setSessionId(jsonObject.get("session-id").getAsString());

                        new TokenPOSTAsyncTask(this).execute(token);
                        finish();

                        break;

                    case 2:
                        //TODO: Okay, here scan QR for adding record.
                        Intent intent = new Intent(this,AliasActivity.class);

                        String addToken = jsonObject.get("token").getAsString();
                        String clinicName = jsonObject.get("name").getAsString();
                        String clinicId = jsonObject.get("clinic-id").getAsString();
                        //String URL = jsonObject.get("api").getAsString();
                        String URL = "http://198.23.112.251";
                        intent.putExtra("addToken",addToken);
                        intent.putExtra("clinicName",clinicName);
                        intent.putExtra("clinicId",clinicId);
                        intent.putExtra("URL",URL);

                        startActivity(intent);

                        finish();

                        break;
                }

            }
        }



    }
}
