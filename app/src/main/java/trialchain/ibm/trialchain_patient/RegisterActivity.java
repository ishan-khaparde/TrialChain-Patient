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

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Initialise the register button
        Button register = (Button)findViewById(R.id.register);
        //Setting up the listener for the register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Here, initialise 3 text fields. One for name, pin and confirm PIN each.
                EditText name = (EditText)findViewById(R.id.registerNameField);
                EditText pin = (EditText)findViewById(R.id.registerPINField);
                EditText confirmPin = (EditText)findViewById(R.id.registerConfirmPINField);

                //Check if the form is correctly filled or not.
                if(name.getText().length()==0 || pin.getText().length() == 0||confirmPin.getText().length()==0)
                {
                    //If the user has blanked out something, show a pop up.
                    Toast.makeText(getApplicationContext(),"You need to finish the form to proceed.",Toast.LENGTH_LONG).show();
                }

                //If PIN and Confirm PIN fields hold different values, show a popup to the user.
                if(!pin.getText().toString().equals(confirmPin.getText().toString())){
                    Log.d("PIN",pin.getText().toString());
                    Log.d("Confirm PIN",confirmPin.getText().toString());

                    Toast.makeText(getApplicationContext(),"You entered a different PIN in the confirm field.",Toast.LENGTH_LONG).show();
                }
                else{
                    if(name.getText().length()>0){
                        //Form validation successful. Now we are ready to save the PIN and Name of user.
                        SharedPreferences sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("Name",name.getText().toString());
                        editor.putString("PIN",pin.getText().toString());
                        editor.apply(); //Commit the written values to storage.

                        //Registration successful.Show a pop up and redirect user to Login Activity.
                        Toast.makeText(getApplicationContext(),"Registration Successful.",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                }

            }
        });
    }
}
