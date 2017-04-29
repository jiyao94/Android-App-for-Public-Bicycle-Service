package com.example.publicbicycleservice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by jiyao on 2017/4/24.
 */

public class ExtendFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private int hours;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.extend_layout, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.extend_spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button mReserveButton = (Button) view.findViewById(R.id.extend_button);
        mReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Selected: " + hours + " hours", Toast.LENGTH_LONG).show();
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
}
