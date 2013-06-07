package ru.vlgu.homeautomation;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

class loadLight extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private View rootView;
    public ArrayList<ApiGateway.Sensor> data;
    private ProgressDialog progress;

    public loadLight(Context context, View rootView) {
        this.mContext=context;
        this.rootView=rootView;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(mContext);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage(mContext.getResources().getString(R.string.dialog_loading));
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ApiGateway gw = new ApiGateway();
        data = gw.getLights();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        ListView list = (ListView)rootView.findViewById(R.id.lightListView);
        LightListAdapter adapter = new LightListAdapter(mContext, data);
        list.setAdapter(adapter);
        progress.dismiss();
    }
}

public class LightFragment extends Fragment {
    public View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_light, null);
        return view;
    }

    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadLight AT = new loadLight(getActivity(), view);
        AT.execute();
    }
}
