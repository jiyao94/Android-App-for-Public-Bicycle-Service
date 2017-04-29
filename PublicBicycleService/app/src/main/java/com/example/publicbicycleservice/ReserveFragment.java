package com.example.publicbicycleservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by jiyao on 2017/4/24.
 */

public class ReserveFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private int hours;
    private EditText mBicycleCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reserve_layout, container, false);

        mBicycleCode = (EditText) view.findViewById(R.id.bicycle_code);

        Spinner spinner = (Spinner) view.findViewById(R.id.reserve_spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button mReserveButton = (Button) view.findViewById(R.id.reserve_button);
        mReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Selected: " + hours + " hours", Toast.LENGTH_LONG).show();
                new ReserveFragment.HttpAsyncTask().execute("https://ap2.salesforce.com/services/apexrest/Bicycle", "https://ap2.salesforce.com/services/apexrest/Rent", "false");
            }
        });

        Button mReturnButton = (Button) view.findViewById(R.id.return_button);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Bicycle has been returned", Toast.LENGTH_LONG).show();
                new ReserveFragment.HttpAsyncTask2().execute("https://ap2.salesforce.com/services/apexrest/Bicycle", "https://ap2.salesforce.com/services/apexrest/Rent", "true");
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (pos < 8) {
            hours = pos + 1;
        }
        else {
            hours = 12;
        }
        String item = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return PUT(urls, mBicycleCode, urls[2]) + POST(urls, mBicycleCode);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return PUT(urls, mBicycleCode, urls[2]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public String PUT(String[] url, EditText mBicycleCode, String locked) {
        String result = "";

        try {
            //unlock bicycle
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httpPut = new HttpPut(url[0]);
            BicycleBook bicycle = new BicycleBook(SaveSharedPreference.getUserName(getContext()), mBicycleCode.getText().toString(), Boolean.parseBoolean(locked));
            Gson gson = new Gson();
            String json = gson.toJson(bicycle);
            Log.d("Json: ", json);
            StringEntity se = new StringEntity(json);
            httpPut.setEntity(se);
            httpPut.setHeader("Authorization", "OAuth " + SaveAccessToken.getAccessToken(getContext()));
            httpPut.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPut);
            HttpEntity resEntity = httpResponse.getEntity();
            result = EntityUtils.toString(resEntity);
        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    public String POST(String[] url, EditText mBicycleCode) {
        String result = "";

        try {
            //set rent time
            HttpClient httpclient2 = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url[1]);
            BicycleTime bicycle2 = new BicycleTime(SaveSharedPreference.getUserName(getContext()), mBicycleCode.getText().toString(), hours);
            Gson gson2 = new Gson();
            String json2 = gson2.toJson(bicycle2);
            Log.d("Json: ", json2);
            StringEntity se2 = new StringEntity(json2);
            httpPost.setEntity(se2);
            httpPost.setHeader("Authorization", "OAuth " + SaveAccessToken.getAccessToken(getContext()));
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse2 = httpclient2.execute(httpPost);
            HttpEntity resEntity2 = httpResponse2.getEntity();
            result = EntityUtils.toString(resEntity2);
        }
        catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }
}
