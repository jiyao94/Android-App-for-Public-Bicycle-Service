package com.example.publicbicycleservice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RegisterActivity extends AppCompatActivity {
    private AutoCompleteTextView mEmailView;
    private EditText mNameView, mPasswordView, mPhoneView, mBalanceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle extras = getIntent().getExtras();

        mNameView = (EditText) findViewById(R.id.register_name);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mPhoneView = (EditText) findViewById(R.id.register_phone);
        mBalanceView = (EditText) findViewById(R.id.register_balance);

        mEmailView.setText(extras.getString("LOGIN_EMAIL"));
        mPasswordView.setText(extras.getString("LOGIN_PASSWORD"));

        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpAsyncTask().execute("https://ap2.salesforce.com/services/apexrest/Customer");

                SaveSharedPreference.setUserName(getBaseContext(), "", mNameView.getText().toString(), mEmailView.getText().toString(),
                        mPasswordView.getText().toString(), mPhoneView.getText().toString());

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0], mNameView, mEmailView, mPasswordView, mPhoneView, mBalanceView);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public String POST(String url, EditText name, AutoCompleteTextView email, EditText passwd, EditText phone, EditText balance){
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json;

            /*// 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", name.getText());
            jsonObject.accumulate("email", email.getText());
            jsonObject.accumulate("phone", phone.getText());
            jsonObject.accumulate("balance", balance.getText());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();*/

            Customer customer = new Customer(name.getText().toString(), email.getText().toString(), phone.getText().toString(),
                    passwd.getText().toString(), Double.parseDouble(balance.getText().toString()));
            Gson gson = new Gson();
            json = gson.toJson(customer);
            Log.d("Json: ", json);

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Authorization", "OAuth " + SaveAccessToken.getAccessToken(this));
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity resEntity = httpResponse.getEntity();
            result = EntityUtils.toString(resEntity);

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }
}
