package trialchain.ibm.trialchain_patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.libsodium.jni.keys.KeyPair;

import java.util.ArrayList;
import java.util.List;

import trialchain.ibm.trialchain_patient.database.DBHelper;

/**
 * Description:
 * This activity is the primary dashboard activity of the patient application.
 * This activity encompasses a pager view,that puts up two fragments. First one as a record list and second as a messaging interface.
 * This activity is largely a navigation controller, the data is controlled by the fragments.
 * */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    List<Record> list = new ArrayList<>();
    DBHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialise and set listener for the + button.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);


        //Initialise a list view for holding alias and keys.
     /*   sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/
        helper = new DBHelper(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            //When the user hits + , take user to a different activity and ask for an alias to be associated with a new pk sk pair.
            Intent intent = new Intent(this,AliasActivity.class);
            startActivityForResult(intent,0);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode==RESULT_OK && requestCode == 0)
        {
            String alias = data.getStringExtra("alias");

            Record pairAlias = new Record(alias);

            //After asking for a new alias generate a new pk sk pair.
            KeyPair kp = new KeyPair();

            pairAlias.setPrivateKey(kp.getPrivateKey());
            pairAlias.setPublicKey(kp.getPublicKey());


            //Initialise the helper
            DBHelper helper = new DBHelper(this);

            //Add newly generated key pair to database.
           boolean didListChange =  helper.addKeyPair(alias,pairAlias.getPublicKey(),pairAlias.getPrivateKey());

        }
    }
}

