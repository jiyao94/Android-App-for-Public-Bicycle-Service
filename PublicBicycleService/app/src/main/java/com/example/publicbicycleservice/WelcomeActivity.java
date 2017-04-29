package com.example.publicbicycleservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new WelcomeActivity.HttpAsyncTask().execute("https://login.salesforce.com/services/oauth2/token");
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urls[0]);
                String json = "grant_type=password&client_id=3MVG9ZL0ppGP5UrB6uC2N8e85CSb6Lwde8J1EpFzLZIHZzFQ1HPQHoQr_7xBxOyFuVeUdFU3OJbTEkhZ.R36s&client_secret=5728887411816613532&username=admin@pbs.com&password=1pbs_lut";
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                HttpEntity resEntity = httpResponse.getEntity();
                result = EntityUtils.toString(resEntity);
                JSONObject jsonObject = new JSONObject(result);
                result = jsonObject.getString("access_token");
            } catch (Exception e) {
                Log.d("InputStream:::::", e.getLocalizedMessage());
                return result;
            }

            return result;
        }

        protected void onPostExecute(String result) {
            SaveAccessToken.clearAccessToken(getBaseContext());
            SaveAccessToken.setAccessToken(getBaseContext(), result);
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
