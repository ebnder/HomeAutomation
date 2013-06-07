package ru.vlgu.homeautomation;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter{
    Activity context;
    ArrayList<ApiGateway.Camera> strings;

    public SpinnerAdapter(Context context, ArrayList<ApiGateway.Camera> objects) {
        this.context = (Activity)context;
        this.strings = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row==null) {
            LayoutInflater inflater=context.getLayoutInflater();
            row=inflater.inflate(R.layout.view_listview_light, parent, false);
            TextView tw = (TextView)row.findViewById(R.id.textSensorName);
            tw.setText(strings.get(position).name);

        }
          return row;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}