package ru.vlgu.homeautomation;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    public View view;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private EditText et;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, null);
        return view;
    }

    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button test = (Button)view.findViewById(R.id.buttonTestConnection);
        et = (EditText)view.findViewById(R.id.editTextServerAddress);
        settings = getActivity().getSharedPreferences("userdetails", 0);
        editor = settings.edit();
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("server_address", et.getText().toString());
                editor.commit();

                textConnection tc = new textConnection(getActivity());
                tc.execute();
            }
        });

        if (settings.getString("server_address", "").length() > 0) {
            et.setText(settings.getString("server_address", ""));
        }




    }
}
