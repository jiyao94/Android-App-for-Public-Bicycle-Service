package com.example.publicbicycleservice;

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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by jiyao on 2017/4/24.
 */

public class TopUpFragment extends Fragment {
    EditText mBalanceView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.topup_layout, container, false);

        mBalanceView = (EditText) view.findViewById(R.id.balance_number);

        Button mTopUpButton = (Button) view.findViewById(R.id.topup_button);
        mTopUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TopUpFragment.HttpAsyncTask().execute("https://ap2.salesforce.com/services/apexrest/Customer");
            }
        });

        return view;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return PUT(urls[0], mBalanceView);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public String PUT(String url, EditText mBalanceView) {
        String result = "";

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httpPut = new HttpPut(url);
            Customer customer = new Customer("", SaveSharedPreference.getUserName(getContext()), "", SaveSharedPreference.getUserPasswd(getContext()),
                    Double.parseDouble(mBalanceView.getText().toString()));
            Gson gson = new Gson();
            String json = gson.toJson(customer);
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

}
