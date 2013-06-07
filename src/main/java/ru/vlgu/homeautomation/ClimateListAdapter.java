package ru.vlgu.homeautomation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class ClimateListAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<ApiGateway.Sensor> data;

    public ClimateListAdapter(Context context, ArrayList<ApiGateway.Sensor> data) {
        super();
        this.context = (Activity)context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if ((rowView == null)) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.view_listview_light, null);
            TextView text = (TextView)rowView.findViewById(R.id.textSensorName);
            SeekBar seek = (SeekBar)rowView.findViewById(R.id.seekSensor);
            ToggleButton toggle = (ToggleButton)rowView.findViewById(R.id.toggleSensor);

            text.setText(data.get(i).name);
            if (data.get(i).type.equals("climate")) {
                seek.setVisibility(SeekBar.VISIBLE);
                seek.setProgress(data.get(i).reading);
                seek.setTag(data.get(i).id);
            } else if (data.get(i).type.equals("digital")) {
                toggle.setVisibility(ToggleButton.VISIBLE);
                Boolean check = false;
                if (data.get(i).reading > 0) check = true;
                toggle.setChecked(check);
                toggle.setTag(data.get(i).id);
            }

            seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Log.d("Oppa", "id: " + seekBar.getTag() + " set: " + seekBar.getProgress());
                }
            });

            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.d("Oppa", "id: "+compoundButton.getTag()+" set: "+b);
                }
            });
        }
        return rowView;
    }
}


