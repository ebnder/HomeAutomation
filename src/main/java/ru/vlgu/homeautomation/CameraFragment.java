package ru.vlgu.homeautomation;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

class loadCameras extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private View rootView;
    public ArrayList<ApiGateway.Camera> data;

    public loadCameras(Context context, View rootView) {
        this.mContext=context;
        this.rootView=rootView;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ApiGateway gw = new ApiGateway();
        data = gw.getCameras();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Spinner spinner = (Spinner)rootView.findViewById(R.id.spinnerCameras);
        spinner.setAdapter(new SpinnerAdapter(mContext, data));
        Button capture = (Button)rootView.findViewById(R.id.buttonCapture);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mPath = Environment.getExternalStorageDirectory().toString() + "/screenshots/";

                Bitmap bitmap;
                View v1 = rootView.findViewById(R.id.mjpegFragment);
                v1.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false);
                File imageFile = new File(mPath+System.currentTimeMillis()+".jpg");

                try {
                    OutputStream fout = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
                    fout.flush();
                    fout.close();
                 } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
}

public class CameraFragment extends Fragment {
    public View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_camera, null);
        return view;
    }

    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadCameras AT = new loadCameras(getActivity(), view);
        AT.execute();
    }
}
