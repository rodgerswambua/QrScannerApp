package com.alhazmy13.myapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DisplayAllActivity extends AppCompatActivity {

    JSONParser jsonParser;
    ProgressDialog progressDialog;
    int value;
    String[] names,emails,mobiles,ids;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all);
        jsonParser=new JSONParser();
        listView=(ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int postion, long l) {

                new AlertDialog.Builder(DisplayAllActivity.this)
                        .setTitle("Select your option")
                        .setMessage("......")
                        .setPositiveButton("Edit",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(DisplayAllActivity.this,EditStudent.class);
                                intent.putExtra("ID",ids[postion]);
                                intent.putExtra("Name",names[postion]);
                                intent.putExtra("Email",emails[postion]);
                                intent.putExtra("Mobile",mobiles[postion]);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Delete",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DeleteTask().execute(ids[postion]);
                            }
                        })
                        .show();






            }
        });
        new DisplayAllTask().execute();
    }


    class DisplayAllTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(DisplayAllActivity.this);
            progressDialog.setTitle("Wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> list= new ArrayList<NameValuePair>();


            JSONObject jsonObject=jsonParser.makeHttpRequest("http://192.168.1.4/myapp/display_all.php","POST",list);

            try{
                if(jsonObject!=null && !jsonObject.isNull("value")){
                    value=jsonObject.getInt("value");
                    JSONArray jsonArray=jsonObject.getJSONArray("students");
                    names=new String[jsonArray.length()];
                    emails=new String[jsonArray.length()];
                    mobiles=new String[jsonArray.length()];
                    ids=new String[jsonArray.length()];
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject objcet=jsonArray.getJSONObject(i);
                        ids[i]=objcet.getString("ID");
                        names[i]=objcet.getString("Name");
                        emails[i]=objcet.getString("Email");
                        mobiles[i]=objcet.getString("Mobile");
                    }
                }else{
                    value=0;
                }

            }catch (Exception e){
                Log.d("ERROR", e.getMessage());
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(value==1){
                Toast.makeText(getApplicationContext(), "Done...", Toast.LENGTH_LONG).show();
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(DisplayAllActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,names);
                listView.setAdapter(adapter);
            }else{
                Toast.makeText(getApplicationContext(),"Error...",Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }


    }


    class DeleteTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(DisplayAllActivity.this);
            progressDialog.setTitle("Wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> list= new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("ID",strings[0]));

            JSONObject jsonObject=jsonParser.makeHttpRequest("http://192.168.1.4/myapp/delete_student.php","POST",list);

            try{
                if(jsonObject!=null && !jsonObject.isNull("value")){
                    value=jsonObject.getInt("value");
                }else{
                    value=0;
                }

            }catch (Exception e){
                Log.d("ERROR", e.getMessage());
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(value==1){
                Toast.makeText(getApplicationContext(), "Done...", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Error...",Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_all, menu);
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
}
