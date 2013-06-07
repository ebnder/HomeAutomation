package ru.vlgu.homeautomation;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

class loadOther extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private View rootView;
    public ArrayList<ApiGateway.Sensor> data;

    public loadOther(Context context, View rootView) {
        this.mContext=context;
        this.rootView=rootView;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ApiGateway gw = new ApiGateway();
        data = gw.getOther();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        ListView mainlist = (ListView)rootView.findViewById(R.id.otherListView);
        mainlist.setAdapter(new OtherListAdapter(mContext, data));
    }
}

public class OtherFragment extends Fragment {
    public View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_other, null);
        return view;
    }

    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadOther AT = new loadOther(getActivity(), view);
        AT.execute();
    }
}
