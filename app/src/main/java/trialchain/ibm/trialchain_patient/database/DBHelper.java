package trialchain.ibm.trialchain_patient.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.libsodium.jni.keys.PrivateKey;
import org.libsodium.jni.keys.PublicKey;

import java.util.ArrayList;
import java.util.List;

import trialchain.ibm.trialchain_patient.Record;

/**
 * Description: This is a helper class that manages all transactions to the SQLite database. This class must be instantiated to perform
 * any database operation.
 * */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DBVersion = 1;
    private static final String databaseName = "KEY_DB";

    public DBHelper(Context context) {
        super(context, databaseName, null, DBVersion);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Tables
        String createTable = "CREATE TABLE keys(alias TEXT ,pk TEXT,sk TEXT);";
        String recordTable = "CREATE TABLE records(alias TEXT , sign_key TXT);";

        db.execSQL(createTable);
        db.execSQL(recordTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Recreate tables when we have a newer version of database.
        db.execSQL("DROP TABLE IF EXISTS keys");
        db.execSQL("DROP TABLE IF EXISTS records");
        onCreate(db);
    }

    public void addRecord(String alias,String signkey)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("alias",alias);
        values.put("sign_key",signkey);
        db.insert("records",null,values);
        db.close();
    }

    public Record getRecord(String alias)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Record record = null;
        Cursor cursor = db.rawQuery("SELECT * FROM records WHERE alias="+alias,null);
        cursor.moveToFirst();

        while(cursor.moveToNext())
        {
             record = new Record(cursor.getString(0),cursor.getString(1));

        }
        cursor.close();

        return record;
    }

    public List<Record> getAllRecords()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Record> records = new ArrayList<>();
        Record record;
        Cursor cursor = db.rawQuery("SELECT * FROM records",null);
        cursor.moveToFirst();
        while(cursor.moveToNext()) {
            record = new Record(cursor.getString(0), cursor.getString(1));
            records.add(record);
        }
        Log.d("Number of Records",cursor.getCount()+"");
        cursor.close();
        return records;
    }



    /**
     * Description: This method takes alias, public key and private key as input and attempts to insert them
     * into the database. It calls method checkForDuplicates to check against duplicate values.
     *
     * @return true, if insertion was successful; false otherwise.
     * */

    public boolean addKeyPair(String alias, PublicKey pk, PrivateKey sk)
    {
        //Get a writable instance of the database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Check if the values we are trying to insert exist or not.
        if(!checkForDuplicates(alias))
        {
            values.put("alias",alias);
            values.put("pk",pk.toString());
            values.put("sk",sk.toString());
            db.insert("keys",null,values);

            db.close();
            return true;
        }
        else
            return false;

    }

    public void deleteItem(String alias)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete("keys","alias" + " = ?",new String[]{alias});

        db.close();
    }
    /**
     * Description: This is a method for returning all the key pairs present in the database.
     * This method is called when the list is to be populated.
     *
     * @return List of all key pairs currently in the database.
     * */
    public List<Record> getAllKeyPairs()
    {
        List<Record> data = new ArrayList<>();

        //Get a read only instance of the database.
        SQLiteDatabase db = this.getReadableDatabase();

        //Initialise a cursor with the result of a SQL raw query.
        Cursor cursor = db.rawQuery("SELECT * FROM keys",null);
        //Ensure the cursor points to the first row in the result set.
        cursor.moveToFirst();

        //Iterate until the cursor has crossed the final row.
        while(!cursor.isAfterLast())
        {
            Record temp = new Record(cursor.getString(0),new PrivateKey(cursor.getString(1)),new PublicKey(cursor.getString(2)));
            data.add(temp); //populate the list with the data from result set.
            cursor.moveToNext();
        }

        cursor.close();
        return data;
    }

    /**
     * Description: This is called when we try to insert a duplicate alias for a key pair. If a duplicate is found,
     * method returns false and does not insert into database, otherwise, return true and proceed with insert operation.
     *
     * @return true if no duplicate was found, false otherwise.
     * */

    private boolean checkForDuplicates(String alias)
    {
        //Get a readable instance of the database.
        SQLiteDatabase db = this.getReadableDatabase();

        //Have a cursor for the result set and move the cursor to the first row.
        Cursor cursor = db.query("keys",new String[]{"alias","pk","sk"},"alias =?",new String[]{alias},null,null,null);
        cursor.moveToFirst();

        boolean isDuplicate = false;

        //if the cursor has no rows, obviously no duplicates.
        if(cursor.getCount()==0)
        {
            return false;
        }
        else
        {
            //If the cursor has rows, we need to check values, row by row. Iterating over it.
            while(!cursor.isAfterLast())
            {
                if(cursor.getString(0).equals(alias))
                {
                    isDuplicate = true; //set flag true if duplicate value is found, and break the loop.
                    break;
                }
                cursor.moveToNext();
            }

        }
        cursor.close();
        return isDuplicate; //return the status of the flag.
    }
}
