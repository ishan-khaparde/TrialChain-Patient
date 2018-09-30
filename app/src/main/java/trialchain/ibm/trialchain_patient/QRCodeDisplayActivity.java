package trialchain.ibm.trialchain_patient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.libsodium.jni.crypto.Random;
import org.libsodium.jni.keys.SigningKey;

/**
 *
 * Description An activity that displays QR codes for any given public key.
 * */

public class QRCodeDisplayActivity extends AppCompatActivity implements View.OnClickListener {
    String publickey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_display);

        final ImageView qrCode = (ImageView)findViewById(R.id.qr_code); //Initialise a Imageview to contain the qr code.
        Button scanURLButton = (Button)findViewById(R.id.scanQRCodeButton);
        scanURLButton.setOnClickListener(this);
        Random random = new Random();

        byte[] seed =  random.randomBytes(32);
        SigningKey signingKey = new SigningKey(seed);

        //Obtain the public key that was passed to this activity from calling activity. This key will be encoded into the QR code.
        publickey = getIntent().getStringExtra("publickey");


        BitMatrix bitMatrix;
        QRCodeWriter write = new QRCodeWriter(); //Initialise the QR Code writer object. This is a qr code writer from a third party lib.
        try{
            Log.d("CHECK PK",publickey);

            //Set the QR code resolution to 450x450.
            bitMatrix = write.encode(publickey, BarcodeFormat.QR_CODE,450,450);

            //We create a bitmap here before we can use it to populate the imageview.
            Bitmap bmp = Bitmap.createBitmap(450,450, Bitmap.Config.RGB_565);

            for(int i=0;i<450;i++)
                for(int j=0;j<450;j++)
                {
                    //Set the pixels to show the qr code.
                    bmp.setPixel(i,j,bitMatrix.get(i,j) ? Color.BLACK:Color.WHITE);
                }
            qrCode.setImageBitmap(bmp);
        }catch (WriterException e){
            e.printStackTrace();
        }

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.scanQRCodeButton) //Check if the view v corresponds to the button associated with R.id.scanQRCodeButton
        {
            Intent intent = new Intent(this,QRCodeScannerActivity.class);
            String privateKey = getIntent().getStringExtra("privatekey");
            intent.putExtra("calling-activity",1);
            intent.putExtra("privatekey",privateKey);
            intent.putExtra("publickey",publickey);
            startActivity(intent);
        }
    }
}
