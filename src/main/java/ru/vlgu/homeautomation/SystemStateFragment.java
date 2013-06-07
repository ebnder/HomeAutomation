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

class loadSystem extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private View rootView;
    public ApiGateway.StateData data;
    private ProgressDialog progress;

    public loadSystem(Context context, View rootView) {
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
        TextView twCurTemp = (TextView)rootView.findViewById(R.id.textCurTempValue);
        TextView twCurOutTemp = (TextView)rootView.findViewById(R.id.textCurOutsideTempValue);
        TextView twCurHum = (TextView)rootView.findViewById(R.id.textCurHumidityValue);
        TextView twWaterHeat = (TextView)rootView.findViewById(R.id.textCurWaterTempValue);
        TextView twCurVoltage = (TextView)rootView.findViewById(R.id.textCurVoltageValue);
        TextView twCurState = (TextView)rootView.findViewById(R.id.textSystemStateValue);

        twCurTemp.setText(data.curTemp+mContext.getResources().getString(R.string.measuremet_celsium));
        twCurOutTemp.setText(data.curOutsideTemp+mContext.getResources().getString(R.string.measuremet_celsium));
        twCurHum.setText(data.curHumidity+mContext.getResources().getString(R.string.measuremet_humidity));
        twWaterHeat.setText(data.curWaterTemp+mContext.getResources().getString(R.string.measuremet_celsium));
        twCurVoltage.setText(data.curInputVoltage+mContext.getResources().getString(R.string.measuremet_voltage));
        switch (data.serverStatus) {
            case 0: twCurState.setText(mContext.getResources().getString(R.string.system_state_0)); break;
            case 1: twCurState.setText(mContext.getResources().getString(R.string.system_state_1)); break;
            default: twCurState.setText(mContext.getResources().getString(R.string.system_state_2)); break;
        }
        progress.dismiss();
    }
}

public class SystemStateFragment extends Fragment {
    public View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_system_state, null);
        return view;
    }

    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadSystem AT = new loadSystem(getActivity(), view);
        AT.execute();
    }
}
