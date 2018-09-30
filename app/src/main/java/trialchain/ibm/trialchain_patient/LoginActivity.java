package trialchain.ibm.trialchain_patient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Description This is the launcher activity of the application. This activity is shown when user first launches the application.
 * */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Use shared prefs to store user pin.
        SharedPreferences loginPreferences = getSharedPreferences("Login",MODE_PRIVATE);

        if(loginPreferences.getString("PIN","NO PIN").equals("NO PIN"))
        {
            //If no record is found on the shared pref file, redirect user for registration
            Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
            startActivity(intent);
        }

        //Initialise and set listener for login button.
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //When the user hits login, compare the entered pin with stored copy. If match, redirect user to Main Activity
                EditText pin = findViewById(R.id.pinField);
                SharedPreferences loginPreferences = getSharedPreferences("Login",MODE_PRIVATE);

                    if(loginPreferences.getString("PIN","NO PIN").equals(pin.getText().toString()))
                    {
                        //PIN match found. Redirect to Main Activity.
                        Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                        //TODO Move this to record PK activity.
                       // Intent intent = new Intent(getApplicationContext(),QRCodeScannerActivity.class);

                       Intent intent = new Intent(getBaseContext(),Dashboard.class);
                        intent.putExtra("calling-activity",1);
                        startActivity(intent);
                    }
                }


        });

    }


}
