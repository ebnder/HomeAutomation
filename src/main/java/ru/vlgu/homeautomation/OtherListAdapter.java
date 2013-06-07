package ru.vlgu.homeautomation;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class OtherListAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<ApiGateway.Sensor> data;
    private ArrayList<Integer> curId = new ArrayList<Integer>();
    private Switch toggle;

    public OtherListAdapter(Context context, ArrayList<ApiGateway.Sensor> data) {
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
            rowView = inflater.inflate(R.layout.view_listview_other, null);

            TextView text = (TextView)rowView.findViewById(R.id.textOtherName);
            toggle = (Switch)rowView.findViewById(R.id.switchOtherToggle);
            ImageView icon = (ImageView)rowView.findViewById(R.id.imageOtherState);
            ProgressBar level = (ProgressBar)rowView.findViewById(R.id.progressOtherLevel);

            text.setText(data.get(i).name);
            curId.add(data.get(i).id);

            if (data.get(i).reading > 0) icon.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_green));
                    else icon.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_red));

            if (data.get(i).type.equals("smoke")) {

            } else if (data.get(i).type.equals("switch")) {
                toggle.setVisibility(Switch.VISIBLE);
                toggle.setTag(i);
                if (data.get(i).reading > 0) toggle.setChecked(true);
                toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        setValue sv;
                        if (b) {
                            sv = new setValue(context, curId.get((Integer)toggle.getTag()), 1);
                        } else {
                            sv = new setValue(context, curId.get((Integer)toggle.getTag()), 0);
                        }
                        sv.execute();
                    }
                });
            } else if (data.get(i).type.equals("level")) {
                level.setVisibility(ProgressBar.VISIBLE);
                level.setProgress(data.get(i).reading);
            } else if (data.get(i).type.equals("water")) {

            } else if (data.get(i).type.equals("security")) {

            }

        }
        return rowView;
    }
}


