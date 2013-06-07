package ru.vlgu.homeautomation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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



public class LightListAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<ApiGateway.Sensor> data;
    private int curId;

    public LightListAdapter(Context context, ArrayList<ApiGateway.Sensor> data) {
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
            if (data.get(i).type.equals("analog")) {
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
            rowView.setTag(data.get(i).id);

            seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    setValue sv = new setValue(context, (Integer)seekBar.getTag(), seekBar.getProgress());
                    sv.execute();
                }
            });

            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int temp = 0;
                    if (b) temp = 1;
                    setValue sv = new setValue(context, (Integer)compoundButton.getTag(), temp);
                }
            });

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    TextView tw = (TextView)view.findViewById(R.id.textSensorName);
                    curId = (Integer)view.getTag();
                    builder.setMessage(tw.getText())
                            .setTitle(R.string.dialog_choose_action);
                    builder.setNeutralButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            delSensor ds = new delSensor(curId, context);
                            ds.execute();
                        }
                    });
                    builder.setNegativeButton(R.string.dialog_back, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
        return rowView;
    }
}


