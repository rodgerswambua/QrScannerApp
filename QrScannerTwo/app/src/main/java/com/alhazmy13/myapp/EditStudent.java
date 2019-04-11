package com.alhazmy13.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alhazmy on 3/25/15.
 */
public class EditStudent extends AppCompatActivity {

    Button button;
    EditText name,email,mobile;
    JSONParser jsonParser;
    ProgressDialog progressDialog;
    String idString,nameString,emailString,mobileString;
    int value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        Intent i=getIntent();
        idString=i.getStringExtra("ID");
        nameString=i.getStringExtra("Name");
        emailString=i.getStringExtra("Email");
        mobileString=i.getStringExtra("Mobile");


        button=(Button)findViewById(R.id.addStudentButton);
        name=(EditText)findViewById(R.id.nameEditText);
        name.setText(nameString);
        email=(EditText)findViewById(R.id.emailEditText);
        email.setText(emailString);
        mobile=(EditText)findViewById(R.id.mobileEditText);
        mobile.setText(mobileString);
        jsonParser=new JSONParser();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EditStudentTask().execute();
            }
        });

    }

    class EditStudentTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(EditStudent.this);
            progressDialog.setTitle("Wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> list= new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("ID",idString));
            list.add(new BasicNameValuePair("Name",name.getText().toString()));
            list.add(new BasicNameValuePair("Mobile",mobile.getText().toString()));
            list.add(new BasicNameValuePair("Email",email.getText().toString()));

            JSONObject jsonObject=jsonParser.makeHttpRequest("http://192.168.1.4/myapp/edit_student.php","POST",list);

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
        getMenuInflater().inflate(R.menu.menu_add_student, menu);
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
