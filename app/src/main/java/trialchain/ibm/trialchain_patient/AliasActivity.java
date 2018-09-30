package trialchain.ibm.trialchain_patient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

/**
 * Description This activity prompts the user for an alias. It saves the alias, associates it with a key pair and saves it in a database
 * */

public class AliasActivity extends AppCompatActivity implements View.OnClickListener {

    String addToken;
    String clinicName;
    String clinicId;
    String URL;
    String aliasSuggest;
    EditText aliasField;
    Button aliasButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alias);

        //Instantiate the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addToken = getIntent().getStringExtra("addToken");
        clinicName = getIntent().getStringExtra("clinicName");
        clinicId = getIntent().getStringExtra("clinicId");
        URL = getIntent().getStringExtra("URL");

        aliasField = (EditText)findViewById(R.id.aliasActivityField);
        aliasButton = (Button)findViewById(R.id.aliasActivityButton);
        aliasButton.setOnClickListener(this);
        aliasSuggest = clinicName + new Date().toString();

        aliasField.setHint(aliasSuggest);
        //Set the click listener for the button

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        AddRecordToken addRecordToken = new AddRecordToken();


        addRecordToken.setToken(addToken);
        addRecordToken.setClinicName(clinicName);
        addRecordToken.setClinic_id(clinicId);
        addRecordToken.setApi("http://198.23.112.251");

        new AddRecordAsyncTask(this,aliasField.getText().toString()).execute(addRecordToken);
        finish();
    }
}
