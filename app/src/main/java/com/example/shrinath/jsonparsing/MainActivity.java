package com.example.shrinath.jsonparsing;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ListView lv;
    private static  String url="https://api.androidhive.info/contacts/";

    ArrayList<HashMap<String,String>> contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList=new ArrayList();
        lv=(ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh=new HttpHandler();
            String jsonStr=sh.makeServiceCall(url);
            Log.d("MainActivity","Response "+jsonStr);

            if(jsonStr!=null)
            {
                try {
                    JSONObject jsonObject=new JSONObject(jsonStr);

                    JSONArray contacts=jsonObject.getJSONArray("contacts");

                    for(int i=0;i<contacts.length();i++)
                    {
                        JSONObject c=contacts.getJSONObject(i);

                        String id=c.getString("id");
                        String name=c.getString("name");
                        String email=c.getString("email");
                        String address=c.getString("address");
                        String gender=c.getString("gender");


                        JSONObject phone=c.getJSONObject("phone");
                        String mobile=phone.getString("mobile");
                        String home=phone.getString("home");
                        String office=phone.getString("office");

                        HashMap<String,String> contact=new HashMap<>();
                        contact.put("id",id);
                        contact.put("name",name);
                        contact.put("email",email);
                        contact.put("mobile",mobile);

                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("MainActivty","JSON parsing error"+e.getMessage());
                }
            }
            else
            {
                Log.e("MainActivity","Couldn't get json from server");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(progressDialog.isShowing())
                progressDialog.dismiss();

            ListAdapter adapter=new SimpleAdapter(MainActivity.this,contactList,R.layout.list_item,new String[]
                    {"name","email","mobile"},new int[]{R.id.tvName,R.id.tvEmail,R.id.tvMobile});
            lv.setAdapter(adapter);
        }
    }
}
