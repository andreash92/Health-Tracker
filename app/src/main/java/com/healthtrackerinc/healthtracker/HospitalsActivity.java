package com.healthtrackerinc.healthtracker;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import au.com.bytecode.opencsv.CSVReader;

import static android.R.id.list;

public class HospitalsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public String location = "Όλα";
    public List<String[]> list = new ArrayList<String[]>();
    public int rowsNum, colNum;
    public String jsonFileName = "hospitals.json";
    private ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    public JSONObject jsonObj;
    public JSONArray contacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals);


        //JSON Data
        try {
            getJSONData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        /** Spinner Location **/

        // Spinner element
        Spinner spinnerLoc = (Spinner) findViewById(R.id.location_spinner);

        // Spinner click listener
        spinnerLoc.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categoriesLoc = new ArrayList<String>();
        categoriesLoc.add("Όλα");
        categoriesLoc.add("Αθήνα");
        categoriesLoc.add("Θεσσαλονίκη");
        categoriesLoc.add("Λάρισα");
        categoriesLoc.add("Πάτρα");
        categoriesLoc.add("Βέροια");
        categoriesLoc.add("Κύπρος");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterLoc = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesLoc);

        // Drop down layout style - list view with radio button
        dataAdapterLoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerLoc.setAdapter(dataAdapterLoc);



    }



    // Generate the Action Bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (Account.getInstance().getAdminValue()==1)
            getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String select = item.getTitle().toString();
        if (select.equals("Add")){
            Intent myIntent = new Intent(HospitalsActivity.this, AddHospitalActivity.class);
            startActivity(myIntent);
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

        if (adapterView.getId()==R.id.location_spinner){
            location = adapterView.getItemAtPosition(pos).toString();
        }

        try {
            display();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //displayResults();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do something
    }


    public void getJSONData() throws IOException, JSONException {
        String jsonStr = loadJSONFromAssets();
        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_view);

        if (jsonStr!=null) {
            jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            contacts = jsonObj.getJSONArray("hospitals");
        }



        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("title", "3");
            jsonObj.put("address", "NAME OF STUDENT");
            jsonObj.put("phone", "99999");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        contacts.put(jsonObj);
        if (AddHospital.getInstance().getAdd()==1){

        }

    }


    public void  display() throws JSONException {

        contactList.clear();

        // looping through All Contacts
        for (int i = 0; i < contacts.length(); i++) {
            JSONObject c = contacts.getJSONObject(i);

            String name = c.getString("title");
            String loc = c.getString("address");
            String phone = c.getString("phone");

            // tmp hash map for single contact
            HashMap<String, String> contact = new HashMap<>();

            if (loc.contains(loc)) {
                // adding each child node to HashMap key => value
                contact.put("title", name );
                contact.put("address", loc);
                contact.put("phone", phone);

                // adding contact to contact list
                contactList.add(contact);
            } else if (loc.equals("Όλα")){
                // adding each child node to HashMap key => value
                contact.put("title", name );
                contact.put("address", loc);
                contact.put("phone", phone);

                // adding contact to contact list
                contactList.add(contact);
            }
        }

        ListAdapter adapter = new SimpleAdapter(
                HospitalsActivity.this, contactList, R.layout.list_item,
                new String[]{"title", "address", "phone"}, new int[]{R.id.title, R.id.semi_title, R.id.bottom_title});

        listView.setAdapter(adapter);
    }




    public String loadJSONFromAssets() {
        String json = null;
        try {
            InputStream is = getAssets().open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
