package ru.vlgu.homeautomation;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class loadAbout extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private View rootView;
    public ApiGateway.StateData data;
    private ProgressDialog progress;

    public loadAbout(Context context, View rootView) {
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
        data = gw.getSystem();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        TextView twTitle = (TextView)rootView.findViewById(R.id.textAboutTitleVal);
        TextView twApiVer = (TextView)rootView.findViewById(R.id.textAboutApiVerVal);
        TextView twAndrVer = (TextView)rootView.findViewById(R.id.textAboutAndrVerVal);

        twTitle.setText(data.systemName);
        twApiVer.setText(data.systemVer);
        twAndrVer.setText(data.androidVer);
        progress.dismiss();
    }
}

public class AboutFragment extends Fragment {
    public View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, null);
        return view;
    }

    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadAbout AT = new loadAbout(getActivity(), view);
        AT.execute();
    }
}
